# 网盘资源共享平台

一个简约的网盘资源分享平台，基于 Spring Boot + Vue3 构建。

## 技术栈

- **后端**: Spring Boot 3.2 + Spring Data JPA + H2 Database + SpringDoc OpenAPI
- **前端**: Vue 3 + Vite + Vue Router
- **设计风格**: 简约黑白灰，参考 FastAPI Docs 设计语言

## 功能特性

- 发布网盘资源（标题、链接、描述、分类、网盘类型）
- 浏览资源列表（分页、搜索）
- 查看资源详情（浏览量、下载量、评分）
- 资源评分功能
- Swagger API 文档

## 快速启动

### 后端

```bash
cd backend

# 使用 Maven 运行
mvnw spring-boot:run

# 或使用已安装的 Maven
mvn spring-boot:run
```

后端服务将在 `http://localhost:8080` 启动。

- API 文档: http://localhost:8080/swagger-ui/index.html
- H2 数据库控制台: http://localhost:8080/h2-console

### 前端

```bash
cd frontend

# 安装依赖
npm install

# 开发模式
npm run dev

# 生产构建
npm run build
```

前端开发服务器将在 `http://localhost:3000` 启动。

## 项目结构

```
backend/
├── src/main/java/com/disk/share/
│   ├── config/           # CORS、OpenAPI 配置
│   ├── controller/       # REST API 控制器
│   ├── dto/              # 请求/响应 DTO
│   ├── entity/           # JPA 实体
│   ├── exception/        # 全局异常处理
│   ├── repository/       # 数据访问层
│   └── service/          # 业务逻辑层
└── src/main/resources/
    └── application.properties

frontend/
├── src/
│   ├── api/              # API 请求封装
│   ├── assets/styles/    # 全局样式
│   ├── components/       # 通用组件
│   ├── router/           # 路由配置
│   └── views/            # 页面视图
└── index.html
```

## 设计规范

- **主题色**: 黑白灰浅蓝绿 (`#0d9488`)
- **图标**: 纯 SVG 矢量图标，无 Emoji
- **字体**: 系统默认无衬线字体 + JetBrains Mono 等宽字体
- **布局**: 响应式网格，移动端适配

## 部署

### 前端（GitHub Pages）

前端已自动部署到 GitHub Pages，地址：https://esdkaiyuan.github.io/cloud-stap/

更新前端时，需要重新构建并推送到 `gh-pages` 分支：

```bash
cd frontend

# 设置后端 API 地址（部署后端后替换为实际地址）
$env:VITE_API_BASE="https://your-backend.up.railway.app/api"

# 构建
npm run build

# 部署到 gh-pages 分支
cd ..
git checkout gh-pages
git rm -rf .
cp -r frontend/dist/* .
rm -rf frontend
git add -A && git commit -m "Deploy"
git push origin gh-pages
git checkout main
```

### 后端（Railway 推荐）

1. 安装 Railway CLI 并登录：
```bash
npm install -g @railway/cli
railway login
railway init
```

2. 部署：
```bash
cd backend
railway up
```

3. 部署完成后，Railway 会提供一个 HTTPS 地址（如 `https://xxx.up.railway.app`）

4. 重新构建前端时，设置 `VITE_API_BASE` 为后端地址：
```bash
cd frontend
$env:VITE_API_BASE="https://xxx.up.railway.app/api"
npm run build
```

### 环境变量

| 变量 | 说明 | 默认值 |
|------|------|--------|
| `PORT` | 服务端口 | `8080` |
| `CORS_ORIGINS` | 允许的跨域来源，逗号分隔 | `http://localhost:5173,http://localhost:3000,https://esdkaiyuan.github.io` |
| `DATA_DIR` | H2 数据库文件目录 | `./data` |
| `VITE_API_BASE` | 前端构建时指定的后端 API 地址 | `/api`（开发模式代理） |
