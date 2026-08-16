<template>
  <div class="detail-page">
    <div class="detail-card">
      <div class="detail-header">
        <h2 class="detail-title">资源详情</h2>
        <router-link to="/" class="btn-close" title="返回">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <line x1="18" y1="6" x2="6" y2="18"/>
            <line x1="6" y1="6" x2="18" y2="18"/>
          </svg>
        </router-link>
      </div>
      <div v-if="resource" class="detail-body">
        <h3 class="resource-title">{{ resource.title }}</h3>
        <p class="resource-desc">{{ resource.description }}</p>
        <div class="detail-meta">
          <span v-if="resource.category" class="meta-tag">{{ resource.category }}</span>
          <span v-if="resource.sourceType" class="meta-tag">{{ sourceLabel(resource.sourceType) }}</span>
        </div>
        <div class="detail-stats">
          <span class="stat-item">
            <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/>
            </svg>
            {{ resource.views }} 次浏览
          </span>
          <span class="stat-item">
            <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"/><polyline points="7 10 12 15 17 10"/><line x1="12" y1="15" x2="12" y2="3"/>
            </svg>
            {{ resource.downloads }} 次下载
          </span>
        </div>
        <a :href="resource.url" target="_blank" rel="noopener noreferrer" class="btn btn-primary detail-link">
          <svg class="btn-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M18 13v6a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h6"/><polyline points="15 3 21 3 21 9"/><line x1="10" y1="14" x2="21" y2="3"/>
          </svg>
          访问资源链接
        </a>
      </div>
      <div v-else class="empty-body">
        <p>资源不存在或已下架</p>
        <router-link to="/" class="btn btn-secondary">返回列表</router-link>
      </div>
    </div>
  </div>
</template>

<script>
import { resourceApi } from '@/api/index.js'

const SOURCE_MAP = {
  baidu: '百度网盘', ali: '阿里云盘', tianyi: '天翼云盘', '123': '123云盘', other: '其他'
}

export default {
  name: 'ResourceDetailView',
  data() {
    return { resource: null }
  },
  computed: {
    sourceLabel() {
      return (type) => SOURCE_MAP[type] || type || '其他'
    }
  },
  async mounted() {
    try {
      this.resource = await resourceApi.getById(this.$route.params.id)
    } catch (e) {
      this.resource = null
    }
  }
}
</script>

<style scoped>
.detail-page {
  max-width: 640px;
  margin: 0 auto;
  padding-top: 1rem;
}
.detail-card {
  background: var(--color-bg-primary);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-lg);
  padding: 1.75rem;
  box-shadow: var(--shadow-md);
}
.detail-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 1.5rem;
}
.detail-title {
  font-size: 1.2rem;
  font-weight: 700;
  color: var(--color-text-primary);
}
.btn-close {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  border-radius: var(--radius-md);
  color: var(--color-text-muted);
  text-decoration: none;
  transition: all var(--transition-fast);
}
.btn-close:hover {
  background: var(--color-bg-hover);
  color: var(--color-text-primary);
}
.btn-close svg { width: 18px; height: 18px; }
.resource-title {
  font-size: 1.1rem;
  font-weight: 600;
  color: var(--color-text-primary);
  margin-bottom: 0.75rem;
}
.resource-desc {
  font-size: 0.9rem;
  color: var(--color-text-muted);
  line-height: 1.6;
  margin-bottom: 1rem;
}
.detail-meta {
  display: flex;
  gap: 8px;
  margin-bottom: 1rem;
}
.meta-tag {
  font-family: var(--font-mono);
  font-size: 0.75rem;
  padding: 2px 8px;
  border-radius: var(--radius-sm);
  background: var(--color-bg-secondary);
  color: var(--color-text-muted);
}
.detail-stats {
  display: flex;
  gap: 1.25rem;
  margin-bottom: 1.5rem;
  padding-bottom: 1rem;
  border-bottom: 1px solid var(--color-border-light);
}
.stat-item {
  display: inline-flex;
  align-items: center;
  gap: 0.35rem;
  font-family: var(--font-mono);
  font-size: 0.82rem;
  color: var(--color-text-muted);
}
.stat-item .icon {
  width: 0.9em;
  height: 0.9em;
}
.detail-link {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  text-decoration: none;
}
.btn-icon {
  width: 16px;
  height: 16px;
}
.empty-body {
  text-align: center;
  padding: 2rem 0;
  color: var(--color-text-muted);
}
.empty-body p {
  margin-bottom: 1rem;
}
</style>
