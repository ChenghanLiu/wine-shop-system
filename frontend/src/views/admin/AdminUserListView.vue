<template>
  <div class="page-wrap">
    <div class="section-title">用户管理</div>
    <div class="card" style="padding:16px;">
      <el-table :data="users" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" min-width="160" />
        <el-table-column prop="nickname" label="昵称" min-width="140" />
        <el-table-column prop="phone" label="手机号" min-width="160" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">{{ Number(row.status) === 1 ? '正常' : '禁用' }}</template>
        </el-table-column>
        <el-table-column label="操作" width="160">
          <template #default="{ row }">
            <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="remove(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="visible" title="编辑用户" width="460px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="昵称"><el-input v-model="form.nickname" /></el-form-item>
        <el-form-item label="手机号"><el-input v-model="form.phone" /></el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status" style="width:100%">
            <el-option :value="1" label="正常" />
            <el-option :value="0" label="禁用" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="visible = false">取消</el-button>
        <el-button type="primary" @click="submitEdit">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { deleteAdminUser, getAdminUsers, updateAdminUser } from '../../api/admin'

const loading = ref(false)
const users = ref([])
const visible = ref(false)
const form = ref({ id: null, nickname: '', phone: '', status: 1 })

const load = async () => {
  loading.value = true
  try {
    users.value = await getAdminUsers()
  } finally {
    loading.value = false
  }
}

const openEdit = (row) => {
  form.value = { id: row.id, nickname: row.nickname || '', phone: row.phone || '', status: Number(row.status || 0) }
  visible.value = true
}

const submitEdit = async () => {
  await updateAdminUser(form.value.id, {
    nickname: form.value.nickname,
    phone: form.value.phone,
    status: form.value.status
  })
  ElMessage.success('保存成功')
  visible.value = false
  await load()
}

const remove = async (id) => {
  await ElMessageBox.confirm('确认删除该用户吗？', '提示', { type: 'warning' })
  await deleteAdminUser(id)
  ElMessage.success('删除成功')
  await load()
}

onMounted(load)
</script>
