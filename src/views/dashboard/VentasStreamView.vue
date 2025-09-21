<template>
  <div class="min-h-screen bg-gradient-to-br from-base-100 to-base-200/30">
    <!-- Header - Mobile Optimized -->
    <div class="bg-base-100 shadow-sm border-b border-base-200 sticky top-0 z-10">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
        <div class="flex items-center justify-between flex-wrap gap-4">
          <div class="flex-1 min-w-0">
            <h1 class="text-xl sm:text-2xl lg:text-3xl font-bold text-base-content mb-1 truncate">
              📊 Streaming de Ventas
            </h1>
            <p class="text-sm text-base-content/70">
              Monitoreo en tiempo real de ventas entrantes
            </p>
          </div>

          <!-- Live Status & Controls -->
          <div class="flex items-center gap-3">
            <!-- Connection Status -->
            <StreamStatusIndicator
              :status="connectionStatus"
              :stats="streamStats"
              class="hidden sm:flex"
            />

            <!-- Mobile Status Badge -->
            <div class="flex sm:hidden">
              <div
                :class="[
                  'badge badge-sm font-medium px-3 py-1',
                  connectionStatus === 'connected' ? 'badge-success' : 'badge-error'
                ]"
              >
                <div class="flex items-center gap-1">
                  <div
                    :class="[
                      'w-2 h-2 rounded-full',
                      connectionStatus === 'connected' ? 'bg-green-400 animate-pulse' :
                      connectionStatus === 'connecting' ? 'bg-yellow-400 animate-pulse' :
                      'bg-red-400'
                    ]"
                  ></div>
                  <span class="text-xs">
                    {{ connectionStatus === 'connected' ? 'EN VIVO' :
                       connectionStatus === 'connecting' ? 'CONECTANDO...' : 'DESCONECTADO' }}
                  </span>
                </div>
              </div>
            </div>

            <!-- Control Buttons -->
            <div class="flex items-center gap-2">
              <!-- Pause/Resume Button -->
              <button
                @click="togglePause"
                :class="[
                  'btn btn-circle btn-sm',
                  isPaused ? 'btn-success' : 'btn-warning'
                ]"
                :title="isPaused ? 'Reanudar streaming' : 'Pausar streaming'"
              >
                <svg v-if="isPaused" xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M14.828 14.828a4 4 0 01-5.656 0M9 10h1.586a1 1 0 01.707.293l.707.707A1 1 0 0012.414 11H15m-3 3.5A2.5 2.5 0 1015.5 16H15m0 0v2m0-2h2" />
                </svg>
                <svg v-else xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 9v6m4-6v6m7-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
              </button>

              <!-- Refresh Button -->
              <button
                @click="reconnect"
                :disabled="connectionStatus === 'connecting'"
                class="btn btn-circle btn-sm btn-ghost"
                title="Reconectar"
              >
                <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
                </svg>
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Main Content -->
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
      <!-- Stats Cards - Mobile Optimized -->
      <div class="grid grid-cols-2 sm:grid-cols-4 gap-3 mb-6">
        <div class="bg-base-100 shadow-lg rounded-xl border border-base-200 p-3 hover:shadow-xl transition-all duration-300">
          <div class="flex items-center gap-2">
            <div class="w-8 h-8 bg-blue-500 rounded-full flex items-center justify-center">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
              </svg>
            </div>
            <div class="min-w-0 flex-1">
              <p class="text-xs text-base-content/70 truncate">Total Hoy</p>
              <p class="text-lg font-bold text-base-content">{{ todayStats.total }}</p>
            </div>
          </div>
        </div>

        <div class="bg-base-100 shadow-lg rounded-xl border border-base-200 p-3 hover:shadow-xl transition-all duration-300">
          <div class="flex items-center gap-2">
            <div class="w-8 h-8 bg-green-500 rounded-full flex items-center justify-center">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z" />
              </svg>
            </div>
            <div class="min-w-0 flex-1">
              <p class="text-xs text-base-content/70 truncate">Velocidad</p>
              <p class="text-lg font-bold text-green-600">{{ todayStats.velocity }}/min</p>
            </div>
          </div>
        </div>

        <div class="bg-base-100 shadow-lg rounded-xl border border-base-200 p-3 hover:shadow-xl transition-all duration-300">
          <div class="flex items-center gap-2">
            <div class="w-8 h-8 bg-purple-500 rounded-full flex items-center justify-center">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1" />
              </svg>
            </div>
            <div class="min-w-0 flex-1">
              <p class="text-xs text-base-content/70 truncate">Última Venta</p>
              <p class="text-sm font-bold text-base-content">{{ lastSaleTime }}</p>
            </div>
          </div>
        </div>

        <div class="bg-base-100 shadow-lg rounded-xl border border-base-200 p-3 hover:shadow-xl transition-all duration-300">
          <div class="flex items-center gap-2">
            <div class="w-8 h-8 bg-orange-500 rounded-full flex items-center justify-center">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" />
              </svg>
            </div>
            <div class="min-w-0 flex-1">
              <p class="text-xs text-base-content/70 truncate">Empresas</p>
              <p class="text-lg font-bold text-base-content">{{ uniqueCompanies }}</p>
            </div>
          </div>
        </div>
      </div>

      <!-- Stream Controls -->
      <div class="bg-base-100 shadow-lg rounded-xl border border-base-200 p-4 mb-6">
        <div class="flex flex-col sm:flex-row gap-4 items-start sm:items-center justify-between">
          <!-- Search & Filters -->
          <div class="flex flex-col sm:flex-row gap-3 items-start sm:items-center flex-1 w-full sm:w-auto">
            <!-- Company Search -->
            <div class="form-control flex-1 max-w-full sm:max-w-xs">
              <div class="relative">
                <input
                  v-model="searchTerm"
                  type="text"
                  placeholder="Buscar por empresa..."
                  class="input input-bordered input-sm w-full pl-9 pr-4 bg-base-50 focus:bg-base-100 transition-colors duration-200"
                  @input="debouncedFilter"
                />
                <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                  <svg class="h-4 w-4 text-base-content/40" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"/>
                  </svg>
                </div>
              </div>
            </div>

            <!-- Time Filter -->
            <div class="form-control">
              <select
                v-model="timeFilter"
                class="select select-bordered select-sm min-w-[120px] bg-base-100 border-base-300 focus:outline-none focus:ring-2 focus:ring-primary/20"
                @change="applyTimeFilter"
              >
                <option value="all">🕐 Todas</option>
                <option value="5min">📈 Últimos 5 min</option>
                <option value="1hour">📈 Última hora</option>
                <option value="today">📅 Hoy</option>
              </select>
            </div>
          </div>

          <!-- Display Toggle -->
          <div class="flex items-center gap-2">
            <button
              @click="toggleAutoScroll"
              :class="[
                'btn btn-sm',
                autoScrollEnabled ? 'btn-primary' : 'btn-ghost'
              ]"
              :title="autoScrollEnabled ? 'Desactivar auto-scroll' : 'Activar auto-scroll'"
            >
              <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 14l-7 7m0 0l-7-7m7 7V3" />
              </svg>
              Auto
            </button>
          </div>
        </div>
      </div>

      <!-- Stream Data Container -->
      <div class="bg-base-100 shadow-lg rounded-xl border border-base-200 overflow-hidden">
        <!-- Loading State -->
        <div v-if="isConnecting" class="flex items-center justify-center p-12">
          <div class="flex items-center space-x-4">
            <div class="loading loading-spinner loading-lg text-primary"></div>
            <span class="text-lg font-medium">Conectando al stream de ventas...</span>
          </div>
        </div>

        <!-- Error State -->
        <div v-else-if="connectionError" class="p-8 text-center">
          <div class="text-6xl mb-4">🔌</div>
          <h3 class="text-xl font-bold text-error mb-2">Error de Conexión</h3>
          <p class="text-base-content/70 mb-4">{{ connectionError }}</p>
          <button @click="reconnect" class="btn btn-primary">Reintentar Conexión</button>
        </div>

        <!-- No Data State -->
        <div v-else-if="filteredVentas.length === 0" class="text-center p-12">
          <div class="text-6xl mb-4">📊</div>
          <h3 class="text-xl font-bold text-base-content mb-2">Esperando ventas...</h3>
          <p class="text-base-content/70">
            Las ventas aparecerán aquí en tiempo real cuando llegue nueva información.
          </p>
        </div>

        <!-- Data Views -->
        <div v-else>
          <!-- Mobile Card View -->
          <div class="lg:hidden p-4 space-y-3 max-h-96 overflow-y-auto" ref="mobileScrollContainer">
            <VentasStreamCard
              v-for="venta in visibleVentas"
              :key="venta.id"
              :venta="venta"
              class="animate-fade-in"
            />
          </div>

          <!-- Desktop Table View -->
          <div class="hidden lg:block">
            <VentasStreamTable
              :ventas="visibleVentas"
              :auto-scroll="autoScrollEnabled && !isPaused"
              class="max-h-96 overflow-y-auto"
              ref="tableScrollContainer"
            />
          </div>
        </div>

        <!-- Load More Button (if not auto-scrolling) -->
        <div v-if="!autoScrollEnabled && hasMoreItems" class="flex justify-center p-4 border-t border-base-300">
          <button @click="loadMoreItems" class="btn btn-outline btn-sm">
            Cargar más ventas
          </button>
        </div>
      </div>
    </div>

    <!-- Mobile Floating Status -->
    <div class="fixed bottom-4 right-4 lg:hidden z-50">
      <StreamStatusIndicator
        :status="connectionStatus"
        :stats="streamStats"
        compact
      />
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onBeforeUnmount, nextTick } from 'vue'
import VentasStreamCard from '@/components/VentasStreamCard.vue'
import VentasStreamTable from '@/components/VentasStreamTable.vue'
import StreamStatusIndicator from '@/components/StreamStatusIndicator.vue'
import { ventasStreamService } from '@/services/ventasStream.js'

// Reactive data
const ventas = ref([])
const isConnecting = ref(true)
const connectionError = ref('')
const connectionStatus = ref('connecting') // 'connecting', 'connected', 'disconnected'
const isPaused = ref(false)
const searchTerm = ref('')
const timeFilter = ref('all')
const autoScrollEnabled = ref(true)
const streamStats = ref({
  total: 0,
  velocity: 0,
  uptime: 0
})

// Filters
const currentPage = ref(1)
const pageSize = ref(50)
const maxItems = ref(1000) // Prevent memory issues

// Refs
const mobileScrollContainer = ref(null)
const tableScrollContainer = ref(null)

// Computed properties
const filteredVentas = computed(() => {
  let filtered = ventas.value

  // Search filter
  if (searchTerm.value.trim()) {
    const term = searchTerm.value.toLowerCase()
    filtered = filtered.filter(venta =>
      venta.nombre_empresa.toLowerCase().includes(term) ||
      venta.email.toLowerCase().includes(term)
    )
  }

  // Time filter
  const now = new Date()
  switch (timeFilter.value) {
    case '5min':
      filtered = filtered.filter(venta => {
        const ventaTime = new Date(venta.fecha_envio)
        return (now - ventaTime) <= 5 * 60 * 1000
      })
      break
    case '1hour':
      filtered = filtered.filter(venta => {
        const ventaTime = new Date(venta.fecha_envio)
        return (now - ventaTime) <= 60 * 60 * 1000
      })
      break
    case 'today':
      const today = new Date(now.getFullYear(), now.getMonth(), now.getDate())
      filtered = filtered.filter(venta => {
        const ventaTime = new Date(venta.fecha_envio)
        return ventaTime >= today
      })
      break
  }

  return filtered
})

const visibleVentas = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return filteredVentas.value.slice(start, end)
})

const hasMoreItems = computed(() => {
  return visibleVentas.value.length < filteredVentas.value.length
})

const todayStats = computed(() => {
  const today = new Date()
  today.setHours(0, 0, 0, 0)

  const todayVentas = ventas.value.filter(venta => {
    const ventaDate = new Date(venta.fecha_envio)
    return ventaDate >= today
  })

  // Calculate velocity (sales per minute over last 5 minutes)
  const fiveMinAgo = new Date(Date.now() - 5 * 60 * 1000)
  const recentVentas = todayVentas.filter(venta => {
    const ventaDate = new Date(venta.fecha_envio)
    return ventaDate >= fiveMinAgo
  })

  return {
    total: todayVentas.length,
    velocity: Math.round((recentVentas.length / 5) * 10) / 10 // Round to 1 decimal
  }
})

const lastSaleTime = computed(() => {
  if (ventas.value.length === 0) return 'Nunca'
  const lastVenta = ventas.value[ventas.value.length - 1]
  const time = new Date(lastVenta.fecha_envio)
  const now = new Date()
  const diffMinutes = Math.floor((now - time) / (1000 * 60))

  if (diffMinutes < 1) return 'Ahora'
  if (diffMinutes < 60) return `${diffMinutes}min`
  return time.toLocaleTimeString('es-CL', { hour: '2-digit', minute: '2-digit' })
})

const uniqueCompanies = computed(() => {
  const companies = new Set(ventas.value.map(v => v.nombre_empresa))
  return companies.size
})

// Methods
let debounceTimer = null
const debouncedFilter = () => {
  clearTimeout(debounceTimer)
  debounceTimer = setTimeout(() => {
    currentPage.value = 1
  }, 300)
}

const applyTimeFilter = () => {
  currentPage.value = 1
}

const togglePause = () => {
  isPaused.value = !isPaused.value
}

const toggleAutoScroll = () => {
  autoScrollEnabled.value = !autoScrollEnabled.value
  if (autoScrollEnabled.value) {
    scrollToBottom()
  }
}

const reconnect = () => {
  // Clear existing data for fresh start on manual reconnect
  ventas.value = []
  currentPage.value = 1
  streamStats.value = {
    total: 0,
    velocity: 0,
    uptime: 0
  }

  connectionStatus.value = 'connecting'
  isConnecting.value = true
  connectionError.value = ''

  ventasStreamService.reconnect()
}

const loadMoreItems = () => {
  if (hasMoreItems.value) {
    currentPage.value++
  }
}

const scrollToBottom = () => {
  nextTick(() => {
    const container = autoScrollEnabled.value ? mobileScrollContainer.value || tableScrollContainer.value : null
    if (container && !isPaused.value) {
      container.scrollTop = container.scrollHeight
    }
  })
}

const handleNewVenta = (venta) => {
  // Add new venta to the beginning (most recent first)
  ventas.value.unshift(venta)

  // Keep only the most recent items to prevent memory issues
  if (ventas.value.length > maxItems.value) {
    ventas.value.splice(maxItems.value)
  }

  // Update stats
  streamStats.value.total = ventas.value.length

  // Auto-scroll if enabled
  if (autoScrollEnabled.value && !isPaused.value) {
    scrollToBottom()
  }

  // Update velocity calculation
  const now = new Date()
  const fiveMinAgo = new Date(now - 5 * 60 * 1000)
  const recentVentas = ventas.value.filter(v => new Date(v.fecha_envio) >= fiveMinAgo)
  streamStats.value.velocity = Math.round((recentVentas.length / 5) * 10) / 10
}

const handleConnectionStatusChange = (status) => {
  connectionStatus.value = status
  isConnecting.value = status === 'connecting'

  if (status === 'connected') {
    connectionError.value = ''
  } else if (status === 'disconnected') {
    // Check if we have data - if so, it was a successful stream completion
    // If no data, it was a connection error
    if (ventas.value.length > 0) {
      connectionError.value = '' // Successful completion, show the data
    } else {
      connectionError.value = 'Conexión perdida. Intentando reconectar...'
    }
  }
}

const handleConnectionError = (error) => {
  connectionError.value = error.message || 'Error de conexión'
  connectionStatus.value = 'disconnected'
}

// Lifecycle
onMounted(() => {
  // Initialize WebSocket connection
  ventasStreamService.connect()
  ventasStreamService.onStatusChange(handleConnectionStatusChange)
  ventasStreamService.onVenta(handleNewVenta)
  ventasStreamService.onError(handleConnectionError)
})

onBeforeUnmount(() => {
  ventasStreamService.disconnect()
  if (debounceTimer) {
    clearTimeout(debounceTimer)
  }
})

// In development, simulate connection delay if needed
if (import.meta.env.DEV) {
  // Optional: Add development-specific behavior here if needed
  // For now, let the real WebSocket connection handle everything
}
</script>

<style scoped>
/* Custom animations */
@keyframes fade-in {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

.animate-fade-in {
  animation: fade-in 0.3s ease-out;
}

/* Custom scrollbar */
.max-h-96::-webkit-scrollbar {
  width: 6px;
}

.max-h-96::-webkit-scrollbar-track {
  background: hsl(var(--b2));
  border-radius: 3px;
}

.max-h-96::-webkit-scrollbar-thumb {
  background: hsl(var(--b3));
  border-radius: 3px;
}

.max-h-96::-webkit-scrollbar-thumb:hover {
  background: hsl(var(--p));
}

/* Mobile optimizations */
@media (max-width: 640px) {
  .max-h-96 {
    max-height: 60vh;
  }
}
</style>
