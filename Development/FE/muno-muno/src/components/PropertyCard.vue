<!-- PropertyCard.vue -->
<template>
  <div
    class="property-card"
    :class="{ 'negative-background': shouldShowNegativeBackground }"
  >
    <h3 class="title">{{ title }}</h3>
    <p v-if="type !== 'ai'" class="description">
      <template
        v-if="type === 'asset' || type === 'expense' || type === 'investment'"
      >
        <span :class="getValueClass()"> {{ sign }}{{ formattedDiff }}원 </span
        ><br />
        {{ increaseOrDecrease }}했어요
      </template>
    </p>
    <p v-else class="description">고객님 맞춤 상품을<br />추천드려요</p>
  </div>
</template>

<script>
export default {
  name: 'PropertyCard',
  props: {
    title: String,
    amount: {
      type: Number,
      default: 0,
    },
    previousAmount: {
      type: Number,
      default: 0,
    },
    type: String,
  },
  computed: {
    diff() {
      return this.amount - this.previousAmount;
    },
    absDiff() {
      return Math.abs(this.diff);
    },
    formattedDiff() {
      return this.formatNumber(this.absDiff);
    },
    increaseOrDecrease() {
      return this.diff > 0 ? '증가' : '감소';
    },
    sign() {
      return this.diff > 0 ? '+' : '-';
    },
    shouldShowNegativeBackground() {
      if (this.type === 'expense') {
        return this.diff > 0; // 소비가 증가했을 때 빨간 배경
      }
      return this.diff < 0; // 다른 경우는 기존 로직 유지
    },
  },
  methods: {
    formatNumber(value) {
      return value.toLocaleString();
    },
    getValueClass() {
      if (this.type === 'expense') {
        return this.diff > 0 ? 'negative' : 'positive';
      }
      return this.diff > 0 ? 'positive' : 'negative';
    },
  },
};
</script>

<style scoped>
.property-card {
  width: 162px;
  height: 141px;
  flex-shrink: 0;
  border-radius: 10px;
  background: var(--p80);
  padding: 15px;
  box-sizing: border-box;
  transition: background-color 0.3s ease;
}

.negative-background {
  background-color: #fff0f0;
}

.title {
  color: var(--gr20);
  font-family: Pretendard;
  font-size: 16px;
  font-style: normal;
  font-weight: 600;
  line-height: 135%;
  margin: 0 0 4px 0;
}

.description {
  color: var(--gr30);
  font-family: Pretendard;
  font-size: 14px;
  font-style: normal;
  font-weight: 400;
  line-height: 135%;
  margin: 0;
}

.positive {
  color: var(--p10);
  font-weight: 600;
}

.negative {
  color: #ff3e41;
  font-weight: 600;
}
</style>
