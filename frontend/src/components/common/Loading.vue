<template>
  <div v-if="loading" class="loading-container">
    <div class="spinner" />
    <p class="loading-text">加载中...</p>
  </div>
</template>

<script setup>
defineProps({ loading: Boolean })
</script>

<style scoped>
.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 3rem;
  gap: 0.75rem;
}
.spinner {
  width: 24px;
  height: 24px;
  border-width: 2.5px;
  border-color: var(--color-border);
  border-top-color: var(--color-accent);
}
.loading-text {
  font-family: var(--font-mono);
  font-size: 0.82rem;
  color: var(--color-text-muted);
}
</style>
