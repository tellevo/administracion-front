<template>
  <div class="overflow-x-auto bg-base-100 rounded-xl border border-base-200">
    <table class="table table-zebra w-full">
      <!-- Table Header with Sorting -->
      <thead>
        <tr class="bg-base-200/80">
          <th class="w-16 font-semibold text-left">#</th>
          <th
            class="font-semibold text-left cursor-pointer hover:bg-base-300/50 transition-colors duration-200"
            @click="sortBy('id')"
          >
            <div class="flex items-center gap-1">
              ID
              <SortIndicator :field="'id'" :sortField="sortField" :sortOrder="sortOrder" />
            </div>
          </th>
          <th
            class="font-semibold text-left cursor-pointer hover:bg-base-300/50 transition-colors duration-200"
            @click="sortBy('nombre_empresa')"
          >
            <div class="flex items-center gap-1">
              Empresa
              <SortIndicator :field="'nombre_empresa'" :sortField="sortField" :sortOrder="sortOrder" />
            </div>
          </th>
          <th class="font-semibold text-left">Email</th>
          <th
            class="font-semibold text-left cursor-pointer hover:bg-base-300/50 transition-colors duration-200"
            @click="sortBy('fecha_envio')"
          >
            <div class="flex items-center gap-1">
              Fecha/Hora
              <SortIndicator :field="'fecha_envio'" :sortField="sortField" :sortOrder="sortOrder" />
            </div>
          </th>
          <th class="w-24 font-semibold text-center">Estado</th>
          <th class="w-32 font-semibold text-center">Acciones</th>
        </tr>
      </thead>

      <!-- Table Body -->
      <tbody>
        <tr
          v-for="venta in sortedVentas"
          :key="venta.id"
          :class="[
            'hover:bg-base-200/50 transition-colors duration-200',
            isRecent(venta.fecha_envio) ? 'bg-green-50/30 border-l-4 border-l-green-400' : ''
          ]"
        >
          <!-- Row Number -->
          <td class="font-mono text-sm text-base-content/70">
            {{ getRowNumber(venta) }}
          </td>

          <!-- Sale ID -->
          <td class="font-mono text-sm font-medium text-primary">
            #{{ venta.id }}
          </td>

          <!-- Company Name -->
          <td>
            <div class="flex items-center gap-3">
              <!-- Company Avatar -->
              <div class="avatar">
                <div class="w-8 h-8 rounded-full bg-gradient-to-br from-primary to-secondary flex items-center justify-center text-white font-bold text-xs">
                  {{ getCompanyInitial(venta.nombre_empresa) }}
                </div>
              </div>

              <!-- Company Name -->
              <div class="min-w-0 flex-1">
                <div class="font-medium text-base-content text-sm leading-tight truncate max-w-xs">
                  {{ venta.nombre_empresa }}
                </div>
                <!-- Live indicator for recent sales -->
                <div v-if="isRecent(venta.fecha_envio)" class="flex items-center gap-1 mt-1">
                  <div class="w-1.5 h-1.5 bg-green-400 rounded-full animate-pulse"></div>
                  <span class="text-xs text-green-600 font-medium">LIVE</span>
                </div>
              </div>
            </div>
          </td>

          <!-- Email (truncated on smaller screens) -->
          <td class="min-w-0">
            <span class="text-sm text-base-content/80 truncate block max-w-xs" :title="venta.email">
              {{ venta.email }}
            </span>
          </td>

          <!-- Date/Time -->
          <td class="min-w-0">
            <div class="flex flex-col">
              <span class="text-sm font-medium text-base-content">
                {{ formatDate(venta.fecha_envio) }}
              </span>
              <span class="text-xs text-base-content/60">
                {{ formatTime(venta.fecha_envio) }}
              </span>
              <!-- Relative time -->
              <span class="text-xs text-base-content/50">
                {{ getRelativeTime(venta.fecha_envio) }}
              </span>
            </div>
          </td>

          <!-- Status -->
          <td class="text-center">
            <div class="flex justify-center">
              <span
                :class="[
                  'badge badge-xs font-medium px-2 py-1',
                  isRecent(venta.fecha_envio) ? 'badge-success badge-outline' : 'badge-ghost'
                ]"
              >
                {{ isRecent(venta.fecha_envio) ? 'Activa' : 'Procesada' }}
              </span>
            </div>
          </td>

          <!-- Actions -->
          <td class="text-center">
            <div class="flex items-center justify-center gap-1">
              <!-- View Details -->
              <button
                @click="$emit('view-details', venta)"
                class="btn btn-ghost btn-square btn-xs"
                :title="'Ver detalles de venta #' + venta.id"
              >
                <svg xmlns="http://www.w3.org/2000/svg" class="h-3 w-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
                </svg>
              </button>

              <!-- Copy Email -->
              <button
                @click="copyToClipboard(venta.email)"
                class="btn btn-ghost btn-square btn-xs"
                :title="'Copiar email: ' + venta.email"
              >
                <svg xmlns="http://www.w3.org/2000/svg" class="h-3 w-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 16H6a2 2 0 01-2-2V6a2 2 0 012-2h8a2 2 0 012 2v2m-6 12h8a2 2 0 002-2v-8a2 2 0 00-2-2h-8a2 2 0 00-2 2v8a2 2 0 002 2z" />
                </svg>
              </button>
            </div>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- Empty State -->
    <div v-if="ventas.length === 0" class="flex items-center justify-center p-12">
      <div class="text-center">
        <div class="text-6xl mb-4">📊</div>
        <h3 class="text-xl font-bold text-base-content mb-2">No hay ventas para mostrar</h3>
        <p class="text-base-content/70">Las ventas aparecerán aquí cuando se conecte al stream.</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, ref, watch, nextTick } from 'vue'
import SortIndicator from './SortIndicator.vue'

const props = defineProps({
  ventas: {
    type: Array,
    default: () => []
  },
  autoScroll: {
    type: Boolean,
    default: false
  }
})

defineEmits(['view-details'])

const sortField = ref('fecha_envio')
const sortOrder = ref('desc') // 'asc' or 'desc'

const sortedVentas = computed(() => {
  if (!props.ventas.length) return []

  return [...props.ventas].sort((a, b) => {
    let aVal = a[sortField.value]
    let bVal = b[sortField.value]

    // Handle date sorting
    if (sortField.value === 'fecha_envio') {
      aVal = new Date(aVal).getTime()
      bVal = new Date(bVal).getTime()
    }

    // Handle string sorting (case-insensitive)
    if (typeof aVal === 'string') {
      aVal = aVal.toLowerCase()
      bVal = bVal.toLowerCase()
    }

    if (sortOrder.value === 'asc') {
      return aVal > bVal ? 1 : aVal < bVal ? -1 : 0
    } else {
      return aVal < bVal ? 1 : aVal > bVal ? -1 : 0
    }
  })
})

// Watch for new ventas when auto-scroll is enabled
watch(() => props.ventas.length, (newLength, oldLength) => {
  if (props.autoScroll && newLength > oldLength && newLength > 0) {
    // Scroll to top to show newest items first
    nextTick(() => {
      const container = document.querySelector('.max-h-96.overflow-y-auto')
      if (container) {
        container.scrollTop = 0
      }
    })
  }
})

const sortBy = (field) => {
  if (sortField.value === field) {
    sortOrder.value = sortOrder.value === 'asc' ? 'desc' : 'asc'
  } else {
    sortField.value = field
    sortOrder.value = 'desc' // Default to descending for new sorts
  }
}

const getRowNumber = (venta) => {
  const index = props.ventas.findIndex(v => v.id === venta.id)
  return index + 1
}

const getCompanyInitial = (companyName) => {
  return companyName.charAt(0).toUpperCase()
}

const formatDate = (dateString) => {
  const date = new Date(dateString)
  return date.toLocaleDateString('es-CL', {
    year: 'numeric',
    month: 'short',
    day: '2-digit'
  })
}

const formatTime = (dateString) => {
  const date = new Date(dateString)
  return date.toLocaleTimeString('es-CL', {
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  })
}

const getRelativeTime = (dateString) => {
  const now = new Date()
  const date = new Date(dateString)
  const diffMs = now - date
  const diffMins = Math.floor(diffMs / (1000 * 60))

  if (diffMins < 1) return 'Ahora'
  if (diffMins < 60) return `Hace ${diffMins}min`

  const diffHours = Math.floor(diffMins / 60)
  if (diffHours < 24) return `Hace ${diffHours}h`

  const diffDays = Math.floor(diffHours / 24)
  return `Hace ${diffDays}d`
}

const isRecent = (dateString) => {
  const date = new Date(dateString)
  const now = new Date()
  const diffMinutes = (now - date) / (1000 * 60)
  return diffMinutes <= 5 // Consider recent if within last 5 minutes
}

const copyToClipboard = async (text) => {
  try {
    await navigator.clipboard.writeText(text)
    // You could show a toast notification here
    console.log('Email copiado:', text)
  } catch (err) {
    console.error('Error copying to clipboard:', err)
  }
}
</script>

<style scoped>
/* Enhanced table styling */
.table {
  font-size: 0.875rem;
}

.table th {
  font-weight: 600;
  background-color: hsl(var(--b2));
  color: hsl(var(--bc));
  padding: 1rem;
  white-space: nowrap;
}

.table td {
  padding: 0.75rem 1rem;
  vertical-align: middle;
}

/* Highlight recent rows */
.bg-green-50\/30 {
  background-color: rgb(240 253 244 / 0.3);
}

/* Custom scrollbar */
.overflow-x-auto::-webkit-scrollbar {
  height: 6px;
}

.overflow-x-auto::-webkit-scrollbar-track {
  background: hsl(var(--b2));
  border-radius: 3px;
}

.overflow-x-auto::-webkit-scrollbar-thumb {
  background: hsl(var(--b3));
  border-radius: 3px;
}

.overflow-x-auto::-webkit-scrollbar-thumb:hover {
  background: hsl(var(--p));
}

/* Responsive adjustments */
@media (max-width: 1024px) {
  .table th,
  .table td {
    padding: 0.5rem 0.75rem;
    font-size: 0.8125rem;
  }
}

/* High contrast support */
@media (prefers-contrast: high) {
  .table th {
    border-bottom: 2px solid hsl(var(--bc));
  }

  .table td {
    border-bottom: 1px solid hsl(var(--b3));
  }
}
</style>
