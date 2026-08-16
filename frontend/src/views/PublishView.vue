<template>
  <div class="publish-page">
    <div class="publish-card">
      <div class="publish-header">
        <h2 class="publish-title">发布新资源</h2>
        <router-link to="/" class="btn-close" title="返回">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <line x1="18" y1="6" x2="6" y2="18"/>
            <line x1="6" y1="6" x2="18" y2="18"/>
          </svg>
        </router-link>
      </div>

      <form @submit.prevent="submitResource" class="publish-form">
        <div v-if="message" :class="['alert', messageType === 'success' ? 'alert-success' : 'alert-error']">
          <svg v-if="messageType === 'success'" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/>
            <polyline points="22 4 12 14.01 9 11.01"/>
          </svg>
          <svg v-else viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <circle cx="12" cy="12" r="10"/>
            <line x1="15" y1="9" x2="9" y2="15"/>
            <line x1="9" y1="9" x2="15" y2="15"/>
          </svg>
          <span>{{ message }}</span>
        </div>

        <div class="form-group">
          <label class="form-label" for="title">资源标题 <span class="required">*</span></label>
          <input
            id="title"
            v-model="form.title"
            type="text"
            placeholder="例如：Python编程从入门到实践.pdf"
            :class="{ 'input-error': errors.title }"
            @blur="validateField('title')"
          />
          <span v-if="errors.title" class="field-error">{{ errors.title }}</span>
        </div>

        <div class="form-group">
          <label class="form-label" for="url">资源链接 <span class="required">*</span></label>
          <input
            id="url"
            v-model="form.url"
            type="url"
            placeholder="https://pan.baidu.com/s/xxxxx 或分享链接"
            :class="{ 'input-error': errors.url }"
            @blur="validateField('url')"
          />
          <span v-if="errors.url" class="field-error">{{ errors.url }}</span>
        </div>

        <div class="form-group">
          <label class="form-label" for="description">资源描述 <span class="required">*</span></label>
          <textarea
            id="description"
            v-model="form.description"
            placeholder="简要描述资源内容..."
            rows="3"
          ></textarea>
        </div>

        <div class="form-group">
          <label class="checkbox-label">
            <input type="checkbox" v-model="form.hasExtractCode" />
            <span>此链接需要提取码</span>
          </label>
        </div>
        <div v-if="form.hasExtractCode" class="form-group">
          <label class="form-label" for="extractCode">提取码</label>
          <input
            id="extractCode"
            v-model="form.extractCode"
            type="text"
            placeholder="如：abcd"
            maxlength="50"
            class="extract-input"
          />
        </div>

        <div class="form-row">
          <div class="form-group">
            <label class="form-label" for="category">分类</label>
            <input
              id="category"
              v-model="form.category"
              type="text"
              placeholder="如：教程、软件、资料"
            />
          </div>
          <div class="form-group">
            <label class="form-label" for="sourceType">网盘类型</label>
            <select id="sourceType" v-model="form.sourceType" class="form-select">
              <option value="">请选择</option>
              <option value="baidu">百度网盘</option>
              <option value="ali">阿里云盘</option>
              <option value="tianyi">天翼云盘</option>
              <option value="123">123云盘</option>
              <option value="xunlei">迅雷网盘</option>
              <option value="quark">夸克网盘</option>
              <option value="uc">UC网盘</option>
              <option value="115">115网盘</option>
              <option value="other">其他</option>
            </select>
          </div>
        </div>

        <div class="form-actions">
          <router-link to="/" class="btn btn-secondary">取消</router-link>
          <button type="submit" class="btn btn-primary" :disabled="submitting">
            <svg v-if="!submitting" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"/>
              <polyline points="17 8 12 3 7 8"/>
              <line x1="12" y1="3" x2="12" y2="15"/>
            </svg>
            {{ submitting ? '发布中...' : '发布资源' }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import { resourceApi } from '@/api/index.js'

export default {
  name: 'PublishView',
  data() {
    return {
      form: { title: '', url: '', description: '', category: '', sourceType: '', hasExtractCode: false, extractCode: '' },
      submitting: false,
      message: '',
      messageType: 'success',
      errors: { title: '', url: '' }
    }
  },
  methods: {
    async submitResource() {
      if (!this.validateAll()) return
      this.submitting = true
      try {
        const payload = {
          title: this.form.title.trim(),
          description: this.form.description.trim(),
          url: this.form.url.trim(),
          category: this.form.category.trim() || undefined,
          sourceType: this.form.sourceType || undefined,
          extractCode: this.form.hasExtractCode && this.form.extractCode.trim() ? this.form.extractCode.trim() : undefined
        }
        await resourceApi.create(payload)
        this.message = '资源发布成功！正在跳转...'
        this.messageType = 'success'
        this.form = { title: '', url: '', description: '', category: '', sourceType: '', hasExtractCode: false, extractCode: '' }
        this.errors = { title: '', url: '' }
        setTimeout(() => this.$router.push('/'), 1200)
      } catch (e) {
        this.message = e.message || '发布失败，请稍后重试'
        this.messageType = 'error'
      } finally {
        this.submitting = false
      }
    },
    validateField(field) {
      if (field === 'title') {
        this.errors.title = this.form.title.trim() ? '' : '请输入资源标题'
      }
      if (field === 'url') {
        if (!this.form.url.trim()) {
          this.errors.url = '请输入资源链接'
        } else if (!this.isValidUrl(this.form.url)) {
          this.errors.url = '请输入有效的URL地址'
        } else {
          this.errors.url = ''
        }
      }
    },
    validateAll() {
      this.validateField('title')
      this.validateField('url')
      if (!this.form.description.trim()) {
        this.message = '请输入资源描述'
        this.messageType = 'error'
        return false
      }
      return !this.errors.title && !this.errors.url
    },
    isValidUrl(str) {
      try {
        const url = new URL(str)
        return url.protocol === 'http:' || url.protocol === 'https:'
      } catch { return false }
    }
  }
}
</script>

<style scoped>
.publish-page {
  max-width: 640px;
  margin: 0 auto;
  padding-top: 1rem;
}

.publish-card {
  background: var(--color-bg-primary);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-lg);
  padding: 1.75rem;
  box-shadow: var(--shadow-md);
}

.publish-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 1.5rem;
}

.publish-title {
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

.btn-close svg {
  width: 18px;
  height: 18px;
}

.alert {
  display: flex;
  align-items: flex-start;
  gap: 10px;
  padding: 12px 16px;
  border-radius: var(--radius-md);
  font-size: 0.88rem;
  margin-bottom: 1rem;
}

.alert svg {
  width: 18px;
  height: 18px;
  flex-shrink: 0;
  margin-top: 1px;
}

.alert-success {
  background: var(--color-success-light);
  color: var(--color-success);
  border: 1px solid #a7f3d0;
}

.alert-error {
  background: var(--color-error-light);
  color: var(--color-error);
  border: 1px solid #fecaca;
}

.publish-form {
  display: flex;
  flex-direction: column;
  gap: 1.1rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.form-label {
  font-size: 0.85rem;
  font-weight: 500;
  color: var(--color-text-secondary);
}

.required {
  color: var(--color-error);
}

.field-error {
  font-family: var(--font-mono);
  font-size: 0.8rem;
  color: var(--color-error);
}

.input-error {
  border-color: var(--color-error) !important;
  box-shadow: 0 0 0 3px rgba(220, 38, 38, 0.08) !important;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 0.75rem;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
}

.form-select {
  width: 100%;
  padding: 10px 14px;
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  font-size: 0.9rem;
  font-family: inherit;
  color: var(--color-text-primary);
  background: var(--color-bg-primary);
  transition: border-color var(--transition-fast), box-shadow var(--transition-fast);
  outline: none;
  appearance: auto;
}

.form-select:focus {
  border-color: var(--color-accent);
  box-shadow: 0 0 0 3px rgba(13, 148, 136, 0.1);
}

.checkbox-label {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  font-size: 0.85rem;
  color: var(--color-text-secondary);
  cursor: pointer;
  user-select: none;
}

.checkbox-label input[type="checkbox"] {
  width: 16px;
  height: 16px;
  accent-color: var(--color-accent);
  cursor: pointer;
}

.extract-input {
  width: 100%;
  padding: 10px 14px;
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  font-size: 0.9rem;
  font-family: var(--font-mono);
  color: var(--color-text-primary);
  background: var(--color-bg-primary);
  transition: border-color var(--transition-fast), box-shadow var(--transition-fast);
  outline: none;
  letter-spacing: 0.15em;
}

.extract-input:focus {
  border-color: var(--color-accent);
  box-shadow: 0 0 0 3px rgba(13, 148, 136, 0.1);
}
</style>
