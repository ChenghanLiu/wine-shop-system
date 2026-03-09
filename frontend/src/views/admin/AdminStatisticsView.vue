<template>
  <div class="page-wrap" v-loading="loading">
    <div class="section-title">统计概览</div>

    <div class="summary-grid">
      <div class="summary-card">
        <div class="label">累计用户</div>
        <div class="value">{{ Number(overview.userCount || 0) }}</div>
      </div>
      <div class="summary-card">
        <div class="label">累计商品</div>
        <div class="value">{{ Number(overview.productCount || 0) }}</div>
      </div>
      <div class="summary-card">
        <div class="label">累计订单</div>
        <div class="value">{{ Number(overview.orderCount || 0) }}</div>
      </div>
      <div class="summary-card">
        <div class="label">累计销售额</div>
        <div class="value">¥{{ Number(overview.paidAmount || 0).toFixed(2) }}</div>
      </div>
    </div>

    <div class="card chart-card">
      <div class="sub-title">热销商品 TOP10</div>
      <div ref="topProductsChartRef" class="chart-box"></div>
    </div>

    <div class="card chart-card">
      <div class="sub-title">分类销售额占比</div>
      <div ref="categoryRatioChartRef" class="chart-box"></div>
    </div>
  </div>
</template>

<script setup>
import * as echarts from 'echarts'
import { nextTick, onBeforeUnmount, onMounted, ref } from 'vue'
import { getAdminCategoryRatio, getAdminOverview, getAdminTopProducts } from '../../api/admin'

const loading = ref(false)
const overview = ref({})
const topProducts = ref([])
const categoryRatio = ref([])
const topProductsChartRef = ref(null)
const categoryRatioChartRef = ref(null)
let topProductsChart
let categoryRatioChart

const renderCharts = () => {
  if (topProductsChartRef.value) {
    topProductsChart = topProductsChart || echarts.init(topProductsChartRef.value)
    topProductsChart.setOption({
      tooltip: { trigger: 'axis' },
      grid: { left: 40, right: 16, top: 24, bottom: 48 },
      xAxis: {
        type: 'category',
        data: topProducts.value.map((item) => item.productName),
        axisLabel: { rotate: 20 }
      },
      yAxis: { type: 'value' },
      series: [{ type: 'bar', data: topProducts.value.map((item) => Number(item.sales || 0)), itemStyle: { color: '#7b1325' } }]
    })
  }

  if (categoryRatioChartRef.value) {
    categoryRatioChart = categoryRatioChart || echarts.init(categoryRatioChartRef.value)
    categoryRatioChart.setOption({
      tooltip: { trigger: 'item' },
      legend: { bottom: 0 },
      series: [
        {
          type: 'pie',
          radius: ['35%', '65%'],
          data: categoryRatio.value.map((item) => ({ name: item.categoryName, value: Number(item.amount || 0) }))
        }
      ]
    })
  }
}

const load = async () => {
  loading.value = true
  try {
    const [o, t, c] = await Promise.all([getAdminOverview(), getAdminTopProducts(), getAdminCategoryRatio()])
    overview.value = o || {}
    topProducts.value = Array.isArray(t) ? t : []
    categoryRatio.value = Array.isArray(c) ? c : []
    await nextTick()
    renderCharts()
  } finally {
    loading.value = false
  }
}

const handleResize = () => {
  topProductsChart?.resize()
  categoryRatioChart?.resize()
}

onMounted(async () => {
  await load()
  window.addEventListener('resize', handleResize)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)
  topProductsChart?.dispose()
  categoryRatioChart?.dispose()
})
</script>

<style scoped>
.summary-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 12px;
  margin-bottom: 12px;
}

.summary-card {
  background: #fff;
  border: 1px solid var(--line-color);
  border-radius: 8px;
  padding: 14px;
}

.label {
  color: var(--text-secondary);
  font-size: 13px;
}

.value {
  margin-top: 6px;
  font-size: 22px;
  font-weight: 600;
  color: var(--wine-red);
}

.chart-card {
  margin-bottom: 12px;
  padding: 16px;
}

.sub-title {
  margin-bottom: 8px;
  font-weight: 600;
}

.chart-box {
  height: 320px;
}
</style>
