<template>
  <div class="page-wrap">
    <div class="section-title">评论管理</div>
    <div class="card" style="padding:16px;">
      <el-table :data="comments" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="productId" label="商品ID" width="100" />
        <el-table-column prop="userId" label="用户ID" width="100" />
        <el-table-column prop="score" label="评分" width="90" />
        <el-table-column prop="content" label="评论内容" min-width="280" show-overflow-tooltip />
        <el-table-column prop="createdAt" label="评论时间" min-width="170" />
        <el-table-column label="状态" width="120">
          <template #default="{ row }">
            <el-switch :model-value="Number(row.status)===1" @change="(v)=>toggleStatus(row, v)" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100">
          <template #default="{ row }">
            <el-button link type="danger" @click="remove(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { deleteAdminComment, getAdminComments, updateAdminCommentStatus } from '../../api/admin'

const loading = ref(false)
const comments = ref([])

const load = async () => {
  loading.value = true
  try {
    comments.value = await getAdminComments()
  } finally {
    loading.value = false
  }
}

const toggleStatus = async (row, checked) => {
  await updateAdminCommentStatus(row.id, checked ? 1 : 0)
  ElMessage.success('状态已更新')
  await load()
}

const remove = async (id) => {
  await ElMessageBox.confirm('确认删除该评论吗？', '提示', { type: 'warning' })
  await deleteAdminComment(id)
  ElMessage.success('删除成功')
  await load()
}

onMounted(load)
</script>
