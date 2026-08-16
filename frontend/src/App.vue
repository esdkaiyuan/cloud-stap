<template>
  <div id="app">
    <header class="site-header">
      <div class="header-inner">
        <div class="logo">
          <svg class="logo-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round">
            <path d="M17.5 19H9a7 7 0 1 1 6.71-9h1.79a4.5 4.5 0 1 1 0 9Z"/>
          </svg>
          <span class="logo-text">NetDisk Share</span>
        </div>
        <nav class="nav-links">
          <router-link to="/" class="nav-link">资源列表</router-link>
          <router-link to="/publish" class="nav-link nav-link--accent">发布资源</router-link>
        </nav>
      </div>
    </header>

    <main class="main-content">
      <router-view />
    </main>

    <footer class="site-footer">
      <p>NetDisk Share &mdash; 简约资源共享平台</p>
    </footer>
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style>
/* ===== Layout ===== */
#app {
  flex-direction: column;
}

.site-header {
  border-bottom: 1px solid var(--color-border);
  background: var(--color-bg-primary);
  position: sticky;
  top: 0;
  z-index: 100;
  flex-shrink: 0;
}

.header-inner {
  max-width: 1100px;
  margin: 0 auto;
  padding: 0 2rem;
  height: 56px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.logo {
  display: flex;
  align-items: center;
  gap: 10px;
  text-decoration: none;
}

.logo-icon {
  width: 22px;
  height: 22px;
  color: var(--color-text-primary);
}

.logo-text {
  font-family: var(--font-mono);
  font-size: 1.05rem;
  font-weight: 600;
  color: var(--color-text-primary);
  letter-spacing: -0.01em;
}

.nav-links {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.nav-link {
  text-decoration: none;
  color: var(--color-text-muted);
  font-size: 0.9rem;
  padding: 6px 14px;
  border-radius: var(--radius-md);
  transition: all var(--transition-fast);
  font-weight: 500;
}

.nav-link:hover {
  color: var(--color-text-primary);
  background: var(--color-bg-hover);
}

.nav-link.router-link-exact-active {
  color: var(--color-accent);
}

.nav-link--accent {
  background: var(--color-text-primary);
  color: var(--color-bg-primary) !important;
  padding: 6px 18px;
  font-weight: 600;
}

.nav-link--accent:hover {
  background: var(--color-text-secondary);
}

.main-content {
  flex: 1;
  max-width: 1100px;
  width: 100%;
  margin: 0 auto;
  padding: 2.5rem 2rem;
}

.site-footer {
  border-top: 1px solid var(--color-border);
  padding: 1.5rem 2rem;
  text-align: center;
  flex-shrink: 0;
}

.site-footer p {
  font-size: 0.8rem;
  color: var(--color-text-subtle);
}
</style>
