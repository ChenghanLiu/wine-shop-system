import axios from 'axios'
import { ElMessage } from 'element-plus'

const http = axios.create({
    baseURL: '/',
    timeout: 10000
})

http.interceptors.request.use((config) => {
    const token = localStorage.getItem('token')
    if (token) config.headers.Authorization = `Bearer ${token}`
    return config
})

http.interceptors.response.use(
    (res) => {
        const data = res.data
        if (typeof data?.code !== 'undefined' && data.code !== 200) {
            const error = {
                type: 'business',
                code: data.code,
                message: data.message || 'Request failed'
            }
            ElMessage.error(error.message)
            return Promise.reject(error)
        }
        return data?.data ?? data
    },
    (err) => {
        const status = err.response?.status
        const message = err.response?.data?.message || err.message || 'Network error'

        if (status === 401) {
            localStorage.removeItem('token')
            ElMessage.warning('登录状态已失效，请重新登录')
        } else {
            ElMessage.error(message)
        }

        return Promise.reject({
            type: 'network',
            status,
            message
        })
    }
)

export default http
