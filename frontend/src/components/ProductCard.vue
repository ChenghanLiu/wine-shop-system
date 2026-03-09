<template>
  <div class="card product-card" @click="goDetail">
    <img :src="product.coverImage || fallback" class="cover" alt="cover" />
    <div class="body">
      <div class="name">{{ product.name }}</div>
      <div class="meta">销量 {{ product.sales || 0 }}</div>
      <div class="price">¥{{ Number(product.price || 0).toFixed(2) }}</div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const props = defineProps({ product: { type: Object, required: true } })
const router = useRouter()
const fallback = 'https://dummyimage.com/600x400/f3f3f3/999&text=Wine'

const goDetail = () => router.push(`/products/${props.product.id}`)
</script>

<style scoped>
.product-card {
  cursor: pointer;
  overflow: hidden;
  transition: transform .2s ease, box-shadow .2s ease;
}
.product-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 16px 30px rgba(125, 16, 32, 0.12);
}
.cover {
  width: 100%;
  height: 180px;
  object-fit: cover;
  display: block;
  background: #f8f8f8;
}
.body {
  padding: 14px;
}
.name {
  font-weight: 600;
  line-height: 1.4;
  min-height: 44px;
}
.meta {
  margin-top: 6px;
  color: var(--text-secondary);
  font-size: 13px;
}
.price {
  margin-top: 10px;
  color: var(--wine-red);
  font-size: 22px;
  font-weight: 700;
}
</style>
