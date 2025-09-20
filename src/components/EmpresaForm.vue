<template>
  <div class="w-full">
    <form @submit.prevent="submitForm" class="space-y-8">
            
            <!-- Nombre Field -->
            <div class="form-control group">
              <label class="label mb-3">
                <span class="label-text text-lg font-semibold text-gray-700 flex items-center">
                  <div class="inline-flex items-center justify-center w-8 h-8 bg-gradient-to-br from-emerald-400 to-emerald-600 rounded-lg mr-3 shadow-sm">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" />
                    </svg>
                  </div>
                  Nombre de la Empresa
                  <span class="text-red-500 ml-1">*</span>
                </span>
              </label>
              <div class="relative">
                <input
                  v-model="form.nombre"
                  type="text"
                  class="input input-bordered w-full h-14 text-lg pl-5 pr-12 rounded-xl border-2 transition-all duration-300 focus:border-blue-500 focus:shadow-lg focus:shadow-blue-500/20"
                  placeholder="Ingrese el nombre de la empresa"
                  :class="{
                    'border-red-300 bg-red-50 focus:border-red-500': errors.nombre,
                    'border-green-300 bg-green-50 focus:border-green-500': form.nombre && !errors.nombre
                  }"
                  @input="validateField('nombre')"
                />
                <div v-if="form.nombre && !errors.nombre" class="absolute right-4 top-1/2 transform -translate-y-1/2">
                  <div class="w-6 h-6 bg-green-500 rounded-full flex items-center justify-center">
                    <svg class="w-4 h-4 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path>
                    </svg>
                  </div>
                </div>
              </div>
              <label v-if="errors.nombre" class="label">
                <span class="label-text-alt text-red-500 font-medium flex items-center">
                  <svg class="w-4 h-4 mr-1" fill="currentColor" viewBox="0 0 20 20">
                    <path fill-rule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7 4a1 1 0 11-2 0 1 1 0 012 0zm-1-9a1 1 0 00-1 1v4a1 1 0 102 0V6a1 1 0 00-1-1z" clip-rule="evenodd"></path>
                  </svg>
                  {{ errors.nombre }}
                </span>
              </label>
            </div>

            <!-- Dominio Field -->
            <div class="form-control group">
              <label class="label mb-3">
                <span class="label-text text-lg font-semibold text-gray-700 flex items-center">
                  <div class="inline-flex items-center justify-center w-8 h-8 bg-gradient-to-br from-blue-400 to-blue-600 rounded-lg mr-3 shadow-sm">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 12a9 9 0 01-9 9m9-9a9 9 0 00-9-9m9 9H3m9 9v-9m0-9v9" />
                    </svg>
                  </div>
                  Dominio de la Empresa
                  <span class="text-red-500 ml-1">*</span>
                </span>
                <div class="badge badge-info badge-sm">
                  Formato: @empresa.dominio (ej: @empresa.cl, @empresa.com)
                </div>
              </label>
              <div class="relative">
                <input
                  v-model="form.dominio"
                  type="text"
                  class="input input-bordered w-full h-14 text-lg pl-5 pr-12 rounded-xl border-2 transition-all duration-300 focus:border-blue-500 focus:shadow-lg focus:shadow-blue-500/20"
                  placeholder="Ej: @pucv.cl"
                  :class="{
                    'border-red-300 bg-red-50 focus:border-red-500': errors.dominio,
                    'border-green-300 bg-green-50 focus:border-green-500': form.dominio && !errors.dominio
                  }"
                  @input="validateField('dominio')"
                />
                <div v-if="form.dominio && !errors.dominio" class="absolute right-4 top-1/2 transform -translate-y-1/2">
                  <div class="w-6 h-6 bg-green-500 rounded-full flex items-center justify-center">
                    <svg class="w-4 h-4 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path>
                    </svg>
                  </div>
                </div>
              </div>
              <label v-if="errors.dominio" class="label">
                <span class="label-text-alt text-red-500 font-medium flex items-center">
                  <svg class="w-4 h-4 mr-1" fill="currentColor" viewBox="0 0 20 20">
                    <path fill-rule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7 4a1 1 0 11-2 0 1 1 0 012 0zm-1-9a1 1 0 00-1 1v4a1 1 0 102 0V6a1 1 0 00-1-1z" clip-rule="evenodd"></path>
                  </svg>
                  {{ errors.dominio }}
                </span>
              </label>
              <label v-else-if="form.dominio && !errors.dominio" class="label">
                <span class="label-text-alt text-green-600 font-medium flex items-center">
                  <svg class="w-4 h-4 mr-1 text-green-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path>
                  </svg>
                  Formato válido
                </span>
              </label>
            </div>

            <!-- Logo URL Field with Preview -->
            <div class="form-control group">
              <label class="label mb-3">
                <span class="label-text text-lg font-semibold text-gray-700 flex items-center">
                  <div class="inline-flex items-center justify-center w-8 h-8 bg-gradient-to-br from-purple-400 to-purple-600 rounded-lg mr-3 shadow-sm">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
                    </svg>
                  </div>
                  URL del Logo
                  <span class="text-red-500 ml-1">*</span>
                </span>
                <div class="badge badge-secondary badge-sm">
                  Formato: .svg
                </div>
              </label>
              <div class="relative">
                <input
                  v-model="form.logoUrl"
                  type="url"
                  class="input input-bordered w-full h-14 text-lg pl-5 pr-12 rounded-xl border-2 transition-all duration-300 focus:border-blue-500 focus:shadow-lg focus:shadow-blue-500/20"
                  placeholder="https://ejemplo.com/logo.svg"
                  :class="{
                    'border-red-300 bg-red-50 focus:border-red-500': errors.logoUrl,
                    'border-green-300 bg-green-50 focus:border-green-500': form.logoUrl && !errors.logoUrl && isValidSvgUrl
                  }"
                  @input="validateLogoUrl"
                />
                <div v-if="form.logoUrl && !errors.logoUrl && isValidSvgUrl && imageLoaded" class="absolute right-4 top-1/2 transform -translate-y-1/2">
                  <div class="w-6 h-6 bg-green-500 rounded-full flex items-center justify-center">
                    <svg class="w-4 h-4 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path>
                    </svg>
                  </div>
                </div>
              </div>
              
              <!-- Error Messages -->
              <label v-if="errors.logoUrl" class="label">
                <span class="label-text-alt text-red-500 font-medium flex items-center">
                  <svg class="w-4 h-4 mr-1" fill="currentColor" viewBox="0 0 20 20">
                    <path fill-rule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7 4a1 1 0 11-2 0 1 1 0 012 0zm-1-9a1 1 0 00-1 1v4a1 1 0 102 0V6a1 1 0 00-1-1z" clip-rule="evenodd"></path>
                  </svg>
                  {{ errors.logoUrl }}
                </span>
              </label>
              <label v-else-if="form.logoUrl && !isValidSvgUrl" class="label">
                <span class="label-text-alt text-red-500 font-medium flex items-center">
                  <svg class="w-4 h-4 mr-1" fill="currentColor" viewBox="0 0 20 20">
                    <path fill-rule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7 4a1 1 0 11-2 0 1 1 0 012 0zm-1-9a1 1 0 00-1 1v4a1 1 0 102 0V6a1 1 0 00-1-1z" clip-rule="evenodd"></path>
                  </svg>
                  La URL debe terminar en .svg
                </span>
              </label>

              <!-- Logo Preview Section -->
              <div v-if="form.logoUrl && isValidSvgUrl" class="mt-6">
                <div class="bg-gradient-to-br from-gray-50 to-gray-100 rounded-2xl p-6 border border-gray-200">
                  <h4 class="text-lg font-semibold text-gray-700 mb-4 flex items-center">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2 text-purple-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
                    </svg>
                    Vista Previa del Logo
                  </h4>
                  
                  <div class="flex flex-col items-center space-y-4">
                    <!-- Loading State -->
                    <div v-if="loadingImage" class="flex items-center space-x-3 py-8">
                      <div class="w-8 h-8 border-4 border-blue-500 border-t-transparent rounded-full animate-spin"></div>
                      <span class="text-gray-600 font-medium">Cargando imagen...</span>
                    </div>

                    <!-- Logo Display -->
                    <div v-else-if="imageLoaded" class="relative group">
                      <div class="bg-white rounded-2xl p-6 shadow-lg border-2 border-gray-100 group-hover:shadow-xl transition-all duration-300">
                        <img
                          :src="form.logoUrl"
                          alt="Logo preview"
                          class="h-24 w-24 object-contain mx-auto rounded-lg"
                          @error="handleImageError"
                          @load="handleImageLoad"
                        />
                      </div>
                      <div class="absolute -top-2 -right-2 bg-gradient-to-br from-green-400 to-green-600 text-white rounded-full w-8 h-8 flex items-center justify-center shadow-lg">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
                        </svg>
                      </div>
                    </div>

                    <!-- Success Message -->
                    <div v-if="imageLoaded" class="text-center bg-green-50 rounded-xl p-4 border border-green-200">
                      <p class="text-green-700 font-semibold flex items-center justify-center">
                        <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path>
                        </svg>
                        Logo cargado correctamente
                      </p>
                      <p class="text-green-600 text-sm mt-1">
                        El logo está listo para usar
                      </p>
                    </div>
                  </div>
                </div>
              </div>

              <!-- Examples -->
              <div class="mt-4 p-4 bg-blue-50 rounded-xl border border-blue-200">
                <p class="text-sm text-blue-700 font-medium mb-2 flex items-center">
                  <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                  </svg>
                  Ejemplo de URL válida:
                </p>
                <code class="text-xs text-blue-600 bg-blue-100 px-2 py-1 rounded">
                  https://cdn.worldvectorlogo.com/logos/universidad-catolica.svg
                </code>
              </div>
            </div>

            <!-- Submit Button -->
            <div class="form-control pt-6">
              <button
                type="submit"
                class="btn h-16 w-full text-lg font-semibold rounded-xl border-0 transition-all duration-300 shadow-lg hover:shadow-xl transform hover:-translate-y-0.5"
                :class="{
                  'bg-gradient-to-r from-blue-500 to-indigo-600 hover:from-blue-600 hover:to-indigo-700 text-white': isFormValid && !isSubmitting,
                  'bg-gray-300 text-gray-500 cursor-not-allowed': !isFormValid || isSubmitting
                }"
                :disabled="!isFormValid || isSubmitting"
              >
                <div class="flex items-center justify-center space-x-3">
                  <div v-if="isSubmitting" class="w-6 h-6 border-2 border-white border-t-transparent rounded-full animate-spin"></div>
                  <svg v-else xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
                  </svg>
                  <span>
                    {{ isSubmitting ? (isEditMode ? 'Actualizando Empresa...' : 'Creando Empresa...') : (isEditMode ? 'Actualizar Empresa' : 'Crear Empresa') }}
                  </span>
                </div>
              </button>
            </div>

            <!-- Success Message -->
            <div v-if="successMessage" class="alert bg-green-50 border-green-200 text-green-700 shadow-lg rounded-xl">
              <div class="flex items-center">
                <div class="flex-shrink-0">
                  <div class="w-10 h-10 bg-green-100 rounded-full flex items-center justify-center">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-green-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                    </svg>
                  </div>
                </div>
                <div class="ml-3">
                  <h3 class="text-sm font-medium text-green-800">¡Operación Exitosa!</h3>
                  <div class="mt-2 text-sm text-green-700">
                    <p>{{ successMessage }}</p>
                  </div>
                </div>
              </div>
            </div>

            <!-- Error Message -->
            <div v-if="errorMessage" class="alert bg-red-50 border-red-200 text-red-700 shadow-lg rounded-xl">
              <div class="flex items-center">
                <div class="flex-shrink-0">
                  <div class="w-10 h-10 bg-red-100 rounded-full flex items-center justify-center">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-red-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z" />
                    </svg>
                  </div>
                </div>
                <div class="ml-3">
                  <h3 class="text-sm font-medium text-red-800">Error en la Operación</h3>
                  <div class="mt-2 text-sm text-red-700">
                    <p>{{ errorMessage }}</p>
                  </div>
                </div>
              </div>
            </div>
          </form>
  </div>
</template>

<script setup>
import { ref, computed, reactive, watch, nextTick } from 'vue'
import { api } from '@/services/api'

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

// Regular expression for dominio validation (matches backend pattern)
const dominioRegex = /^@[a-zA-Z0-9.-]+\.[a-z]{2,}$/

// Computed properties
const isFormValid = computed(() => {
  return (
    form.nombre.trim() &&
    form.dominio &&
    form.logoUrl &&
    !errors.nombre &&
    !errors.dominio &&
    !errors.logoUrl &&
    isValidSvgUrl.value
    // Removed: && imageLoaded.value - Allow submission with valid URL format even if image preview fails
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
        errors.dominio = 'El dominio debe tener el formato @empresa.dominio válido (ej: @empresa.cl, @empresa.com)'
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

    if (isEditMode.value && empresaId.value) {
      // Update existing empresa
      response = await api.empresas.update(empresaId.value, requestData)
      successMessage.value = response.data.message || 'Empresa actualizada exitosamente'

      // Emit update event
      emit('empresa-updated', response.data)

      // Clear localStorage after edit
      localStorage.removeItem('empresaToEdit')

      // Navigate back to list (optional)
      // router.push('/dashboard/empresas')
    } else {
      // Create new empresa
      response = await api.empresas.create(requestData)
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
        errorMsg = 'Sesión expirada. Por favor, inicie sesión nuevamente.'
        // Clear invalid token
        localStorage.removeItem('token')
        localStorage.removeItem('usuario')
        // Optionally redirect to login
        setTimeout(() => {
          window.location.href = '/'
        }, 3000)
      } else if (error.response.status === 403) {
        errorMsg = 'Acceso denegado. No tiene permisos para esta operación.'
      } else if (error.response.status === 400) {
        errorMsg = 'Datos inválidos. Verifique la información proporcionada.'
      } else if (error.response.status === 409) {
        errorMsg = 'El dominio ya está en uso. Elija un dominio diferente.'
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
/* Enhanced background gradient */
.min-h-screen {
  min-height: 100vh;
  background-attachment: fixed;
}

/* Custom animations */
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.card {
  animation: fadeInUp 0.6s ease-out;
}

/* Enhanced hover effects */
.group:hover .group-hover\:shadow-xl {
  --tw-shadow: 0 20px 25px -5px rgb(0 0 0 / 0.1), 0 8px 10px -6px rgb(0 0 0 / 0.1);
  --tw-shadow-colored: 0 20px 25px -5px var(--tw-shadow-color), 0 8px 10px -6px var(--tw-shadow-color);
  box-shadow: var(--tw-ring-offset-shadow, 0 0 #0000), var(--tw-ring-shadow, 0 0 #0000), var(--tw-shadow);
}

/* Custom focus states with enhanced glow */
.input:focus {
  outline: none;
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1), 0 4px 14px 0 rgba(59, 130, 246, 0.15);
}

/* Enhanced button hover effects */
.btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.15);
}

/* Loading spinner enhancement */
.animate-spin {
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

/* Success/Error message animations */
.alert {
  animation: slideInDown 0.5s ease-out;
}

@keyframes slideInDown {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* Enhanced backdrop blur effect */
.backdrop-blur-sm {
  backdrop-filter: blur(4px);
  -webkit-backdrop-filter: blur(4px);
}

/* Gradient text enhancement */
.bg-clip-text {
  background-clip: text;
  -webkit-background-clip: text;
}

/* Enhanced shadow effects */
.shadow-xl {
  box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 8px 10px -6px rgba(0, 0, 0, 0.1);
}

/* Custom scrollbar for the page */
.min-h-screen::-webkit-scrollbar {
  width: 8px;
}

.min-h-screen::-webkit-scrollbar-track {
  background: rgba(0, 0, 0, 0.05);
  border-radius: 4px;
}

.min-h-screen::-webkit-scrollbar-thumb {
  background: linear-gradient(135deg, #3b82f6, #6366f1);
  border-radius: 4px;
}

.min-h-screen::-webkit-scrollbar-thumb:hover {
  background: linear-gradient(135deg, #2563eb, #4f46e5);
}

/* Enhanced input field styling */
.input-bordered {
  border-width: 2px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.input-bordered:hover {
  border-color: #93c5fd;
}

/* Badge enhancements */
.badge {
  font-weight: 600;
  letter-spacing: 0.025em;
}

/* Form control group hover effects */
.form-control.group:hover .label-text {
  color: #374151;
}

/* Enhanced card styling */
.card {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.card:hover {
  transform: translateY(-2px);
}
</style>
