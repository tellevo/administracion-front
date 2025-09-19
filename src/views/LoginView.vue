<template>
  <div class="min-h-screen bg-gray-50 flex flex-col justify-center py-12 sm:px-6 lg:px-8">
    <!-- Header -->
    <div class="sm:mx-auto sm:w-full sm:max-w-md">
      <div class="flex justify-center items-center space-x-3 mb-6">
        <div class="bg-white p-3 rounded-full shadow-sm border border-gray-200">
          <img :src="logo" alt="TeLlevo" class="h-8 w-8">
        </div>
      </div>
      <h2 class="text-center text-3xl font-bold tracking-tight text-gray-900">
        Iniciar Sesión
      </h2>
      <p class="mt-2 text-center text-sm text-gray-600">
        Accede a tu panel de administración
        <span class="block mt-1 text-green-600 font-medium">TeLlevo - Movilidad Sostenible</span>
      </p>
    </div>

    <!-- Form -->
    <div class="mt-8 sm:mx-auto sm:w-full sm:max-w-md">
      <div class="bg-white py-8 px-4 shadow-lg sm:rounded-lg sm:px-10 border border-gray-200">
        <form @submit.prevent="login" class="space-y-6">
          <!-- Email Field -->
          <div>
            <label for="email" class="block text-sm font-medium text-gray-700 mb-2">
              Correo Electrónico
            </label>
            <div class="relative">
              <input
                id="email"
                v-model="username"
                type="email"
                autocomplete="email"
                required
                class="block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 sm:text-sm transition-colors"
                placeholder="admin@empresa.com"
              />
            </div>
          </div>

          <!-- Password Field -->
          <div>
            <label for="password" class="block text-sm font-medium text-gray-700 mb-2">
              Contraseña
            </label>
            <div class="relative">
              <input
                id="password"
                v-model="password"
                type="password"
                autocomplete="current-password"
                required
                class="block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 sm:text-sm transition-colors"
                placeholder="••••••••••"
              />
            </div>
          </div>

          <!-- Forgot Password -->
          <div class="flex items-center justify-between">
            <div class="text-sm">
              <a href="#" class="font-medium text-blue-600 hover:text-blue-500 transition-colors">
                ¿Olvidaste tu contraseña?
              </a>
            </div>
          </div>

          <!-- Submit Button -->
          <div>
            <button
              type="submit"
              :disabled="loading"
              class="group relative w-full flex justify-center py-2.5 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
            >
              <span v-if="loading" class="absolute left-0 inset-y-0 flex items-center pl-3">
                <div class="animate-spin h-5 w-5 border-2 border-white border-t-transparent rounded-full"></div>
              </span>
              <span v-if="!loading" class="absolute left-0 inset-y-0 flex items-center pl-3">
                <svg class="h-5 w-5 text-blue-500 group-hover:text-blue-400 transition-colors" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor">
                  <path fill-rule="evenodd" d="M10 1a4.5 4.5 0 00-4.5 4.5V9H5a2 2 0 00-2 2v6a2 2 0 002 2h10a2 2 0 002-2v-6a2 2 0 00-2-2h-.5V5.5A4.5 4.5 0 0010 1zm3 8V5.5a3 3 0 10-6 0V9h6z" clip-rule="evenodd" />
                </svg>
              </span>
              {{ loading ? 'Iniciando sesión...' : 'Iniciar Sesión' }}
            </button>
          </div>

          <!-- Registration Link -->
          <div class="mt-6">
            <div class="relative">
              <div class="absolute inset-0 flex items-center">
                <div class="w-full border-t border-gray-300" />
              </div>
              <div class="relative flex justify-center text-sm">
                <span class="px-2 bg-white text-gray-500">¿Nuevo en TeLlevo?</span>
              </div>
            </div>

            <div class="mt-6">
              <button
                type="button"
                @click="irARegistro"
                class="w-full flex justify-center py-2.5 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 transition-colors"
              >
                Crear Cuenta Administrativa
              </button>
            </div>
          </div>
        </form>

        <!-- Status Message -->
        <transition name="fade">
          <div v-if="mensaje" class="mt-6">
            <div :class="[
              'rounded-md p-4 text-sm',
              esError 
                ? 'bg-red-50 border border-red-200 text-red-800' 
                : 'bg-green-50 border border-green-200 text-green-800'
            ]">
              <div class="flex">
                <div class="flex-shrink-0">
                  <svg v-if="esError" class="h-5 w-5 text-red-400" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor">
                    <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.28 7.22a.75.75 0 00-1.06 1.06L8.94 10l-1.72 1.72a.75.75 0 101.06 1.06L10 11.06l1.72 1.72a.75.75 0 101.06-1.06L11.06 10l1.72-1.72a.75.75 0 00-1.06-1.06L10 8.94 8.28 7.22z" clip-rule="evenodd" />
                  </svg>
                  <svg v-else class="h-5 w-5 text-green-400" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor">
                    <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.857-9.809a.75.75 0 00-1.214-.882l-3.236 4.53L8.407 9.864a.75.75 0 00-1.214.882l2.5 3.5a.75.75 0 001.214 0l4-5.5z" clip-rule="evenodd" />
                  </svg>
                </div>
                <div class="ml-3">
                  <p>{{ mensaje }}</p>
                </div>
              </div>
            </div>
          </div>
        </transition>
      </div>
    </div>

    <!-- Footer -->
    <div class="mt-8 sm:mx-auto sm:w-full sm:max-w-md">
      <div class="bg-green-50 border border-green-200 rounded-lg p-4">
        <div class="flex items-center justify-center space-x-2">
          <svg class="h-5 w-5 text-green-600" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor">
            <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.857-9.809a.75.75 0 00-1.214-.882l-3.236 4.53L8.407 9.864a.75.75 0 00-1.214.882l2.5 3.5a.75.75 0 001.214 0l4-5.5z" clip-rule="evenodd" />
          </svg>
          <p class="text-sm text-green-700 font-medium">
            Sistema seguro y certificado
          </p>
        </div>
        <div class="mt-2 text-center">
          <p class="text-xs text-green-600">
            Hemos reducido <span class="font-semibold">1,245,678 kg</span> de CO₂ este mes
          </p>
          <div class="mt-2 bg-green-200 rounded-full h-2">
            <div class="bg-green-500 h-2 rounded-full transition-all duration-500" style="width: 78%"></div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { api } from "@/services/api";
import { useRouter } from "vue-router";
import logo from '/logo-tellevo2.png'

const router = useRouter();
const username = ref("");
const password = ref("");
const mensaje = ref("");
const esError = ref(false);
const loading = ref(false);

const irARegistro = () => {
  router.push('/register');
};

const login = async () => {
  try {
    loading.value = true;
    esError.value = false;

    const response = await api.login({
      username: username.value,
      password: password.value
    });

    mensaje.value = response.data.message || 'Inicio de sesión exitoso';

    // Guardar token y usuario
    localStorage.setItem('token', response.data.token);
    localStorage.setItem('usuario', username.value);

    // Redirigir después de 1.5 segundos
    setTimeout(() => {
      router.push('/dashboard');
    }, 1500);

  } catch (error) {
    esError.value = true;
    mensaje.value = error.response?.data?.message || 'Error al iniciar sesión';

    // Limpiar mensaje después de 5 segundos
    setTimeout(() => {
      mensaje.value = '';
    }, 5000);

  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

/* Custom focus styles */
input:focus {
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

/* Professional button hover effects */
button:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

button:active {
  transform: translateY(0);
}

/* Smooth transitions for all interactive elements */
* {
  transition: all 0.2s ease-in-out;
}
</style>
