<template>
    <div class="p-6 bg-base-100">
      <!-- Tarjetas de Métricas -->
      <div v-if="cards.length === 0" class="text-center py-10">
        <span class="loading loading-spinner loading-lg text-primary"></span>
        <p class="mt-4 text-base-content">Cargando métricas...</p>
      </div>
  
      <div v-else class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-5 gap-6">
        <div 
          v-for="(card, index) in cards" 
          :key="card.label" 
          class="card bg-base-200 border border-base-300 shadow-sm hover:shadow-md transition-shadow"
        >
          <div class="card-body flex flex-row items-center gap-4">
            <div class="p-3 rounded-full bg-primary text-primary-content">
              <component :is="card.icon" class="h-5 w-5" />
            </div>
            <div>
              <h2 class="text-sm font-semibold text-base-content">{{ card.label }}</h2>
              <p class="text-xl font-bold text-base-content">{{ card.value }}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue'
  import axios from 'axios'
  import { Users, Car, Route, Leaf, HandCoins } from 'lucide-vue-next'
  
  const cards = ref([])
  
  onMounted(async () => {
    try {
      const { data } = await axios.get('/api/dashboard/overview')
      
      cards.value = [
        { label: 'Usuarios Activos', value: data.usuariosActivos ?? 0, icon: Users },
        { label: 'Viajes Completados', value: data.viajesCompletados ?? 0, icon: Car },
        { label: 'Kms Compartidos', value: (data.kmsCompartidos ?? 0).toFixed(1), icon: Route },
        { label: 'CO₂ Ahorrado', value: (data.co2Ahorrado ?? 0).toFixed(1) + ' kg', icon: Leaf },
        { label: 'Pagos Realizados', value: '$' + (data.pagosRealizados ?? 0).toLocaleString(), icon: HandCoins },
      ]
      
    } catch (error) {
      console.error('Error al cargar métricas:', error)
    }
  })
  </script>