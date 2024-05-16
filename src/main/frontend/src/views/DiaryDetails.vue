<template>
  <div class="diary-details-container">
    <button class="back-button" @click="goBack">&#8592;</button>
    <button :class="{ 'like-button': !isLiked, 'not-liked': isLiked }" @click="toggleLike">&#128077;</button>
    <article class="diary-article">
      <header class="diary-header">
        <h1 class="diary-title">{{ diary.title }}</h1>
        <p class="diary-meta"><strong>By:</strong>{{ diary.username }}</p>
      </header>
      <div class="diary-content">{{ diary.content }}</div>
      <div class="author">
        <p><strong>浏览量：</strong> {{ diary.hot }}<strong>  点赞：</strong> {{ diary.likes }}</p>
      </div>
    </article>
  </div>
</template>

<script>
import axios from 'axios';
import { mapState } from 'vuex';
export default {
  data() {
    return {
      diary: {},
      isLiked: false
    };
  },
  computed: {
    ...mapState({
      username1: state => state.username
    })
  },
  methods: {
    goBack() {
      this.$router.go(-1);
    },
    checkLikeStatus() {
      const diaryId = this.diary.id;
      const username = this.username1;
      axios.get(`/api/diaries/${diaryId}/check-like`, { params: { username } })
        .then(response => {
          this.isLiked = response.data;
          // console.log(this.isLiked);
        })
        .catch(error => {
          console.error('Error checking like status:', error);
        });
    },
    toggleLike() {
      const diaryId = this.diary.id;
      const username = this.username1;
      axios.post(`/api/diaries/${diaryId}/toggle-like`, null, { params: { username } })
        .then(response => {
          this.isLiked = !this.isLiked;
          alert(response.data);   
        })
        .catch(error => {
          console.error('Error toggling like status:', error);
        });
    }
  },

  created() {
    axios.get(`/api/diaries/${this.$route.params.id}`)
      .then(response => {
        this.diary = response.data;
        this.checkLikeStatus();
      })
      .catch(error => {
        console.error('获取日记详情失败：', error);
      });
  }
}
</script>

<style scoped>
.author {
  width: 20vw;
  margin-left: 42vw;
}

.diary-details-container {
  max-width: 800px;
  margin: 20px auto;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  position: relative;
}

.like-button,
.not-liked {
  position: absolute;
  top: 20px;
  font-size: 24px;
  color: #555;
  border: none;
  cursor: pointer;
}

.back-button {
  left: 10px;
  background-color:#ffffff;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #c3c4c6;
}

.back-button:hover {
  color: #007BFF;
}

.like-button:hover {
  background-color: #d6d6d6;
}

.like-button {
  right: 10px;
  border-radius: 50%;
  width: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f0f0f0;
}

.not-liked {
  right: 10px;
  border-radius: 50%;
  width: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f260c6;
}

.diary-article {
  position: relative;
}

.diary-header {
  text-align: center;
  margin-bottom: 20px;
}

.diary-title {
  font-size: 28px;
  color: #333;
  margin: 0;
}

.diary-meta {
  font-size: 16px;
  color: #666;
}

.diary-content {
  font-size: 18px;
  line-height: 1.6;
  color: #444;
  text-align: left;
  margin-top: 20px;
}
</style>
