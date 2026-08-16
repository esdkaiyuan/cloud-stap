<template>
  <div class="search-bar">
    <svg class="search-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
      <circle cx="11" cy="11" r="8" />
      <line x1="21" y1="21" x2="16.65" y2="16.65" />
    </svg>
    <input
      v-model="modelValue"
      type="text"
      :placeholder="placeholder"
      class="search-input"
      @keydown.enter="onSearch"
    />
    <button v-if="modelValue" class="search-clear btn-ghost" @click="modelValue = ''; $emit('update:modelValue', '')">
      <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
        <line x1="18" y1="6" x2="6" y2="18" />
        <line x1="6" y1="6" x2="18" y2="18" />
      </svg>
    </button>
  </div>
</template>

<script setup>
defineProps({
  modelValue: { type: String, default: '' },
  placeholder: { type: String, default: '搜索资源标题或描述...' }
})
defineEmits(['update:modelValue', 'search'])
function onSearch() {
  defineEmits(['search'])
}
</script>

<style scoped>
.search-bar {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  background: var(--color-bg-secondary);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  padding: 0 0.85rem;
  transition: border-color var(--transition-fast), box-shadow var(--transition-fast);
  width: 100%;
  max-width: 420px;
}
.search-bar:focus-within {
  border-color: var(--color-accent);
  box-shadow: 0 0 0 3px rgba(13, 148, 136, 0.08);
}
.search-icon {
  width: 1em;
  height: 1em;
  color: var(--color-text-subtle);
  flex-shrink: 0;
}
.search-input {
  border: none;
  background: transparent;
  padding: 0.65rem 0;
  font-size: 0.9rem;
  box-shadow: none;
}
.search-input:focus {
  border: none;
  box-shadow: none;
}
.search-input::placeholder {
  color: var(--color-text-subtle);
}
.search-clear {
  padding: 0.2rem;
  flex-shrink: 0;
}
</style>
