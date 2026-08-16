import axios from 'axios'

const API_BASE = '/api'

const api = axios.create({
  baseURL: API_BASE,
  headers: { 'Content-Type': 'application/json' }
})

api.interceptors.response.use(
  res => res.data,
  err => {
    const msg = err.response?.data?.message || err.message || '请求失败'
    return Promise.reject(new Error(msg))
  }
)

export const resourceApi = {
  getAll(page = 0, size = 20, sortBy = 'createdAt') {
    return api.get('/resources', { params: { page, size, sortBy } })
  },

  search(keyword, page = 0, size = 20) {
    return api.get('/resources', { params: { q: keyword, page, size, sortBy: 'createdAt' } })
  },

  getById(id) {
    return api.get(`/resources/${id}`)
  },

  create(data) {
    return api.post('/resources', data)
  },

  getRecent() {
    return api.get('/resources/recent')
  },

  getPopular() {
    return api.get('/resources/popular')
  },

  getCategories() {
    return api.get('/resources/categories')
  },

  getStats() {
    return api.get('/resources/stats')
  }
}

export default api
