<template>
  <div class="bg-gradient-to-r from-base-100 to-base-50 border border-base-200 rounded-xl p-4 shadow-sm hover:shadow-md transition-all duration-300 hover:border-primary/20">
    <!-- Card Header -->
    <div class="flex items-start justify-between mb-3">
      <div class="flex items-center gap-3 flex-1 min-w-0">
        <!-- Company Avatar -->
        <div class="avatar shrink-0">
          <div class="w-10 h-10 rounded-full bg-gradient-to-br from-primary to-secondary flex items-center justify-center text-white font-bold text-sm">
            {{ companyInitial }}
          </div>
        </div>

        <!-- Company Info -->
        <div class="flex-1 min-w-0">
          <h3 class="font-semibold text-base-content text-sm leading-tight truncate">
            {{ venta.nombre_empresa }}
          </h3>
          <p class="text-xs text-base-content/70 mt-0.5">
            📧 {{ venta.email }}
          </p>
        </div>
      </div>

      <!-- Sale ID Badge -->
      <div class="flex flex-col items-end gap-1 shrink-0">
        <div class="badge badge-primary badge-xs font-mono">
          #{{ venta.id }}
        </div>
      </div>
    </div>

    <!-- Timestamp & Status -->
    <div class="flex items-center justify-between">
      <div class="flex items-center gap-2">
        <div class="flex items-center gap-1">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-3 w-3 text-base-content/60" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
          <span class="text-xs text-base-content/70 font-medium">
            {{ formattedTime }}
          </span>
        </div>

        <!-- Live Indicator -->
        <div v-if="isRecent" class="flex items-center gap-1">
          <div class="w-2 h-2 bg-green-400 rounded-full animate-pulse"></div>
          <span class="text-xs text-green-600 font-medium">LIVE</span>
        </div>
      </div>

      <!-- Quick Actions -->
      <div class="flex items-center gap-1">
        <button
          @click="$emit('view-details', venta)"
          class="btn btn-ghost btn-circle btn-xs"
          :title="'Ver detalles de venta #' + venta.id"
        >
          <svg xmlns="http://www.w3.org/2000/svg" class="h-3 w-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
          </svg>
        </button>
      </div>
    </div>

    <!-- Expansion Indicator -->
    <div v-if="expanded" class="mt-3 pt-3 border-t border-base-200">
      <div class="text-xs text-base-content/60 space-y-1">
        <div><strong>ID:</strong> {{ venta.id }}</div>
        <div><strong>Empresa:</strong> {{ venta.nombre_empresa }}</div>
        <div><strong>Email:</strong> {{ venta.email }}</div>
        <div><strong>Fecha:</strong> {{ fullFormattedTime }}</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  venta: {
    type: Object,
    required: true,
    validator: (value) => {
      return value &&
             typeof value.id === 'number' &&
             typeof value.email === 'string' &&
             typeof value.nombre_empresa === 'string' &&
             value.fecha_envio
    }
  },
  expanded: {
    type: Boolean,
    default: false
  }
})

defineEmits(['view-details'])

const companyInitial = computed(() => {
  return props.venta.nombre_empresa.charAt(0).toUpperCase()
})

const formattedTime = computed(() => {
  const time = new Date(props.venta.fecha_envio)
  const now = new Date()
  const diffMs = now - time

  // Less than 1 minute
  if (diffMs < 60000) {
    return 'Ahora'
  }

  // Less than 1 hour
  if (diffMs < 3600000) {
    const minutes = Math.floor(diffMs / 60000)
    return `${minutes}min`
  }

  // Less than 24 hours
  if (diffMs < 86400000) {
    return time.toLocaleTimeString('es-CL', {
      hour: '2-digit',
      minute: '2-digit'
    })
  }

  // More than 24 hours
  return time.toLocaleDateString('es-CL', {
    day: '2-digit',
    month: 'short',
    hour: '2-digit',
    minute: '2-digit'
  })
})

const fullFormattedTime = computed(() => {
  return new Date(props.venta.fecha_envio).toLocaleString('es-CL', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  })
})

const isRecent = computed(() => {
  const time = new Date(props.venta.fecha_envio)
  const now = new Date()
  const diffMinutes = (now - time) / (1000 * 60)
  return diffMinutes <= 5 // Consider recent if within last 5 minutes
})
</script>

<style scoped>
/* Mobile-first animations */
@media (hover: hover) and (pointer: fine) {
  .hover\\:shadow-md:hover {
    box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
  }
}

/* Touch-friendly interactions */
@media (pointer: coarse) {
  /* Slightly larger touch targets on mobile */
  .btn-circle {
    min-width: 44px;
    min-height: 44px;
  }
}

/* High contrast mode support */
@media (prefers-contrast: high) {
  .border-base-200 {
    border-width: 2px;
  }
}
</style>
