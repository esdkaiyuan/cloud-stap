<template>
  <aside class="sidebar" :class="{ collapsed }">
    <div class="sidebar-header">
      <div class="logo">
        <svg class="logo-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <path d="M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16z" />
          <polyline points="3.27 6.96 12 12.01 20.73 6.96" />
          <line x1="12" y1="22.08" x2="12" y2="12.01" />
        </svg>
        <span v-if="!collapsed" class="logo-text">DiskShare</span>
      </div>
      <button class="collapse-btn btn-ghost" @click="$emit('toggle')">
        <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <polyline points="11 17 6 12 11 7" />
          <polyline points="18 17 13 12 18 7" />
        </svg>
      </button>
    </div>

    <nav class="sidebar-nav">
      <router-link to="/" class="nav-item" active-class="active">
        <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z" />
          <polyline points="9 22 9 12 15 12 15 22" />
        </svg>
        <span v-if="!collapsed">资源总览</span>
      </router-link>

      <router-link to="/submit" class="nav-item" active-class="active">
        <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <line x1="12" y1="5" x2="12" y2="19" />
          <line x1="5" y1="12" x2="19" y2="12" />
        </svg>
        <span v-if="!collapsed">发布资源</span>
      </router-link>
    </nav>

    <div v-if="!collapsed" class="sidebar-footer">
      <div class="sidebar-divider" />
      <div class="api-info">
        <div class="api-label">API Docs</div>
        <a href="/swagger-ui/index.html" target="_blank" class="api-link">
          Swagger UI
          <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" style="width:0.85em;height:0.85em">
            <path d="M18 13v6a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h6" />
            <polyline points="15 3 21 3 21 9" />
            <line x1="10" y1="14" x2="21" y2="3" />
          </svg>
        </a>
      </div>
    </div>
  </aside>
</template>

<script setup>
defineProps({ collapsed: Boolean })
defineEmits(['toggle'])
</script>

<style scoped>
.sidebar {
  width: var(--sidebar-width);
  height: 100vh;
  background: var(--color-bg-secondary);
  border-right: 1px solid var(--color-border-light);
  display: flex;
  flex-direction: column;
  flex-shrink: 0;
  transition: width var(--transition-base);
  position: sticky;
  top: 0;
}
.sidebar.collapsed {
  width: 64px;
}

.sidebar-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 1rem 0.85rem;
  border-bottom: 1px solid var(--color-border-light);
  min-height: var(--header-height);
}

.logo {
  display: flex;
  align-items: center;
  gap: 0.6rem;
}
.logo-icon {
  width: 24px;
  height: 24px;
  color: var(--color-accent);
  flex-shrink: 0;
}
.logo-text {
  font-family: var(--font-mono);
  font-weight: 600;
  font-size: 1.05rem;
  color: var(--color-text-primary);
  letter-spacing: -0.02em;
}

.collapse-btn {
  padding: 0.3rem;
  border-radius: var(--radius-sm);
}

.sidebar-nav {
  flex: 1;
  padding: 0.75rem 0.5rem;
  display: flex;
  flex-direction: column;
  gap: 0.2rem;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 0.65rem;
  padding: 0.55rem 0.75rem;
  border-radius: var(--radius-md);
  color: var(--color-text-muted);
  font-size: 0.9rem;
  font-weight: 500;
  transition: all var(--transition-fast);
  text-decoration: none;
}
.nav-item:hover {
  background: var(--color-bg-hover);
  color: var(--color-text-primary);
}
.nav-item.active {
  background: var(--color-accent-light);
  color: var(--color-accent);
}
.nav-item .icon {
  width: 1.15em;
  height: 1.15em;
  flex-shrink: 0;
}

.sidebar-footer {
  padding: 0.75rem 1rem;
}
.sidebar-divider {
  height: 1px;
  background: var(--color-border-light);
  margin-bottom: 0.75rem;
}
.api-info {
  display: flex;
  flex-direction: column;
  gap: 0.35rem;
}
.api-label {
  font-family: var(--font-mono);
  font-size: 0.7rem;
  color: var(--color-text-subtle);
  text-transform: uppercase;
  letter-spacing: 0.08em;
}
.api-link {
  display: flex;
  align-items: center;
  gap: 0.35rem;
  font-family: var(--font-mono);
  font-size: 0.82rem;
  color: var(--color-accent);
}
.api-link:hover {
  color: var(--color-accent-hover);
}
</style>
