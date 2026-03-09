<template>
  <div class="page-wrap">
    <div class="section-title">商品管理</div>
    <div class="card" style="padding:16px;">
      <div style="margin-bottom:12px;">
        <el-button type="primary" @click="openCreate">新增商品</el-button>
      </div>
      <el-table :data="products" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column label="封面" width="110">
          <template #default="{ row }">
            <el-image
                v-if="row.coverImage"
                :src="row.coverImage"
                fit="cover"
                style="width:60px;height:60px;border-radius:4px;"
            >
              <template #error>
                <div class="image-fallback">Image failed to load</div>
              </template>
            </el-image>
            <span v-else>--</span>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="商品名" min-width="220" />
        <el-table-column label="分类" min-width="120">
          <template #default="{ row }">{{ getCategoryName(row.categoryId) }}</template>
        </el-table-column>
        <el-table-column label="子分类" min-width="140">
          <template #default="{ row }">{{ getSubcategoryName(row.subcategoryId) }}</template>
        </el-table-column>
        <el-table-column prop="price" label="价格" width="120">
          <template #default="{ row }">¥{{ Number(row.price || 0).toFixed(2) }}</template>
        </el-table-column>
        <el-table-column prop="stock" label="库存" width="100" />
        <el-table-column prop="status" label="状态" width="120">
          <template #default="{ row }">
            <el-switch :model-value="Number(row.status)===1" @change="(v)=>toggleStatus(row, v)" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="170">
          <template #default="{ row }">
            <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="remove(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="visible" :title="form.id ? '编辑商品' : '新增商品'" width="620px">
      <el-form :model="form" label-width="90px">
        <el-form-item label="商品名"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="分类">
          <el-select v-model="form.categoryId" style="width:100%" @change="onCategoryChange">
            <el-option v-for="item in categories" :key="item.id" :label="item.name" :value="Number(item.id)" />
          </el-select>
        </el-form-item>
        <el-form-item label="子分类">
          <el-select v-model="form.subcategoryId" style="width:100%">
            <el-option v-for="item in subcategoriesByCategory" :key="item.id" :label="item.name" :value="Number(item.id)" />
          </el-select>
        </el-form-item>
        <el-form-item label="封面图URL"><el-input v-model="form.coverImage" /></el-form-item>
        <el-form-item label="图片预览">
          <el-image
              v-if="form.coverImage"
              :src="form.coverImage"
              fit="cover"
              style="width:120px;height:120px;border-radius:6px;"
          >
            <template #error>
              <div class="image-fallback image-preview-fallback">Image failed to load</div>
            </template>
          </el-image>
          <div v-else class="image-fallback image-preview-fallback">请输入图片URL</div>
        </el-form-item>
        <el-form-item label="价格"><el-input-number v-model="form.price" :min="0" :precision="2" /></el-form-item>
        <el-form-item label="库存"><el-input-number v-model="form.stock" :min="0" /></el-form-item>
        <el-form-item label="描述"><el-input v-model="form.description" type="textarea" :rows="2" /></el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status" style="width:100%">
            <el-option :value="1" label="上架" />
            <el-option :value="0" label="下架" />
          </el-select>
        </el-form-item>
        <el-form-item label="推荐">
          <el-select v-model="form.isRecommend" style="width:100%">
            <el-option :value="1" label="是" />
            <el-option :value="0" label="否" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="visible = false">取消</el-button>
        <el-button type="primary" @click="submit">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  createAdminProduct,
  deleteAdminProduct,
  getAdminCategories,
  getAdminProducts,
  getAdminSubcategories,
  updateAdminProduct,
  updateAdminProductStatus
} from '../../api/admin'

const loading = ref(false)
const products = ref([])
const categories = ref([])
const subcategories = ref([])
const visible = ref(false)
const form = ref({ id: null, name: '', categoryId: 1, subcategoryId: 1, coverImage: '', price: 0, stock: 0, sales: 0, description: '', status: 1, isRecommend: 0 })

const subcategoriesByCategory = computed(() => {
  return subcategories.value.filter((item) => Number(item.categoryId) === Number(form.value.categoryId))
})

const getCategoryName = (id) => {
  const category = categories.value.find((item) => Number(item.id) === Number(id))
  return category ? category.name : id
}

const getSubcategoryName = (id) => {
  const subcategory = subcategories.value.find((item) => Number(item.id) === Number(id))
  return subcategory ? subcategory.name : id
}

const load = async () => {
  loading.value = true
  try {
    const [productList, categoryList, subcategoryList] = await Promise.all([
      getAdminProducts(),
      getAdminCategories(),
      getAdminSubcategories()
    ])
    products.value = productList
    categories.value = categoryList
    subcategories.value = subcategoryList
  } finally {
    loading.value = false
  }
}

const onCategoryChange = () => {
  const first = subcategoriesByCategory.value[0]
  if (!first || Number(first.id) === Number(form.value.subcategoryId)) {
    return
  }
  form.value.subcategoryId = Number(first.id)
}

const openCreate = () => {
  const defaultCategoryId = categories.value[0] ? Number(categories.value[0].id) : 1
  const firstSub = subcategories.value.find((item) => Number(item.categoryId) === defaultCategoryId)
  form.value = {
    id: null,
    name: '',
    categoryId: defaultCategoryId,
    subcategoryId: firstSub ? Number(firstSub.id) : 1,
    coverImage: '',
    price: 0,
    stock: 0,
    sales: 0,
    description: '',
    status: 1,
    isRecommend: 0
  }
  visible.value = true
}

const openEdit = (row) => {
  form.value = {
    id: row.id,
    name: row.name || '',
    categoryId: Number(row.categoryId || 1),
    subcategoryId: Number(row.subcategoryId || 1),
    coverImage: row.coverImage || '',
    price: Number(row.price || 0),
    stock: Number(row.stock || 0),
    sales: Number(row.sales || 0),
    description: row.description || '',
    status: Number(row.status || 0),
    isRecommend: Number(row.isRecommend || 0)
  }
  if (!subcategoriesByCategory.value.some((item) => Number(item.id) === Number(form.value.subcategoryId))) {
    onCategoryChange()
  }
  visible.value = true
}

const submit = async () => {
  const payload = {
    name: form.value.name,
    categoryId: form.value.categoryId,
    subcategoryId: form.value.subcategoryId,
    coverImage: form.value.coverImage,
    price: form.value.price,
    stock: form.value.stock,
    sales: Number(form.value.sales || 0),
    description: form.value.description,
    status: form.value.status,
    isRecommend: form.value.isRecommend
  }
  if (form.value.id) {
    await updateAdminProduct(form.value.id, payload)
  } else {
    await createAdminProduct(payload)
  }
  ElMessage.success('保存成功')
  visible.value = false
  await load()
}

const toggleStatus = async (row, checked) => {
  await updateAdminProductStatus(row.id, checked ? 1 : 0)
  ElMessage.success('状态已更新')
  await load()
}

const remove = async (id) => {
  await ElMessageBox.confirm('确认删除该商品吗？', '提示', { type: 'warning' })
  await deleteAdminProduct(id)
  ElMessage.success('删除成功')
  await load()
}

onMounted(load)
</script>

<style scoped>
.image-fallback {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
  font-size: 12px;
  color: #909399;
  background: #f5f7fa;
  text-align: center;
  padding: 4px;
  box-sizing: border-box;
}

.image-preview-fallback {
  width: 120px;
  height: 120px;
  border-radius: 6px;
}
</style>



