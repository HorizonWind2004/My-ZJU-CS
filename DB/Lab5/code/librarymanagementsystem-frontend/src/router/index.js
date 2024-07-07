import { createRouter, createWebHistory } from 'vue-router'
import BookVue from '@/components/Book.vue'
import CardVue from '@/components/Card.vue'
import BorrowVue from '@/components/Borrow.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/book'
    },
    {
      path: '/book',
      component: BookVue
    },
    {
      path: '/card',
      component: CardVue
    },
    {
      path: '/borrow',
      component: BorrowVue
    }
  ]
})

export default router
