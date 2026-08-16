<template>
  <div class="star-rating">
    <span
      v-for="star in 5"
      :key="star"
      class="star"
      :class="{ active: star <= displayRating }"
    >
      <svg viewBox="0 0 24 24" fill="currentColor" stroke="currentColor" stroke-width="1">
        <polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2" />
      </svg>
    </span>
    <span v-if="showValue" class="rating-value">{{ ratingText }}</span>
  </div>
</template>

<script setup>
const props = defineProps({
  rating: { type: Number, default: 0 },
  max: { type: Number, default: 5 },
  showValue: { type: Boolean, default: false }
})

const displayRating = computed(() => {
  return Math.round(props.rating * 2) / 2
})

const ratingText = computed(() => {
  return props.rating > 0 ? ` ${props.rating.toFixed(1)}` : ' 暂无评分'
})
</script>

<style scoped>
.star-rating {
  display: inline-flex;
  align-items: center;
  gap: 0.2rem;
}
.star {
  display: inline-flex;
  color: var(--color-border);
  transition: color var(--transition-fast);
}
.star svg {
  width: 0.95em;
  height: 0.95em;
}
.star.active {
  color: #f59e0b;
}
.rating-value {
  font-family: var(--font-mono);
  font-size: 0.82rem;
  color: var(--color-text-muted);
  margin-left: 0.15rem;
}
</style>
