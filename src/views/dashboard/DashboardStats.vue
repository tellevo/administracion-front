<template>
  <div class="p-4 min-h-[calc(100vh-4rem)] bg-base-100">
    <!-- Encabezado -->
    <div class="text-center mb-8 pt-6">
      <h1 class="text-3xl font-bold text-primary mb-2">Estadísticas de Viajes</h1>
      <p class="text-lg text-base-content/80">Visualización de viajes compartidos</p>
    </div>

    <!-- Selector de período -->
    <div class="flex justify-center mb-8">
      <div class="tabs tabs-boxed bg-base-200/50 shadow-inner">
        <button 
          class="tab tab-lg font-medium" 
          :class="{ 'tab-active !bg-primary !text-primary-content': activeTab === 'semanal' }"
          @click="cambiarVista('semanal')"
        >
          Semanal
        </button> 
        <button 
          class="tab tab-lg font-medium" 
          :class="{ 'tab-active !bg-primary !text-primary-content': activeTab === 'mensual' }"
          @click="cambiarVista('mensual')"
        >
          Mensual
        </button>
      </div>
    </div>

    <!-- Tarjeta principal -->
    <div class="bg-base-100 rounded-xl p-6 min-h-[400px]">
      <div v-if="loading" class="flex flex-col items-center justify-center h-full">
        <span class="loading loading-dots loading-lg text-primary mb-4"></span>
        <p class="text-base-content/70">Cargando tus estadísticas...</p>
      </div>

      <div v-else class="space-y-8">
        <!-- Tarjetas de resumen -->
        <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
          <div class="stats bg-primary/5 hover:shadow-lg transition-all border border-base-300">
            <div class="stat">
              <div class="stat-title text-base-content/70">Viajes totales</div>
              <div class="stat-value text-primary">{{ totalViajes }}</div>
              <div class="stat-desc text-base-content/50">{{ activeTab === 'semanal' ? 'Esta semana' : 'Este mes' }}</div>
            </div>
          </div>
          
          <div class="stats bg-success/5 hover:shadow-lg transition-all border border-base-300">
            <div class="stat">
              <div class="stat-title text-base-content/70">CO₂ Ahorrado</div>
              <div class="stat-value text-success">{{ totalCO2 }} kg</div>
              <div class="stat-desc text-base-content/50">≈ {{ equivalentCO2 }}</div>
            </div>
          </div>
          
          <div class="stats bg-info/5 hover:shadow-lg transition-all border border-base-300">
            <div class="stat">
              <div class="stat-title text-base-content/70">{{ activeTab === 'semanal' ? 'Día más activo' : 'Semana más activa' }}</div>
              <div class="stat-value text-info">{{ diaPico }}</div>
              <div class="stat-desc text-base-content/50">{{ viajesPico }} viajes</div>
            </div>
          </div>
        </div>

        <!-- Gráfico -->
        <div class="h-[400px] -mx-2">
          <h3 class="text-xl font-semibold text-base-content mb-4 text-center">
            Comparativa {{ activeTab === 'semanal' ? 'semanal' : 'mensual' }}
          </h3>
          <div class="relative h-full w-full bg-base-100 rounded-lg border border-base-300 p-2">
            <CombinedChart 
    :chartData="data" 
    :chartOptions="chartOptions"
    :key="`chart-${activeTab}-${themeKey}`"
  />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, computed, onMounted, watch } from 'vue'
import CombinedChart from '../../components/charts/CombinedChart.vue'

const loading = ref(true)
const activeTab = ref('semanal')
const themeKey = ref(0) // Para forzar recarga del gráfico al cambiar tema

// Datos simulados
const datosSemana = {
  labels: ['Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado', 'Domingo'],
  viajes: [12, 18, 21, 25, 17, 10, 5],
  co2: [3.1, 4.7, 5.2, 6.3, 4.2, 2.1, 1.0]
}

const datosMes = {
  labels: ['Sem 1', 'Sem 2', 'Sem 3', 'Sem 4'],
  viajes: [82, 95, 107, 76],
  co2: [24.5, 28.3, 31.8, 22.1]
}

const data = reactive({
  labels: [],
  datasets: []
})

// Detectar cambio de tema
watch(() => document.documentElement.getAttribute('data-theme'), () => {
  themeKey.value++ // Forzar recarga del gráfico
})

// Opciones del gráfico adaptadas al modo oscuro
const chartOptions = computed(() => {
  const isDark = document.documentElement.getAttribute('data-theme') === 'dark'
  
  return {
    responsive: true,
    maintainAspectRatio: false,
    animation: {
      duration: 800,
      easing: 'easeOutQuart'
    },
    plugins: {
      legend: { 
        position: 'top',
        labels: {
          color: isDark ? '#e5e7eb' : '#1f2937',
          font: {
            family: "'Inter', sans-serif",
            size: 12,
            weight: '500'
          },
          boxWidth: 12,
          padding: 16,
          usePointStyle: true,
          pointStyle: 'circle'
        }
      },
      tooltip: {
        backgroundColor: isDark ? '#1e293b' : '#ffffff',
        titleColor: isDark ? '#f3f4f6' : '#111827',
        bodyColor: isDark ? '#f3f4f6' : '#111827',
        borderColor: isDark ? '#334155' : '#d1d5db',
        borderWidth: 1,
        padding: 12,
        callbacks: {
          label: function(context) {
            let label = context.dataset.label || ''
            if (label) label += ': '
            label += context.raw + (context.dataset.type === 'line' ? ' kg' : '')
            return label
          }
        }
      }
    },
    scales: {
      y: {
        beginAtZero: true,
        grid: {
          color: isDark ? '#4b556380' : '#e5e7eb80',
          drawBorder: false
        },
        ticks: {
          color: isDark ? '#e5e7ebCC' : '#1f2937CC',
          font: {
            family: "'Inter', sans-serif"
          }
        }
      },
      x: {
        grid: {
          color: isDark ? '#4b556340' : '#e5e7eb40',
          drawBorder: false
        },
        ticks: {
          color: isDark ? '#e5e7ebCC' : '#1f2937CC',
          font: {
            family: "'Inter', sans-serif"
          }
        }
      }
    }
  }
})

// Datos calculados
const totalViajes = computed(() => data.datasets[0]?.data?.reduce((a, b) => a + b, 0) || 0)
const totalCO2 = computed(() => data.datasets[1]?.data?.reduce((a, b) => a + b, 0).toFixed(1) || 0)

const equivalentCO2 = computed(() => {
  const kg = parseFloat(totalCO2.value)
  if (kg > 1000) return `Equivale a ${(kg/1000).toFixed(1)} toneladas`
  if (kg > 100) return `Equivale a ${Math.round(kg/5)} árboles plantados`
  return `Equivale a ${Math.round(kg*5)} km en auto`
})

const diaPico = computed(() => {
  if (!data.labels?.length) return '-'
  const maxIndex = data.datasets[0]?.data?.indexOf(Math.max(...data.datasets[0].data))
  return data.labels[maxIndex]
})

const viajesPico = computed(() => {
  if (!data.datasets[0]?.data?.length) return 0
  return Math.max(...data.datasets[0].data)
})

// Cargar datos
const cargarDatos = async (tipo) => {
  loading.value = true
  try {
    await new Promise((r) => setTimeout(r, 800))
    
    const datos = tipo === 'semanal' ? datosSemana : datosMes
    const isDark = document.documentElement.getAttribute('data-theme') === 'dark'

    data.labels = datos.labels
    data.datasets = [
      {
        type: 'bar',
        label: 'Viajes realizados',
        backgroundColor: isDark ? 'rgba(59, 130, 246, 0.7)' : 'rgba(37, 99, 235, 0.7)',
        borderColor: isDark ? '#93c5fd' : '#1d4ed8',
        borderWidth: 1,
        borderRadius: 4,
        data: datos.viajes
      },
      {
        type: 'line',
        label: 'CO₂ Ahorrado',
        borderColor: isDark ? '#86efac' : '#16a34a',
        borderWidth: 3,
        pointBackgroundColor: isDark ? '#86efac' : '#16a34a',
        pointRadius: 5,
        pointHoverRadius: 7,
        fill: false,
        tension: 0.3,
        data: datos.co2
      }
    ]
  } catch (e) {
    console.error('Error al cargar datos:', e)
  } finally {
    loading.value = false
  }
}

const cambiarVista = (tipo) => {
  activeTab.value = tipo
  cargarDatos(tipo)
}

onMounted(() => cargarDatos('semanal'))
</script>

<style>
.tab {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.stats {
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.stats:hover {
  transform: translateY(-2px);
}

.chartjs-render-monitor {
  font-family: 'Inter', sans-serif;
}

canvas {
  display: block;
  width: 100% !important;
  height: 100% !important;
}
</style>
