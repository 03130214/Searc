import Vue from 'vue';
import Router from 'vue-router';
import Home from '../views/UserHome.vue';
import Login from '../views/UserLogin.vue';
import Register from '../views/UserRegister.vue';  // 确保路径正确
import Map from '../views/UserMap.vue';  // 确保路径正确
import Plan from '../views/UserPlan.vue';  // 确保路径正确
import Diary from '../views/DiaryMain.vue';  // 确保路径正确
import EditDiary from '../views/EditDiary.vue';
import CheckDiary from '../views/CheckDiary.vue';
import DiaryDetails from '../views/DiaryDetails.vue';
import MainPage from'../views/MainPage.vue';

Vue.use(Router);

export default new Router({
  mode: 'history',  // 使用 HTML5 History 模式
  routes: [
    {
      path: '/',
      name: 'MainPage',
      component: MainPage,
    },
    {
      path: '/home',
      name: 'Home',
      component: Home,
      meta: { requiresAuth: true }  // 需要登录才能访问
    },
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
    {
      path: '/register',
      name: 'Register',
      component: Register
    },
    {
      path: '/map',
      name: 'Map',
      component: Map,
      meta: { requiresAuth: true }  // 需要登录才能访问
    },
    {
      path: '/plan',
      name: 'Plan',
      component: Plan,
      meta: { requiresAuth: true }  // 需要登录才能访问
    },
    {
      path: '/diary',
      name: 'Diary',
      component: Diary,
      meta: { requiresAuth: true }  // 需要登录才能访问
    },
    {
      path: '/editdiary',
      name: 'EditDiary',
      component: EditDiary,
      meta: { requiresAuth: true }  // 需要登录才能访问
    },
    {
      path: '/diarydetails',
      name: 'DiaryDetails',
      component: DiaryDetails,
      meta: { requiresAuth: true }  // 需要登录才能访问
    },
    {
      path: '/checkdiary',
      name: 'CheckDiary',
      component: CheckDiary,
      meta: { requiresAuth: true }  // 需要登录才能访问
    }
  ]
});
