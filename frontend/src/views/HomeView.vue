<template>
  <div class="home">
    <!-- Hero Section -->
    <section class="hero">
      <h1 class="hero-title">网盘资源共享平台</h1>
      <p class="hero-desc">简洁、快速地分享网盘资源链接。发布资源，一键获取。</p>
      <router-link to="/publish" class="btn btn-primary hero-btn">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <line x1="12" y1="5" x2="12" y2="19"/>
          <line x1="5" y1="12" x2="19" y2="12"/>
        </svg>
        发布资源
      </router-link>
    </section>

    <!-- Search & Filter -->
    <section class="list-section">
      <div class="list-header">
        <div>
          <h2 class="section-title">资源列表</h2>
          <p class="section-subtitle">共 {{ totalElements }} 个资源</p>
        </div>
        <div class="search-box">
          <svg class="search-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <circle cx="11" cy="11" r="8"/>
            <line x1="21" y1="21" x2="16.65" y2="16.65"/>
          </svg>
          <input
            v-model="keyword"
            type="text"
            placeholder="搜索资源..."
            class="search-input"
            @input="onSearch"
          />
        </div>
      </div>

      <!-- Loading -->
      <div v-if="loading" class="loading-state">
        <div class="spinner"></div>
        <p>加载中...</p>
      </div>

      <!-- Empty -->
      <div v-else-if="resources.length === 0" class="empty-state">
        <svg class="empty-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round">
          <path d="M13 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V9z"/>
          <polyline points="13 2 13 9 20 9"/>
          <line x1="9" y1="13" x2="15" y2="13"/>
          <line x1="9" y1="17" x2="15" y2="17"/>
        </svg>
        <p class="empty-text">{{ keyword ? '未找到匹配的资源' : '暂无资源，快来发布第一个吧' }}</p>
        <router-link v-if="!keyword" to="/publish" class="btn btn-primary">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <line x1="12" y1="5" x2="12" y2="19"/>
            <line x1="5" y1="12" x2="19" y2="12"/>
          </svg>
          发布资源
        </router-link>
      </div>

      <!-- Resource Cards -->
      <div v-else class="resource-grid">
        <div v-for="resource in resources" :key="resource.id" class="resource-card fade-in">
          <div class="card-header">
            <div class="card-icon">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round">
                <path d="M13 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V9z"/>
                <polyline points="13 2 13 9 20 9"/>
              </svg>
            </div>
            <h3 class="card-title">{{ resource.name }}</h3>
          </div>
          <p v-if="resource.description" class="card-desc">{{ resource.description }}</p>
          <div class="card-meta">
            <span v-if="resource.category" class="card-category">{{ resource.category }}</span>
            <span v-if="resource.sourceType" class="card-source">{{ sourceLabel(resource.sourceType) }}</span>
          </div>
          <div class="card-footer">
            <time class="card-time">{{ formatDate(resource.createdAt) }}</time>
            <a :href="resource.url" target="_blank" rel="noopener noreferrer" class="card-link">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path d="M18 13v6a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h6"/>
                <polyline points="15 3 21 3 21 9"/>
                <line x1="10" y1="14" x2="21" y2="3"/>
              </svg>
              访问链接
            </a>
          </div>
        </div>
      </div>

      <!-- Pagination -->
      <div v-if="totalPages > 1" class="pagination">
        <button class="btn btn-secondary" :disabled="currentPage === 0" @click="goToPage(currentPage - 1)">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <polyline points="15 18 9 12 15 6"/>
          </svg>
          上一页
        </button>
        <span class="page-info">{{ currentPage + 1 }} / {{ totalPages }}</span>
        <button class="btn btn-secondary" :disabled="currentPage >= totalPages - 1" @click="goToPage(currentPage + 1)">
          下一页
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <polyline points="9 18 15 12 9 6"/>
          </svg>
        </button>
      </div>
    </section>
  </div>
</template>

<script>
import { resourceApi } from '@/api/index.js'

export default {
  name: 'HomeView',
  data() {
    return {
      resources: [],
      loading: false,
      currentPage: 0,
      pageSize: 12,
      totalElements: 0,
      totalPages: 0,
      keyword: '',
      searchTimer: null
    }
  },
  mounted() {
    this.fetchResources()
  },
  methods: {
    async fetchResources() {
      this.loading = true
      try {
        const params = {
          page: this.currentPage,
          size: this.pageSize,
          sortBy: 'createdAt'
        }
        const data = await resourceApi.search(this.keyword, this.currentPage, this.pageSize)
        this.resources = data.content || []
        this.totalElements = data.totalElements || 0
        this.totalPages = data.totalPages || 0
        this.currentPage = data.page || 0
      } catch (e) {
        console.error('Failed to fetch resources:', e)
      } finally {
        this.loading = false
      }
    },

    onSearch() {
      clearTimeout(this.searchTimer)
      this.searchTimer = setTimeout(() => {
        this.currentPage = 0
        this.fetchResources()
      }, 400)
    },

    goToPage(page) {
      this.currentPage = page
      this.fetchResources()
      window.scrollTo({ top: 0, behavior: 'smooth' })
    },

    sourceLabel(type) {
      const map = { baidu: '百度网盘', ali: '阿里云盘', tianyi: '天翼云盘', '123': '123云盘', other: '其他' }
      return map[type] || type || '其他'
    },

    formatDate(dateStr) {
      if (!dateStr) return ''
      const d = new Date(dateStr)
      const now = new Date()
      const diff = now - d
      if (diff < 60000) return '刚刚'
      if (diff < 3600000) return Math.floor(diff / 60000) + ' 分钟前'
      if (diff < 86400000) return Math.floor(diff / 3600000) + ' 小时前'
      const pad = n => n.toString().padStart(2, '0')
      return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}`
    }
  }
}
</script>

<style scoped>
/* ===== Hero ===== */
.hero {
  text-align: center;
  padding: 3rem 0 2.5rem;
}

.hero-title {
  font-size: 2rem;
  font-weight: 700;
  color: var(--color-text-primary);
  letter-spacing: -0.03em;
  margin-bottom: 0.5rem;
}

.hero-desc {
  font-size: 1rem;
  color: var(--color-text-muted);
  margin-bottom: 1.5rem;
  max-width: 480px;
  margin-left: auto;
  margin-right: auto;
}

.hero-btn {
  padding: 12px 28px;
  font-size: 0.95rem;
  gap: 8px;
}

.hero-btn svg {
  width: 18px;
  height: 18px;
}

/* ===== List Section ===== */
.list-section {
  padding-top: 0.5rem;
}

.list-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  margin-bottom: 1.5rem;
  flex-wrap: wrap;
  gap: 1rem;
}

.search-box {
  position: relative;
  width: 280px;
}

.search-icon {
  position: absolute;
  left: 12px;
  top: 50%;
  transform: translateY(-50%);
  width: 16px;
  height: 16px;
  color: var(--color-text-muted);
  pointer-events: none;
}

.search-input {
  padding-left: 36px;
  width: 100%;
}

/* ===== Loading ===== */
.loading-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 4rem 0;
  color: var(--color-text-muted);
}

.spinner {
  width: 32px;
  height: 32px;
  border: 2px solid var(--color-border);
  border-top-color: var(--color-text-primary);
  border-radius: 50%;
  animation: spin 0.6s linear infinite;
  margin-bottom: 1rem;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

/* ===== Empty State ===== */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 4rem 0;
}

.empty-icon {
  width: 48px;
  height: 48px;
  color: var(--color-text-muted);
  margin-bottom: 1rem;
}

.empty-text {
  color: var(--color-text-muted);
  font-size: 0.9rem;
  margin-bottom: 1.25rem;
}

/* ===== Resource Cards ===== */
.resource-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 1rem;
}

.resource-card {
  background: var(--color-bg-primary);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-lg);
  padding: 1.25rem;
  transition: box-shadow var(--transition-base), border-color var(--transition-fast);
}

.resource-card:hover {
  box-shadow: var(--shadow-md);
  border-color: var(--color-text-subtle);
}

.card-header {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  margin-bottom: 0.5rem;
}

.card-icon {
  width: 36px;
  height: 36px;
  background: var(--color-bg-secondary);
  border-radius: var(--radius-md);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.card-icon svg {
  width: 18px;
  height: 18px;
  color: var(--color-text-muted);
}

.card-title {
  font-size: 0.95rem;
  font-weight: 600;
  color: var(--color-text-primary);
  line-height: 1.4;
  word-break: break-word;
}

.card-desc {
  font-size: 0.83rem;
  color: var(--color-text-muted);
  line-height: 1.5;
  margin-bottom: 0.5rem;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.card-meta {
  display: flex;
  gap: 8px;
  margin-bottom: 0.5rem;
}

.card-category,
.card-source {
  font-size: 0.75rem;
  padding: 2px 8px;
  border-radius: var(--radius-sm);
  background: var(--color-bg-secondary);
  color: var(--color-text-muted);
  font-weight: 500;
}

.card-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding-top: 0.75rem;
  border-top: 1px solid var(--color-border-light);
}

.card-time {
  font-size: 0.78rem;
  color: var(--color-text-muted);
  font-family: var(--font-mono);
}

.card-link {
  display: inline-flex;
  align-items: center;
  gap: 5px;
  font-size: 0.82rem;
  font-weight: 600;
  color: var(--color-accent);
  text-decoration: none;
  transition: color var(--transition-fast);
}

.card-link:hover {
  color: var(--color-accent-hover);
}

.card-link svg {
  width: 14px;
  height: 14px;
}

/* ===== Pagination ===== */
.pagination {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 1rem;
  margin-top: 2rem;
  padding-top: 1.5rem;
  border-top: 1px solid var(--color-border);
}

.page-info {
  font-size: 0.85rem;
  color: var(--color-text-muted);
  font-family: var(--font-mono);
  min-width: 60px;
  text-align: center;
}
</style>
