# TeLlevo - Administración Frontend

## 🌱 Descripción del Proyecto

**TeLlevo** es una plataforma de administración para un sistema de carpooling eco-amigable que busca reducir la huella de CO₂ mediante la compartición inteligente de viajes. Este frontend proporciona herramientas de gestión y análisis para administradores del sistema.

### 🎯 Objetivos de Negocio
- **Sostenibilidad**: Reducir emisiones de CO₂ promoviendo viajes compartidos
- **Análisis**: Proporcionar insights sobre patrones de uso y impacto ambiental
- **Gestión**: Herramientas administrativas para optimizar rutas y usuarios
- **Transparencia**: Visualización clara del impacto ambiental positivo

---

## 🏗️ Arquitectura del Sistema

### Stack Tecnológico
```
Frontend: Vue 3 + Composition API + <script setup>
Build Tool: Vite
Styling: Tailwind CSS + DaisyUI
Charts: Chart.js + vue-chartjs  
Icons: Lucide Vue Next
HTTP Client: Axios
Authentication: JWT + localStorage
```

### Estructura del Proyecto
```
src/
├── components/          # Componentes reutilizables
│   ├── charts/         # Componentes de visualización
│   ├── EmpresaForm.vue # Formulario de empresa con validación y logo preview
│   ├── DashboardSummary.vue
│   └── DrawerLayout.vue
├── views/              # Vistas principales
│   ├── dashboard/      # Módulo dashboard
│   │   ├── EmpresaView.vue  # Vista de nueva empresa
│   │   ├── DashboardHome.vue
│   │   ├── DashboardStats.vue
│   │   └── DashboardSettings.vue
│   ├── LoginView.vue
│   └── RegisterView.vue
├── router/             # Configuración de rutas
├── assets/             # Recursos estáticos
└── main.js             # Punto de entrada
```

---

## 🚀 Configuración e Instalación

### Prerequisitos
- **Node.js** >= 16.x
- **npm** >= 8.x o **pnpm** >= 7.x

### Instalación
```bash
# Clonar el repositorio
git clone https://github.com/tellevo/administracion-front.git
cd administracion-front

# Instalar dependencias
npm install

# Ejecutar en modo desarrollo
npm run dev

# Construir para producción
npm run build

# Vista previa de la build
npm run preview
```

### Variables de Entorno
```env
# .env.local
VITE_API_BASE_URL=http://localhost:8080/api
VITE_APP_NAME=TeLlevo Admin
VITE_APP_VERSION=1.0.0
```

---

## 🎨 Principios de Diseño

### Sistema de Colores
- **Primario**: Azul (#3B82F6) - Confianza y profesionalismo
- **Secundario**: Cian (#06B6D4) - Frescura y sostenibilidad  
- **Éxito**: Verde (#10B981) - Impacto ambiental positivo
- **Advertencia**: Ámbar (#F59E0B) - Alertas y notificaciones

### Componentes de UI
```vue
<!-- Ejemplo de uso de componentes DaisyUI -->
<button class="btn btn-primary">
  Acción Principal
</button>

<div class="card bg-base-200 shadow-md">
  <div class="card-body">
    <h2 class="card-title">Título</h2>
    <p>Contenido de la tarjeta</p>
  </div>
</div>
```

### Responsive Design
- **Mobile First**: Diseño optimizado para dispositivos móviles
- **Breakpoints**: sm(640px), md(768px), lg(1024px), xl(1280px)
- **Grid System**: CSS Grid + Flexbox para layouts adaptativos

---

## 🔐 Sistema de Autenticación

### Flujo de Autenticación
1. **Login**: POST `/api/login` → JWT Token
2. **Almacenamiento**: localStorage(`token`, `usuario`)
3. **Protección**: Router guards en rutas protegidas
4. **Logout**: Limpieza de localStorage + redirect

### Implementación
```js
// Router Guard
router.beforeEach((to, from, next) => {
  const usuario = localStorage.getItem('usuario')
  
  if (to.meta.requiresAuth && !usuario) {
    next({ path: '/' })
  } else {
    next()
  }
})
```

---

## 📊 Sistema de Visualización

### Componentes de Charts
```vue
<template>
  <!-- Componente reutilizable -->
  <BarChart :chartData="data" :chartOptions="options" />
</template>

<script setup>
import BarChart from '@/components/charts/BarChart.vue'

const data = {
  labels: ['Enero', 'Febrero', 'Marzo'],
  datasets: [{
    label: 'CO₂ Ahorrado (kg)',
    data: [120, 190, 300],
    backgroundColor: '#10B981'
  }]
}
</script>
```

### Métricas Clave
- **Usuarios Activos**: Cantidad de usuarios registrados y activos
- **Viajes Completados**: Total de trayectos exitosos
- **Kms Compartidos**: Distancia total en carpooling
- **CO₂ Ahorrado**: Impacto ambiental positivo en kg
- **Pagos Realizados**: Volumen económico de transacciones

---

## 🔗 Integración API

### Endpoints Principales
```js
// Dashboard Overview
GET /api/dashboard/overview
Response: {
  usuariosActivos: number,
  viajesCompletados: number,
  kmsCompartidos: number,
  co2Ahorrado: number,
  pagosRealizados: number
}

// Gestión de Empresas
POST /api/empresas
Body: {
  nombre: string,
  dominio: string,      // formato: @nombreempresa.cl
  logoUrl: string       // debe terminar en .svg
}
Response: { id: number, nombre: string, dominio: string, logoUrl: string, message: string }

GET /api/empresas
Response: [Empresa[]]

GET /api/empresas/{id}
Response: Empresa

PUT /api/empresas/{id}
Body: { nombre: string, dominio: string, logoUrl: string }
Response: Empresa

DELETE /api/empresas/{id}
Response: { message: string }

// Autenticación
POST /api/login
Body: { username: string, password: string }
Response: { token: string, message: string }
```

### Cliente HTTP (Axios)
```js
// Configuración base
import axios from 'axios'

const apiClient = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  headers: {
    'Content-Type': 'application/json'
  }
})

// Interceptor para tokens
apiClient.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})
```

---

## 🎯 Funcionalidades Clave

### Dashboard Principal
- **Métricas en Tiempo Real**: KPIs actualizados del sistema
- **Gráficos Interactivos**: Visualización de tendencias y patrones
- **Navegación Intuitiva**: Sidebar colapsible con rutas organizadas

### Gestión de Rutas
- **Configuración**: Definición y optimización de rutas
- **Análisis**: Estadísticas de uso por ruta
- **Impacto**: Cálculo de beneficio ambiental por ruta

### Gestión de Empresas
- **Crear Nueva Empresa**: Formulario con validación completa
- **Vista Previa del Logo**: Imagen SVG se muestra en tiempo real
- **Validación de Dominio**: Solo formato `@xxxxx.cl` aceptado
- **Validación de Logo**: Solo URLs terminadas en `.svg`
- **Feedback Visual**: Indicadores de éxito/error durante la validación

### Formulario de Empresa
```vue
<template>
  <form @submit.prevent="submitForm">
    <!-- Campos validados con preview del logo -->
    <input v-model="form.nombre" placeholder="Nombre de la empresa" />
    <input v-model="form.dominio" placeholder="Ej: @pucv.cl" />
    <input v-model="form.logoUrl" placeholder="URL del logo SVG" />

    <!-- Preview del logo -->
    <div v-if="isValidSvgUrl" class="logo-preview">
      <img :src="form.logoUrl" alt="Logo preview" />
    </div>
  </form>
</template>
```

### Sistema de Temas
```js
// Cambio dinámico de tema
const toggleTheme = () => {
  const current = document.documentElement.getAttribute('data-theme')
  const nuevoTema = current === 'dark' ? 'light' : 'dark'
  document.documentElement.setAttribute('data-theme', nuevoTema)
  localStorage.setItem('tema', nuevoTema)
}
```

---

## 🚦 Guías de Desarrollo

### Convenciones de Código
```vue
<!-- Estructura de componente Vue 3 -->
<template>
  <div class="container">
    <h1>{{ titulo }}</h1>
    <ComponenteHijo v-model="datos" @evento="manejarEvento" />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import ComponenteHijo from './ComponenteHijo.vue'

// Props y emits
const props = defineProps({
  titulo: String,
  configuracion: Object
})

const emit = defineEmits(['actualizado'])

// Estado reactivo
const datos = ref([])
const cargando = ref(false)

// Computadas
const datosFormateados = computed(() => 
  datos.value.map(item => ({ ...item, formateado: true }))
)

// Métodos
const manejarEvento = (evento) => {
  emit('actualizado', evento)
}

// Lifecycle
onMounted(async () => {
  await cargarDatos()
})
</script>

<style scoped>
.container {
  @apply p-4 bg-base-100 rounded-lg shadow-md;
}
</style>
```

### Estructura de Carpetas
```
components/
├── charts/              # Componentes de gráficos
│   ├── BarChart.vue
│   └── CombinedChart.vue
├── ui/                  # Componentes base reutilizables
│   ├── Button.vue
│   ├── Card.vue
│   └── Modal.vue
└── layout/              # Componentes de diseño
    ├── DrawerLayout.vue
    └── Navbar.vue
```

### Naming Conventions
- **Componentes**: PascalCase (`DashboardSummary.vue`)
- **Props**: camelCase (`chartData`, `isLoading`)
- **Eventos**: kebab-case (`@user-updated`)
- **CSS Classes**: Tailwind + descriptivos (`btn-primary`, `card-eco`)

---

## 🧪 Testing y Calidad

### Testing Strategy
```bash
# Unit Tests con Vitest
npm run test

# E2E Tests con Cypress
npm run test:e2e

# Coverage Report
npm run coverage
```

### Herramientas de Calidad
```json
{
  "devDependencies": {
    "eslint": "^8.x",
    "@vue/eslint-config-prettier": "^8.x",
    "prettier": "^2.x",
    "husky": "^8.x",
    "lint-staged": "^13.x"
  }
}
```

---

## 🔄 CI/CD Pipeline

### GitHub Actions Workflow
```yaml
# .github/workflows/deploy.yml
name: Deploy to Production

on:
  push:
    branches: [main]

jobs:
  build-and-deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-node@v3
        with:
          node-version: '18'
      
      - name: Install dependencies
        run: npm ci
      
      - name: Run tests
        run: npm run test
      
      - name: Build project
        run: npm run build
      
      - name: Deploy to production
        run: npm run deploy
```

---

## 📚 Documentación de Componentes

### DashboardSummary
```vue
<!-- Componente principal del dashboard -->
<DashboardSummary />
```

**Props**: Ninguna
**Emits**: Ninguno
**Funcionalidad**: Muestra métricas clave en tarjetas responsivas

### DrawerLayout
```vue
<!-- Layout principal con sidebar -->
<DrawerLayout>
  <router-view />
</DrawerLayout>
```

**Características**:
- Sidebar colapsible
- Navegación activa
- Cambio de tema
- Logout funcional

### Charts Components
```vue
<!-- Gráfico de barras reutilizable -->
<BarChart 
  :chart-data="data" 
  :chart-options="options" 
/>
```

**Props**:
- `chartData`: Objeto con datos del gráfico
- `chartOptions`: Configuración de Chart.js

---

## 🚀 Deployment

### Build para Producción
```bash
# Generar build optimizada
npm run build

# Los archivos se generan en dist/
dist/
├── index.html
├── assets/
│   ├── index-[hash].js
│   └── index-[hash].css
└── favicon.ico
```

### Configuración de Servidor
```nginx
# nginx.conf para SPA
server {
    listen 80;
    server_name tellevo-admin.com;
    root /var/www/tellevo-admin/dist;
    index index.html;
    
    # Fallback para Vue Router
    location / {
        try_files $uri $uri/ /index.html;
    }
    
    # Cache para assets
    location /assets/ {
        expires 1y;
        add_header Cache-Control "public, immutable";
    }
}
```

---

## 🔍 Monitoreo y Analytics

### Métricas Clave a Monitorear
- **Performance**: Core Web Vitals, tiempo de carga
- **Errors**: JavaScript errors, API failures
- **Usage**: Páginas más visitadas, flujos de usuario
- **Business**: Conversiones, retención de usuarios

### Herramientas Recomendadas
- **Sentry**: Error tracking y performance monitoring
- **Google Analytics**: Análisis de comportamiento de usuario
- **Lighthouse**: Auditorías de performance y accesibilidad

---

## 🌍 Impacto Ambiental y Sostenibilidad

### Cálculos de CO₂
```js
// Fórmulas para cálculo de impacto ambiental
const calcularCO2Ahorrado = (distanciaKm, pasajeros) => {
  const emisionesPorKm = 0.12 // kg CO₂ por km por persona
  const ahorroTotal = distanciaKm * (pasajeros - 1) * emisionesPorKm
  return parseFloat(ahorroTotal.toFixed(2))
}

const calcularImpactoMensual = (viajes) => {
  return viajes.reduce((total, viaje) => 
    total + calcularCO2Ahorrado(viaje.distancia, viaje.pasajeros), 0
  )
}
```

### Visualización de Impacto
- **Gráficos Temporales**: Evolución del ahorro de CO₂
- **Comparativas**: vs transporte individual
- **Objetivos**: Metas de sostenibilidad mensual/anual

---

## 🤝 Contribución y Mantenimiento

### Proceso de Contribución
1. **Fork** del repositorio
2. **Branch** para nueva feature: `git checkout -b feature/nueva-funcionalidad`
3. **Commit** con mensajes descriptivos
4. **Push** y crear **Pull Request**
5. **Code Review** y merge

### Commit Convention
```bash
# Formato: tipo(scope): descripción
feat(dashboard): añadir gráfico de CO₂ mensual
fix(auth): corregir validación de tokens expirados  
docs(readme): actualizar guía de instalación
style(components): aplicar nueva paleta de colores
```

---

## 📞 Soporte y Contacto

### Equipo de Desarrollo
- **Tech Lead**: [nombre@tellevo.com]
- **Frontend**: [frontend@tellevo.com]  
- **DevOps**: [devops@tellevo.com]

### Recursos Adicionales
- **Documentación API**: [api-docs.tellevo.com]
- **Design System**: [design.tellevo.com]
- **Status Page**: [status.tellevo.com]

---

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

---

**¿Construyamos un futuro más sostenible juntos?** 🌱🚗💚

TeLlevo Admin Dashboard - Reduciendo la huella de carbono, un viaje a la vez.
