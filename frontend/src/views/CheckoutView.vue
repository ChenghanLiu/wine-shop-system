<template>
  <div class="page-wrap">
    <div class="section-title">提交订单</div>
    <div class="card" style="padding:20px;">
      <div style="display:flex;justify-content:space-between;align-items:center;gap:12px;margin-bottom:10px;">
        <div style="font-weight:600;">已选商品</div>
        <el-button size="small" @click="loadCart">刷新</el-button>
      </div>
      <el-empty v-if="!selectedItems.length" description="没有可结算商品，请先在购物车勾选商品" />
      <el-table v-else :data="selectedItems" size="small">
        <el-table-column label="商品" min-width="240">
          <template #default="{ row }">{{ detailMap[row.productId]?.name || `商品#${row.productId}` }}</template>
        </el-table-column>
        <el-table-column label="单价" width="120">
          <template #default="{ row }">¥{{ Number(detailMap[row.productId]?.price || 0).toFixed(2) }}</template>
        </el-table-column>
        <el-table-column prop="quantity" label="数量" width="100" />
        <el-table-column label="小计" width="120">
          <template #default="{ row }">¥{{ Number((detailMap[row.productId]?.price || 0) * (row.quantity || 0)).toFixed(2) }}</template>
        </el-table-column>
      </el-table>
      <div class="sum-line" v-if="selectedItems.length">订单合计：<b>¥{{ selectedTotal.toFixed(2) }}</b></div>

      <div style="display:flex;justify-content:space-between;align-items:center;gap:12px;margin:18px 0 10px;">
        <div style="font-weight:600;">选择收货地址</div>
        <el-button size="small" @click="dialogVisible = true">新增地址</el-button>
      </div>
      <el-empty v-if="!addresses.length" description="暂无地址，请先新增收货地址" />
      <el-radio-group v-else v-model="addressId" style="display:flex;flex-direction:column;gap:10px;">
        <el-radio v-for="a in addresses" :key="a.id" :label="a.id">
          {{ a.receiverName }} {{ a.receiverPhone }} ｜ {{ a.province }}{{ a.city }}{{ a.district }}{{ a.detailAddress }}
        </el-radio>
      </el-radio-group>

      <el-input v-model="remark" type="textarea" :rows="3" placeholder="订单备注(可选)" style="margin-top:18px;" />
      <div style="margin-top:16px;">
        <el-button type="primary" :disabled="!selectedItems.length" @click="submit">提交订单</el-button>
      </div>
    </div>

    <el-dialog v-model="dialogVisible" title="新增地址" width="520px">
      <el-form :model="form" label-width="84px">
        <el-form-item label="收货人"><el-input v-model="form.receiverName" /></el-form-item>
        <el-form-item label="手机号"><el-input v-model="form.receiverPhone" /></el-form-item>
        <el-form-item label="省市区">
          <el-input v-model="form.province" placeholder="省" style="width:32%;margin-right:2%;" />
          <el-input v-model="form.city" placeholder="市" style="width:32%;margin-right:2%;" />
          <el-input v-model="form.district" placeholder="区" style="width:32%;" />
        </el-form-item>
        <el-form-item label="详细地址"><el-input v-model="form.detailAddress" /></el-form-item>
        <el-form-item label="默认地址"><el-switch v-model="form.isDefault" :active-value="1" :inactive-value="0" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAddress">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getAddresses, createAddress } from '../api/address'
import { checkout } from '../api/order'
import { getCart } from '../api/cart'
import { getProductDetail } from '../api/product'

const router = useRouter()
const addresses = ref([])
const addressId = ref()
const remark = ref('')
const cartItems = ref([])
const detailMap = ref({})
const dialogVisible = ref(false)
const form = ref({ receiverName: '', receiverPhone: '', province: '', city: '', district: '', detailAddress: '', isDefault: 0 })

const normalizeList = (value) => {
  if (Array.isArray(value)) return value
  if (Array.isArray(value?.list)) return value.list
  if (Array.isArray(value?.records)) return value.records
  return []
}

const selectedItems = computed(() => cartItems.value.filter((i) => Number(i.selected) === 1))
const selectedTotal = computed(() => selectedItems.value.reduce((sum, i) => sum + Number(detailMap.value[i.productId]?.price || 0) * Number(i.quantity || 0), 0))

const loadCart = async () => {
  const cart = await getCart()
  cartItems.value = normalizeList(cart)
  const ids = [...new Set(selectedItems.value.map((i) => i.productId))]
  const pairs = await Promise.all(ids.map(async (id) => {
    try {
      return [id, await getProductDetail(id)]
    } catch {
      return [id, null]
    }
  }))
  detailMap.value = Object.fromEntries(pairs)
}

const loadAddresses = async () => {
  const res = await getAddresses()
  addresses.value = normalizeList(res)
  const def = addresses.value.find((a) => Number(a.isDefault) === 1) || addresses.value[0]
  addressId.value = def?.id
}

const submitAddress = async () => {
  const required = ['receiverName', 'receiverPhone', 'province', 'city', 'district', 'detailAddress']
  if (required.some((k) => !String(form.value[k] || '').trim())) {
    ElMessage.warning('请完整填写地址信息')
    return
  }
  await createAddress(form.value)
  ElMessage.success('地址已保存')
  dialogVisible.value = false
  form.value = { receiverName: '', receiverPhone: '', province: '', city: '', district: '', detailAddress: '', isDefault: 0 }
  await loadAddresses()
}

const submit = async () => {
  if (!selectedItems.value.length) {
    ElMessage.warning('请先在购物车勾选商品')
    return
  }
  if (!addressId.value) {
    ElMessage.warning('请选择地址')
    return
  }
  await checkout({ addressId: addressId.value, remark: remark.value || undefined })
  ElMessage.success('下单成功')
  router.push('/orders')
}

onMounted(async () => {
  await Promise.all([loadAddresses(), loadCart()])
})
</script>

<style scoped>
.sum-line {
  text-align: right;
  margin-top: 10px;
  color: var(--wine-red);
}
</style>
