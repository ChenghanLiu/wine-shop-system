<template>
  <div class="page-wrap">
    <div class="section-title">商品列表</div>
    <div class="card filter-card">
      <el-input v-model="query.keyword" placeholder="搜索商品" clearable style="width:260px" @change="load" />
      <el-select v-model="query.categoryId" placeholder="一级分类" clearable style="width:180px" @change="onCategoryChange">
        <el-option v-for="c in categories" :key="c.id" :label="c.name" :value="c.id" />
      </el-select>
      <el-select v-model="query.subcategoryId" placeholder="二级分类" clearable style="width:180px" @change="load">
        <el-option v-for="s in subcategories" :key="s.id" :label="s.name" :value="s.id" />
      </el-select>
      <el-button type="primary" @click="load">筛选</el-button>
    </div>

    <el-row :gutter="18" style="margin-top: 20px;">
      <el-col v-for="item in list" :key="item.id" :xs="24" :sm="12" :md="8" :lg="6">
        <ProductCard :product="item" />
        <div class="item-ops">
          <el-button size="small" plain :type="favoriteIds.has(item.id) ? 'danger' : undefined" @click="toggleFavorite(item.id)">
            {{ favoriteIds.has(item.id) ? '取消收藏' : '收藏' }}
          </el-button>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getProducts, getCategories, getSubcategories } from '../api/product'
import { addFavorite, getFavorites, removeFavorite } from '../api/favorite'
import ProductCard from '../components/ProductCard.vue'

const query = ref({ keyword: '', categoryId: undefined, subcategoryId: undefined })
const router = useRouter()
const route = useRoute()
const list = ref([])
const categories = ref([])
const subcategories = ref([])
const favoriteIds = ref(new Set())

const load = async () => {
  const params = {
    keyword: query.value.keyword || undefined,
    categoryId: query.value.categoryId,
    subcategoryId: query.value.subcategoryId
  }
  list.value = await getProducts(params)
}

const onCategoryChange = async () => {
  query.value.subcategoryId = undefined
  subcategories.value = await getSubcategories({ categoryId: query.value.categoryId })
  await load()
}


const loadFavorites = async () => {
  try {
    const list = await getFavorites()
    favoriteIds.value = new Set((Array.isArray(list) ? list : []).map((i) => i.productId))
  } catch {
    favoriteIds.value = new Set()
  }
}

const toggleFavorite = async (productId) => {
  try {
    if (favoriteIds.value.has(productId)) {
      await removeFavorite(productId)
      favoriteIds.value.delete(productId)
    } else {
      await addFavorite(productId)
      favoriteIds.value.add(productId)
    }
    favoriteIds.value = new Set(favoriteIds.value)
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
  categories.value = await getCategories()
  subcategories.value = await getSubcategories({})
  await Promise.all([load(), loadFavorites()])
})
</script>

<style scoped>
.item-ops {
  margin: 8px 0 18px;
}

.filter-card {
  padding: 16px;
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  align-items: center;
}
</style>

