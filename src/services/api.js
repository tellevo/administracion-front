import axios from 'axios'

const apiClient = axios.create({
  // Use explicit API URL if provided, otherwise fallback to relative '/api' so Nginx proxy works in prod
  baseURL: import.meta.env.VITE_API_URL || '/api',
  //baseURL: 'https://admin.tellevoapp.com/api',
  headers: {
    'Content-Type': 'application/json',
  },
  withCredentials: true
})

// Add request interceptor to include auth token
apiClient.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

// Add response interceptor to handle errors
apiClient.interceptors.response.use(
  response => response,
  error => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('usuario')
      window.location.href = '/'
    }
    return Promise.reject(error)
  }
)

export const api = {
  login: (credentials) => apiClient.post('/login', credentials),
  health: () => apiClient.get('/health'),
  empresas: {
    list: () => apiClient.get('/empresas'),
    get: (id) => apiClient.get(`/empresas/${id}`),
    create: (data) => apiClient.post('/empresas', data),
    update: (id, data) => apiClient.put(`/empresas/${id}`, data),
    delete: (id) => apiClient.delete(`/empresas/${id}`),
    // New file upload endpoints
    createWithUpload: (formData) => apiClient.post('/empresas/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    }),
    updateWithUpload: (id, formData) => apiClient.put(`/empresas/${id}/upload`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    }),
    uploadLogo: (id, formData) => apiClient.post(`/empresas/${id}/logo`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
  },
  dashboard: {
    overview: () => apiClient.get('/dashboard/overview')
  },
  // Optional: registration endpoint if backend supports it later
  register: (data) => apiClient.post('/register', data)
}

export default api
