<template>
  <div class="page-wrap">
    <div class="section-title">我的订单</div>
    <div class="card" style="padding:16px;">
      <div style="margin-bottom:12px;">
        <el-select v-model="status" clearable placeholder="按状态筛选" style="width: 180px" @change="load">
          <el-option label="待支付" :value="0" />
          <el-option label="待发货" :value="1" />
          <el-option label="待收货" :value="2" />
          <el-option label="已完成" :value="3" />
          <el-option label="已取消" :value="4" />
        </el-select>
      </div>
      <el-table :data="orders" v-loading="loading">
        <el-table-column prop="orderNo" label="订单号" min-width="180" />
        <el-table-column prop="payAmount" label="金额" width="120">
          <template #default="{ row }">¥{{ Number(row.payAmount || 0).toFixed(2) }}</template>
        </el-table-column>
        <el-table-column label="状态" width="120">
          <template #default="{ row }"><el-tag :type="tagType(row.status)">{{ statusText(row.status) }}</el-tag></template>
        </el-table-column>
        <el-table-column label="操作" width="280">
          <template #default="{ row }">
            <el-button v-if="row.status===0" link type="primary" @click="pay(row.id)">支付</el-button>
            <el-button v-if="row.status===0" link type="danger" @click="cancel(row.id)">取消</el-button>
            <el-button v-if="row.status===2" link type="primary" @click="confirm(row.id)">确认收货</el-button>
            <el-button v-if="row.status===3" link type="primary" @click="openComment(row.id)">去评价</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="commentVisible" title="订单评价" width="640px">
      <el-empty v-if="!commentItems.length" description="暂无可评价商品" />
      <div v-for="item in commentItems" :key="item.id" class="comment-item">
        <div class="title">{{ item.productName }}</div>
        <el-rate v-model="commentForm[item.id].score" />
        <el-input v-model="commentForm[item.id].content" type="textarea" :rows="2" placeholder="请输入评价内容" />
        <div class="mt8">
          <el-button type="primary" size="small" @click="submitComment(item.id)">提交评价</el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getMyOrders, payOrder, cancelOrder, confirmOrder, getOrderDetail } from '../api/order'
import { createComment } from '../api/comment'

const status = ref()
const loading = ref(false)
const orders = ref([])
const commentVisible = ref(false)
const commentItems = ref([])
const commentForm = ref({})

const statusText = (v) => ({ 0: '待支付', 1: '待发货', 2: '待收货', 3: '已完成', 4: '已取消', 5: '已退款' }[v] || '-')
const tagType = (v) => ({ 0: 'warning', 1: 'info', 2: 'primary', 3: 'success', 4: 'danger', 5: 'danger' }[v] || 'info')

const load = async () => {
  loading.value = true
  try {
    orders.value = await getMyOrders({ status: status.value })
  } finally {
    loading.value = false
  }
}

const pay = async (id) => { await payOrder(id); ElMessage.success('支付成功'); await load() }
const cancel = async (id) => { await cancelOrder(id); ElMessage.success('已取消'); await load() }
const confirm = async (id) => { await confirmOrder(id); ElMessage.success('已确认收货'); await load() }

const openComment = async (orderId) => {
  const detail = await getOrderDetail(orderId)
  commentItems.value = detail?.items || []
  commentForm.value = Object.fromEntries(commentItems.value.map((i) => [i.id, { score: 5, content: '' }]))
  commentVisible.value = true
}

const submitComment = async (orderItemId) => {
  const data = commentForm.value[orderItemId]
  if (!data?.content?.trim()) {
    ElMessage.warning('请输入评价内容')
    return
  }
  await createComment({ orderItemId, score: data.score, content: data.content })
  ElMessage.success('评价成功')
  commentItems.value = commentItems.value.filter((i) => i.id !== orderItemId)
}

onMounted(load)
</script>

<style scoped>
.comment-item {
  border: 1px solid #f0f0f0;
  border-radius: 8px;
  padding: 12px;
  margin-bottom: 10px;
}
.title {
  font-weight: 600;
  margin-bottom: 8px;
}
.mt8 {
  margin-top: 8px;
}
</style>
