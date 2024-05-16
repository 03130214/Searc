<template>
  <div class="content-wrapper">
    <SideBox />
    <div class="main-content">
      <h1 class="title">Welcome to Your Home</h1>
    </div>
  </div>
</template>

<script>
import SideBox from '../components/SideBox.vue';
export default {
  name: 'UserHome',
  components: {
    SideBox
  }
}
</script>

<style>
.title{
  color: black;
}
.content-wrapper {
  display: flex;
  height: 100vh; /* 视图高度全屏 */
}

.main-content {
  flex-grow: 1; /* 填充剩余空间 */
  padding: 20px; /* 添加内边距 */
  overflow-y: auto; /* 如果内容很多，允许滚动 */
}
</style>
