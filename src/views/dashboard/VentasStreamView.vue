<template>
  <div class="min-h-screen bg-gradient-to-br from-base-100 to-base-200/30">
    <!-- Header - Mobile Optimized -->
    <div class="bg-base-100 shadow-sm border-b border-base-200 sticky top-0 z-10">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
        <div class="flex items-center justify-between flex-wrap gap-4">
          <div class="flex-1 min-w-0">
            <h1 class="text-xl sm:text-2xl lg:text-3xl font-bold text-base-content mb-1 truncate">
              👥 Monitoreo de Candidatos
            </h1>
            <p class="text-sm text-base-content/70">
              Monitoreo de candidatos interesados en las aplicaciones de TeLlevo
            </p>
          </div>

          <!-- Control Buttons -->
          <div class="flex items-center gap-2">
            <!-- Refresh Button -->
            <button
              @click="reconnect"
              class="btn btn-circle btn-sm btn-ghost"
              title="Actualizar candidatos"
            >
              <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
              </svg>
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Main Content -->
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
      <!-- Stats Cards - Mobile Optimized -->
      <div class="grid grid-cols-2 sm:grid-cols-3 gap-3 mb-6">
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
            <div class="w-8 h-8 bg-purple-500 rounded-full flex items-center justify-center">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1" />
              </svg>
            </div>
            <div class="min-w-0 flex-1">
              <p class="text-xs text-base-content/70 truncate">Último Candidato</p>
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
        <div v-if="isLoading" class="flex items-center justify-center p-12">
          <div class="flex items-center space-x-4">
            <div class="loading loading-spinner loading-lg text-primary"></div>
            <span class="text-lg font-medium">Cargando candidatos...</span>
          </div>
        </div>

        <!-- No Data State -->
        <div v-else-if="filteredCandidatos.length === 0" class="text-center p-12">
          <div class="text-6xl mb-4">📊</div>
          <h3 class="text-xl font-bold text-base-content mb-2">Esperando candidatos...</h3>
          <p class="text-base-content/70">
            Los candidatos aparecerán aquí cuando llegue nueva información.
          </p>
        </div>

        <!-- Data Views -->
        <div v-else>
          <!-- Mobile Card View -->
          <div class="lg:hidden p-4 space-y-3 max-h-96 overflow-y-auto" ref="mobileScrollContainer">
            <VentasStreamCard
              v-for="candidato in visibleCandidatos"
              :key="candidato.id"
              :candidato="candidato"
              class="animate-fade-in"
            />
          </div>

          <!-- Desktop Table View -->
          <div class="hidden lg:block">
            <VentasStreamTable
              :candidatos="visibleCandidatos"
              :auto-scroll="autoScrollEnabled"
              class="max-h-96 overflow-y-auto"
              ref="tableScrollContainer"
            />
          </div>
        </div>

        <!-- Load More Button (if not auto-scrolling) -->
        <div v-if="!autoScrollEnabled && hasMoreItems" class="flex justify-center p-4 border-t border-base-300">
          <button @click="loadMoreItems" class="btn btn-outline btn-sm">
            Cargar más candidatos
          </button>
        </div>
      </div>
    </div>


  </div>
</template>

<script setup>
import { ref, computed, onMounted, onBeforeUnmount, nextTick } from 'vue'
import VentasStreamCard from '@/components/VentasStreamCard.vue'
import VentasStreamTable from '@/components/VentasStreamTable.vue'
import { ventasStreamService } from '@/services/ventasStream.js'

// Reactive data
const candidatos = ref([])
const isLoading = ref(true)
const searchTerm = ref('')
const timeFilter = ref('all')
const autoScrollEnabled = ref(true)

// Filters
const currentPage = ref(1)
const pageSize = ref(50)
const maxItems = ref(1000) // Prevent memory issues

// Refs
const mobileScrollContainer = ref(null)
const tableScrollContainer = ref(null)

// Computed properties
const filteredCandidatos = computed(() => {
  let filtered = candidatos.value

  // Search filter
  if (searchTerm.value.trim()) {
    const term = searchTerm.value.toLowerCase()
    filtered = filtered.filter(candidato =>
      candidato.nombre_empresa.toLowerCase().includes(term) ||
      candidato.email.toLowerCase().includes(term)
    )
  }

  // Time filter
  const now = new Date()
  switch (timeFilter.value) {
    case '5min':
      filtered = filtered.filter(candidato => {
        const candidatoTime = new Date(candidato.fecha_envio)
        return (now - candidatoTime) <= 5 * 60 * 1000
      })
      break
    case '1hour':
      filtered = filtered.filter(candidato => {
        const candidatoTime = new Date(candidato.fecha_envio)
        return (now - candidatoTime) <= 60 * 60 * 1000
      })
      break
    case 'today':
      const today = new Date(now.getFullYear(), now.getMonth(), now.getDate())
      filtered = filtered.filter(candidato => {
        const candidatoTime = new Date(candidato.fecha_envio)
        return candidatoTime >= today
      })
      break
  }

  return filtered
})

const visibleCandidatos = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return filteredCandidatos.value.slice(start, end)
})

const hasMoreItems = computed(() => {
  return visibleCandidatos.value.length < filteredCandidatos.value.length
})

const todayStats = computed(() => {
  const today = new Date()
  today.setHours(0, 0, 0, 0)

  const todayCandidatos = candidatos.value.filter(candidato => {
    const candidatoDate = new Date(candidato.fecha_envio)
    return candidatoDate >= today
  })

  return {
    total: todayCandidatos.length
  }
})

const lastSaleTime = computed(() => {
  if (candidatos.value.length === 0) return 'Nunca'
  const lastCandidato = candidatos.value[candidatos.value.length - 1]
  const time = new Date(lastCandidato.fecha_envio)
  const now = new Date()
  const diffMinutes = Math.floor((now - time) / (1000 * 60))

  if (diffMinutes < 1) return 'Ahora'
  if (diffMinutes < 60) return `${diffMinutes}min`
  return time.toLocaleTimeString('es-CL', { hour: '2-digit', minute: '2-digit' })
})

const uniqueCompanies = computed(() => {
  const companies = new Set(candidatos.value.map(c => c.nombre_empresa))
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

const toggleAutoScroll = () => {
  autoScrollEnabled.value = !autoScrollEnabled.value
  if (autoScrollEnabled.value) {
    scrollToBottom()
  }
}

const reconnect = () => {
  // Clear existing data for fresh start
  candidatos.value = []
  currentPage.value = 1
  isLoading.value = true

  // Force WebSocket reconnection
  try {
    ventasStreamService.reconnect()

    // Hide loading after reconnection attempt
    setTimeout(() => {
      isLoading.value = false
    }, 1000)
  } catch (error) {
    console.warn('Reconnection failed:', error)
    isLoading.value = false
  }
}

const loadMoreItems = () => {
  if (hasMoreItems.value) {
    currentPage.value++
  }
}

const scrollToBottom = () => {
  nextTick(() => {
    const container = autoScrollEnabled.value ? mobileScrollContainer.value || tableScrollContainer.value : null
    if (container) {
      container.scrollTop = container.scrollHeight
    }
  })
}

const handleNewCandidato = (candidato) => {
  // Add new candidato to the beginning (most recent first)
  candidatos.value.unshift(candidato)

  // Keep only the most recent items to prevent memory issues
  if (candidatos.value.length > maxItems.value) {
    candidatos.value.splice(maxItems.value)
  }

  // Auto-scroll if enabled
  if (autoScrollEnabled.value) {
    scrollToBottom()
  }
}

// Lifecycle
onMounted(() => {
  isLoading.value = false

  // Initialize WebSocket connection for real candidate data
  try {
    ventasStreamService.connect()
    ventasStreamService.onCandidato(handleNewCandidato)

    // Set a fallback if no connection after a delay
    setTimeout(() => {
      if (candidatos.value.length === 0) {
        isLoading.value = false
      }
    }, 2000)
  } catch (error) {
    console.warn('WebSocket connection failed, showing empty state:', error)
    isLoading.value = false
  }
})

onBeforeUnmount(() => {
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
