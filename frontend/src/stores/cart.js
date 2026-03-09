import { defineStore } from 'pinia'
import { getCart } from '../api/cart'

export const useCartStore = defineStore('cart', {
    state: () => ({
        items: []
    }),
    getters: {
        itemCount: (state) => state.items.reduce((sum, i) => sum + (i.quantity || 0), 0),
        selectedCount: (state) => state.items.filter((i) => i.selected === 1).length
    },
    actions: {
        async refresh() {
            this.items = await getCart()
        },
        clear() {
            this.items = []
        }
    }
})
