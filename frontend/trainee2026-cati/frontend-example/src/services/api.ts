import axios from 'axios'

const api = axios.create({
  baseURL: '/',
})

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ` + token
  }
  return config
})

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401 || error.response?.status === 403) {
      localStorage.removeItem('token')
      window.location.reload()
    }
    return Promise.reject(error)
  }
)

export function decodeToken(token: string): { sub: string; exp: number; iat: number } | null {
  try {
    const payload = token.split('.')[1]
    return JSON.parse(atob(payload))
  } catch {
    return null
  }
}

export function getUserIdFromToken(): string | null {
  const token = localStorage.getItem('token')
  if (!token) return null
  const decoded = decodeToken(token)
  return decoded?.sub ?? null
}

export default api
