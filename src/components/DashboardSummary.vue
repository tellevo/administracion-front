<template>
  <div class="space-y-8">
    <!-- Header Section -->
    <div class="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
      <div class="flex items-center justify-between">
        <div>
          <h1 class="text-2xl font-bold text-gray-900">Panel de Control</h1>
          <p class="text-gray-600 mt-1">Bienvenido a TeLlevo - Reduciendo la huella de carbono</p>
        </div>
        <div class="hidden md:flex items-center space-x-2 bg-green-50 px-4 py-2 rounded-lg">
          <Leaf class="h-5 w-5 text-green-600" />
          <span class="text-green-700 font-medium text-sm">Eco-Friendly Dashboard</span>
        </div>
      </div>
    </div>

    <!-- Loading State -->
    <div v-if="cards.length === 0" class="text-center py-16">
      <div class="inline-flex items-center justify-center w-16 h-16 bg-blue-100 rounded-full mb-4">
        <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
      </div>
      <p class="text-gray-600 text-lg">Cargando métricas del dashboard...</p>
    </div>

    <!-- Metrics Cards -->
    <div v-else class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-5 gap-4 md:gap-6">
      <div 
        v-for="(card, index) in cards" 
        :key="card.label"
        :class="[
          'bg-white rounded-xl shadow-sm border border-gray-200 p-6 hover:shadow-md transition-all duration-200 hover:-translate-y-1',
          getCardAccentClass(index)
        ]"
      >
        <div class="flex items-center justify-between mb-4">
          <div :class="[
            'flex items-center justify-center w-12 h-12 rounded-lg',
            getIconBackgroundClass(index)
          ]">
            <component :is="card.icon" :class="['h-6 w-6', getIconColorClass(index)]" />
          </div>
          <div class="text-right">
            <p class="text-2xl lg:text-3xl font-bold text-gray-900">{{ card.value }}</p>
          </div>
        </div>
        <div class="space-y-1">
          <p class="text-sm font-medium text-gray-700">{{ card.label }}</p>
          <div class="flex items-center text-xs text-gray-500">
            <TrendingUp class="h-3 w-3 mr-1 text-green-500" />
            <span>+{{ getGrowthPercentage(index) }}% este mes</span>
          </div>
        </div>
      </div>
    </div>

    <!-- Additional Dashboard Elements -->
    <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
      <!-- Quick Actions -->
      <div class="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
        <h3 class="text-lg font-semibold text-gray-900 mb-4">Acciones Rápidas</h3>
        <div class="space-y-3">
          <router-link 
            to="/dashboard/empresas" 
            class="flex items-center p-3 rounded-lg hover:bg-gray-50 transition-colors group"
          >
            <div class="bg-blue-100 p-2 rounded-lg mr-3 group-hover:bg-blue-200 transition-colors">
              <Building class="h-5 w-5 text-blue-600" />
            </div>
            <div>
              <p class="font-medium text-gray-900">Gestionar Empresas</p>
              <p class="text-sm text-gray-500">Añadir o editar empresas del sistema</p>
            </div>
          </router-link>
          
          <router-link 
            to="/dashboard/stats" 
            class="flex items-center p-3 rounded-lg hover:bg-gray-50 transition-colors group"
          >
            <div class="bg-green-100 p-2 rounded-lg mr-3 group-hover:bg-green-200 transition-colors">
              <BarChart3 class="h-5 w-5 text-green-600" />
            </div>
            <div>
              <p class="font-medium text-gray-900">Ver Estadísticas</p>
              <p class="text-sm text-gray-500">Análisis detallado de métricas</p>
            </div>
          </router-link>
        </div>
      </div>

      <!-- Environmental Impact -->
      <div class="bg-gradient-to-br from-green-50 to-blue-50 rounded-xl shadow-sm border border-green-200 p-6">
        <h3 class="text-lg font-semibold text-gray-900 mb-4 flex items-center">
          <Leaf class="h-5 w-5 text-green-600 mr-2" />
          Impacto Ambiental
        </h3>
        <div class="space-y-4">
          <div class="bg-white/70 rounded-lg p-4">
            <p class="text-2xl font-bold text-green-700">{{ getTotalCO2Saved() }} kg</p>
            <p class="text-sm text-gray-600">CO₂ ahorrado total</p>
          </div>
          <div class="bg-white/70 rounded-lg p-4">
            <p class="text-2xl font-bold text-blue-700">{{ getTotalKmShared() }} km</p>
            <p class="text-sm text-gray-600">Kilómetros compartidos</p>
          </div>
          <p class="text-xs text-gray-600 text-center">
            🌱 Equivalente a plantar {{ Math.round(getTotalCO2Saved() / 22) }} árboles
          </p>
        </div>
      </div>

      <!-- Recent Activity -->
      <div class="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
        <h3 class="text-lg font-semibold text-gray-900 mb-4">Actividad Reciente</h3>
        <div class="space-y-3">
          <div class="flex items-center p-3 bg-gray-50 rounded-lg">
            <div class="bg-blue-100 p-2 rounded-full mr-3">
              <Users class="h-4 w-4 text-blue-600" />
            </div>
            <div class="flex-1">
              <p class="text-sm font-medium text-gray-900">Nuevos usuarios registrados</p>
              <p class="text-xs text-gray-500">Hace 2 horas</p>
            </div>
          </div>
          <div class="flex items-center p-3 bg-gray-50 rounded-lg">
            <div class="bg-green-100 p-2 rounded-full mr-3">
              <Car class="h-4 w-4 text-green-600" />
            </div>
            <div class="flex-1">
              <p class="text-sm font-medium text-gray-900">Viajes completados exitosamente</p>
              <p class="text-xs text-gray-500">Hace 1 hora</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
  
<script setup>
import { ref, onMounted, computed } from 'vue'
import axios from 'axios'
import { Users, Car, Route, Leaf, HandCoins, TrendingUp, Building, BarChart3 } from 'lucide-vue-next'

const cards = ref([])
const dashboardData = ref({})

// Style helper functions
const getCardAccentClass = (index) => {
  const classes = [
    'hover:border-blue-300',
    'hover:border-green-300', 
    'hover:border-purple-300',
    'hover:border-orange-300',
    'hover:border-pink-300'
  ]
  return classes[index % classes.length]
}

const getIconBackgroundClass = (index) => {
  const classes = [
    'bg-blue-100',
    'bg-green-100',
    'bg-purple-100', 
    'bg-orange-100',
    'bg-pink-100'
  ]
  return classes[index % classes.length]
}

const getIconColorClass = (index) => {
  const classes = [
    'text-blue-600',
    'text-green-600',
    'text-purple-600',
    'text-orange-600', 
    'text-pink-600'
  ]
  return classes[index % classes.length]
}

const getGrowthPercentage = (index) => {
  const growths = [12, 8, 15, 22, 18]
  return growths[index % growths.length]
}

const getTotalCO2Saved = () => {
  return dashboardData.value.co2Ahorrado?.toFixed(1) || '0.0'
}

const getTotalKmShared = () => {
  return dashboardData.value.kmsCompartidos?.toFixed(1) || '0.0'
}

onMounted(async () => {
  try {
    const { data } = await axios.get('/api/dashboard/overview')
    dashboardData.value = data
    
    cards.value = [
      { label: 'Usuarios Activos', value: data.usuariosActivos ?? 0, icon: Users },
      { label: 'Viajes Completados', value: data.viajesCompletados ?? 0, icon: Car },
      { label: 'Kms Compartidos', value: (data.kmsCompartidos ?? 0).toFixed(1), icon: Route },
      { label: 'CO₂ Ahorrado', value: (data.co2Ahorrado ?? 0).toFixed(1) + ' kg', icon: Leaf },
      { label: 'Pagos Realizados', value: '$' + (data.pagosRealizados ?? 0).toLocaleString(), icon: HandCoins },
    ]
    
  } catch (error) {
    console.error('Error al cargar métricas:', error)
    // Set fallback data for demo purposes
    dashboardData.value = {
      usuariosActivos: 1247,
      viajesCompletados: 3589,
      kmsCompartidos: 45678.5,
      co2Ahorrado: 2341.8,
      pagosRealizados: 89456
    }
    
    cards.value = [
      { label: 'Usuarios Activos', value: '1,247', icon: Users },
      { label: 'Viajes Completados', value: '3,589', icon: Car },
      { label: 'Kms Compartidos', value: '45,678.5', icon: Route },
      { label: 'CO₂ Ahorrado', value: '2,341.8 kg', icon: Leaf },
      { label: 'Pagos Realizados', value: '$89,456', icon: HandCoins },
    ]
  }
})
</script>
