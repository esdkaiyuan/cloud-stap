<template>
  <router-link :to="`/resource/${resource.id}`" class="resource-card" custom v-slot="{ navigate }">
    <div @click="navigate" class="card-inner">
      <div class="card-header">
        <div class="card-title-group">
          <h3 class="card-title">{{ resource.title }}</h3>
          <span v-if="resource.category" class="card-category">{{ resource.category }}</span>
        </div>
        <div class="card-source" :class="sourceClass">
          {{ sourceLabel }}
        </div>
      </div>

      <p class="card-description">{{ resource.description }}</p>

      <div class="card-footer">
        <div class="card-stats">
          <span class="stat">
            <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z" />
              <circle cx="12" cy="12" r="3" />
            </svg>
            {{ resource.views }}
          </span>
          <span class="stat">
            <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4" />
              <polyline points="7 10 12 15 17 10" />
              <line x1="12" y1="15" x2="12" y2="3" />
            </svg>
            {{ resource.downloads }}
          </span>
        </div>

        <div class="card-rating">
          <StarRating :rating="resource.avgRating || 0" :show-value="true" />
        </div>
      </div>

      <div class="card-url">
        <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <path d="M10 13a5 5 0 0 0 7.54.54l3-3a5 5 0 0 0-7.07-7.07l-1.72 1.71" />
          <path d="M14 11a5 5 0 0 0-7.54-.54l-3 3a5 5 0 0 0 7.07 7.07l1.71-1.71" />
        </svg>
        <code class="url-text">{{ truncateUrl(resource.url) }}</code>
      </div>
    </div>
  </router-link>
</template>

<script setup>
import StarRating from '@/components/common/StarRating.vue'

const props = defineProps({
  resource: { type: Object, required: true }
})

const sourceMap = {
  baidu: { label: '百度网盘', class: 'source-baidu' },
  ali: { label: '阿里云盘', class: 'source-ali' },
  tianyi: { label: '天翼云盘', class: 'source-tianyi' },
  '123': { label: '123云盘', class: 'source-123' },
  xunlei: { label: '迅雷网盘', class: 'source-xunlei' },
  quark: { label: '夸克网盘', class: 'source-quark' },
  uc: { label: 'UC网盘', class: 'source-uc' },
  '115': { label: '115网盘', class: 'source-115' },
  other: { label: '其他', class: 'source-other' }
}

const sourceInfo = computed(() => {
  return sourceMap[props.resource.sourceType] || sourceMap.other
})

const sourceLabel = computed(() => sourceInfo.value.label)
const sourceClass = computed(() => sourceInfo.value.class)

function truncateUrl(url) {
  if (url.length > 55) {
    return url.substring(0, 40) + '...' + url.substring(url.length - 15)
  }
  return url
}
</script>

<style scoped>
.resource-card {
  display: block;
  text-decoration: none;
  color: inherit;
}
.card-inner {
  border: 1px solid var(--color-border-light);
  border-radius: var(--radius-lg);
  padding: 1.15rem 1.25rem;
  background: var(--color-bg-primary);
  transition: border-color var(--transition-fast), box-shadow var(--transition-fast);
  cursor: pointer;
}
.resource-card:hover .card-inner {
  border-color: var(--color-accent);
  box-shadow: 0 0 0 1px rgba(13, 148, 136, 0.08), var(--shadow-md);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 0.75rem;
  margin-bottom: 0.6rem;
}
.card-title-group {
  flex: 1;
  min-width: 0;
}
.card-title {
  font-size: 0.95rem;
  font-weight: 600;
  color: var(--color-text-primary);
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
.card-category {
  display: inline-block;
  font-family: var(--font-mono);
  font-size: 0.7rem;
  font-weight: 500;
  color: var(--color-accent);
  background: var(--color-accent-light);
  padding: 0.15rem 0.5rem;
  border-radius: 3px;
  margin-top: 0.4rem;
}

.card-source {
  font-family: var(--font-mono);
  font-size: 0.68rem;
  font-weight: 500;
  padding: 0.2rem 0.5rem;
  border-radius: 3px;
  flex-shrink: 0;
  white-space: nowrap;
}
.source-baidu { color: #2563eb; background: #eff6ff; }
.source-ali { color: #0d9488; background: #f0fdfa; }
.source-tianyi { color: #d97706; background: #fffbeb; }
.source-123 { color: #7c3aed; background: #f5f3ff; }
.source-xunlei { color: #e53935; background: #fef2f2; }
.source-quark { color: #6d28d9; background: #f5f3ff; }
.source-uc { color: #ea580c; background: #fff7ed; }
.source-115 { color: #0891b2; background: #ecfeff; }
.source-other { color: #6c757d; background: #f8f9fa; }

.card-description {
  font-size: 0.85rem;
  color: var(--color-text-muted);
  line-height: 1.55;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  margin-bottom: 0.75rem;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.6rem;
}
.card-stats {
  display: flex;
  gap: 0.85rem;
}
.stat {
  display: inline-flex;
  align-items: center;
  gap: 0.3rem;
  font-family: var(--font-mono);
  font-size: 0.75rem;
  color: var(--color-text-muted);
}
.stat .icon {
  width: 0.9em;
  height: 0.9em;
}
.card-rating {
  display: flex;
  align-items: center;
}

.card-url {
  display: flex;
  align-items: center;
  gap: 0.4rem;
  padding: 0.35rem 0.55rem;
  background: var(--color-bg-secondary);
  border-radius: var(--radius-sm);
  border: 1px solid var(--color-border-light);
}
.card-url .icon {
  width: 0.8em;
  height: 0.8em;
  color: var(--color-text-subtle);
  flex-shrink: 0;
}
.url-text {
  font-family: var(--font-mono);
  font-size: 0.72rem;
  color: var(--color-text-muted);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  background: transparent;
  padding: 0;
}
</style>
