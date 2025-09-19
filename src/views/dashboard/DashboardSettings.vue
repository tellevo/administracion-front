<template>
  <div class="p-6 bg-base-100">
    <div class="overflow-x-auto">
      <table class="table w-full border border-base-300">
        <thead>
          <tr class="bg-base-200">
            <th>ID Ruta</th>
            <th>Fecha Pago</th>
            <th>Fecha Ruta</th>
            <th>Origen</th>
            <th>Destino</th>
            <th>Conductor</th>
            <th>ID Pasajero</th>
            <th>Pagador</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="loading">
            <td colspan="8" class="text-center py-4">
              <span class="loading loading-spinner loading-md text-primary"></span>
              <p class="mt-2 text-base-content">Cargando rutas de pago...</p>
            </td>
          </tr>
          
          <tr v-else-if="routes.length === 0">
            <td colspan="8" class="text-center py-4 text-base-content">
              No se encontraron rutas de pago
            </td>
          </tr>
          
          <tr v-for="route in routes" :key="route.rutaId" class="hover:bg-base-200">
            <td>{{ route.rutaId }}</td>
            <td>{{ formatDate(route.fechaPago) }}</td>
            <td>{{ formatDate(route.fechaRuta) }}</td>
            <td>{{ route.txtOrigen }}</td>
            <td>{{ route.txtDestino }}</td>
            <td>{{ route.nombreConductor }}</td>
            <td>{{ route.idPasajeroPago }}</td>
            <td>{{ route.nombrePagador }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const routes = ref([])
const loading = ref(true)

const formatDate = (dateString) => {
  if (!dateString) return '-'
  try {
    return new Date(dateString).toLocaleDateString()
  } catch {
    return dateString
  }
}

onMounted(async () => {
  try {
    const { data } = await axios.get('/api/dashboard/payment-routes')
    routes.value = data
  } catch (error) {
    console.error('Error al cargar rutas de pago:', error)
  } finally {
    loading.value = false
  }
})
</script>

<style>
.table th, .table td {
  padding: 0.75rem 1rem;
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
}
</style>
