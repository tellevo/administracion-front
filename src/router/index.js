import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import RegisterView from '../views/RegisterView.vue'
import DrawerLayout from '../components/DrawerLayout.vue'

import DashboardHome from '../views/dashboard/DashboardHome.vue'
import DashboardStats from '../views/dashboard/DashboardStats.vue'
import DashboardSettings from '../views/dashboard/DashboardSettings.vue'
import EmpresaView from '../views/dashboard/EmpresaView.vue'
import EmpresaListView from '../views/dashboard/EmpresaListView.vue'
import VentasStreamView from '../views/dashboard/VentasStreamView.vue'

const routes = [
  { path: '/', component: LoginView },
  { path: '/register', component: RegisterView },

  {
    path: '/dashboard',
    component: DrawerLayout,
    meta: { requiresAuth: true },
    children: [
      { path: '', component: DashboardHome },
      { path: 'stats', component: DashboardStats },
      { path: 'settings', component: DashboardSettings },
      { path: 'empresa', component: EmpresaView },
      { path: 'empresas', component: EmpresaListView },
      { path: 'ventas-stream', component: VentasStreamView }
    ]
  }
]


const router = createRouter({
  history: createWebHistory(),
  routes
})

// 🔒 Protección global
router.beforeEach((to, from, next) => {
  const usuario = localStorage.getItem('usuario')

  if (to.meta.requiresAuth && !usuario) {
    next({ path: '/' }) // redirige al login si no está logueado
  } else {
    next() // continúa normal
  }
})

export default router
