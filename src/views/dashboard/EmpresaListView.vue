<template>
  <div class="min-h-screen bg-base-100 p-6">
    <!-- Header -->
    <div class="flex items-center justify-between mb-8">
      <div>
        <h1 class="text-3xl font-bold text-base-content mb-2">
          Gestionar Empresas
        </h1>
        <p class="text-base-content/70">
          Lista completa de empresas registradas
        </p>
      </div>

      <!-- Actions -->
      <div class="flex items-center gap-4">
        <!-- Breadcrumb -->
        <div class="text-sm breadcrumbs">
          <ul>
            <li>
              <router-link to="/dashboard" class="text-base-content/60 hover:text-primary">
                Dashboard
              </router-link>
            </li>
            <li class="text-base-content/60">Empresas</li>
          </ul>
        </div>

        <!-- New Empresa Button -->
        <router-link to="/dashboard/empresa" class="btn btn-primary">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
          </svg>
          Nueva Empresa
        </router-link>
      </div>
    </div>

    <!-- Filters and Search -->
    <div class="bg-base-100 shadow-lg rounded-2xl border border-base-300 p-6 mb-6">
      <div class="flex flex-col md:flex-row gap-4 items-start md:items-center justify-between">
        <div class="flex flex-1 gap-4 items-center">
          <!-- Search Input -->
          <div class="form-control flex-1 max-w-xs">
            <div class="input-group">
              <input
                v-model="searchTerm"
                type="text"
                placeholder="Buscar empresas..."
                class="input input-bordered"
              />
              <button class="btn btn-square">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                </svg>
              </button>
            </div>
          </div>

          <!-- Filter by Status -->
          <select v-model="filterStatus" class="select select-bordered">
            <option value="all">Todos</option>
            <option value="active">Activos</option>
            <option value="inactive">Inactivos</option>
          </select>
        </div>

        <!-- Refresh Button -->
        <button
          @click="loadEmpresas"
          :disabled="loading"
          class="btn btn-outline"
        >
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
          </svg>
          Actualizar
        </button>
      </div>
    </div>

    <!-- Companies Table -->
    <div class="bg-base-100 shadow-lg rounded-2xl border border-base-300 overflow-hidden">
      <!-- Loading State -->
      <div v-if="loading" class="flex items-center justify-center p-12">
        <div class="flex items-center space-x-4">
          <div class="loading loading-spinner loading-lg"></div>
          <span class="text-lg font-medium">Cargando empresas...</span>
        </div>
      </div>

      <!-- Error State -->
      <div v-else-if="error" class="p-8 text-center">
        <div class="text-6xl mb-4">😞</div>
        <h3 class="text-xl font-bold text-error mb-2">Error al cargar empresas</h3>
        <p class="text-base-content/70 mb-4">{{ error }}</p>
        <button @click="loadEmpresas" class="btn btn-primary">Reintentar</button>
      </div>

      <!-- Table -->
      <div v-else-if="empresas.length > 0" class="overflow-x-auto">
        <table class="table table-zebra w-full">
          <!-- Table Header -->
          <thead>
            <tr class="bg-base-200">
              <th class="w-16">#</th>
              <th class="w-32">Logo</th>
              <th>Nombre</th>
              <th>Dominio</th>
              <th class="w-32">País</th>
              <th class="w-32">Estado</th>
              <th class="w-48 text-center">Acciones</th>
            </tr>
          </thead>

          <!-- Table Body -->
          <tbody>
            <tr v-for="(empresa, index) in filteredEmpresas" :key="empresa.id" class="hover">
              <!-- ID -->
              <td class="font-mono text-sm">{{ empresa.id }}</td>

              <!-- Logo -->
              <td>
                <div class="avatar">
                  <div class="w-10 rounded-full bg-base-200 border">
                    <img
                      v-if="empresa.logoUrl"
                      :src="empresa.logoUrl"
                      :alt="`Logo ${empresa.nombre}`"
                      class="w-full h-full object-contain"
                      @error="handleImageError($event, empresa.id)"
                    />
                    <div v-else class="w-full h-full flex items-center justify-center text-base-content/40">
                      <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" />
                      </svg>
                    </div>
                  </div>
                </div>
              </td>

              <!-- Nombre -->
              <td class="font-medium">{{ empresa.nombre }}</td>

              <!-- Dominio -->
              <td>
                <span class="badge badge-primary">{{ empresa.dominio }}</span>
              </td>

              <!-- País -->
              <td>
                <span class="badge badge-outline">{{ empresa.codigoPais }}</span>
              </td>

              <!-- Estado -->
              <td>
                <span class="badge badge-success">Activo</span>
              </td>

              <!-- Acciones -->
              <td class="text-center">
                <div class="flex items-center justify-center gap-2">
                  <!-- Edit Button -->
                  <button
                    @click="editEmpresa(empresa)"
                    class="btn btn-sm btn-info"
                    title="Editar empresa"
                  >
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                    </svg>
                  </button>

                  <!-- Delete Button -->
                  <button
                    @click="confirmDelete(empresa)"
                    class="btn btn-sm btn-error"
                    title="Eliminar empresa"
                  >
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                    </svg>
                  </button>

                  <!-- View Details -->
                  <button
                    @click="viewEmpresa(empresa)"
                    class="btn btn-sm btn-ghost"
                    title="Ver detalles"
                  >
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
                    </svg>
                  </button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- Empty State -->
      <div v-else class="text-center p-12">
        <div class="text-6xl mb-4">🏢</div>
        <h3 class="text-xl font-bold text-base-content mb-2">No hay empresas registradas</h3>
        <p class="text-base-content/70 mb-6">
          Comienza creando tu primera empresa en el sistema
        </p>
        <router-link to="/dashboard/empresa" class="btn btn-primary btn-lg">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
          </svg>
          Crear Primera Empresa
        </router-link>
      </div>

      <!-- Pagination -->
      <div v-if="empresas.length > 0 && totalPages > 1" class="flex justify-center p-6 border-t border-base-300">
        <div class="btn-group">
          <button
            class="btn"
            :disabled="currentPage === 1"
            @click="currentPage--"
          >
            «
          </button>
          <button class="btn">Página {{ currentPage }} de {{ totalPages }}</button>
          <button
            class="btn"
            :disabled="currentPage === totalPages"
            @click="currentPage++"
          >
            »
          </button>
        </div>
      </div>
    </div>

    <!-- Delete Confirmation Modal -->
    <div v-if="showDeleteModal" class="modal modal-open">
      <div class="modal-box">
        <h3 class="font-bold text-lg text-error">Confirmar Eliminación</h3>
        <p class="py-4">
          ¿Estás seguro de que deseas eliminar la empresa <strong>{{ empresaToDelete?.nombre }}</strong>?
          Esta acción no se puede deshacer.
        </p>
        <div class="modal-action">
          <button @click="showDeleteModal = false" class="btn">Cancelar</button>
          <button @click="deleteEmpresa" class="btn btn-error" :disabled="deleting">
            <span v-if="deleting" class="loading loading-spinner loading-sm"></span>
            Eliminar
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

// Data
const empresas = ref([])
const loading = ref(false)
const error = ref('')
const searchTerm = ref('')
const filterStatus = ref('all')
const currentPage = ref(1)
const pageSize = ref(10)

// Modal states
const showDeleteModal = ref(false)
const empresaToDelete = ref(null)
const deleting = ref(false)

// Router
const router = useRouter()

// API URL
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api'

// Computed properties
const filteredEmpresas = computed(() => {
  let filtered = empresas.value

  // Filter by search term
  if (searchTerm.value.trim()) {
    const term = searchTerm.value.toLowerCase()
    filtered = filtered.filter(empresa =>
      empresa.nombre.toLowerCase().includes(term) ||
      empresa.dominio.toLowerCase().includes(term)
    )
  }

  // Filter by status (placeholder - all are active for now)
  if (filterStatus.value !== 'all') {
    filtered = filtered.filter(empresa => {
      // Add status filtering logic if needed
      return filterStatus.value === 'active'
    })
  }

  // Pagination
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return filtered.slice(start, end)
})

const totalPages = computed(() => {
  const filteredCount = searchTerm.value.trim() || filterStatus.value !== 'all'
    ? filteredEmpresas.value.length
    : empresas.value.length
  return Math.ceil(filteredCount / pageSize.value)
})

// Methods
const loadEmpresas = async () => {
  loading.value = true
  error.value = ''

  try {
    const response = await axios.get(`${API_BASE_URL}/empresas`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    })

    empresas.value = response.data
  } catch (err) {
    console.error('Error loading empresas:', err)
    error.value = err.response?.data?.message || 'Error al cargar las empresas'
  } finally {
    loading.value = false
  }
}

const handleImageError = (event, empresaId) => {
  console.warn(`Failed to load logo for empresa ${empresaId}`)
  // Replace with placeholder
  event.target.style.display = 'none'
}

const editEmpresa = (empresa) => {
  // Navigate to edit mode - we'll implement this as a modal or separate view
  console.log('Edit empresa:', empresa)
  // For now, we can pass empresa data through route params or localStorage
  // Since Vue Router doesn't easily support passing objects, we'll use localStorage
  localStorage.setItem('empresaToEdit', JSON.stringify(empresa))
  router.push('/dashboard/empresa')
}

const confirmDelete = (empresa) => {
  empresaToDelete.value = empresa
  showDeleteModal.value = true
}

const deleteEmpresa = async () => {
  if (!empresaToDelete.value) return

  deleting.value = true

  try {
    await axios.delete(`${API_BASE_URL}/empresas/${empresaToDelete.value.id}`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    })

    // Remove from local array
    empresas.value = empresas.value.filter(e => e.id !== empresaToDelete.value.id)

    showDeleteModal.value = false
    empresaToDelete.value = null

    // Show success message (you might want to use a toast library)
    console.log('Empresa deleted successfully')

  } catch (err) {
    console.error('Error deleting empresa:', err)
    error.value = err.response?.data?.message || 'Error al eliminar la empresa'
  } finally {
    deleting.value = false
  }
}

const viewEmpresa = (empresa) => {
  console.log('View empresa details:', empresa)
  // Could open a modal with detailed view or navigate to detail page
}

// Lifecycle
onMounted(() => {
  loadEmpresas()
})

// Watchers
watch([searchTerm, filterStatus], () => {
  currentPage.value = 1 // Reset to first page when filters change
})
</script>

<style scoped>
/* Custom table styling */
.table {
  font-size: 0.875rem;
}

.table th {
  font-weight: 600;
  background-color: #f8fafc;
  color: inherit;
}

.table td {
  padding-top: 1rem;
  padding-bottom: 1rem;
}

/* Custom avatar styling for logos */
.avatar img {
  object-fit: contain;
  padding: 0.25rem;
}

/* Modal backdrop blur */
.modal {
  background-color: rgba(0, 0, 0, 0.5);
  backdrop-filter: blur(4px);
}

/* Enhanced badge styling */
.badge-primary {
  background-color: #1d4ed8;
  color: white;
}

.badge-error {
  background-color: #ef4444;
  color: white;
}

/* Animation for loading states */
@keyframes fade-in {
  from { opacity: 0; }
  to { opacity: 1; }
}

.loading {
  animation: fade-in 0.3s ease-in-out;
}
</style>
