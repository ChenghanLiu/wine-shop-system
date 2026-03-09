<template>
  <div class="page-wrap">
    <div class="section-title">修改管理员密码</div>
    <div class="card" style="padding:16px;max-width:560px;">
      <el-form :model="form" label-width="100px">
        <el-form-item label="旧密码">
          <el-input v-model="form.oldPassword" type="password" show-password />
        </el-form-item>
        <el-form-item label="新密码">
          <el-input v-model="form.newPassword" type="password" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="submit">保存</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { changeAdminPassword } from '../../api/auth'

const loading = ref(false)
const form = ref({ oldPassword: '', newPassword: '' })

const submit = async () => {
  if (!form.value.oldPassword || !form.value.newPassword) {
    ElMessage.warning('请填写完整密码信息')
    return
  }
  loading.value = true
  try {
    await changeAdminPassword(form.value)
    ElMessage.success('密码修改成功，请重新登录')
    form.value = { oldPassword: '', newPassword: '' }
  } finally {
    loading.value = false
  }
}
</script>
