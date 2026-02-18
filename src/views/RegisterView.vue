<template>
  <div class="min-h-screen flex items-center justify-center bg-base-200 py-8">
    <!-- Contenedor principal con animación de entrada -->
    <div class="animate-fade-in-up">
      <div class="card w-96 bg-base-100 shadow-2xl rounded-xl overflow-hidden border border-base-300">
        <!-- Header con tema primario -->
        <div class="bg-primary text-primary-content p-6">
         <div class="flex items-center justify-center space-x-3">
  <div class="bg-base-100 p-1.5 rounded-full shadow-sm">
    <img :src="logo" alt="Logo TeLlevo" class="h-8 w-8 object-contain">
  </div>
  <h1 class="text-2xl font-bold">TeLlevo</h1>
</div>
          <p class="text-sm text-primary-content/80 text-center mt-2">Únete a nuestra comunidad eco-friendly</p>
        </div>

        <div class="card-body p-6">
          <h2 class="card-title text-primary mb-4">Crear Cuenta</h2>

          <form @submit.prevent="register" class="space-y-4">
            <div class="form-control">
              <label class="label">
                <span class="label-text text-base-content/80">Nombre Completo</span>
              </label>
              <input 
                v-model="name" 
                type="text" 
                class="input input-bordered border-base-300 focus:border-primary focus:ring-1 focus:ring-primary/20 transition text-base-content bg-base-100" 
                placeholder="Tu nombre" 
                required 
              />
            </div>

            <div class="form-control">
              <label class="label">
                <span class="label-text text-base-content/80">Correo electrónico</span>
              </label>
              <input 
                v-model="email" 
                type="email" 
                class="input input-bordered border-base-300 focus:border-primary focus:ring-1 focus:ring-primary/20 transition text-base-content bg-base-100" 
                placeholder="tu@email.com" 
                required 
              />
            </div>

            <div class="form-control">
              <label class="label">
                <span class="label-text text-base-content/80">Contraseña</span>
              </label>
              <input 
                v-model="password" 
                type="password" 
                autocomplete="new-password"
                class="input input-bordered border-base-300 focus:border-primary focus:ring-1 focus:ring-primary/20 transition text-base-content bg-base-100" 
                placeholder="••••••••" 
                required 
              />
            </div>

            <div class="form-control">
              <label class="label">
                <span class="label-text text-base-content/80">Confirmar Contraseña</span>
              </label>
              <input 
                v-model="confirmPassword" 
                type="password" 
                autocomplete="new-password"
                class="input input-bordered border-base-300 focus:border-primary focus:ring-1 focus:ring-primary/20 transition text-base-content bg-base-100" 
                placeholder="••••••••" 
                required 
              />
            </div>

            <div class="form-control mt-6">
              <button 
                class="btn btn-primary text-white shadow-md hover:shadow-lg transform hover:-translate-y-0.5 transition-all duration-200"
                :disabled="loading"
              >
                <span v-if="loading" class="loading loading-spinner"></span>
                Registrarse
                <svg v-if="!loading" xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 ml-1" viewBox="0 0 20 20" fill="currentColor">
                  <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm1-11a1 1 0 10-2 0v2H7a1 1 0 100 2h2v2a1 1 0 102 0v-2h2a1 1 0 100-2h-2V7z" clip-rule="evenodd" />
                </svg>
              </button>
            </div>
          </form>

          <div class="text-center mt-4">
            <button class="btn btn-link text-primary hover:text-primary-focus text-sm transition-colors" @click="irALogin">
              ¿Ya tienes cuenta? <span class="font-semibold ml-1">Inicia sesión</span>
            </button>
          </div>

          <!-- Mensaje de estado con animación -->
          <transition name="fade">
            <div v-if="mensaje" :class="{'alert alert-info': !esError, 'alert alert-error': esError}" class="p-3 rounded-lg mt-4 text-sm flex items-center shadow-sm">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" viewBox="0 0 20 20" fill="currentColor">
                <path fill-rule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7-4a1 1 0 11-2 0 1 1 0 012 0zM9 9a1 1 0 000 2v3a1 1 0 001 1h1a1 1 0 100-2h-1V9z" clip-rule="evenodd" />
              </svg>
              {{ mensaje }}
            </div>
          </transition>
        </div>
      </div>

      <!-- Pie de página -->
      <div class="text-center mt-6 text-primary text-sm">
        <p>Al registrarte, aceptas nuestros <a href="#" class="link link-hover font-semibold">Términos</a> y <a href="#" class="link link-hover font-semibold">Política de Privacidad</a></p>
        <div class="flex justify-center mt-2">
          <div class="badge badge-outline border-primary/40 text-primary mr-2">♻️ Eco-Friendly</div>
          <div class="badge badge-outline border-primary/40 text-primary">🚗 Comparte Viajes</div>
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
const name = ref("");
const email = ref("");
const password = ref("");
const confirmPassword = ref("");
const mensaje = ref("");
const esError = ref(false);
const loading = ref(false);

const irALogin = () => {
  router.push('/');
};

const register = async () => {
  // Validar que las contraseñas coincidan
  if (password.value !== confirmPassword.value) {
    mensaje.value = "Las contraseñas no coinciden";
    esError.value = true;
    setTimeout(() => { mensaje.value = ''; }, 3000);
    return;
  }

  try {
    loading.value = true;
    esError.value = false;
    
    const response = await api.register({
      name: name.value,
      email: email.value,
      password: password.value
    });

    mensaje.value = response.data.message || '¡Registro exitoso! Redirigiendo...';
    
    // Redirigir al login después de 2 segundos
    setTimeout(() => {
      router.push('/');
    }, 2000);
    
  } catch (error) {
    esError.value = true;
    mensaje.value = error.response?.data?.message || 'Error al registrarse';
    
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

.fade-enter-active, .fade-leave-active {
  transition: opacity 0.3s;
}

.fade-enter-from, .fade-leave-to {
  opacity: 0;
}
</style>
