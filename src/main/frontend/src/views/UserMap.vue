<template>
  <div class="content-wrapper">
    <!-- 搜索框容器 -->
    <SideBox />
    <div class="search-container">
      <input 
        type="text" 
        v-model="searchQuery" 
        @input="filterLocations" 
        placeholder="搜索地点..." 
        class="search-input"
      />
      <ul v-if="filteredLocations.length" class="suggestions-list">
        <li 
          v-for="(location, index) in filteredLocations" 
          :key="index" 
          @click="selectLocation(location)"
          class="suggestion-item"
        >
          {{ location.name }}
        </li>
      </ul>
      <button @click="searchLocation" class="search-button">搜索</button>
      <div id="map" style="height: 600px;"></div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import SideBox from '../components/SideBox.vue';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import 'leaflet.markercluster/dist/MarkerCluster.css';
import 'leaflet.markercluster/dist/MarkerCluster.Default.css';
import 'leaflet.markercluster';

// 导入默认的Leaflet标记图标
import markerIcon2x from 'leaflet/dist/images/marker-icon-2x.png';
import markerIcon from 'leaflet/dist/images/marker-icon.png';
import markerShadow from 'leaflet/dist/images/marker-shadow.png';

export default {
  name: 'MapComponent', // 组件名称
  components: {
    SideBox
  },
  data() {
    return {
      map: null, // 用于存储Leaflet地图实例
      markerCluster: null, // 用于存储MarkerClusterGroup实例
      locations: [], // 用于存储从API获取的地点数据
      searchQuery: '', // 用于绑定搜索框的输入值
      filteredLocations: [] // 用于存储过滤后的地点数据
    };
  },
  mounted() {
    // 组件挂载后初始化地图和获取地点数据
    this.initMap();
    this.fetchLocations();
  },
  methods: {
    initMap() {
      // 初始化地图视图，设置中心点和缩放级别
      this.map = L.map('map').setView([39.8794, 116.4038], 15);
      // 添加OpenStreetMap图层
      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19, // 最大缩放级别
        attribution: '© OpenStreetMap' // 地图版权信息
      }).addTo(this.map);

      // 覆盖默认的标记图标选项
      delete L.Icon.Default.prototype._getIconUrl;
      L.Icon.Default.mergeOptions({
        iconRetinaUrl: markerIcon2x, // 高分辨率图标
        iconUrl: markerIcon, // 默认图标
        shadowUrl: markerShadow, // 阴影图标
      });

      // 创建MarkerClusterGroup实例
      this.markerCluster = L.markerClusterGroup();
      // 将MarkerClusterGroup添加到地图
      this.map.addLayer(this.markerCluster);
    },
    fetchLocations() {
      // 使用axios从API获取地点数据
      axios.get('/api/map')
        .then(response => {
          console.log('Fetched locations:', response.data);
          // 将获取的数据存储在locations中
          this.locations = response.data;
          // 调用方法将标记添加到地图
          this.addMarkers();
        })
        .catch(error => {
          console.error('Error fetching locations:', error);
        });
    },
    addMarkers() {
      // 遍历每个地点数据，创建并添加标记
      this.locations.forEach(location => {
        const marker = L.marker([location.latitude, location.longitude])
          .bindPopup(`<b>${location.name}</b><br>${location.type}`); // 设置弹出框内容
        // 将标记添加到MarkerClusterGroup
        this.markerCluster.addLayer(marker);
      });
    },
    filterLocations() {
      // 根据搜索查询过滤地点数据
      this.filteredLocations = this.locations.filter(location =>
        location.name.toLowerCase().includes(this.searchQuery.toLowerCase())
      );
    },
    selectLocation(location) {
      // 选择建议中的地点，将其名称填充到搜索框中
      this.searchQuery = location.name;
      this.filteredLocations = [];
    },
    searchLocation() {
      // 在地图上定位到搜索框中的地点
      const location = this.locations.find(loc => loc.name === this.searchQuery);
      if (location) {
        this.map.setView([location.latitude, location.longitude], 17);
      } else {
        alert('未找到相关地点');
      }
    }
  }
};
</script>
<style scoped>
/* 样式调整 */
.content-wrapper {
  display: flex;
  height: 100vh;
  /* 视图高度全屏 */
}
.search-container {
  height: 10vh;
  width: 60%; /* 搜索框宽度为右侧80%区域的60% */
  margin: 10px auto; /* 上下10px的外边距，左右自动 */
  align-items: center; /* 垂直居中 */
  justify-content: center; /* 水平居中 */
  position: relative; /* 相对定位 */
}

.search-input {
  margin-top: 5vh;
  width: 70%; /* 搜索输入框占60%的宽度 */
  background-color: rgba(247, 247, 247, 0.9);
  padding: 10px; /* 内边距10px */
  border: 1px solid #ccc; /* 边框颜色和样式 */
  border-radius: 4px; /* 圆角 */
}

.suggestions-list {
  width: 70%; /* 与搜索输入框宽度一致 */
  position: absolute; /* 绝对定位 */
  top: 38px; /* 距离顶部38px */
  background: white; /* 背景颜色 */
  border: 1px solid #ccc; /* 边框颜色和样式 */
  border-top: none; /* 移除顶部边框 */
  max-height: 200px; /* 最大高度 */
  overflow-y: auto; /* 超出时垂直滚动 */
  z-index: 1000; /* 设置较高的z-index以确保在其他元素上方显示 */
}

.suggestion-item {
  padding: 10px; /* 内边距 */
  cursor: pointer; /* 鼠标指针 */
}

.suggestion-item:hover {
  background-color: #f0f0f0; /* 悬停背景色 */
}

.search-button {
  margin-left: 10px; /* 左边距 */
  padding: 10px 20px; /* 内边距 */
  background-color: #007bff; /* 按钮背景色 */
  color: white; /* 按钮文字颜色 */
  border: none; /* 无边框 */
  border-radius: 4px; /* 圆角 */
  cursor: pointer; /* 鼠标指针 */
}

.search-button:hover {
  background-color: #0056b3; /* 悬停背景色 */
}

#map {
  width: 50vw; /* 设置地图宽度为视口宽度的70% */
  margin-top: 5vh;
}
</style>