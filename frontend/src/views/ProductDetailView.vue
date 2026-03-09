<template>
  <div class="page-wrap" v-if="product">
    <div class="card detail-wrap">
      <img :src="product.coverImage || fallback" class="detail-cover" />
      <div class="detail-main">
        <h2>{{ product.name }}</h2>
        <div class="price">¥{{ Number(product.price || 0).toFixed(2) }}</div>
        <p class="desc">{{ product.description || '暂无描述' }}</p>
        <div class="ops">
          <el-input-number v-model="quantity" :min="1" />
          <el-button type="primary" @click="addToCartNow">加入购物车</el-button>
          <el-button plain :type="isFavorite ? 'danger' : undefined" @click="toggleFavorite">{{ isFavorite ? '已收藏' : '收藏' }}</el-button>
        </div>
      </div>
    </div>

    <div class="card comments" style="margin-top: 20px;">
      <div class="section-title" style="font-size:20px;margin:0 0 12px;">用户评论</div>
      <el-empty v-if="!comments.length" description="暂无评论" />
      <div v-for="c in comments" :key="c.id" class="comment-item">
        <el-rate :model-value="c.score" disabled size="small" />
        <div class="content">{{ c.content }}</div>
        <div class="time">{{ c.createdAt }}</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getProductDetail, getProductComments } from '../api/product'
import { addCartItem } from '../api/cart'
import { addFavorite, getFavorites, removeFavorite } from '../api/favorite'

const route = useRoute()
const router = useRouter()
const product = ref(null)
const comments = ref([])
const quantity = ref(1)
const fallback = 'https://dummyimage.com/800x600/f3f3f3/999&text=Wine'
const isFavorite = ref(false)

const addToCartNow = async () => {
  try {
    await addCartItem({ productId: product.value.id, quantity: quantity.value, selected: 1 })
    ElMessage.success('已加入购物车')
    router.push('/cart')
  } catch (e) {
    if (e?.status === 401 || /登录|unauthorized/i.test(e?.message || '')) {
      ElMessage.warning('请先登录后再加入购物车')
      router.push(`/login?redirect=${encodeURIComponent(route.fullPath)}`)
      return
    }
    if (e?.type === 'business') {
      ElMessage.warning(e.message || '暂时无法加入购物车')
      return
    }
    ElMessage.error('网络异常，请稍后重试')
  }
}


const loadFavoriteState = async () => {
  try {
    const list = await getFavorites()
    isFavorite.value = (Array.isArray(list) ? list : []).some((i) => i.productId === Number(product.value?.id))
  } catch {
    isFavorite.value = false
  }
}

const toggleFavorite = async () => {
  if (!product.value?.id) return
  try {
    if (!isFavorite.value) {
      await addFavorite(product.value.id)
      isFavorite.value = true
      ElMessage.success('已加入收藏')
    } else {
      await removeFavorite(product.value.id)
      isFavorite.value = false
      ElMessage.success('已取消收藏')
    }
  } catch (e) {
    if (e?.status === 401) {
      ElMessage.warning('请先登录后再收藏')
      router.push(`/login?redirect=${encodeURIComponent(route.fullPath)}`)
      return
    }
    ElMessage.error('收藏操作失败，请稍后重试')
  }
}

onMounted(async () => {
  const id = route.params.id
  product.value = await getProductDetail(id)
  comments.value = await getProductComments(id)
  await loadFavoriteState()
})
</script>

<style scoped>
.detail-wrap {
  display: grid;
  grid-template-columns: 420px 1fr;
  gap: 28px;
  padding: 22px;
}
.detail-cover {
  width: 100%;
  height: 360px;
  object-fit: cover;
  border-radius: 12px;
}
.price {
  color: var(--wine-red);
  font-size: 30px;
  font-weight: 700;
}
.desc {
  color: var(--text-secondary);
  line-height: 1.7;
}
.ops {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-top: 18px;
}
.comments { padding: 20px; }
.comment-item { padding: 12px 0; border-bottom: 1px solid #f2f2f2; }
.content { margin-top: 8px; }
.time { color: #999; font-size: 12px; margin-top: 6px; }
</style>
