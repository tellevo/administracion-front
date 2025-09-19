import axios from 'axios'

const apiClient = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
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
    list: () => apiClient.get('/empresa'),
    get: (id) => apiClient.get(`/empresa/${id}`),
    create: (data) => apiClient.post('/empresa', data),
    update: (id, data) => apiClient.put(`/empresa/${id}`, data),
    delete: (id) => apiClient.delete(`/empresa/${id}`)
  }
}

export default api