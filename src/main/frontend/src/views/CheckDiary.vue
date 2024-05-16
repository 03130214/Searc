<template>
  <div class="view-diaries-container">
    <button class="back-button" @click="changeRoute('/diary')">&#8592;</button>
    <h1 class="header">查看日记</h1>
    <select v-model="searchType" class="search-select">
      <option value="title">按标题</option>
      <option value="location">按地点</option>
    </select>
    <div class="search-container">
      <input type="text" v-model="searchQuery" placeholder="请输入..." class="search-input" />
      <button @click="searchDiaries" class="search-button">搜索</button>
    </div>
    <div class="select-container">
      <select v-model="sortKey" @change="sortDiaries">
        <option value="time">时间</option>
        <option value="hot">热度</option>
        <option value="likes">点赞量</option>
      </select>
    </div>
    <div class="results-container">
      <div v-if="diaries.length">
        <div v-for="diary in diaries" :key="diary.id" class="diary-item" @click="goToDiaryDetails(diary.id)">
          <h2>{{ diary.title }}<span class="tag">{{ diary.location }}</span></h2>
          <p class="content-preview">{{ formattedContent(diary.content) }}</p>
          <p class="author"><strong>By:</strong> {{ diary.username }}</p>
        </div>
      </div>
      <p v-else>没有找到匹配的日记。</p>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      searchQuery: '',
      searchTitle: '',
      searchType: 'title',
      diaries: [],
      sortKey: 'time'
    };
  },
  methods: {
    formattedContent(content) {
      return content.length > 20 ? content.slice(0, 20) + '...' : content;
    },
    changeRoute(route) {
      this.$router.push(route);
    },
    searchDiaries() {
      const params = {};
      if (this.searchType === 'title') {
        params.title = this.searchQuery;
      } else if (this.searchType === 'location') {
        params.location = this.searchQuery;
      }

      axios.get('/api/diaries', { params })
        .then(response => {
          this.diaries = response.data;
        })
        .catch(error => {
          console.error('搜索失败：', error);
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
.author{
  position: absolute;  /* 绝对定位，相对于最近的定位祖先（.diary-entry） */
  right: 0;
  bottom: 0;
  padding: 5px;
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

.select-container {
  margin: 0px;
  padding: 0;
}

.search-select {
  width: 8vw;
  padding: 8px 10px;
  margin-left: 1.4vw;
  border: 1px solid #ccc;
  border-radius: 5px;
  background-color: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  font-size: 16px;
  color: #333;
  cursor: pointer;
}

.select-container select {
  width: 8vw;
  padding: 8px 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  background-color: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  font-size: 16px;
  color: #333;
  cursor: pointer;
}

.select-container select:hover,
.select-container select:focus,
.search-select:hover,
.search-select:focus {
  border-color: #888;
  outline: none;
}

.view-diaries-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
  background-color: #f9f9f9;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.header {
  text-align: center;
  font-size: 24px;
  margin-bottom: 20px;
  color: #333;
}

.back-button {
  left: 10px;
  background-color:#f9f9f9;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #c3c4c6;
}

.back-button:hover {
  color: #0056b3;
}

.search-container {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}

.search-input {
  padding: 10px;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 4px;
  width: 45vw;
}

.search-button {
  padding: 10px 15px;
  font-size: 14px;
  color: #fff;
  background-color: #007BFF;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-left: 10px;
  transition: background-color 0.3s ease;
}

.search-button:hover {
  background-color: #0056b3;
}

.results-container {
  margin-top: 20px;
}

.diary-item {
  position: relative;
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