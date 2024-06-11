<template>
  <div class="content-wrapper">
    <SideBox />
    <div class="main-content">
      <div class="left-content">
        <div class="choice">
          <button :class="{ 'toggle-button': true, 'active': isSearch }" @click="searchfood()">
            {{ isSearch ? '今日推荐' : '🦞🦞' }}
          </button>
          <div class="select-container" v-if="isSearch">
            <select v-model="selectedType" @change="fetchLocations" class="select-style">
              <option value="">默认</option>
              <option value="popularity">热度</option>
              <option value="score">评分</option>
            </select>
          </div>
          <div class="search-container" v-if="isSearch">
            <input type="text" v-model="searchQuery2" placeholder="走遍大中国..." class="search-input" />
            <button @click="searchLocations()" class="search-button">搜索</button>
          </div>
          <div class="select-container" v-if="isSearch">
            <select v-model="locationkind" class="select-style">
              <option value="name">名称</option>
              <option value="type">类型</option>
            </select>
          </div>
          <div class="select-container" v-if="!isSearch">
            <select v-model="searchType" class="select-style">
              <option value="name">菜名</option>
              <option value="kind">菜系</option>
              <option value="location">地点</option>
            </select>
          </div>
          <div class="search-container" v-if="!isSearch">
            <input type="text" v-model="searchQuery" placeholder="尝遍天下鲜..." class="search-input" />
            <button @click="searchDishes()" class="search-button">搜索</button>
          </div>
          <div class="select-container" v-if="!isSearch">
            <select v-model="resultchoice" class="select-style">
              <option value="hot">热门</option>
              <option value="score">评分</option>
              <option value="distance">距离</option>
            </select>
          </div>
        </div>
        <div class="result" v-if="!isSearch">
          <!-- 搜索结果 -->
          <div v-for="dish in seardishes" :key="dish.id" class="dish-row">
            <h2>{{ dish.name }}<span class="tag">{{ dish.kind }}</span></h2>
            <div class="dish-location">评分:{{ dish.score }}</div>
            <div class="dish-location">{{ dish.location }}</div>
          </div>
        </div>
        <div class="result" v-if="isSearch">
          <!-- 搜索结果 -->
          <div v-for="location in locations" :key="location.id" class="dish-row">
            <h2>{{ location.name }}<span class="tag">{{ location.type }}</span></h2>
            <div class="dish-location">热度:{{ location.popularity }}</div>
            <div class="dish-location">评分:{{ location.score }}</div>
            <button @click="locate(location.name)" class="food">🍽️</button>
          </div>
        </div>
        <!-- <table class="styled-table" v-if="isSearch">
          <thead>
            <tr>
              <th>名称</th>
              <th>类型</th>
              <th>热度</th>
              <th>评分</th>
              <th>相关美食</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="location in locations" :key="location.id">
              <td>{{ location.name }}</td>
              <td>{{ location.type }}</td>
              <td>{{ location.popularity }}</td>
              <td>{{ location.score }}</td>
              <button @click="locate(location.name)" class="food">🍽️</button>
            </tr>
          </tbody>
        </table> -->
      </div>
      <div class="right-content">
        <div class="search">
          <div class="choice">
            <div class="choice">
              <div class="select-container">
                <select v-model="kind" class="select-style">
                  <option value="">默认</option>
                  <option value="小吃">小吃</option>
                  <option value="鲁菜">鲁菜</option>
                  <option value="川菜">川菜</option>
                  <option value="粤菜">粤菜</option>
                  <option value="闽菜">闽菜</option>
                  <option value="苏菜">苏菜</option>
                  <option value="浙菜">浙菜</option>
                  <option value="徽菜">徽菜</option>
                  <option value="湘菜">湘菜</option>
                </select>
              </div>
              <div class="select-container">
                <select v-model="choice" class="select-style">
                  <option value="hot">热门</option>
                  <option value="score">评分</option>
                </select>
              </div>
              <!-- <button class="righttitle">🍱热门</button> -->
            </div>
            <button @click="searchfood()" class="circle-button">🔍</button>
          </div>
          <ul class="dishes-list">
            <li v-for="(dish, index) in top10Dishes" :key="dish.id" class="dish-row">
              <div class="dish-name">
                <!-- 🔥 -->
                <span v-if="index === 0" class="dish-index dish-index-1">1.</span>
                <span v-else-if="index === 1" class="dish-index dish-index-2">2. </span>
                <span v-else-if="index === 2" class="dish-index dish-index-3">3. </span>
                {{ dish.name }}
              </div>
              <div class="dish-location">{{ dish.location }}</div>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>


<script>
import axios from 'axios';
import SideBox from '../components/SideBox.vue';

export default {
  name: 'UserHome',
  components: {
    SideBox
  },
  data() {
    return {
      locations: [],
      dishes: [],
      top10Dishes: [],
      seardishes: [],
      searchType: 'name',
      locationkind: 'name',
      selectedType: '',
      searchQuery: '',
      searchQuery2: '',
      kind: '',
      choice: 'hot',
      resultchoice: 'hot',
      isSearch: true,
      currentname: '',
      distances: {}
    };
  },
  computed: {
    queryParams() {
      let params = [];
      if (this.currentname) {
        params.push(`location=${this.currentname}`);
      }
      if (this.choice) {
        params.push(`choice=${this.choice}`);
      }
      if (this.kind) {
        params.push(`kind=${this.kind}`);
      }
      return params.join('&');
    }
  },
  mounted() {
    this.fetchLocations();
    this.fetchTop10Dishes();
  },
  methods: {
    // async fetchDistance(destination) {
    //   try {
    //     const response = await axios.get(`/api/locations/distance`, {
    //       params: { name1: this.currentname, name2: destination }
    //     });
    //     this.$set(this.distances, destination, response.data.toFixed(2)); // 保留两位小数
    //   } catch (error) {
    //     console.error("Error fetching distance:", error);
    //     this.$set(this.distances, destination, 'N/A');
    //   }
    // },
    // getDistance(destination) {
    //   if (!this.distances[destination]) {
    //     this.fetchDistance(destination);
    //   }
    //   return this.distances[destination] || '计算中...';
    // },
    async searchLocations() {
      let choice = this.selectedType;
      let url = '/api/locations?';
      if (this.locationkind === 'name') {
        url += `name=${encodeURIComponent(this.searchQuery2)}`;
      } else if (this.locationkind === 'type') {
        url += `type=${encodeURIComponent(this.searchQuery2)}`;
      }
      if (choice) {
        url += `&category=${encodeURIComponent(choice)}`;
      }
      try {
        const response = await axios.get(url);
        this.locations = response.data;
      } catch (error) {
        console.error('Error fetching dishes:', error);
      }
    },
    async searchDishes() {
      let choice = this.resultchoice;
      let url = '/api/dishes?';
      if (this.searchType === 'name') {
        url += `name=${encodeURIComponent(this.searchQuery)}`;
      } else if (this.searchType === 'kind') {
        url += `kind=${encodeURIComponent(this.searchQuery)}`;
      } else if (this.searchType === 'location') {
        url += `location=${encodeURIComponent(this.searchQuery)}`;
      }
      if (choice) {
        url += `&category=${encodeURIComponent(choice)}`;
      }
      try {
        const response = await axios.get(url);
        this.seardishes = response.data;
      } catch (error) {
        console.error('Error fetching dishes:', error);
      }
    },
    async fetchTop10Dishes() {
      try {
        const response = await axios.get(`/api/10dishes?choice=${this.choice}`);
        this.top10Dishes = response.data;
      } catch (error) {
        console.error('Error fetching top 10 dishes:', error);
      }
    },
    locate(name) {
      this.currentname = name;
      axios.get(`/api/10dishes?${this.queryParams}`)
        .then(response => {
          this.top10Dishes = response.data;
        })
        .catch(error => {
          console.error('搜索失败：', error);
        });
    },
    searchfood() {
      this.isSearch = !this.isSearch;
      this.fetchTop10Dishes();
    },
    fetchLocations() {
      axios.get(`/api/select?type=${this.selectedType}`)
        .then(response => {
          this.locations = response.data;
        })
        .catch(error => console.error("错误:", error));
    },
  },
  watch: {
    currentname() {
      this.locate(this.currentname);
    },
    choice() {
      this.locate(this.currentname);
    },
    kind() {
      this.locate(this.currentname);
    },
    resultchoice() {
      this.searchDishes();
    }
  }
}
</script>


<style scoped>
.tag {
  color: #fff;
  margin-top: 0.5vh;
  margin-left: 1vw;
  border: 2px solid #ff57c7;
  border-radius: 5px;
  font-size: 10px;
  background-color: #ff57c7;
}

.result {
  padding: 5px;
  margin-top: 2vh;
  max-height: 80vh;
  overflow-y: auto;
}

.result::-webkit-scrollbar {
  width: 12px;
}

.result::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 6px;
}

.result::-webkit-scrollbar-thumb {
  background: #ffffff;
  border-radius: 6px;
}

.result::-webkit-scrollbar-thumb:hover {
  background: #b8b8b8;
}

.dishes-list {
  margin-top: 5px;
  list-style-type: none;
  padding: 0;
}

.dish-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 0.5rem 1.5rem;
  margin-bottom: 1rem;
}

.dish-name {
  font-size: 1.1rem;
  font-weight: bold;
  color: #333;
  display: flex;
  align-items: center;
}

.dish-index {
  font-size: 1rem;
  font-weight: bold;
  color: #007bff;
}

.dish-index-1 {
  color: #ff0000;
}

.dish-index-2 {
  color: #e68200;
}

.dish-index-3 {
  color: #ffc107;
}

.dish-location {
  font-size: 0.95rem;
  color: #555;
}

.search-container {
  display: flex;
  justify-content: center;
  margin-left: 4vw;
}

.search-input {
  padding: 10px;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 10px;
  width: 20vw;
}

.search-button {
  padding: 10px 15px;
  font-size: 14px;
  color: #fff;
  background-color: #43d9ff;
  border: none;
  border-radius: 12px;
  cursor: pointer;
  margin-left: 10px;
  transition: background-color 0.3s ease;
}

.search-button:hover {
  background-color: #0056b3;
}

.food {
  background: rgba(238, 238, 238, 0.15);
  border: #0056b3;
  border-radius: 12px;
  font-size: 32px;
  cursor: pointer;
}

.choice {
  display: flex;
  flex-direction: row;
  gap: 1vw;
}

.circle-button {
  top: 0.5vw;
  right: 2vw;
  border-radius: 50%;
  width: 40px;
  height: 40px;
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

.righttitle {
  width: 5vw;
  background-color: #ff76df;
  color: #ffffff;
  border: none;
  border-radius: 10px;
  font-size: 16px;
  text-align: center;
  margin-bottom: 2px;
}

.content-wrapper {
  display: flex;
  height: 100vh;
  background: rgba(238, 238, 238, 0.15);
}

.main-content {
  flex-grow: 1;
  display: flex;
  padding: 20px;
}

.left-content {
  width: 55vw;
  flex: 2;
  padding: 20px;
  background-color: rgba(247, 247, 247, 0.9);
  border: 1px solid #ccc;
  border-radius: 5px;
}

.right-content {
  flex: 1;
  width: 18vw;
}

.search {
  padding: 10px;
  width: 80%;
  height: 80vh;
  background-color: rgba(247, 247, 247, 0.9);
  border: 1px solid #ccc;
  border-radius: 5px;
  margin-top: 6vh;
  margin-left: 5vh;
  position: relative;
}

.controls {
  margin-bottom: 10px;
}

.select-style {
  height: 5vh;
  padding: 8px;
  border-radius: 5px;
  border: 1px solid #ccc;
}

</style>
