import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    username: null,  // 用于存储用户名
    isAuthenticated: false  // 跟踪登录状态
  },
  mutations: {
    setUsername(state, username) {
      state.username = username;  // 设置用户名
    },
    setAuthentication(state, status) {
      state.isAuthenticated = status;  // 设置认证状态
      if (!status) {
        state.username = null;  // 如果是登出操作，清除用户名
      }
    }
  },
  actions: {
    login({ commit }, { username, isAuthenticated }) {
      commit('setUsername', username);  // 提交用户名
      commit('setAuthentication', isAuthenticated);  // 提交认证状态
      // 这里可以添加登录逻辑
    },
    logout({ commit }) {
      commit('setAuthentication', false); // 清除认证状态及用户名
    }
  }
});
