import http from './http'

export const createComment = (payload) => http.post('/api/comments', payload)