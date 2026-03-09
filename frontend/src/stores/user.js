import { defineStore } from 'pinia'
import { me } from '../api/auth'

export const useUserStore = defineStore('user', {
    state: () => ({
        token: localStorage.getItem('token') || '',
        user: null
    }),
    getters: {
        isLogin: (state) => !!state.token,
        role: (state) => (state.user?.role || '').toUpperCase(),
        isAdmin: (state) => (state.user?.role || '').toUpperCase() === 'ADMIN'
    },
    actions: {
        setToken(token) {
            this.token = token || ''
            if (token) localStorage.setItem('token', token)
            else localStorage.removeItem('token')
        },
        async fetchMe() {
            if (!this.token) return null
            this.user = await me()
            return this.user
        },
        logout() {
            this.setToken('')
            this.user = null
        }
    }
})
