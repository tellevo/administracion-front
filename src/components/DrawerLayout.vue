<template>
  <div class="flex h-screen bg-base-100">
    <!-- Sidebar Eco-Friendly -->
    <div :class="['bg-base-100 border-r border-base-300 shadow-sm transition-all duration-300 ease-in-out',
        sidebarAbierto ? 'w-64' : 'w-16']">
      <!-- Logo y Botón -->
      <div class="p-4 flex justify-between items-center border-b border-base-300">
        <div v-if="sidebarAbierto" class="flex items-center space-x-2">
          <div class="bg-white/90 p-1.5 rounded-full shadow-sm"> <!-- Fondo blanco con sombra -->
    <img :src="logo" alt="Logo TeLlevo" class="h-8 w-8 object-contain">
  </div>
          <span class="text-lg font-bold">TeLlevo</span>
        </div>
        <button @click="toggleSidebar" class="btn btn-ghost btn-sm">
          ☰
        </button>
      </div>

      <!-- Menú -->
      <nav v-if="sidebarAbierto" class="p-4">
        <ul class="space-y-3 mt-4">
          <li>
            <router-link 
              to="/dashboard" 
              class="flex items-center px-3 py-2 rounded-lg transition-colors"
              :class="isActive('/dashboard') ? 'bg-primary text-primary-content font-medium' : 'hover:bg-base-200'"
            >
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" />
              </svg>
              Inicio
            </router-link>
          </li>
          <li>
            <router-link 
              to="/dashboard/stats" 
              class="flex items-center px-3 py-2 rounded-lg transition-colors"
              :class="isActive('/dashboard/stats') ? 'bg-primary text-primary-content font-medium' : 'hover:bg-base-200'"
            >
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
              </svg>
              Estadísticas
            </router-link>
          </li>
          <li>
            <router-link
              to="/dashboard/settings"
              class="flex items-center px-3 py-2 rounded-lg transition-colors"
              :class="isActive('/dashboard/settings') ? 'bg-primary text-primary-content font-medium' : 'hover:bg-base-200'"
            >
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z" />
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
              </svg>
              Rutas
            </router-link>
          </li>
          <li>
            <router-link
              to="/dashboard/empresa"
              class="flex items-center px-3 py-2 rounded-lg transition-colors"
              :class="isActive('/dashboard/empresa') ? 'bg-primary text-primary-content font-medium' : 'hover:bg-base-200'"
            >
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" />
              </svg>
              Nueva Empresa
            </router-link>
          </li>
        </ul>

        <!-- Pie del Sidebar -->
        <div class="mt-8 border-t border-base-300 pt-4">
          <button 
            @click="toggleTheme" 
            class="btn btn-outline btn-primary w-full mb-2 flex items-center justify-center"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20.354 15.354A9 9 0 018.646 3.646 9.003 9.003 0 0012 21a9.003 9.003 0 008.354-5.646z" />
            </svg>
            Cambiar tema
          </button>

          <button 
            @click="logout" 
            class="btn btn-error text-white w-full flex items-center justify-center"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
            </svg>
            Cerrar sesión
          </button>
        </div>
      </nav>

      <!-- Menú colapsado -->
      <nav v-else class="p-2 mt-4">
        <ul class="space-y-4">
          <li>
            <router-link 
              to="/dashboard" 
              class="tooltip tooltip-right flex items-center justify-center p-2 rounded-lg"
              data-tip="Inicio"
              :class="isActive('/dashboard') ? 'bg-primary text-primary-content' : 'hover:bg-base-200'"
            >
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" />
              </svg>
            </router-link>
          </li>
          <li>
            <router-link 
              to="/dashboard/stats" 
              class="tooltip tooltip-right flex items-center justify-center p-2 rounded-lg"
              data-tip="Estadísticas"
              :class="isActive('/dashboard/stats') ? 'bg-primary text-primary-content' : 'hover:bg-base-200'"
            >
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
              </svg>
            </router-link>
          </li>
          <li>
            <router-link
              to="/dashboard/settings"
              class="tooltip tooltip-right flex items-center justify-center p-2 rounded-lg"
              data-tip="Rutas"
              :class="isActive('/dashboard/settings') ? 'bg-primary text-primary-content' : 'hover:bg-base-200'"
            >
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z" />
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
              </svg>
            </router-link>
          </li>
          <li>
            <router-link
              to="/dashboard/empresa"
              class="tooltip tooltip-right flex items-center justify-center p-2 rounded-lg"
              data-tip="Nueva Empresa"
              :class="isActive('/dashboard/empresa') ? 'bg-primary text-primary-content' : 'hover:bg-base-200'"
            >
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" />
              </svg>
            </router-link>
          </li>
        </ul>

        <div class="mt-8 border-t border-base-300 pt-4">
          <button 
            @click="toggleTheme" 
            class="tooltip tooltip-right flex items-center justify-center p-2 w-full hover:bg-base-200 rounded-lg"
            data-tip="Cambiar tema"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20.354 15.354A9 9 0 018.646 3.646 9.003 9.003 0 0012 21a9.003 9.003 0 008.354-5.646z" />
            </svg>
          </button>

          <button 
            @click="logout" 
            class="tooltip tooltip-right flex items-center justify-center p-2 w-full mt-2 bg-error text-error-content hover:bg-error-focus rounded-lg"
            data-tip="Cerrar sesión"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
            </svg>
          </button>
        </div>
      </nav>
    </div>

    <!-- Contenido Principal -->
    <div class="flex-1 overflow-auto bg-base-100">
      <router-view />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import logo from '/logo-tellevo2.png'
const route = useRoute()
const router = useRouter()
const sidebarAbierto = ref(true)

const isActive = (path) => {
  // Comparación exacta para la ruta de inicio
  if (path === '/dashboard') {
    return route.path === path
  }
  // Para otras rutas, permite coincidencias parciales
  return route.path.startsWith(path)
}

const toggleSidebar = () => {
  sidebarAbierto.value = !sidebarAbierto.value
}

const toggleTheme = () => {
  const current = document.documentElement.getAttribute('data-theme')
  const nuevoTema = current === 'dark' ? 'light' : 'dark'
  document.documentElement.setAttribute('data-theme', nuevoTema)
  localStorage.setItem('tema', nuevoTema)
}

const logout = () => {
  localStorage.removeItem('usuario')
  localStorage.removeItem('token')
  router.push('/')
}

onMounted(() => {
  const usuario = localStorage.getItem('usuario')
  if (!usuario) router.push('/')

  // Verificar preferencia del sistema
  const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches
  const temaGuardado = localStorage.getItem('tema') || (prefersDark ? 'dark' : 'light')
  document.documentElement.setAttribute('data-theme', temaGuardado)
})
</script>

<style>
/* Transiciones suaves para el menú */
.router-link-active {
  transition: all 0.2s ease;
}

/* Estilo para tooltips */
.tooltip::before {
  @apply bg-neutral text-neutral-content text-xs py-1 px-2 rounded;
}

/* Asegurar que el SVG herede el color del texto */
svg {
  stroke: currentColor;
}
</style>
