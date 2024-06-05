import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    username: null,  // 用于存储用户名
    isAuthenticated: false,  // 跟踪登录状态
    currentLocation: '北京邮电大学'  // 默认当前位置
  },
  getters: {
    getCurrentLocation: state => state.currentLocation,
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
    },
    setCurrentLocation(state, location) {
      state.currentLocation = location;  // 更新当前位置
    }
  },
  actions: {
    login({ commit }, { username, isAuthenticated }) {
      commit('setUsername', username);  // 提交用户名
      commit('setAuthentication', isAuthenticated);  // 提交认证状态
      // 这里可以添加登录逻辑
    },
    logout({ commit }) {
      commit('setCurrentLocation', '北京邮电大学');  // 登出时重置位置
      commit('setAuthentication', false); // 清除认证状态及用户名
    },
    selectLocation({ commit }, location) {
      commit('setCurrentLocation', location);
    }
  }
});
