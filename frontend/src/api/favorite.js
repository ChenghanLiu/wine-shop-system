import http from './http'

export const getFavorites = () => http.get('/api/favorites')
export const addFavorite = (productId) => http.post(`/api/favorites/${productId}`)
export const removeFavorite = (productId) => http.delete(`/api/favorites/${productId}`)
