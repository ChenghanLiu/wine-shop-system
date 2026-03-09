<template>
  <div class="page-wrap">
    <div class="section-title">我的收藏</div>
    <div class="card" style="padding:16px;">
      <el-empty v-if="!products.length" description="你还没有收藏商品" />
      <el-row v-else :gutter="18">
        <el-col v-for="item in products" :key="item.id" :xs="24" :sm="12" :md="8" :lg="6">
          <ProductCard :product="item" />
          <div class="ops">
            <el-button size="small" type="danger" plain @click="unfavorite(item.id)">取消收藏</el-button>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import ProductCard from '../components/ProductCard.vue'
import { getFavorites, removeFavorite } from '../api/favorite'
import { getProductDetail } from '../api/product'

const products = ref([])

const load = async () => {
  const list = await getFavorites()
  const ids = (Array.isArray(list) ? list : []).map((i) => i.productId)
  const details = await Promise.all(ids.map(async (id) => {
    try {
      return await getProductDetail(id)
    } catch {
      return null
    }
  }))
  products.value = details.filter(Boolean)
}

const unfavorite = async (productId) => {
  await removeFavorite(productId)
  ElMessage.success('已取消收藏')
  await load()
}

onMounted(load)
</script>

<style scoped>
.ops {
  margin: 8px 0 18px;
}
</style>
