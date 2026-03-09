import http from './http'

export const getAdminUsers = () => http.get('/api/admin/users')
export const updateAdminUser = (id, payload) => http.put(`/api/admin/users/${id}`, payload)
export const deleteAdminUser = (id) => http.delete(`/api/admin/users/${id}`)

export const getAdminProducts = () => http.get('/api/admin/products')
export const createAdminProduct = (payload) => http.post('/api/admin/products', payload)
export const updateAdminProduct = (id, payload) => http.put(`/api/admin/products/${id}`, payload)
export const updateAdminProductStatus = (id, status) => http.put(`/api/admin/products/${id}/status`, null, { params: { status } })
export const deleteAdminProduct = (id) => http.delete(`/api/admin/products/${id}`)


export const getAdminCategories = () => http.get('/api/admin/categories')
export const getAdminSubcategories = () => http.get('/api/admin/subcategories')

export const getAdminOrders = (params) => http.get('/api/admin/orders', { params })
export const deliverAdminOrder = (id) => http.post(`/api/admin/orders/${id}/deliver`)
export const refundAdminOrder = (id) => http.post(`/api/admin/orders/${id}/refund`)

export const getAdminBanners = () => http.get('/api/admin/banners')
export const createAdminBanner = (payload) => http.post('/api/admin/banners', payload)
export const updateAdminBanner = (id, payload) => http.put(`/api/admin/banners/${id}`, payload)
export const deleteAdminBanner = (id) => http.delete(`/api/admin/banners/${id}`)

export const getAdminComments = () => http.get('/api/admin/comments')
export const updateAdminCommentStatus = (id, status) => http.put(`/api/admin/comments/${id}/status`, null, { params: { status } })
export const deleteAdminComment = (id) => http.delete(`/api/admin/comments/${id}`)

export const getAdminOverview = () => http.get('/api/admin/statistics/overview')
export const getAdminTopProducts = () => http.get('/api/admin/statistics/top-products')
export const getAdminCategoryRatio = () => http.get('/api/admin/statistics/category-ratio')
