<template>
  <div class="page-wrap">
    <section class="hero card">
      <el-carousel height="280px" indicator-position="outside">
        <el-carousel-item v-for="item in slides" :key="item.title">
          <div class="slide" :style="{ backgroundImage: `url(${item.image})` }">
            <div class="overlay">
              <h1>{{ item.title }}</h1>
              <p>{{ item.desc }}</p>
              <el-button type="primary" @click="$router.push('/products')">立即选购</el-button>
            </div>
          </div>
        </el-carousel-item>
      </el-carousel>
    </section>

    <section style="margin-top: 28px;">
      <div class="section-title">推荐商品</div>
      <el-row :gutter="18">
        <el-col v-for="item in recommendList" :key="item.id" :xs="24" :sm="12" :md="8" :lg="6">
          <ProductCard :product="item" />
        </el-col>
      </el-row>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getRecommendProducts } from '../api/product'
import ProductCard from '../components/ProductCard.vue'

const recommendList = ref([])
const slides = [
  { title: '醇香佳酿，尽在 Wine Shop', desc: '精选白酒、啤酒、红酒与洋酒', image: 'https://images.unsplash.com/photo-1516594915697-87eb3b1c14ea?auto=format&fit=crop&w=1400&q=80' },
  { title: '节日礼盒推荐', desc: '高颜值礼盒，送礼更体面', image: 'https://images.unsplash.com/photo-1470337458703-46ad1756a187?auto=format&fit=crop&w=1400&q=80' },
  { title: '新人专享专区', desc: '优选口粮酒，价格更友好', image: 'https://images.unsplash.com/photo-1504674900247-0877df9cc836?auto=format&fit=crop&w=1400&q=80' }
]

onMounted(async () => {
  recommendList.value = await getRecommendProducts()
})
</script>

<style scoped>
.hero {
  padding: 16px;
}
.slide {
  height: 280px;
  border-radius: 12px;
  background-size: cover;
  background-position: center;
  overflow: hidden;
}
.overlay {
  height: 100%;
  padding: 34px;
  background: linear-gradient(90deg, rgba(0, 0, 0, 0.45), rgba(0, 0, 0, 0.08));
}
h1 {
  margin: 0;
  font-size: 34px;
  color: #fff;
}
p {
  margin: 12px 0 18px;
  color: rgba(255,255,255,0.92);
}
</style>
