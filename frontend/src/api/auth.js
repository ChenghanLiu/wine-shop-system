import http from './http'

export const login = (payload) => http.post('/api/auth/login', payload)
export const adminLogin = (payload) => http.post('/api/admin/auth/login', payload)
export const register = (payload) => http.post('/api/auth/register', payload)
export const me = () => http.get('/api/auth/me')
export const updateMe = (payload) => http.put('/api/auth/me', payload)
export const changeAdminPassword = (payload) => http.put('/api/admin/auth/password', payload)
