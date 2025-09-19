<template>
  <form @submit.prevent="submitForm" class="space-y-6">
    <!-- Nombre Field -->
    <div class="form-control">
      <label class="label">
        <span class="label-text font-medium">Nombre de la Empresa *</span>
      </label>
      <input
        v-model="form.nombre"
        type="text"
        class="input input-bordered"
        placeholder="Ingrese el nombre de la empresa"
        :class="{
          'input-error': errors.nombre,
          'input-success': form.nombre && !errors.nombre
        }"
        @input="validateField('nombre')"
      />
      <label v-if="errors.nombre" class="label">
        <span class="label-text-alt text-error">{{ errors.nombre }}</span>
      </label>
    </div>

    <!-- Dominio Field -->
    <div class="form-control">
      <label class="label">
        <span class="label-text font-medium">Dominio *</span>
        <span class="label-text-alt text-sm text-base-content/60">
          Debe tener el formato @nombreempresa.cl
        </span>
      </label>
      <input
        v-model="form.dominio"
        type="text"
        class="input input-bordered"
        placeholder="Ej: @pucv.cl"
        :class="{
          'input-error': errors.dominio,
          'input-success': form.dominio && !errors.dominio
        }"
        @input="validateField('dominio')"
      />
      <label v-if="errors.dominio" class="label">
        <span class="label-text-alt text-error">{{ errors.dominio }}</span>
      </label>
      <label v-if="form.dominio && !errors.dominio" class="label">
        <span class="label-text-alt text-success">✓ Formato válido</span>
      </label>
    </div>

    <!-- Logo URL Field with Preview -->
    <div class="form-control">
      <label class="label">
        <span class="label-text font-medium">URL del Logo *</span>
        <span class="label-text-alt text-sm text-base-content/60">
          Debe terminar en .svg
        </span>
      </label>
      <input
        v-model="form.logoUrl"
        type="url"
        class="input input-bordered"
        placeholder="https://ejemplo.com/logo.svg"
        :class="{
          'input-error': errors.logoUrl,
          'input-success': form.logoUrl && !errors.logoUrl
        }"
        @input="validateLogoUrl"
      />
      <label v-if="errors.logoUrl" class="label">
        <span class="label-text-alt text-error">{{ errors.logoUrl }}</span>
      </label>

      <!-- Logo Preview -->
      <div v-if="form.logoUrl && isValidSvgUrl" class="mt-4">
        <label class="label">
          <span class="label-text font-medium">Vista Previa del Logo:</span>
        </label>
        <div class="border-2 border-dashed border-base-300 rounded-lg p-6 bg-base-200">
          <div class="flex flex-col items-center space-y-4">
            <!-- Loading State -->
            <div v-if="loadingImage" class="flex items-center space-x-2">
              <div class="loading loading-spinner loading-sm"></div>
              <span class="text-sm text-base-content/60">Cargando imagen...</span>
            </div>

            <!-- Logo Display -->
            <div v-else-if="imageLoaded" class="relative">
              <img
                :src="form.logoUrl"
                alt="Logo preview"
                class="h-20 w-20 object-contain mx-auto border border-base-300 rounded-lg bg-white p-2 shadow-sm"
                @error="handleImageError"
                @load="handleImageLoad"
              />
              <div class="absolute -top-2 -right-2 bg-success text-success-content rounded-full w-6 h-6 flex items-center justify-center">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-3 w-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
                </svg>
              </div>
            </div>

            <!-- Success Message -->
            <div v-if="imageLoaded" class="text-center">
              <p class="text-sm text-success font-medium">✓ Logo cargado correctamente</p>
              <p class="text-xs text-base-content/60 mt-1">
                El logo se ve perfecto para usar
              </p>
            </div>
          </div>
        </div>
      </div>

      <!-- Validation Message -->
      <label v-if="form.logoUrl && !isValidSvgUrl" class="label">
        <span class="label-text-alt text-error">✗ La URL debe terminar en .svg</span>
      </label>

      <!-- Examples -->
      <label class="label">
        <span class="label-text-alt text-xs text-base-content/50">
          Ejemplos válidos: https://cdn.worldvectorlogo.com/logos/universidad-catolica.svg
        </span>
      </label>
    </div>

    <!-- Submit Button -->
    <div class="form-control pt-4">
      <button
        type="submit"
        class="btn btn-primary btn-lg w-full"
        :disabled="!isFormValid || isSubmitting"
      >
        <span v-if="isSubmitting" class="loading loading-spinner loading-sm"></span>
        <svg v-else xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
        </svg>
        {{ isSubmitting ? (isEditMode ? 'Actualizando Empresa...' : 'Creando Empresa...') : (isEditMode ? 'Actualizar Empresa' : 'Crear Empresa') }}
      </button>
    </div>

    <!-- Success Message -->
    <div v-if="successMessage" class="alert alert-success">
      <svg xmlns="http://www.w3.org/2000/svg" class="stroke-current shrink-0 h-6 w-6" fill="none" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
      </svg>
      <span>{{ successMessage }}</span>
    </div>

    <!-- Error Message -->
    <div v-if="errorMessage" class="alert alert-error">
      <svg xmlns="http://www.w3.org/2000/svg" class="stroke-current shrink-0 h-6 w-6" fill="none" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z" />
      </svg>
      <span>{{ errorMessage }}</span>
    </div>
  </form>
</template>

<script setup>
import { ref, computed, reactive, watch, nextTick } from 'vue'
import axios from 'axios'

// Props
const props = defineProps({
  empresa: {
    type: Object,
    default: () => null
  }
})

// Emits
const emit = defineEmits(['empresa-created', 'empresa-updated', 'empresa-error'])

// Reactive form data
const form = reactive({
  nombre: '',
  dominio: '',
  logoUrl: ''
})

// Validation errors
const errors = reactive({
  nombre: '',
  dominio: '',
  logoUrl: ''
})

// Logo validation states
const isValidSvgUrl = ref(false)
const loadingImage = ref(false)
const imageLoaded = ref(false)
const imageError = ref(false)

// Form states
const isSubmitting = ref(false)
const successMessage = ref('')
const errorMessage = ref('')

// API URL - configurable
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api'

// Regular expression for dominio validation
const dominioRegex = /^@[a-zA-Z]+\.cl$/

// Computed properties
const isFormValid = computed(() => {
  return (
    form.nombre.trim() &&
    form.dominio &&
    form.logoUrl &&
    !errors.nombre &&
    !errors.dominio &&
    !errors.logoUrl &&
    isValidSvgUrl.value &&
    imageLoaded.value
  )
})

const isEditMode = computed(() => {
  return !!(props.empresa || localStorage.getItem('empresaToEdit'))
})

const empresaId = computed(() => {
  if (props.empresa) return props.empresa.id
  const stored = localStorage.getItem('empresaToEdit')
  if (stored) {
    const empresa = JSON.parse(stored)
    return empresa.id
  }
  return null
})

// Validation functions
const validateField = (field) => {
  switch (field) {
    case 'nombre':
      if (!form.nombre.trim()) {
        errors.nombre = 'El nombre es obligatorio'
      } else if (form.nombre.length < 3) {
        errors.nombre = 'El nombre debe tener al menos 3 caracteres'
      } else if (form.nombre.length > 255) {
        errors.nombre = 'El nombre no puede exceder 255 caracteres'
      } else {
        errors.nombre = ''
      }
      break

    case 'dominio':
      if (!form.dominio) {
        errors.dominio = 'El dominio es obligatorio'
      } else if (!dominioRegex.test(form.dominio)) {
        errors.dominio = 'El dominio debe tener el formato @nombreempresa.cl'
      } else if (form.dominio.length > 255) {
        errors.dominio = 'El dominio no puede exceder 255 caracteres'
      } else {
        errors.dominio = ''
      }
      break
  }
}

const validateLogoUrl = () => {
  const url = form.logoUrl

  // Basic URL validation
  if (!url) {
    errors.logoUrl = 'La URL del logo es obligatoria'
    isValidSvgUrl.value = false
    return
  }

  // Check if URL ends with .svg
  if (!url.toLowerCase().endsWith('.svg')) {
    errors.logoUrl = 'La URL debe terminar en .svg'
    isValidSvgUrl.value = false
    imageLoaded.value = false
    imageError.value = false
    return
  }

  // URL is valid for .svg extension
  errors.logoUrl = ''
  isValidSvgUrl.value = true

  // Attempt to load the image
  validateImage(url)
}

const validateImage = (url) => {
  if (!url) return

  loadingImage.value = true
  imageLoaded.value = false
  imageError.value = false

  const img = new Image()

  img.onload = () => {
    loadingImage.value = false
    imageLoaded.value = true
    imageError.value = false
  }

  img.onerror = () => {
    loadingImage.value = false
    imageLoaded.value = false
    imageError.value = true
    errors.logoUrl = 'No se pudo cargar la imagen desde esta URL'
  }

  img.src = url
}

const handleImageError = () => {
  imageError.value = true
  imageLoaded.value = false
  errors.logoUrl = 'Error al cargar la imagen del logo'
}

const handleImageLoad = () => {
  imageLoaded.value = true
  imageError.value = false
  errors.logoUrl = ''
}

// Submit form
const submitForm = async () => {
  if (!isFormValid.value) {
    return
  }

  isSubmitting.value = true
  successMessage.value = ''
  errorMessage.value = ''

  try {
    const requestData = {
      nombre: form.nombre.trim(),
      dominio: form.dominio.trim(),
      logoUrl: form.logoUrl.trim()
    }

    let response
    const headers = {
      'Authorization': `Bearer ${localStorage.getItem('token')}`,
      'Content-Type': 'application/json'
    }

    if (isEditMode.value && empresaId.value) {
      // Update existing empresa
      response = await axios.put(`${API_BASE_URL}/empresas/${empresaId.value}`, requestData, { headers })
      successMessage.value = response.data.message || 'Empresa actualizada exitosamente'

      // Emit update event
      emit('empresa-updated', response.data)

      // Clear localStorage after edit
      localStorage.removeItem('empresaToEdit')

      // Navigate back to list (optional)
      // router.push('/dashboard/empresas')
    } else {
      // Create new empresa
      response = await axios.post(`${API_BASE_URL}/empresas`, requestData, { headers })
      successMessage.value = response.data.message || 'Empresa creada exitosamente'

      // Emit create event
      emit('empresa-created', response.data)

      // Reset form after successful submission
      resetForm()
    }

  } catch (error) {
    console.error('Error submitting empresa:', error)

    let errorMsg = isEditMode.value ? 'Error desconocido al actualizar la empresa' : 'Error desconocido al crear la empresa'

    if (error.response) {
      // Server responded with error
      if (error.response.data && error.response.data.message) {
        errorMsg = error.response.data.message
      } else if (error.response.status === 401) {
        errorMsg = 'No autorizado. Verifique su sesión.'
      } else if (error.response.status === 400) {
        errorMsg = 'Datos inválidos. Revise la información proporcionada.'
      } else if (error.response.status >= 500) {
        errorMsg = 'Error del servidor. Intente nuevamente más tarde.'
      }
    } else if (error.request) {
      // Network error
      errorMsg = 'Error de conexión. Verifique su conexión a internet.'
    }

    errorMessage.value = errorMsg

    // Emit error event
    emit('empresa-error', error)

  } finally {
    isSubmitting.value = false
  }
}

// Reset form
const resetForm = () => {
  form.nombre = ''
  form.dominio = ''
  form.logoUrl = ''

  Object.keys(errors).forEach(key => {
    errors[key] = ''
  })

  isValidSvgUrl.value = false
  loadingImage.value = false
  imageLoaded.value = false
  imageError.value = false
}

// Watch for form changes to clear messages
watch(form, () => {
  successMessage.value = ''
  errorMessage.value = ''
})

// Initialize form if empresa prop is provided or from localStorage
if (props.empresa) {
  form.nombre = props.empresa.nombre || ''
  form.dominio = props.empresa.dominio || ''
  form.logoUrl = props.empresa.logoUrl || ''

  if (form.logoUrl) {
    validateLogoUrl()
  }
} else if (localStorage.getItem('empresaToEdit')) {
  const storedEmpresa = JSON.parse(localStorage.getItem('empresaToEdit'))
  form.nombre = storedEmpresa.nombre || ''
  form.dominio = storedEmpresa.dominio || ''
  form.logoUrl = storedEmpresa.logoUrl || ''

  if (form.logoUrl) {
    validateLogoUrl()
  }
}
</script>

<style scoped>
/* Enhance loading spinner animation */
.loading {
  animation: spin 1s linear infinite;
}

@keyframes spin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

/* Custom focus states */
input:focus {
  @apply border-primary;
}

/* Error shake animation */
.input-error {
  animation: shake 0.5s ease-in-out;
}

@keyframes shake {
  0%, 100% { transform: translateX(0); }
  25% { transform: translateX(-5px); }
  75% { transform: translateX(5px); }
}

/* Success glow effect */
.input-success:focus {
  box-shadow: 0 0 0 2px rgba(34, 197, 94, 0.2);
}
</style>
