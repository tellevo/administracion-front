<template>
  <div class="min-h-screen bg-gradient-to-br from-base-100 to-base-200/30">
    <!-- Header - Simple Clean Design -->
    <div class="bg-base-100 shadow-lg border-b border-base-200 sticky top-0 z-10">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
        <div class="flex items-center justify-between flex-wrap gap-4">
          <div class="flex-1 min-w-0">
            <h1 class="text-2xl sm:text-3xl lg:text-4xl font-bold text-base-content mb-2">
              Gestionar Empresas
            </h1>

            <!-- Breadcrumb - Now clearly visible -->
            <div class="text-sm breadcrumbs">
              <ul>
                <li>
                  <router-link to="/dashboard" class="text-base-content/60 hover:text-primary transition-colors">
                    Dashboard
                  </router-link>
                </li>
                <li class="text-base-content">Empresas</li>
              </ul>
            </div>

            <p class="text-base text-base-content/70 mt-2">
              Lista completa de empresas registradas en el sistema
            </p>
          </div>

          <!-- Mobile Menu Button -->
          <div class="flex items-center gap-3">

            <!-- Mobile Refresh Button -->
            <button
              @click="loadEmpresas"
              :disabled="loading"
              class="btn btn-circle btn-ghost lg:hidden"
              title="Actualizar"
            >
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor" :class="loading ? 'animate-spin' : ''">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
              </svg>
            </button>

            <!-- New Empresa Button - Mobile Optimized -->
            <router-link
              to="/dashboard/empresa"
              class="btn btn-primary btn-sm sm:btn-md shadow-lg hover:shadow-xl transition-all duration-300 transform hover:-translate-y-0.5"
            >
              <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
              </svg>
              <span class="hidden sm:inline ml-2">Nueva Empresa</span>
              <span class="sm:hidden ml-1">+</span>
            </router-link>
          </div>
        </div>
      </div>
    </div>

    <!-- Main Content -->
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
      <!-- Stats Cards - Mobile Optimized -->
      <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 mb-6">
        <div class="bg-base-100 shadow-lg rounded-xl border border-base-200 p-4 hover:shadow-xl transition-all duration-300">
          <div class="flex items-center">
            <div class="w-10 h-10 bg-blue-500 rounded-full flex items-center justify-center mr-3">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" />
              </svg>
            </div>
            <div>
              <p class="text-sm text-base-content/70">Total Empresas</p>
              <p class="text-xl font-bold text-base-content">{{ empresas.length }}</p>
            </div>
          </div>
        </div>

        <div class="bg-base-100 shadow-lg rounded-xl border border-base-200 p-4 hover:shadow-xl transition-all duration-300">
          <div class="flex items-center">
            <div class="w-10 h-10 bg-green-500 rounded-full flex items-center justify-center mr-3">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
            <div>
              <p class="text-sm text-base-content/70">Activas</p>
              <p class="text-xl font-bold text-base-content">{{ empresas.length }}</p>
            </div>
          </div>
        </div>

        <div class="bg-base-100 shadow-lg rounded-xl border border-base-200 p-4 hover:shadow-xl transition-all duration-300">
          <div class="flex items-center">
            <div class="w-10 h-10 bg-purple-500 rounded-full flex items-center justify-center mr-3">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 16H6a2 2 0 01-2-2V6a2 2 0 012-2h8a2 2 0 012 2v2m-6 12h8a2 2 0 002-2v-8a2 2 0 00-2-2h-8a2 2 0 00-2 2v8a2 2 0 002 2z" />
              </svg>
            </div>
            <div>
              <p class="text-sm text-base-content/70">Dominios Únicos</p>
              <p class="text-xl font-bold text-base-content">{{ uniqueDomains }}</p>
            </div>
          </div>
        </div>

        <div class="bg-base-100 shadow-lg rounded-xl border border-base-200 p-4 hover:shadow-xl transition-all duration-300">
          <div class="flex items-center">
            <div class="w-10 h-10 bg-orange-500 rounded-full flex items-center justify-center mr-3">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 7h8m0 0v8m0-8l-8 8-4-4-6 6" />
              </svg>
            </div>
            <div>
              <p class="text-sm text-base-content/70">Crecimiento</p>
              <p class="text-xl font-bold text-green-600">+{{ Math.floor(Math.random() * 20) + 5 }}%</p>
            </div>
          </div>
        </div>
      </div>

      <!-- Filters and Search -->
      <div class="bg-base-100 shadow-lg rounded-xl border border-base-200 p-4 sm:p-6 mb-6">
        <div class="flex flex-col lg:flex-row gap-4 items-start lg:items-center justify-between">
          <div class="flex flex-col sm:flex-row gap-4 items-start sm:items-center flex-1 w-full lg:w-auto">
            <!-- Enhanced Search Component - Modern UX -->
            <div class="form-control flex-1 max-w-full sm:max-w-xs">
              <div class="relative group">
                <!-- Floating Label -->
                <label class="absolute -top-2 left-4 bg-base-100 px-2 text-sm font-medium text-primary z-10 transition-all duration-200">
                  🔍 Buscar Empresas
                </label>

                <!-- Enhanced Search Input -->
                <div class="relative">
                  <div class="absolute inset-y-0 left-0 pl-4 flex items-center pointer-events-none">
                    <svg class="h-5 w-5 text-primary transition-colors duration-200 group-focus-within:text-primary group-hover:text-primary/80" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"/>
                    </svg>
                  </div>

                  <input
                    v-model="searchTerm"
                    type="text"
                    placeholder="Ej: universidad, empresa.cl, organización..."
                    class="w-full pl-12 pr-20 py-4 bg-gradient-to-r from-base-100 to-base-50/50 border-2 border-base-300 rounded-xl text-base font-medium placeholder-base-content/40 shadow-md hover:shadow-lg focus:border-primary focus:ring-4 focus:ring-primary/10 hover:border-base-content/50 transition-all duration-300 outline-none"
                    @keydown.ctrl.k.prevent="focusSearch"
                    @keydown.cmd.k.prevent="focusSearch"
                    @focus="onSearchFocus"
                    @blur="onSearchBlur"
                  />

                  <!-- Search Hints & Clear -->
                  <div class="absolute inset-y-0 right-0 pr-4 flex items-center space-x-2">
                    <kbd class="hidden sm:inline-flex items-center px-2 py-1 text-xs font-semibold text-base-content/60 bg-base-200 rounded-md border border-base-300">
                      ⌘K
                    </kbd>

                    <!-- Clear Button -->
                    <button
                      v-if="searchTerm"
                      @click="clearSearch"
                      class="p-1 rounded-md hover:bg-base-200 transition-colors duration-200"
                      title="Limpiar búsqueda"
                    >
                      <svg class="h-4 w-4 text-base-content/60" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
                      </svg>
                    </button>
                  </div>
                </div>

                <!-- Smart Contextual Help System -->
                <div
                  v-if="searchHelpVisible && !helpDisabled"
                  class="absolute top-full left-0 right-0 mt-3 p-4 bg-gradient-to-r from-blue-50 to-indigo-50 shadow-xl rounded-xl border-2 border-blue-200 z-20 transition-all duration-300"
                >
                  <div class="flex items-start space-x-4">
                    <div class="flex-shrink-0">
                      <!-- Dynamic Icon based on context -->
                      <div v-if="showNoResultsHelp" class="w-10 h-10 bg-orange-100 rounded-full flex items-center justify-center">
                        <svg class="h-6 w-6 text-orange-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-.833-1.864-.833-2.634 0L3.732 16.5c-.77.833.192 2.5 1.732 2.5z"/>
                        </svg>
                      </div>
                      <div v-else class="w-10 h-10 bg-blue-100 rounded-full flex items-center justify-center">
                        <svg class="h-6 w-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"/>
                        </svg>
                      </div>
                    </div>

                    <div class="flex-1 min-w-0">
                      <div class="flex items-center justify-between mb-2">
                        <h4 class="text-sm font-semibold text-blue-900">
                          {{ showNoResultsHelp ? 'No se encontraron resultados' : '💡 Consejos de búsqueda' }}
                        </h4>
                        <button
                          @click="dismissSearchHelp"
                          class="text-blue-400 hover:text-blue-600 transition-colors duration-200 p-1"
                          title="Cerrar ayuda"
                        >
                          <svg class="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
                          </svg>
                        </button>
                      </div>

                      <!-- Contextual Message -->
                      <div class="text-sm text-blue-800 mb-3">
                        <template v-if="showNoResultsHelp">
                          <span class="font-medium">Intenta con:</span>
                          <ul class="mt-2 space-y-1">
                            <li>• Nombre completo: "Universidad Católica"</li>
                            <li>• Parte del nombre: "universidad"</li>
                            <li>• Dominio exacto: "ucv.cl"</li>
                            <li>• Parte del dominio: "@gmail.cl"</li>
                          </ul>
                        </template>
                        <template v-else>
                          <p>Busca empresas por:
                            <strong class="bg-blue-200 px-1 rounded">Nombre completo</strong>,
                            <strong class="bg-blue-200 px-1 rounded">Parte del nombre</strong>,
                            <strong class="bg-blue-200 px-1 rounded">Dominio (@dominio.cl)</strong>
                          </p>
                          <p class="mt-2 text-xs text-blue-600">💡 Presiona <kbd class="bg-blue-200 px-1 rounded text-xs mx-1">⌘K</kbd> para buscar rápido</p>
                        </template>
                      </div>

                      <!-- Action Buttons -->
                      <div class="flex items-center justify-between">
                        <div class="flex space-x-2">
                          <button
                            @click="dismissSearchHelp"
                            class="px-3 py-1 text-xs bg-blue-100 text-blue-700 rounded-md hover:bg-blue-200 transition-colors duration-200"
                          >
                            Entendido
                          </button>

                          <button
                            v-if="!showNoResultsHelp"
                            @click="toggleSearchHelp"
                            class="px-3 py-1 text-xs bg-white text-blue-600 border border-blue-200 rounded-md hover:bg-blue-50 transition-colors duration-200"
                            :title="helpDisabled ? 'Habilitar ayuda' : 'Deshabilitar ayuda'"
                          >
                            {{ helpDisabled ? 'Mostrar más tarde' : 'No mostrar más' }}
                          </button>
                        </div>

                        <div class="text-xs text-blue-500">
                          <span v-if="!showNoResultsHelp">{{ helpShowCount }}/3</span>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <!-- Filter by Status -->
            <div class="form-control">
              <select
                v-model="filterStatus"
                class="select select-bordered min-w-[120px] bg-base-100 border-base-300 focus:outline-none focus:ring-2 focus:ring-primary/20"
              >
                <option value="all">📊 Todos</option>
                <option value="active">✅ Activos</option>
                <option value="inactive">⏸️ Inactivos</option>
              </select>
            </div>
          </div>

          <!-- Desktop Refresh Button -->
          <button
            @click="loadEmpresas"
            :disabled="loading"
            class="btn btn-outline btn-sm sm:btn-md hover:bg-base-200 transition-all duration-200"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" :class="loading ? 'animate-spin' : ''">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
            </svg>
            <span class="ml-2">Actualizar</span>
          </button>
        </div>

        <!-- Search Results Info -->
        <div v-if="searchTerm || filterStatus !== 'all'" class="mt-4 text-sm text-base-content/70">
          <p>
            Mostrando {{ filteredEmpresas.length }} de {{ empresas.length }} empresas
            <button
              v-if="searchTerm || filterStatus !== 'all'"
              @click="clearFilters"
              class="link link-primary ml-2"
            >
              Limpiar filtros
            </button>
          </p>
        </div>
      </div>
    </div>

    <!-- Companies - Mobile Cards / Desktop Table -->
    <div class="bg-base-100 shadow-lg rounded-xl border border-base-200 overflow-hidden">
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

      <!-- Desktop Table View -->
      <div v-else-if="empresas.length > 0" class="hidden lg:block overflow-x-auto">
        <table class="table table-zebra w-full">
          <!-- Table Header -->
          <thead>
            <tr class="bg-base-200/80">
              <th class="w-16 font-semibold">#</th>
              <th class="w-32 font-semibold">Logo</th>
              <th class="font-semibold">Nombre</th>
              <th class="font-semibold">Dominio</th>
              <th class="w-32 font-semibold">País</th>
              <th class="w-32 font-semibold">Estado</th>
              <th class="w-48 text-center font-semibold">Acciones</th>
            </tr>
          </thead>

          <!-- Table Body -->
          <tbody>
            <tr v-for="(empresa, index) in filteredEmpresas" :key="empresa.id"
                class="hover:bg-base-200/50 transition-colors duration-200">
              <!-- ID -->
              <td class="font-mono text-sm text-base-content/70">{{ empresa.id }}</td>

              <!-- Logo -->
              <td>
                <div class="avatar">
                  <div class="w-10 h-10 rounded-full bg-base-200 border-2 border-base-300 overflow-hidden">
                    <img
                      v-if="empresa.logoUrl"
                      :src="empresa.logoUrl"
                      :alt="`Logo ${empresa.nombre}`"
                      class="w-full h-full object-contain p-1"
                      @error="handleImageError($event, empresa.id)"
                    />
                    <div v-else class="w-full h-full flex items-center justify-center text-base-content/40 bg-base-200">
                      <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" />
                      </svg>
                    </div>
                  </div>
                </div>
              </td>

              <!-- Nombre -->
              <td class="font-medium text-base-content">{{ empresa.nombre }}</td>

              <!-- Dominio -->
              <td>
                <span class="badge badge-primary badge-sm">{{ empresa.dominio }}</span>
              </td>

              <!-- País -->
              <td>
                <span class="badge badge-outline badge-sm">{{ empresa.codigoPais }}</span>
              </td>

              <!-- Estado -->
              <td>
                <span class="badge badge-success badge-sm">✓ Activo</span>
              </td>

              <!-- Acciones -->
              <td class="text-center">
                <div class="flex items-center justify-center gap-1">
                  <!-- Edit Button -->
                  <button
                    @click="editEmpresa(empresa)"
                    class="btn btn-sm btn-info btn-circle"
                    title="Editar empresa"
                  >
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                    </svg>
                  </button>

                  <!-- Delete Button -->
                  <button
                    @click="confirmDelete(empresa)"
                    class="btn btn-sm btn-error btn-circle"
                    title="Eliminar empresa"
                  >
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                    </svg>
                  </button>

                  <!-- View Details -->
                  <button
                    @click="viewEmpresa(empresa)"
                    class="btn btn-sm btn-ghost btn-circle"
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

      <!-- Mobile Card View -->
      <div v-else-if="empresas.length > 0" class="lg:hidden">
        <div class="grid grid-cols-1 gap-4 p-4">
          <div
            v-for="(empresa, index) in filteredEmpresas"
            :key="empresa.id"
            class="bg-base-100 rounded-xl border border-base-200 p-4 shadow-lg hover:shadow-xl transition-all duration-300 transform hover:-translate-y-1"
          >
            <!-- Card Header -->
            <div class="flex items-center justify-between mb-4">
              <div class="flex items-center space-x-3">
                <!-- Logo -->
                <div class="avatar">
                  <div class="w-12 h-12 rounded-full bg-base-200 border-2 border-base-300 overflow-hidden">
                    <img
                      v-if="empresa.logoUrl"
                      :src="empresa.logoUrl"
                      :alt="`Logo ${empresa.nombre}`"
                      class="w-full h-full object-contain p-1"
                      @error="handleImageError($event, empresa.id)"
                    />
                    <div v-else class="w-full h-full flex items-center justify-center text-base-content/40 bg-base-200">
                      <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" />
                      </svg>
                    </div>
                  </div>
                </div>

                <!-- Empresa Info -->
                <div class="flex-1 min-w-0">
                  <h3 class="font-bold text-lg text-base-content truncate">{{ empresa.nombre }}</h3>
                  <p class="text-sm text-base-content/70">{{ empresa.dominio }}</p>
                </div>
              </div>

              <!-- Status Badge -->
              <span class="badge badge-success badge-sm shrink-0">✓ Activo</span>
            </div>

            <!-- Card Content -->
            <div class="flex items-center justify-between">
              <div class="flex items-center space-x-2">
                <span class="badge badge-primary badge-sm">{{ empresa.dominio }}</span>
                <span class="badge badge-outline badge-sm">{{ empresa.codigoPais }}</span>
                <span class="text-xs text-base-content/60 font-mono">#{{ empresa.id }}</span>
              </div>

              <!-- Mobile Action Buttons -->
              <div class="flex items-center space-x-2">
                <button
                  @click="viewEmpresa(empresa)"
                  class="btn btn-circle btn-sm btn-ghost"
                  title="Ver detalles"
                >
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
                  </svg>
                </button>

                <button
                  @click="editEmpresa(empresa)"
                  class="btn btn-circle btn-sm btn-info"
                  title="Editar empresa"
                >
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                  </svg>
                </button>

                <button
                  @click="confirmDelete(empresa)"
                  class="btn btn-circle btn-sm btn-error"
                  title="Eliminar empresa"
                >
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                  </svg>
                </button>
              </div>
            </div>
          </div>
        </div>

        <!-- Mobile Pagination -->
        <div v-if="totalPages > 1" class="flex justify-center p-6 border-t border-base-300">
          <div class="join grid grid-cols-2 w-full max-w-xs">
            <button
              class="join-item btn btn-outline"
              :disabled="currentPage === 1"
              @click="currentPage--"
            >
              « Anterior
            </button>
            <button
              class="join-item btn btn-outline"
              :disabled="currentPage === totalPages"
              @click="currentPage++"
            >
              Siguiente »
            </button>
          </div>
        </div>
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
import { api } from '@/services/api'
import { useRouter } from 'vue-router'

// Data
const empresas = ref([])
const loading = ref(false)
const error = ref('')
const searchTerm = ref('')
const filterStatus = ref('all')
const currentPage = ref(1)
const pageSize = ref(10)

// Search help states
const showSearchHelp = ref(false)
const helpDismissed = ref(false)
const helpDisabled = ref(false)
const searchFocused = ref(false)
const helpShowCount = ref(0)

// Modal states
const showDeleteModal = ref(false)
const empresaToDelete = ref(null)
const deleting = ref(false)

// Router
const router = useRouter()


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

// Smart search help computed properties
const searchHelpVisible = computed(() => {
  // Show help if not disabled, not dismissed, and either focused or no search results
  return !helpDisabled.value && (!helpDismissed.value || (searchTerm.value.trim() && filteredEmpresas.value.length === 0))
})

const showNoResultsHelp = computed(() => {
  // Show "no results" help when user searched but found nothing
  return searchTerm.value.trim() && filteredEmpresas.value.filter(empresa =>
    empresa.nombre.toLowerCase().includes(searchTerm.value.toLowerCase()) ||
    empresa.dominio.toLowerCase().includes(searchTerm.value.toLowerCase())
  ).length === 0
})

const uniqueDomains = computed(() => {
  const domains = new Set(empresas.value.map(empresa => empresa.dominio))
  return domains.size
})

// Methods
const loadEmpresas = async () => {
  loading.value = true
  error.value = ''

  try {
    const response = await api.empresas.list()

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
  // Navigate to edit mode with query parameter and localStorage
  console.log('Edit empresa:', empresa)
  localStorage.setItem('empresaToEdit', JSON.stringify(empresa))
  router.push('/dashboard/empresa?edit=true')
}

const confirmDelete = (empresa) => {
  empresaToDelete.value = empresa
  showDeleteModal.value = true
}

const deleteEmpresa = async () => {
  if (!empresaToDelete.value) return

  deleting.value = true

  try {
    await api.empresas.delete(empresaToDelete.value.id)

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

const clearFilters = () => {
  searchTerm.value = ''
  filterStatus.value = 'all'
  currentPage.value = 1
}

const clearSearch = () => {
  searchTerm.value = ''
}

const focusSearch = () => {
  // Focus on search input when Ctrl/Cmd + K is pressed
  const searchInput = document.querySelector('input[placeholder*="Ej: universidad"]')
  if (searchInput) {
    searchInput.focus()
    // Show a subtle indication that search was focused (like a toast notification could be added here)
  }
}

// Smart search help methods
const onSearchFocus = () => {
  searchFocused.value = true

  if (!helpDismissed.value && !helpDisabled.value) {
    showSearchHelp.value = true
  }
}

const onSearchBlur = () => {
  searchFocused.value = false

  // Hide help on blur (but keep if user is interacting with help)
  setTimeout(() => {
    if (!helpDismissed.value && !helpDisabled.value) {
      showSearchHelp.value = false
    }
  }, 200)
}

const dismissSearchHelp = () => {
  helpDismissed.value = true
  showSearchHelp.value = false
}

const toggleSearchHelp = () => {
  helpDisabled.value = !helpDisabled.value
  if (helpDisabled.value) {
    showSearchHelp.value = false
    localStorage.setItem('searchHelpDisabled', 'true')
  } else {
    localStorage.removeItem('searchHelpDisabled')
  }
}

// Lifecycle
onMounted(() => {
  loadEmpresas()

  // Check localStorage for user preferences
  const helpDisabledStored = localStorage.getItem('searchHelpDisabled')
  helpDisabled.value = helpDisabledStored === 'true'
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
