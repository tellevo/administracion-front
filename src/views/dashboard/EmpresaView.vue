<template>
  <div class="min-h-screen bg-base-100 p-6">
    <!-- Header -->
    <div class="flex items-center justify-between mb-8">
      <div>
        <h1 class="text-3xl font-bold text-base-content mb-2">
          {{ isEditMode ? 'Editar Empresa' : 'Nueva Empresa' }}
        </h1>
        <p class="text-base-content/70">
          {{ isEditMode ? 'Actualiza la información de la empresa' : 'Crea una nueva empresa en el sistema' }}
        </p>
      </div>
      <!-- Breadcrumb -->
      <div class="text-sm breadcrumbs">
        <ul>
          <li>
            <router-link to="/dashboard" class="text-base-content/60 hover:text-primary">
              Dashboard
            </router-link>
          </li>
          <li class="text-base-content/60">{{ isEditMode ? 'Editar Empresa' : 'Nueva Empresa' }}</li>
        </ul>
      </div>
    </div>

    <!-- Main Content -->
    <div class="max-w-4xl mx-auto">
      <!-- Form Card -->
      <div class="bg-base-100 shadow-xl rounded-2xl border border-base-300 overflow-hidden">
        <div class="bg-gradient-to-r from-primary to-secondary p-6">
          <h2 class="text-2xl font-bold text-white mb-2">
            {{ isEditMode ? 'Editar Información de la Empresa' : 'Información de la Empresa' }}
          </h2>
          <p class="text-white/90">
            {{ isEditMode ? 'Modifica los campos que deseas actualizar' : 'Complete los siguientes campos para registrar una nueva empresa' }}
          </p>
        </div>

        <!-- Form Container -->
        <div class="p-8">
          <EmpresaForm
            @empresa-created="onEmpresaCreated"
            @empresa-updated="onEmpresaUpdated"
            @empresa-error="onEmpresaError"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import EmpresaForm from '@/components/EmpresaForm.vue'

const router = useRouter()
const route = useRoute()

// Clear localStorage on mount unless coming from edit action
onMounted(() => {
  // Only clear if there's no specific edit flag or if we're explicitly creating new
  if (!route.query.edit) {
    localStorage.removeItem('empresaToEdit')
  }
})

const isEditMode = computed(() => {
  return !!localStorage.getItem('empresaToEdit')
})

const onEmpresaCreated = (empresa) => {
  console.log('Empresa created successfully:', empresa)
  // Could show a success toast here and redirect to empresa list
  // For now, just refresh the form or redirect
  router.push('/dashboard/empresas')
}

const onEmpresaUpdated = (empresa) => {
  console.log('Empresa updated successfully:', empresa)
  // Clear localStorage and redirect to empresa list
  localStorage.removeItem('empresaToEdit')
  router.push('/dashboard/empresas')
}

const onEmpresaError = (error) => {
  console.error('Error submitting empresa:', error)
  // Error handling could be enhanced with toast notifications
}
</script>

<style scoped>
/* Custom animations for the gradient header */
.bg-gradient-to-r {
  background: linear-gradient(135deg, #1d4ed8, #4f46e5);
}

/* Custom breadcrumb styling */
.breadcrumbs ul li {
  position: relative;
}

.breadcrumbs ul li:not(:last-child)::after {
  content: '/';
  margin-left: 0.5rem;
  color: rgba(0, 0, 0, 0.6);
}

/* Enhanced card shadow */
.shadow-xl {
  box-shadow: 0 20px 25px -5px rgb(0 0 0 / 0.1), 0 10px 10px -5px rgb(0 0 0 / 0.04);
}
</style>
