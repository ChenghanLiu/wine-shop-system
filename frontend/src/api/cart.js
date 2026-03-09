import http from './http'

export const getCart = () => http.get('/api/cart')
export const addCartItem = (payload) => http.post('/api/cart/items', payload)
export const updateCartItem = (id, payload) => http.put(`/api/cart/items/${id}`, payload)
export const deleteCartItem = (id) => http.delete(`/api/cart/items/${id}`)