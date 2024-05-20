<template>
  <div class="content-wrapper">
    <SideBox />
    <div class="main-content">
      <h1 class="title">Recommend</h1>
      <div class="controls">
        <select v-model="selectedType" @change="fetchLocations" class="select-style">
          <option value="">所有类型</option>
          <option value="大使馆">大使馆</option>
          <option value="地铁站">地铁站</option>
          <option value="酒店">酒店</option>
          <option value="大学">大学</option>
          <option value="景点">景点</option>
        </select>
      </div>
      <table class="styled-table">
        <thead>
          <tr>
            <th>名称</th>
            <th>类型</th>
            <th>纬度</th>
            <th>经度</th>
            <th>热门程度</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="location in locations" :key="location.id">
            <td>{{ location.name }}</td>
            <td>{{ location.type }}</td>
            <td>{{ location.latitude }}</td>
            <td>{{ location.longitude }}</td>
            <td>{{ location.popularity }}</td>
          </tr>
        </tbody>
      </table>
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
      selectedType: '' // 默认没有筛选类型
    };
  },
  mounted() {
    this.fetchLocations();
  },
  methods: {
    fetchLocations() {
      axios.get('/api/select', {
        params: {
          type: this.selectedType
        }
      })
      .then(response => {
        this.locations = response.data;
      })
      .catch(error => console.error("错误:", error));
    }
  }
}
</script>


<style>
.title {
  color: #333;
  font-size: 24px;
  text-align: center;
  margin-bottom: 20px;
}
.content-wrapper {
  display: flex;
  height: 100vh;
  background: #f4f4f9;
}
.main-content {
  flex-grow: 1;
  padding: 20px;
  overflow-y: auto;
  background: white;
  box-shadow: 0 0 15px rgba(0,0,0,0.05);
}
.controls {
  margin-bottom: 10px;
}
.select-style {
  padding: 8px;
  border-radius: 5px;
  border: 1px solid #ccc;
}
.styled-table {
  width: 100%;
  border-collapse: collapse;
  margin: 25px 0;
  font-size: 0.9em;
  font-family: sans-serif;
  min-width: 400px;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.15);
}
.styled-table thead tr {
  background-color: #009879;
  color: #ffffff;
  text-align: left;
}
.styled-table th,
.styled-table td {
  padding: 12px 15px;
}
.styled-table tbody tr {
  border-bottom: 1px solid #dddddd;
}

.styled-table tbody tr:nth-of-type(even) {
  background-color: #f3f3f3;
}

.styled-table tbody tr:last-of-type {
  border-bottom: 2px solid #009879;
}
.styled-table tbody tr:hover {
  background-color: #f1f1f1;
}
</style>
