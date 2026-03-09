<template>
  <header class="header-wrap">
    <div class="header-inner">
      <router-link class="brand" to="/">酒水销售系统</router-link>
      <nav class="nav" v-if="!userStore.isAdmin">
        <router-link to="/">首页</router-link>
        <router-link to="/products">商品</router-link>
        <router-link to="/cart">购物车</router-link>
        <router-link to="/orders">我的订单</router-link>
        <router-link to="/favorites">我的收藏</router-link>
        <router-link to="/profile">个人中心</router-link>
      </nav>
      <nav class="nav" v-else>
        <router-link to="/admin">管理后台</router-link>
      </nav>
      <div class="actions">
        <el-badge v-if="!userStore.isAdmin" :value="cartStore.itemCount" :hidden="!cartStore.itemCount">
          <el-button link @click="router.push('/cart')">购物车</el-button>
        </el-badge>
        <template v-if="userStore.isLogin">
          <span class="username">{{ userStore.user?.username || '用户' }}</span>
          <el-tag v-if="userStore.isAdmin" size="small" type="danger">管理员</el-tag>
          <el-button link type="danger" @click="logout">退出</el-button>
        </template>
        <el-button v-else type="primary" @click="router.push('/login')">登录</el-button>
      </div>
    </div>
  </header>
</template>

<script setup>
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { useCartStore } from '../stores/cart'

const userStore = useUserStore()
const cartStore = useCartStore()
const router = useRouter()

const logout = () => {
  userStore.logout()
  cartStore.clear()
  router.push('/login')
}

onMounted(async () => {
  if (userStore.isLogin) {
    await userStore.fetchMe().catch(() => userStore.logout())
    if (!userStore.isAdmin) {
      await cartStore.refresh().catch(() => {})
    }
  }
})
</script>

<style scoped>
.header-wrap {
  background: #fff;
  border-bottom: 1px solid var(--line-color);
  position: sticky;
  top: 0;
  z-index: 10;
}
.header-inner {
  max-width: 1200px;
  margin: 0 auto;
  height: 68px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 24px;
  padding: 0 20px;
}
.brand {
  font-size: 24px;
  font-weight: 700;
  color: var(--wine-red);
}
.nav {
  display: flex;
  gap: 20px;
}
.nav a.router-link-active {
  color: var(--wine-red);
  font-weight: 600;
}
.actions {
  display: flex;
  align-items: center;
  gap: 10px;
}
.username {
  color: var(--text-secondary);
}
</style>

