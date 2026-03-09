<template>
  <div>
    <header class="header-wrap">
      <div class="header-inner">
        <router-link class="brand" to="/admin">酒水销售系统管理员后台</router-link>
        <nav class="nav">
          <router-link to="/admin/users">用户</router-link>
          <router-link to="/admin/products">商品</router-link>
          <router-link to="/admin/orders">订单</router-link>
          <router-link to="/admin/banners">轮播图</router-link>
          <router-link to="/admin/comments">评论</router-link>
          <router-link to="/admin/statistics">统计</router-link>
          <router-link to="/admin/password">改密</router-link>
        </nav>
        <div class="actions">
          <span class="username">{{ userStore.user?.username || '管理员' }}</span>
          <el-button link type="danger" @click="logout">退出</el-button>
        </div>
      </div>
    </header>
    <router-view />
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'

const userStore = useUserStore()
const router = useRouter()

const logout = () => {
  userStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.header-wrap { background: #fff; border-bottom: 1px solid var(--line-color); position: sticky; top: 0; z-index: 10; }
.header-inner { max-width: 1200px; margin: 0 auto; height: 68px; display: flex; align-items: center; justify-content: space-between; gap: 24px; padding: 0 20px; }
.brand { font-size: 22px; font-weight: 700; color: var(--wine-red); }
.nav { display: flex; gap: 20px; }
.nav a.router-link-active { color: var(--wine-red); font-weight: 600; }
.actions { display: flex; align-items: center; gap: 10px; }
.username { color: var(--text-secondary); }
</style>

