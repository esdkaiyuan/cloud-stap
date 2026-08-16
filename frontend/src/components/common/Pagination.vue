<template>
  <div v-if="totalPages > 1" class="pagination">
    <button
      class="page-btn"
      :disabled="isFirstPage"
      @click="$emit('change', currentPage - 1)"
    >
      <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
        <polyline points="15 18 9 12 15 6" />
      </svg>
    </button>

    <template v-for="page in visiblePages" :key="page">
      <button
        v-if="page !== '...'"
        class="page-btn"
        :class="{ active: page === currentPage }"
        @click="$emit('change', page)"
      >
        {{ page + 1 }}
      </button>
      <span v-else class="page-ellipsis">...</span>
    </template>

    <button
      class="page-btn"
      :disabled="isLastPage"
      @click="$emit('change', currentPage + 1)"
    >
      <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
        <polyline points="9 18 15 12 9 6" />
      </svg>
    </button>
  </div>
</template>

<script setup>
const props = defineProps({
  currentPage: { type: Number, required: true },
  totalPages: { type: Number, required: true }
})

defineEmits(['change'])

const isFirstPage = computed(() => props.currentPage <= 0)
const isLastPage = computed(() => props.currentPage >= props.totalPages - 1)

const visiblePages = computed(() => {
  const { currentPage, totalPages } = props
  if (totalPages <= 7) {
    return Array.from({ length: totalPages }, (_, i) => i)
  }

  const pages = []
  pages.push(0)

  if (currentPage > 2) pages.push('...')

  const start = Math.max(1, currentPage - 1)
  const end = Math.min(totalPages - 2, currentPage + 1)
  for (let i = start; i <= end; i++) {
    pages.push(i)
  }

  if (currentPage < totalPages - 3) pages.push('...')
  pages.push(totalPages - 1)

  return pages
})
</script>

<style scoped>
.pagination {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.25rem;
  padding: 1.5rem 0;
}
.page-btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 2rem;
  height: 2rem;
  padding: 0 0.4rem;
  font-family: var(--font-mono);
  font-size: 0.8rem;
  color: var(--color-text-secondary);
  background: transparent;
  border: 1px solid transparent;
  border-radius: var(--radius-sm);
  cursor: pointer;
  transition: all var(--transition-fast);
}
.page-btn:hover:not(:disabled):not(.active) {
  background: var(--color-bg-hover);
  border-color: var(--color-border);
}
.page-btn.active {
  background: var(--color-accent);
  color: #fff;
  border-color: var(--color-accent);
}
.page-btn:disabled {
  opacity: 0.3;
  cursor: not-allowed;
}
.page-btn .icon {
  width: 0.9em;
  height: 0.9em;
}
.page-ellipsis {
  padding: 0 0.25rem;
  color: var(--color-text-subtle);
  font-family: var(--font-mono);
  font-size: 0.85rem;
}
</style>
