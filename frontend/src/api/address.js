import http from './http'

export const getAddresses = () => http.get('/api/addresses')
export const createAddress = (payload) => http.post('/api/addresses', payload)
export const setDefaultAddress = (id) => http.put(`/api/addresses/${id}/default`)
