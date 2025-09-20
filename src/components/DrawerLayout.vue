<template>
  <!-- Mobile-First Responsive Layout -->
  <div class="min-h-screen bg-gradient-to-br from-slate-50 to-blue-50">
    <!-- Mobile Header (visible only on small screens) -->
    <div class="lg:hidden bg-white shadow-sm border-b border-gray-200 px-4 py-3 flex items-center justify-between sticky top-0 z-50">
      <div class="flex items-center space-x-3">
        <img :src="logo" alt="TeLlevo" class="h-8 w-8 rounded-full">
        <span class="font-bold text-gray-900 text-lg">TeLlevo</span>
      </div>
      <button @click="toggleMobileMenu" class="p-2 rounded-lg hover:bg-gray-100 transition-colors">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16" />
        </svg>
      </button>
    </div>

    <!-- Mobile Menu Overlay -->
    <div v-if="mobileMenuOpen" class="lg:hidden fixed inset-0 z-40 bg-black bg-opacity-50" @click="closeMobileMenu"></div>

    <!-- Mobile Sidebar -->
    <div :class="[
      'lg:hidden fixed inset-y-0 left-0 z-50 w-64 bg-white shadow-xl transform transition-transform duration-300 ease-in-out',
      mobileMenuOpen ? 'translate-x-0' : '-translate-x-full'
    ]">
      <div class="flex flex-col h-full">
        <!-- Mobile Menu Header -->
        <div class="px-6 py-4 border-b border-gray-200">
          <div class="flex items-center justify-between">
            <div class="flex items-center space-x-3">
              <img :src="logo" alt="TeLlevo" class="h-8 w-8 rounded-full">
              <span class="font-bold text-gray-900 text-lg">TeLlevo</span>
            </div>
            <button @click="closeMobileMenu" class="p-2 rounded-lg hover:bg-gray-100">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>
        </div>

        <!-- Mobile Navigation -->
        <nav class="flex-1 px-6 py-4 space-y-2">
          <router-link 
            to="/dashboard" 
            @click="closeMobileMenu"
            class="flex items-center px-4 py-3 rounded-lg font-medium transition-colors"
            :class="isActive('/dashboard') ? 'bg-blue-500 text-white shadow-md' : 'text-gray-700 hover:bg-gray-100'"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" />
            </svg>
            Dashboard
          </router-link>
          
          <router-link 
            to="/dashboard/stats"
            @click="closeMobileMenu"
            class="flex items-center px-4 py-3 rounded-lg font-medium transition-colors"
            :class="isActive('/dashboard/stats') ? 'bg-blue-500 text-white shadow-md' : 'text-gray-700 hover:bg-gray-100'"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
            </svg>
            Estadísticas
          </router-link>
          
          <router-link
            to="/dashboard/settings"
            @click="closeMobileMenu"
            class="flex items-center px-4 py-3 rounded-lg font-medium transition-colors"
            :class="isActive('/dashboard/settings') ? 'bg-blue-500 text-white shadow-md' : 'text-gray-700 hover:bg-gray-100'"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 20l-5.447-2.724A1 1 0 013 16.382V5.618a1 1 0 011.447-.894L9 7M9 20l6-3m-6 3V7m6 10l4.553 2.276A1 1 0 0021 18.382V7.618a1 1 0 00-.553-.894L15 4M9 7l6 3m0 0l6-3" />
            </svg>
            Rutas
          </router-link>
          
          <router-link
            to="/dashboard/empresas"
            @click="closeMobileMenu"
            class="flex items-center px-4 py-3 rounded-lg font-medium transition-colors"
            :class="isActive('/dashboard/empresas') ? 'bg-blue-500 text-white shadow-md' : 'text-gray-700 hover:bg-gray-100'"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" />
            </svg>
            Empresas
          </router-link>
        </nav>

        <!-- Mobile Menu Footer -->
        <div class="border-t border-gray-200 px-6 py-4 space-y-2">
          <button 
            @click="toggleTheme" 
            class="flex items-center w-full px-4 py-3 text-gray-700 rounded-lg hover:bg-gray-100 transition-colors"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20.354 15.354A9 9 0 018.646 3.646 9.003 9.003 0 0012 21a9.003 9.003 0 008.354-5.646z" />
            </svg>
            Cambiar tema
          </button>

          <button 
            @click="logout" 
            class="flex items-center w-full px-4 py-3 text-red-600 rounded-lg hover:bg-red-50 transition-colors"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
            </svg>
            Cerrar sesión
          </button>
        </div>
      </div>
    </div>

    <div class="flex">
      <!-- Desktop Sidebar -->
      <div :class="[
        'hidden lg:flex flex-col bg-white shadow-xl border-r border-gray-200 transition-all duration-300 ease-in-out',
        sidebarAbierto ? 'w-64' : 'w-20'
      ]">
        <!-- Desktop Header -->
        <div class="px-6 py-4 border-b border-gray-200 flex items-center justify-between">
          <div v-if="sidebarAbierto" class="flex items-center space-x-3">
            <img :src="logo" alt="TeLlevo" class="h-8 w-8 rounded-full">
            <span class="font-bold text-gray-900 text-lg">TeLlevo</span>
          </div>
          <button 
            @click="toggleSidebar" 
            class="p-2 rounded-lg hover:bg-gray-100 transition-colors"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16" />
            </svg>
          </button>
        </div>

        <!-- Desktop Navigation - Expanded -->
        <nav v-if="sidebarAbierto" class="flex-1 px-6 py-4 space-y-2">
          <router-link 
            to="/dashboard" 
            class="flex items-center px-4 py-3 rounded-lg font-medium transition-colors"
            :class="isActive('/dashboard') ? 'bg-blue-500 text-white shadow-md' : 'text-gray-700 hover:bg-gray-100'"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" />
            </svg>
            Dashboard
          </router-link>
          
          <router-link 
            to="/dashboard/stats"
            class="flex items-center px-4 py-3 rounded-lg font-medium transition-colors"
            :class="isActive('/dashboard/stats') ? 'bg-blue-500 text-white shadow-md' : 'text-gray-700 hover:bg-gray-100'"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
            </svg>
            Estadísticas
          </router-link>
          
          <router-link
            to="/dashboard/settings"
            class="flex items-center px-4 py-3 rounded-lg font-medium transition-colors"
            :class="isActive('/dashboard/settings') ? 'bg-blue-500 text-white shadow-md' : 'text-gray-700 hover:bg-gray-100'"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 20l-5.447-2.724A1 1 0 013 16.382V5.618a1 1 0 011.447-.894L9 7M9 20l6-3m-6 3V7m6 10l4.553 2.276A1 1 0 0021 18.382V7.618a1 1 0 00-.553-.894L15 4M9 7l6 3m0 0l6-3" />
            </svg>
            Rutas
          </router-link>
          
          <router-link
            to="/dashboard/empresas"
            class="flex items-center px-4 py-3 rounded-lg font-medium transition-colors"
            :class="isActive('/dashboard/empresas') ? 'bg-blue-500 text-white shadow-md' : 'text-gray-700 hover:bg-gray-100'"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" />
            </svg>
            Empresas
          </router-link>
        </nav>

        <!-- Desktop Navigation - Collapsed -->
        <nav v-else class="flex-1 px-3 py-4 space-y-4">
          <div class="space-y-2">
            <router-link 
              to="/dashboard" 
              class="tooltip tooltip-right flex items-center justify-center p-3 rounded-lg transition-colors"
              data-tip="Dashboard"
              :class="isActive('/dashboard') ? 'bg-blue-500 text-white shadow-md' : 'text-gray-700 hover:bg-gray-100'"
            >
              <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" />
              </svg>
            </router-link>
            
            <router-link 
              to="/dashboard/stats"
              class="tooltip tooltip-right flex items-center justify-center p-3 rounded-lg transition-colors"
              data-tip="Estadísticas"
              :class="isActive('/dashboard/stats') ? 'bg-blue-500 text-white shadow-md' : 'text-gray-700 hover:bg-gray-100'"
            >
              <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
              </svg>
            </router-link>
            
            <router-link
              to="/dashboard/settings"
              class="tooltip tooltip-right flex items-center justify-center p-3 rounded-lg transition-colors"
              data-tip="Rutas"
              :class="isActive('/dashboard/settings') ? 'bg-blue-500 text-white shadow-md' : 'text-gray-700 hover:bg-gray-100'"
            >
              <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 20l-5.447-2.724A1 1 0 013 16.382V5.618a1 1 0 011.447-.894L9 7M9 20l6-3m-6 3V7m6 10l4.553 2.276A1 1 0 0021 18.382V7.618a1 1 0 00-.553-.894L15 4M9 7l6 3m0 0l6-3" />
              </svg>
            </router-link>
            
            <router-link
              to="/dashboard/empresas"
              class="tooltip tooltip-right flex items-center justify-center p-3 rounded-lg transition-colors"
              data-tip="Empresas"
              :class="isActive('/dashboard/empresas') ? 'bg-blue-500 text-white shadow-md' : 'text-gray-700 hover:bg-gray-100'"
            >
              <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" />
              </svg>
            </router-link>
          </div>
        </nav>

        <!-- Desktop Footer -->
        <div class="border-t border-gray-200 px-6 py-4 space-y-2">
          <template v-if="sidebarAbierto">
            <button 
              @click="toggleTheme" 
              class="flex items-center w-full px-4 py-2 text-gray-700 rounded-lg hover:bg-gray-100 transition-colors text-sm font-medium"
            >
              <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20.354 15.354A9 9 0 018.646 3.646 9.003 9.003 0 0012 21a9.003 9.003 0 008.354-5.646z" />
              </svg>
              Cambiar tema
            </button>

            <button 
              @click="logout" 
              class="flex items-center w-full px-4 py-2 text-red-600 rounded-lg hover:bg-red-50 transition-colors text-sm font-medium"
            >
              <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
              </svg>
              Cerrar sesión
            </button>
          </template>
          
          <template v-else>
            <div class="space-y-2">
              <button 
                @click="toggleTheme" 
                class="tooltip tooltip-right flex items-center justify-center p-2 w-full text-gray-700 rounded-lg hover:bg-gray-100 transition-colors"
                data-tip="Cambiar tema"
              >
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20.354 15.354A9 9 0 018.646 3.646 9.003 9.003 0 0012 21a9.003 9.003 0 008.354-5.646z" />
                </svg>
              </button>

              <button 
                @click="logout" 
                class="tooltip tooltip-right flex items-center justify-center p-2 w-full text-red-600 rounded-lg hover:bg-red-50 transition-colors"
                data-tip="Cerrar sesión"
              >
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
                </svg>
              </button>
            </div>
          </template>
        </div>
      </div>

      <!-- Main Content -->
      <div class="flex-1 lg:ml-0 overflow-auto">
        <div class="p-4 lg:p-6">
          <router-view />
        </div>
      </div>
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
const mobileMenuOpen = ref(false)

const isActive = (path) => {
  // Exact matching for dashboard routes
  if (path === '/dashboard') {
    // Dashboard should only be active when at the exact /dashboard path
    return route.path === '/dashboard'
  } else if (path === '/dashboard/stats') {
    return route.path === '/dashboard/stats'
  } else if (path === '/dashboard/settings') {
    return route.path === '/dashboard/settings'
  } else if (path === '/dashboard/empresas') {
    return route.path === '/dashboard/empresas'
  }

  // Fallback for any other routes
  return route.path === path
}

const toggleSidebar = () => {
  sidebarAbierto.value = !sidebarAbierto.value
}

const toggleMobileMenu = () => {
  mobileMenuOpen.value = !mobileMenuOpen.value
}

const closeMobileMenu = () => {
  mobileMenuOpen.value = false
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

  const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches
  const temaGuardado = localStorage.getItem('tema') || (prefersDark ? 'dark' : 'light')
  document.documentElement.setAttribute('data-theme', temaGuardado)
})
</script>

<style scoped>
/* Mobile menu transitions */
.transform {
  transition: transform 0.3s ease-in-out;
}

/* Tooltip styles */
.tooltip:before {
  @apply bg-gray-900 text-white text-xs py-1 px-2 rounded;
}

/* Active link styles handled by isActive function */

/* Smooth transitions */
* {
  transition: all 0.2s ease;
}
</style>
