import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/HomeView.vue')
  },
  {
    path: '/publish',
    name: 'Publish',
    component: () => import('@/views/PublishView.vue')
  },
  {
    path: '/resource/:id',
    name: 'ResourceDetail',
    component: () => import('@/views/ResourceDetailView.vue')
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior() {
    return { top: 0 }
  }
})

export default router
