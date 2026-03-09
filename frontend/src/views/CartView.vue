<template>
  <div class="page-wrap">
    <div class="section-title">购物车</div>
    <div class="card" style="padding:16px;">
      <el-empty v-if="!items.length" description="购物车空空如也，快去选购吧" />
      <template v-else>
        <el-table :data="items" v-loading="loading">
          <el-table-column label="商品" min-width="260">
            <template #default="{ row }">
              <div class="product-cell">
                <img :src="detailMap[row.productId]?.coverImage || fallback" class="thumb" />
                <div>
                  <div class="name">{{ detailMap[row.productId]?.name || `商品#${row.productId}` }}</div>
                  <div class="sub">ID: {{ row.productId }}</div>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="单价" width="120">
            <template #default="{ row }">¥{{ Number(detailMap[row.productId]?.price || 0).toFixed(2) }}</template>
          </el-table-column>
          <el-table-column label="数量" width="170">
            <template #default="{ row }">
              <el-input-number :model-value="row.quantity" :min="1" @change="(val)=>updateQty(row,val)" />
            </template>
          </el-table-column>
          <el-table-column label="选中" width="120">
            <template #default="{ row }">
              <el-switch :model-value="row.selected===1" @change="(v)=>toggleSelected(row,v)" />
            </template>
          </el-table-column>
          <el-table-column label="小计" width="120">
            <template #default="{ row }">¥{{ Number((detailMap[row.productId]?.price || 0) * (row.quantity || 0)).toFixed(2) }}</template>
          </el-table-column>
          <el-table-column label="操作" width="140">
            <template #default="{ row }">
              <el-button link type="danger" @click="remove(row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="summary">
          <span>已选 {{ selectedCount }} 件商品，合计 <b>¥{{ selectedTotal.toFixed(2) }}</b></span>
          <el-button type="primary" :disabled="selectedCount===0" @click="$router.push('/checkout')">去结算</el-button>
        </div>
      </template>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { getCart, updateCartItem, deleteCartItem } from '../api/cart'
import { getProductDetail } from '../api/product'

const loading = ref(false)
const items = ref([])
const detailMap = ref({})
const fallback = 'https://dummyimage.com/120x120/f3f3f3/999&text=Wine'

const selectedCount = computed(() => items.value.filter((i) => i.selected === 1).length)
const selectedTotal = computed(() => items.value
    .filter((i) => i.selected === 1)
    .reduce((sum, i) => sum + Number(detailMap.value[i.productId]?.price || 0) * Number(i.quantity || 0), 0))

const loadDetails = async () => {
  const ids = [...new Set(items.value.map((i) => i.productId))]
  const pairs = await Promise.all(ids.map(async (id) => {
    try {
      return [id, await getProductDetail(id)]
    } catch {
      return [id, null]
    }
  }))
  detailMap.value = Object.fromEntries(pairs)
}

const load = async () => {
  loading.value = true
  try {
    items.value = await getCart()
    await loadDetails()
  } finally {
    loading.value = false
  }
}

const updateQty = async (row, val) => {
  await updateCartItem(row.id, { quantity: val, selected: row.selected })
  await load()
}
const toggleSelected = async (row, checked) => {
  await updateCartItem(row.id, { quantity: row.quantity, selected: checked ? 1 : 0 })
  await load()
}
const remove = async (id) => {
  await deleteCartItem(id)
  ElMessage.success('已删除')
  await load()
}

onMounted(load)
</script>

<style scoped>
.summary {
  margin-top: 18px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.product-cell {
  display: flex;
  align-items: center;
  gap: 10px;
}
.thumb {
  width: 52px;
  height: 52px;
  object-fit: cover;
  border-radius: 8px;
}
.name { font-weight: 600; }
.sub { font-size: 12px; color: #999; }
</style>
