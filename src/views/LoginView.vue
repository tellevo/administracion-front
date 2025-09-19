<template>
  <div class="min-h-screen flex items-center justify-center bg-gradient-to-br from-blue-50 to-cyan-50 py-8">
    <div class="animate-fade-in-up">
      <div class="card w-96 bg-white shadow-2xl rounded-xl overflow-hidden border border-blue-100">
        <!-- Header con tema azul -->
        <div class="bg-gradient-to-r from-blue-500 to-cyan-600 p-6 text-white">
         <div class="flex items-center justify-center space-x-3">
  <div class="bg-white/90 p-1.5 rounded-full shadow-sm"> <!-- Fondo blanco con sombra -->
    <img :src="logo" alt="Logo TeLlevo" class="h-8 w-8 object-contain">
  </div>
  <h1 class="text-2xl font-bold">TeLlevo</h1>
</div>
          <p class="text-sm text-blue-100 text-center mt-2">Reduce tu huella de CO₂ compartiendo viajes</p>
        </div>

        <div class="card-body p-6">
          <h2 class="card-title text-blue-800 mb-4">Iniciar Sesión</h2>

          <form @submit.prevent="login" class="space-y-4">
            <div class="form-control">
              <label class="label">
                <span class="label-text text-blue-700">Correo electrónico</span>
              </label>
              <input v-model="username" type="email"
                class="input input-bordered border-blue-200 focus:border-blue-400 focus:ring-1 focus:ring-blue-200 transition"
                placeholder="tu@email.com" required />
            </div>

            <div class="form-control">
              <label class="label">
                <span class="label-text text-blue-700">Contraseña</span>
              </label>
              <input v-model="password" type="password" autocomplete="current-password"
                class="input input-bordered border-blue-200 focus:border-blue-400 focus:ring-1 focus:ring-blue-200 transition"
                placeholder="••••••••" required />
              <label class="label">
                <a href="#" class="label-text-alt link link-hover text-blue-600">¿Olvidaste tu contraseña?</a>
              </label>
            </div>

            <div class="form-control mt-6">
              <button
                class="btn bg-blue-500 hover:bg-blue-600 border-none text-white shadow-md hover:shadow-lg transform hover:-translate-y-0.5 transition-all duration-200"
                :disabled="loading">
                <span v-if="loading" class="loading loading-spinner"></span>
                Ingresar
                <svg v-if="!loading" xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 ml-1" viewBox="0 0 20 20"
                  fill="currentColor">
                  <path fill-rule="evenodd"
                    d="M10.293 5.293a1 1 0 011.414 0l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414-1.414L12.586 11H5a1 1 0 110-2h7.586l-2.293-2.293a1 1 0 010-1.414z"
                    clip-rule="evenodd" />
                </svg>
              </button>
            </div>
          </form>

          <div class="divider text-blue-600 text-sm">continúa con Google</div>

<div class="flex justify-center space-x-4">
  <button class="btn btn-circle btn-outline border-blue-200 hover:bg-blue-50">
    <img src="https://www.google.com/images/branding/googleg/1x/googleg_standard_color_128dp.png" 
         alt="Google Logo" 
         class="h-6 w-6" />
  </button>
</div>

          <div class="text-center mt-4">
            <button class="btn btn-link text-blue-600 hover:text-blue-800 text-sm transition-colors"
              @click="irARegistro">
              ¿No tienes cuenta? <span class="font-semibold">Regístrate</span>
            </button>
          </div>

          <!-- Mensaje de estado con animación -->
          <transition name="fade">
            <div v-if="mensaje" :class="{ 'bg-blue-100 text-blue-800': !esError, 'bg-red-100 text-red-800': esError }"
              class="p-3 rounded-lg mt-4 text-sm flex items-center">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" viewBox="0 0 20 20" fill="currentColor">
                <path fill-rule="evenodd"
                  d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7-4a1 1 0 11-2 0 1 1 0 012 0zM9 9a1 1 0 000 2v3a1 1 0 001 1h1a1 1 0 100-2h-1V9z"
                  clip-rule="evenodd" />
              </svg>
              {{ mensaje }}
            </div>
          </transition>
        </div>
      </div>

      <!-- Pie de página -->
      <div class="text-center mt-6 text-blue-600 text-sm">
        <p>Juntos hemos reducido <span class="font-bold">1,245,678 kg</span> de CO₂ este mes</p>
        <div class="w-full bg-blue-200 rounded-full h-2 mt-2">
          <div class="bg-blue-500 h-2 rounded-full" style="width: 78%"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import axios from "axios";
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

    const response = await axios.post('http://localhost:8080/api/login', {
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

<style>
.animate-fade-in-up {
  animation: fadeInUp 0.6s ease-out;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }

  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
