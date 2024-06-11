<template>
  <div class="content-wrapper">
    <SideBox />
    <div class="main-content">
      <div class="left-content">
        <div class="choice">
          <button @click="toggleMode" :class="{ 'toggle-button': true, 'active': isRecommended }">
            {{ isRecommended ? '今日推荐' : '全部' }}
          </button>
          <div class="select-container" v-if="!isRecommended">
            <select v-model="sortKey" @change="sortDiaries">
              <option value="time">默认</option>
              <option value="hot">热度</option>
              <option value="likes">点赞量</option>
            </select>
          </div>
        </div>
        <div v-for="diary in displayedDiaries" :key="diary.id" class="diary-item" @click="goToDiaryDetails(diary.id)">
          <div class="txtcontent">
            <h2>{{ diary.title }}<span class="tag">{{ diary.location }}</span></h2>
            <p class="content-preview">{{ formattedContent(diary.content) }}</p>
            <p><strong>views:</strong> {{ diary.hot }} <strong>likes:</strong> {{ diary.likes }}
              <strong>author:</strong>
              {{ diary.username }}
            </p>
          </div>
        </div>
        <button @click="refreshDiaries" class="refresh-button" v-if="isRecommended">Swap ↻</button>
      </div>
      <div class="right-content">
        <div class="personal">
          <h1 class="title">{{ this.username1 }}</h1>
          <button class="user" @click="$router.push('/personaldiary')"></button>
        </div>
        <div class="search">
          <h1 class="title">Write</h1>
          <p class="txtcontent">手写的从前</p>
          <button class="circle-button" @click="$router.push('/editdiary')">✏️</button>
        </div>
        <div class="search">
          <h1 class="title">Search</h1>
          <p class="txtcontent">寻她千百度</p>
          <button class="circle-button" @click="$router.push('/checkdiary')">🔍</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import { mapState } from 'vuex';
import SideBox from '../components/SideBox.vue';
export default {
  data() {
    return {
      diaries: [],
      displayedDiaries: [],
      isRecommended: true,
      sortKey: 'time'
    };
  },
  computed: {
    ...mapState({
      username1: state => state.username
    })
  },
  components: {
    SideBox
  },
  created() {
    this.fetchDiaries();
  },
  methods: {
    formattedContent(content) {
      return content.length > 20 ? content.slice(0, 20) + '...' : content;
    },
    changeRoute(route) {
      this.$router.push(route);
    },
    refreshDiaries() {
      this.displayedDiaries = this.getRandomDiaries(5);
    },
    getRandomDiaries(count) {
      const shuffled = this.diaries.sort(() => 0.5 - Math.random());
      return shuffled.slice(0, count);
    },
    fetchDiaries() {
      axios.get('/api/diaries/top', { params: { limit: 20 } })
        .then(response => {
          this.diaries = response.data;
          this.refreshDiaries();
        })
        .catch(error => {
          console.error("Error fetching diaries:", error);
        });
    },
    sortDiaries() {
      if (this.sortKey === 'time') {
        this.diaries.sort((a, b) => b.id - a.id);
      } else if (this.sortKey === 'hot') {
        this.diaries.sort((a, b) => b.hot - a.hot);
      } else if (this.sortKey === 'likes') {
        this.diaries.sort((a, b) => b.likes - a.likes);
      }
    },
    toggleMode() {
      this.isRecommended = !this.isRecommended;
      if (this.isRecommended) {
        this.refreshDiaries();
      } else {
        this.displayedDiaries = this.diaries;
      }
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
.choice {
  display: flex;
  flex-direction: row;
  gap: 1vw;
}

.select-container {
  margin: 0px;
  padding: 0;
}

.select-container select {
  width: 5vw;
  margin-bottom: 2px;
  padding: 5px;
  font-size: 16px;
  background-color: #ffffff;
  color: #000000;
  border: none;
  border-radius: 10px;
  text-align: center;
  cursor: pointer;
}

.select-container select:hover,
.select-container select:focus,
.search-select:hover,
.search-select:focus {
  border-color: #888;
  outline: none;
}

.txtcontent {
  margin-left: 2vw;
}

.content-preview {
  text-indent: 20px;
  text-decoration: underline;
  color: #a2a2a2;
}

.tag {
  color: #fff;
  margin-left: 1vw;
  border: 2px solid #ff57c7;
  border-radius: 5px;
  font-size: 10px;
  background-color: #ff57c7;
}

.user {
  background-image: url('../assets/bin.jpg');
  background-size: cover;
  background-position: center;
  top: 1vw;
  right: 2vw;
  border-radius: 50%;
  width: 70px;
  height: 70px;
  border: none;
  font-size: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  position: absolute;
  transition: background-color 0.3s;
}

.title {
  margin-top: 3vh;
  margin-left: 2vw;
  color: black;
}

.circle-button {
  top: 2vw;
  right: 2vw;
  border-radius: 50%;
  width: 50px;
  height: 50px;
  border: none;
  font-size: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  position: absolute;
  transition: background-color 0.3s;
}

.circle-button:hover {
  background-color: #b8b6b6;
  /* 悬浮时背景色 */
}

.personal {
  padding: 10px;
  width: 70%;
  height: 12vh;
  background-color: rgba(247, 247, 247, 0.9);
  border: 1px solid #ccc;
  border-radius: 5px;
  margin: 5vh;
  position: relative;
}

.search {
  padding: 10px;
  width: 70%;
  height: 28vh;
  background-color: rgba(247, 247, 247, 0.9);
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
  width: 55vw;
  flex: 2;
  /* 占据两份比例 */
  padding: 20px;
  /* 根据需要添加内边距 */
  background-color: rgba(247, 247, 247, 0.9);
  border: 1px solid #ccc;
  border-radius: 5px;
}

.refresh-button {
  margin-left: 20vw;
  margin-top: 2vh;
  padding: 10px 20px;
  font-size: 16px;
  background-color: #ffffff;
  color: rgb(0, 0, 0);
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.toggle-button {
  width: 5vw;
  margin-bottom: 2px;
  padding: 5px;
  font-size: 16px;
  background-color: #43d9ff;
  color: #ffffff;
  border: none;
  border-radius: 10px;
  cursor: pointer;
}

.toggle-button.active {
  background-color: #13f76a;
  color: #ffffff;
}

.toggle-button:hover {
  background-color: #bdbdbd;
}

.refresh-button:hover {
  background-color: #bdbdbd;
}

.right-content {
  flex: 1;
  /* width: 15vw; */
  padding: 20px;
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