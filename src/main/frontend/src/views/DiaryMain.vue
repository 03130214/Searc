<template>
  <div class="content-wrapper">
    <SideBox />
    <div class="main-content">
      <div class="left-content">
        <div v-for="diary in diaries" :key="diary.id" class="diary-item" @click="goToDiaryDetails(diary.id)">
          <h2>{{ diary.title }}</h2>
          <p>{{ diary.content }}</p>
          <p><strong>By:</strong> {{ diary.username }}</p>
        </div>
      </div>
      <div class="right-content">
        <div class="search">
          <h1 class="title">Write</h1>
          <p>记录美好生活</p>
          <button class="circle-button" @click="$router.push('/editdiary')">✏️</button>
        </div>
        <div class="search">
          <h1 class="title">Search</h1>
          <p>你想找的都在这里</p>
          <button class="circle-button" @click="$router.push('/checkdiary')">🔍</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import SideBox from '../components/SideBox.vue';
export default {
  data() {
    return {
      diaries: []
    };
  },
  components: {
    SideBox
  },
  created() {
    this.fetchDiaries();
  },
  methods: {
    changeRoute(route) {
      this.$router.push(route);
    },
    fetchDiaries() {
      axios.get('/api/diaries')
        .then(response => {
          this.diaries = response.data;
        })
        .catch(error => {
          console.error("Error fetching diaries:", error);
        });
    },
    goToDiaryDetails(id) {
      axios.post(`/api/diaries/${id}/incrementHot`)
        .then(() => {
          this.$router.push({ name: 'DiaryDetails', params: { id: id } });
        })
        .catch(error => {
          console.error('增加日记热度失败：', error);
        });
    }
  }
};
</script>

<style scoped>
.title{
  color: black;
}
.circle-button {
  top: 2vw; /* 距离父容器顶部10px */
  right: 2vw; /* 距离父容器右侧10px */
  border-radius: 50%; /* 圆形 */
  width: 50px; /* 宽度 */
  height: 50px; /* 高度 */
  border: none; /* 无边框 */
  font-size: 20px; /* 图标大小 */
  display: flex; /* 用于居中图标 */
  align-items: center; /* 垂直居中 */
  justify-content: center; /* 水平居中 */
  cursor: pointer; /* 鼠标样式 */
  position: absolute;
  transition: background-color 0.3s; /* 过渡效果 */
}
.circle-button:hover {
  background-color: #b8b6b6; /* 悬浮时背景色 */
}
.search{
  padding: 10px;
  width: 80%;
  height: 35vh;
  background-color: #f9f9f9;
  border: 1px solid #ccc;
  border-radius: 5px;
  margin: 5vh;
  position: relative;
}
.content-wrapper {
  display: flex;
  height: 100vh;
  /* 视图高度全屏 */
}

.main-content {
  display: flex;
  flex-grow: 1;
  /* 填充剩余空间 */
  padding: 20px;
  /* 添加内边距 */
}

.left-content {
  overflow-y: auto;
  /* 如果内容很多，允许滚动 */
  flex: 2;
  /* 占据两份比例 */
  padding: 20px;
  /* 根据需要添加内边距 */
}

.right-content {
  flex: 1;
  /* 占据一份比例 */
  padding: 20px;
  /* 根据需要添加内边距 */
  border-left: 1px solid #ccc;
}

.diary-item {
  cursor: pointer;
  margin-bottom: 10px;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  transition: box-shadow 0.3s ease;
}

.diary-item:hover {
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
}

.diary-item h2 {
  margin: 0;
  font-size: 20px;
}

.diary-item p {
  margin: 5px 0;
}
</style>