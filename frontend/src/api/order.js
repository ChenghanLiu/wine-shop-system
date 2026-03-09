import http from './http'

export const checkout = (payload) => http.post('/api/orders/checkout', payload)
export const getMyOrders = (params) => http.get('/api/orders', { params })
export const getOrderDetail = (id) => http.get(`/api/orders/${id}`)
export const payOrder = (id) => http.post(`/api/orders/${id}/pay`)
export const cancelOrder = (id) => http.post(`/api/orders/${id}/cancel`)
export const confirmOrder = (id) => http.post(`/api/orders/${id}/confirm`)
