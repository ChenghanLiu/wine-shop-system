<template>
  <div class="page-wrap">
    <div class="section-title">订单管理</div>
    <div class="card" style="padding:16px;">
      <el-table :data="orders" v-loading="loading">
        <el-table-column prop="orderNo" label="订单号" min-width="180" />
        <el-table-column prop="payAmount" label="金额" width="120">
          <template #default="{ row }">¥{{ Number(row.payAmount || 0).toFixed(2) }}</template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" />
        <el-table-column label="操作" width="180">
          <template #default="{ row }">
            <el-button v-if="row.status===1" link type="primary" @click="deliver(row.id)">发货</el-button>
            <el-button v-if="row.status===5" link type="danger" @click="refund(row.id)">退款</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { deliverAdminOrder, getAdminOrders, refundAdminOrder } from '../../api/admin'

const loading = ref(false)
const orders = ref([])

const load = async () => {
  loading.value = true
  try {
    orders.value = await getAdminOrders()
  } finally {
    loading.value = false
  }
}

const deliver = async (id) => {
  await deliverAdminOrder(id)
  ElMessage.success('已发货')
  await load()
}

const refund = async (id) => {
  await refundAdminOrder(id)
  ElMessage.success('已退款')
  await load()
}

onMounted(load)
</script>
