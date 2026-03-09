import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '../stores/user'

const routes = [
    { path: '/login', component: () => import('../views/LoginView.vue'), meta: { public: true } },
    {
        path: '/admin',
        component: () => import('../layouts/AdminLayout.vue'),
        meta: { auth: true, admin: true },
        children: [
            { path: '', redirect: '/admin/products' },
            { path: 'users', name: 'admin-users', component: () => import('../views/admin/AdminUserListView.vue') },
            { path: 'products', name: 'admin-products', component: () => import('../views/admin/AdminProductListView.vue') },
            { path: 'orders', name: 'admin-orders', component: () => import('../views/admin/AdminOrderListView.vue') },
            { path: 'banners', name: 'admin-banners', component: () => import('../views/admin/AdminBannerListView.vue') },
            { path: 'comments', name: 'admin-comments', component: () => import('../views/admin/AdminCommentListView.vue') },
            { path: 'statistics', name: 'admin-statistics', component: () => import('../views/admin/AdminStatisticsView.vue') },
            { path: 'password', name: 'admin-password', component: () => import('../views/admin/AdminPasswordView.vue') }
        ]
    },
    {
        path: '/',
        component: () => import('../layouts/UserLayout.vue'),
        children: [
            { path: '', name: 'home', component: () => import('../views/HomeView.vue') },
            { path: 'products', name: 'products', component: () => import('../views/ProductListView.vue') },
            { path: 'products/:id', name: 'product-detail', component: () => import('../views/ProductDetailView.vue') },
            { path: 'cart', name: 'cart', component: () => import('../views/CartView.vue'), meta: { auth: true } },
            { path: 'checkout', name: 'checkout', component: () => import('../views/CheckoutView.vue'), meta: { auth: true } },
            { path: 'orders', name: 'orders', component: () => import('../views/MyOrdersView.vue'), meta: { auth: true } },
            { path: 'favorites', name: 'favorites', component: () => import('../views/FavoritesView.vue'), meta: { auth: true } },
            { path: 'profile', name: 'profile', component: () => import('../views/ProfileView.vue'), meta: { auth: true } }
        ]
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

router.beforeEach(async (to) => {
    const userStore = useUserStore()
    if (userStore.isLogin && !userStore.user) {
        await userStore.fetchMe().catch(() => userStore.logout())
    }

    if (to.meta.auth && !userStore.isLogin) {
        return `/login?redirect=${encodeURIComponent(to.fullPath)}`
    }
    if (to.meta.admin && !userStore.isAdmin) {
        return '/'
    }
    if (to.path === '/login' && userStore.isLogin) {
        return userStore.isAdmin ? '/admin' : '/'
    }
})

export default router
