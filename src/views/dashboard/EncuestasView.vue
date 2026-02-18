<template>
  <div class="space-y-6">
    <!-- Header -->
    <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between">
      <div>
        <h1 class="text-2xl font-bold text-base-content">Encuestas</h1>
        <p class="mt-1 text-sm text-base-content/70">
          Datos de encuestas de movilidad sostenible
        </p>
      </div>
      <div class="mt-4 sm:mt-0">
        <button
          @click="refreshData"
          :disabled="loading"
          class="btn btn-outline bg-base-100"
        >
          <svg v-if="loading" class="animate-spin -ml-1 mr-3 h-5 w-5" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
            <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
            <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
          </svg>
          {{ loading ? 'Cargando...' : 'Actualizar' }}
        </button>
      </div>
    </div>

    <!-- Loading State -->
    <div v-if="loading && !encuestas.length" class="flex justify-center items-center py-12">
      <div class="loading loading-spinner loading-lg text-primary"></div>
      <span class="ml-3 text-base-content/70">Cargando encuestas...</span>
    </div>

    <!-- Error State -->
    <div v-else-if="error" class="alert alert-error">
      <div class="flex">
        <div class="flex-shrink-0">
          <svg class="h-5 w-5 text-error-content" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor">
            <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clip-rule="evenodd" />
          </svg>
        </div>
        <div class="ml-3">
          <h3 class="text-sm font-medium text-error-content">Error al cargar encuestas</h3>
          <div class="mt-2 text-sm text-error-content/80">{{ error }}</div>
        </div>
      </div>
    </div>

    <!-- Data Table -->
    <div v-else class="bg-base-100 shadow overflow-hidden sm:rounded-md border border-base-200">
      <div class="px-4 py-5 sm:p-6">
        <div class="overflow-x-auto">
          <table class="table w-full">
            <thead class="bg-base-200">
              <tr>
                <th class="px-6 py-3 text-left text-xs font-medium text-base-content/70 uppercase tracking-wider">
                  Fecha
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-base-content/70 uppercase tracking-wider">
                  Email
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-base-content/70 uppercase tracking-wider">
                  Empresa
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-base-content/70 uppercase tracking-wider">
                  Tipo Transporte
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-base-content/70 uppercase tracking-wider">
                  Origen
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-base-content/70 uppercase tracking-wider">
                  Destino
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-base-content/70 uppercase tracking-wider">
                  Días Específicos
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-base-content/70 uppercase tracking-wider">
                  Combustión
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-base-content/70 uppercase tracking-wider">
                  Tiempo Trabajo
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-base-content/70 uppercase tracking-wider">
                  Compartir Vehículo
                </th>
              </tr>
            </thead>
            <tbody class="bg-base-100 divide-y divide-base-200">
              <tr v-for="(encuesta, index) in encuestas" :key="index" class="hover:bg-base-200/50">
                <td class="px-6 py-4 whitespace-nowrap text-sm text-base-content">
                  {{ formatDate(encuesta.FECHA) }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-base-content">
                  {{ encuesta.EMAIL }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-base-content">
                  {{ encuesta.EMPRESA }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-base-content">
                  {{ encuesta.TIPO_TRANSPORTE }}
                </td>
                <td class="px-6 py-4 text-sm text-base-content max-w-xs truncate">
                  {{ encuesta.ORIGEN }}
                </td>
                <td class="px-6 py-4 text-sm text-base-content max-w-xs truncate">
                  {{ encuesta.DESTINO }}
                </td>
                <td class="px-6 py-4 text-sm text-base-content">
                  {{ encuesta.DIAS_ESPECIFICOS }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-base-content">
                  {{ encuesta.COMBUSTION }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-base-content">
                  {{ encuesta.TIEMPO_TRABAJO }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-base-content">
                  {{ encuesta.COMPARTIR_VEHICULO || 'No' }}
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- No Data State -->
        <div v-if="!loading && !error && !encuestas.length" class="text-center py-12">
          <div class="text-6xl mb-4 text-base-content/20">📋</div>
          <h3 class="mt-2 text-sm font-medium text-base-content">No hay encuestas</h3>
          <p class="mt-1 text-sm text-base-content/60">No se encontraron datos de encuestas.</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const encuestas = ref([])
const loading = ref(false)
const error = ref('')

const fetchEncuestas = async () => {
  try {
    loading.value = true
    error.value = ''

    const response = await axios.get('https://ms-encuesta.tellevoapp.com/api/v1/encuestas')
    encuestas.value = response.data

  } catch (err) {
    error.value = err.response?.data?.message || 'Error al cargar las encuestas'
    console.error('Error fetching encuestas:', err)
  } finally {
    loading.value = false
  }
}

const refreshData = () => {
  fetchEncuestas()
}

const formatDate = (dateString) => {
  if (!dateString) return '-'
  try {
    return new Date(dateString).toLocaleDateString('es-ES', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    })
  } catch {
    return dateString
  }
}

onMounted(() => {
  fetchEncuestas()
})
</script>

<style scoped>
/* Custom scrollbar for table */
.overflow-x-auto::-webkit-scrollbar {
  height: 8px;
}

.overflow-x-auto::-webkit-scrollbar-track {
  background: #f1f5f9;
  border-radius: 4px;
}

.overflow-x-auto::-webkit-scrollbar-thumb {
  background: #cbd5e1;
  border-radius: 4px;
}

.overflow-x-auto::-webkit-scrollbar-thumb:hover {
  background: #94a3b8;
}

/* Table cell truncation */
.max-w-xs {
  max-width: 200px;
}

.truncate {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>