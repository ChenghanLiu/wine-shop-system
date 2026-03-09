import http from './http'

export const getRecommendProducts = () => http.get('/api/products/recommend')
export const getProducts = (params) => http.get('/api/products', { params })
export const getProductDetail = (id) => http.get(`/api/products/${id}`)
export const getProductComments = (id) => http.get(`/api/products/${id}/comments`)
export const getCategories = () => http.get('/api/categories')
export const getCategoryTree = () => http.get('/api/categories/tree')
export const getSubcategories = (params) => http.get('/api/subcategories', { params })