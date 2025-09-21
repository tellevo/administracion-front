<template>
  <div class="flex items-center gap-3 p-3 bg-base-100 rounded-xl border border-base-200 shadow-sm">
    <!-- Connection Status -->
    <div class="flex items-center gap-2">
      <div class="flex items-center gap-2">
        <!-- Status Light -->
        <div
          :class="[
            'w-3 h-3 rounded-full transition-all duration-300',
            status === 'connected' ? 'bg-green-400 shadow-lg shadow-green-400/50 animate-pulse' :
            status === 'connecting' ? 'bg-yellow-400 shadow-lg shadow-yellow-400/50 animate-pulse' :
            'bg-red-400 shadow-lg shadow-red-400/50'
          ]"
        ></div>

        <!-- Status Text -->
        <div class="min-w-0">
          <div class="text-sm font-medium text-base-content leading-tight">
            {{ statusText }}
          </div>
          <div v-if="statusDescription" class="text-xs text-base-content/70">
            {{ statusDescription }}
          </div>
        </div>
      </div>
    </div>

    <!-- Stats (only show if not compact) -->
    <div v-if="!compact" class="flex items-center gap-4 border-l border-base-200 pl-4 ml-2">
      <div class="flex items-center gap-1">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 text-blue-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
        </svg>
        <span class="text-sm font-medium text-base-content">{{ stats.total || 0 }}</span>
        <span class="text-xs text-base-content/70">total</span>
      </div>

      <div class="flex items-center gap-1">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 text-green-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z" />
        </svg>
        <span class="text-sm font-medium text-base-content">{{ stats.velocity || 0 }}</span>
        <span class="text-xs text-base-content/70">/min</span>
      </div>

      <!-- Uptime (if available) -->
      <div v-if="stats.uptime" class="flex items-center gap-1">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 text-purple-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
        </svg>
        <span class="text-sm font-medium text-base-content">{{ formatUptime(stats.uptime) }}</span>
        <span class="text-xs text-base-content/70">uptime</span>
      </div>
    </div>

    <!-- Actions (only show if not compact) -->
    <div v-if="!compact" class="ml-auto flex items-center gap-2">
      <button
        v-if="status === 'connected'"
        @click="$emit('pause')"
        class="btn btn-ghost btn-sm"
        title="Pausar stream"
      >
        <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 9v6m4-6v6m7-3a9 9 0 11-18 0 9 9 0 0118 0z" />
        </svg>
      </button>

      <button
        v-if="status === 'disconnected'"
        @click="$emit('reconnect')"
        class="btn btn-primary btn-sm"
        title="Reconectar"
      >
        <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
        </svg>
        Reconectar
      </button>
    </div>
  </div>

  <!-- Connection History (expanded view) -->
  <div v-if="showHistory && !compact" class="mt-3 p-3 bg-base-100 rounded-xl border border-base-200 shadow-sm">
    <h4 class="text-sm font-medium text-base-content mb-2">Historial de Conexión</h4>
    <div class="space-y-1 text-xs text-base-content/70 max-h-32 overflow-y-auto">
      <div v-for="event in connectionHistory.slice(0, 10)" :key="event.timestamp" class="flex items-center gap-2">
        <div
          :class="[
            'w-2 h-2 rounded-full flex-shrink-0',
            event.type === 'connected' ? 'bg-green-400' :
            event.type === 'connecting' ? 'bg-yellow-400' :
            'bg-red-400'
          ]"
        ></div>
        <span class="capitalize">{{ event.type }}</span>
        <span class="ml-auto text-base-content/50">
          {{ new Date(event.timestamp).toLocaleTimeString('es-CL', { hour: '2-digit', minute: '2-digit' }) }}
        </span>
      </div>
      <div v-if="connectionHistory.length === 0" class="text-center py-4">
        No hay historial disponible
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, ref, watch } from 'vue'

const props = defineProps({
  status: {
    type: String,
    required: true,
    validator: (value) => ['connecting', 'connected', 'disconnected'].includes(value)
  },
  stats: {
    type: Object,
    default: () => ({})
  },
  compact: {
    type: Boolean,
    default: false
  },
  showHistory: {
    type: Boolean,
    default: false
  }
})

defineEmits(['pause', 'reconnect'])

const connectionHistory = ref([])

// Watch for status changes to build history
watch(() => props.status, (newStatus, oldStatus) => {
  if (oldStatus && newStatus !== oldStatus) {
    connectionHistory.value.unshift({
      type: newStatus,
      timestamp: Date.now()
    })

    // Keep only last 50 events
    if (connectionHistory.value.length > 50) {
      connectionHistory.value.splice(50)
    }
  }
}, { immediate: true })

const statusText = computed(() => {
  switch (props.status) {
    case 'connected':
      return 'Conectado'
    case 'connecting':
      return 'Conectando...'
    case 'disconnected':
      return 'Desconectado'
    default:
      return 'Desconocido'
  }
})

const statusDescription = computed(() => {
  switch (props.status) {
    case 'connected':
      return 'Recibiendo datos en tiempo real'
    case 'connecting':
      return 'Estableciendo conexión...'
    case 'disconnected':
      return 'Sin conexión al stream'
    default:
      return ''
  }
})

const formatUptime = (seconds) => {
  if (!seconds || seconds < 0) return '0s'

  const hours = Math.floor(seconds / 3600)
  const minutes = Math.floor((seconds % 3600) / 60)
  const secs = seconds % 60

  if (hours > 0) {
    return `${hours}h ${minutes}m`
  } else if (minutes > 0) {
    return `${minutes}m ${secs}s`
  } else {
    return `${secs}s`
  }
}
</script>

<style scoped>
/* Enhanced animations for status indicators */
@keyframes status-pulse {
  0%, 100% {
    opacity: 1;
    transform: scale(1);
  }
  50% {
    opacity: 0.8;
    transform: scale(1.1);
  }
}

.bg-green-400 {
  animation: status-pulse 2s infinite;
}

.bg-yellow-400 {
  animation: status-pulse 1.5s infinite;
}

.bg-red-400 {
  animation: status-pulse 1s infinite;
}

/* Accessible focus states */
.btn:focus-visible {
  outline: 2px solid theme('colors.primary');
  outline-offset: 2px;
}

/* High contrast support */
@media (prefers-contrast: high) {
  .bg-green-400 {
    background-color: theme('colors.success');
  }
  .bg-yellow-400 {
    background-color: theme('colors.warning');
  }
  .bg-red-400 {
    background-color: theme('colors.error');
  }
}
</style>
