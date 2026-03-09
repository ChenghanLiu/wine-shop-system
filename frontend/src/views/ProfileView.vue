<template>
  <div class="page-wrap">
    <div class="section-title">个人中心</div>
    <div class="card" style="padding:20px;max-width:720px;">
      <el-form :model="form" label-width="90px">
        <el-form-item label="用户ID"><span>{{ form.id }}</span></el-form-item>
        <el-form-item label="用户名"><span>{{ form.username }}</span></el-form-item>
        <el-form-item label="角色"><span>{{ form.role }}</span></el-form-item>
        <el-form-item label="昵称"><el-input v-model="form.nickname" /></el-form-item>
        <el-form-item label="手机号"><el-input v-model="form.phone" /></el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="saving" @click="save">保存</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { me, updateMe } from '../api/auth'

const saving = ref(false)
const form = ref({ id: '-', username: '-', role: '-', nickname: '', phone: '' })

const load = async () => {
  const data = await me()
  form.value = {
    id: data?.id ?? '-',
    username: data?.username ?? '-',
    role: data?.role ?? '-',
    nickname: data?.nickname || '',
    phone: data?.phone || ''
  }
}

const save = async () => {
  saving.value = true
  try {
    await updateMe({ nickname: form.value.nickname, phone: form.value.phone })
    ElMessage.success('保存成功')
    await load()
  } finally {
    saving.value = false
  }
}

onMounted(load)
</script>
