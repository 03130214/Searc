import Vue from 'vue'
import App from './App.vue'
import Axios from 'axios';
import router from './router';
import store from './store';
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css'; // 引入样式

Vue.use(ElementUI);
Vue.prototype.$http = Axios;
Vue.config.productionTip = false

new Vue({
  router,
  store,   // 使用 Vuex 状态管理
  render: h => h(App),
}).$mount('#app')
