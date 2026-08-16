<template>
  <form @submit.prevent="onSubmit" class="submit-form">
    <div class="form-header">
      <h2 class="form-title">发布资源</h2>
      <p class="form-subtitle">填写以下信息以分享您的网盘资源</p>
    </div>

    <div class="form-body">
      <div class="form-group">
        <label class="form-label" for="title">
          资源标题 <span class="required">*</span>
        </label>
        <input
          id="title"
          v-model="form.title"
          type="text"
          placeholder="例如：Spring Boot 3 教程"
          :class="{ 'input-error': errors.title }"
          maxlength="200"
        />
        <span v-if="errors.title" class="field-error">{{ errors.title }}</span>
      </div>

      <div class="form-group">
        <label class="form-label" for="description">
          资源描述 <span class="required">*</span>
        </label>
        <textarea
          id="description"
          v-model="form.description"
          placeholder="简要描述资源内容、适用人群等..."
          rows="4"
          :class="{ 'input-error': errors.description }"
          maxlength="1000"
        />
        <div class="field-meta">
          <span v-if="errors.description" class="field-error">{{ errors.description }}</span>
          <span class="char-count">{{ form.description.length }}/1000</span>
        </div>
      </div>

      <div class="form-group">
        <label class="form-label" for="url">
          资源链接 <span class="required">*</span>
        </label>
        <input
          id="url"
          v-model="form.url"
          type="text"
          placeholder="https://pan.baidu.com/s/... 或分享链接"
          :class="{ 'input-error': errors.url }"
          maxlength="1000"
        />
        <span v-if="errors.url" class="field-error">{{ errors.url }}</span>
      </div>

      <div class="form-row">
        <div class="form-group">
          <label class="form-label" for="category">分类</label>
          <input
            id="category"
            v-model="form.category"
            type="text"
            placeholder="例如：Programming"
            maxlength="100"
          />
        </div>

        <div class="form-group">
          <label class="form-label" for="sourceType">来源</label>
          <select id="sourceType" v-model="form.sourceType">
            <option value="other">其他</option>
            <option value="baidu">百度网盘</option>
            <option value="ali">阿里云盘</option>
            <option value="tianyi">天翼云盘</option>
            <option value="123">123云盘</option>
            <option value="xunlei">迅雷网盘</option>
            <option value="quark">夸克网盘</option>
            <option value="uc">UC网盘</option>
            <option value="115">115网盘</option>
          </select>
        </div>
      </div>
    </div>

    <div class="form-actions">
      <router-link to="/" class="btn btn-secondary">取消</router-link>
      <button type="submit" class="btn btn-primary" :disabled="submitting">
        <span v-if="submitting" class="spinner" />
        {{ submitting ? '发布中...' : '发布资源' }}
      </button>
    </div>
  </form>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { resourceApi } from '@/api/index.js'

const router = useRouter()
const submitting = ref(false)
const errors = ref({})

const form = reactive({
  title: '',
  description: '',
  url: '',
  category: '',
  sourceType: 'other'
})

function validate() {
  const errs = {}
  if (!form.title.trim()) errs.title = '资源标题不能为空'
  else if (form.title.length > 200) errs.title = '标题长度不能超过200个字符'

  if (!form.description.trim()) errs.description = '资源描述不能为空'
  else if (form.description.length > 1000) errs.description = '描述长度不能超过1000个字符'

  if (!form.url.trim()) errs.url = '资源链接不能为空'
  else if (form.url.length > 1000) errs.url = '链接长度不能超过1000个字符'

  errors.value = errs
  return Object.keys(errs).length === 0
}

async function onSubmit() {
  if (!validate()) return
  submitting.value = true
  try {
    const res = await resourceApi.create({
      title: form.title.trim(),
      description: form.description.trim(),
      url: form.url.trim(),
      category: form.category.trim(),
      sourceType: form.sourceType
    })
    router.push('/')
  } catch (e) {
    errors.value = { _form: e.message }
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.submit-form {
  max-width: 640px;
  width: 100%;
}

.form-header {
  margin-bottom: 1.75rem;
}
.form-title {
  font-size: 1.5rem;
  font-weight: 600;
  margin-bottom: 0.35rem;
}
.form-subtitle {
  font-size: 0.88rem;
  color: var(--color-text-muted);
}

.form-body {
  display: flex;
  flex-direction: column;
  gap: 1.25rem;
}
.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.35rem;
}
.form-label {
  font-size: 0.85rem;
  font-weight: 500;
  color: var(--color-text-secondary);
}
.required {
  color: var(--color-error);
}
.input-error {
  border-color: var(--color-error) !important;
  box-shadow: 0 0 0 3px rgba(220, 38, 38, 0.08) !important;
}
.field-error {
  font-family: var(--font-mono);
  font-size: 0.75rem;
  color: var(--color-error);
}
.field-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.char-count {
  font-family: var(--font-mono);
  font-size: 0.72rem;
  color: var(--color-text-subtle);
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 0.75rem;
  margin-top: 2rem;
  padding-top: 1.5rem;
  border-top: 1px solid var(--color-border-light);
}

.spinner {
  width: 16px;
  height: 16px;
  border-width: 2px;
}
</style>
