<template>
  <div class="content-wrapper">
    <div id="map"></div>
    <div class="controls-container">
      <div class="search-container">
        <input type="text" v-model="searchQuery" @input="filterLocations" placeholder="搜索地点..." class="search-input" />
        <ul v-if="filteredLocations.length" class="suggestions-list">
          <li v-for="(location, index) in filteredLocations" :key="index" @click="selectLocation(location)"
            class="suggestion-item">
            {{ location.name }}
          </li>
        </ul>
      </div>

      <button @click="showFacilitiesDialog = true" class="search-button">查找附近设施</button>
      <!-- 对话框组件 -->
      <el-dialog title="查找附近设施" :visible.sync="showFacilitiesDialog">
        <div>
          <!-- 使用 Element UI 的自动完成组件 -->
          <el-autocomplete v-model="facilitySearchLocation" :fetch-suggestions="querySearch" placeholder="输入地点名称"
            @select="handleSelectFacility" class="search-input"></el-autocomplete>
          <select v-model="facilityCategory" class="search-input">
            <option disabled value="">请选择类别</option>
            <option>超市</option>
            <option>洗手间</option>
            <option>食堂</option>
            <option>宿舍楼</option>
            <option>景点</option>
            <option>教学楼</option>
            <option>商店</option>
            <option>饭店</option>
            <option>图书馆</option>
            <option>咖啡馆</option>
            <option>甜品店</option>
            <option>浴室</option>
            <option>宿舍楼</option>
          </select>
          <button @click="fetchFacilities" class="search-button">搜索设施</button>
        </div>
        <ul v-if="nearbyFacilities.length">
          <li v-for="(facility, index) in nearbyFacilities" :key="index">
            {{ facility.locationName }} - 距离: {{ parseFloat(facility.distance).toFixed(2) }} 米
          </li>
        </ul>
      </el-dialog>

      <el-dialog title="当前位置" :visible.sync="currentLocationDialogVisible" width="400px" center
        custom-class="current-location-dialog">
        <p class="current-location-content">{{ currentLocationInfo }}</p>
        <span slot="footer" class="dialog-footer">
          <el-button size="small" @click="currentLocationDialogVisible = false">关闭</el-button>
        </span>
      </el-dialog>


      <div class="route-planner">
        <el-row type="flex" justify="center">
          <el-col :span="12">
            <h1>
              <font color="grey">路线规划</font>
            </h1>
            <!-- 表单，防止表单默认提交行为 -->
            <el-form @submit.native.prevent="handleSubmit">
              <!-- 起始地点输入框，自动完成 -->
              <el-form-item label="起始地点">
                <el-autocomplete v-model="startLocation" :fetch-suggestions="querySearch" placeholder="请输入起始地点"
                  @select="handleSelect" required>
                </el-autocomplete>
              </el-form-item>
              <!-- 途径地点输入框，自动完成，动态添加或删除 -->
              <el-form-item v-for="(waypoint, index) in waypoints" :key="index" :label="'途径地点 ' + (index + 1)">
                <el-autocomplete v-model="waypoints[index]" :fetch-suggestions="querySearch" placeholder="请输入途径地点"
                  @select="handleSelect">
                  <template slot="append">
                    <el-button type="danger" icon="el-icon-delete" @click.prevent="removeWaypoint(index)">移除</el-button>
                  </template>
                </el-autocomplete>
              </el-form-item>
              <!-- 交通方式选择 -->
              <el-form-item label="交通方式">
                <el-radio-group v-model="transportMode">
                  <el-radio :label="0">步行</el-radio>
                  <el-radio :label="1">电动车</el-radio>
                </el-radio-group>
              </el-form-item>
              <!-- 添加途径地点按钮和提交表单按钮 -->
              <el-form-item>
                <el-button type="success" icon="el-icon-plus" @click.prevent="addWaypoint">添加途径地点</el-button>
                <el-button type="primary" @click.prevent="handleSubmit('shortestPath')">生成最短路径解决方案</el-button>
                <el-button type="info" @click.prevent="handleSubmit('shortestTime')">生成最短时间解决方案</el-button>
              </el-form-item>
            </el-form>
            <!-- 路线规划结果展示区 -->
            <el-dialog title="路线规划结果" :visible.sync="dialogVisible" width="80%" :before-close="handleClose">
              <div v-if="routePlan">
                <l-map :zoom="20" :center="mapCenter" style="height: 400px; width: 100%">
                  <!-- 地图图层 -->
                  <l-tile-layer :url="tileUrl" />
                  <!-- 路线 -->
                  <l-polyline :lat-lngs="pathPoints"></l-polyline>
                  <!-- 路线点标记 -->
                  <l-marker v-for="(point, index) in pathPoints" :key="index" :lat-lng="point">
                    <l-popup>{{ routePlan.steps[index] }}</l-popup>
                  </l-marker>
                  <!-- 路线装饰器（有向路径） -->
                  <polyline-decorator :patterns="patterns" />
                </l-map>
                <h2>路线规划结果</h2>
                <p v-html="formattedSteps"></p>
                <p>总距离: {{ routePlan.totalDistance.toFixed(2) }} 米</p>
                <p>总时间: {{ routePlan.totalTime.toFixed(2) }} 秒</p>
              </div>
              <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">关闭</el-button>
              </span>
            </el-dialog>
          </el-col>
        </el-row>
      </div>
      <button @click="navigateToMap" class="exit-button">退出</button>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import 'leaflet.markercluster/dist/MarkerCluster.css';
import 'leaflet.markercluster/dist/MarkerCluster.Default.css';
import { LMap, LTileLayer, LPolyline, LMarker, LPopup } from 'vue2-leaflet';
import 'leaflet-polylinedecorator';
import router from '../router'; // 确保正确导入Vue Router

export default {
  name: 'InnerMapComponent',
  components: {
    LMap, LTileLayer, LPolyline, LMarker, LPopup
  },
  data() {
    return {
      map: null,
      markerCluster: null,
      locations: [],
      searchQuery: '',
      filteredLocations: [],
      startLocation: '',     // 起始地点
      waypoints: [],         // 途径地点列表
      routePlan: null,       // 路线规划结果
      transportMode: 0,      // 默认为步行
      dialogVisible: false,  // 控制悬浮框的可见性
      pathPoints: [],        // 地图上的路径点
      mapCenter: [39.9042, 116.4074], // 默认地图中心点（北京）
      tileUrl: 'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', // 地图图层URL
      patterns: [], // 用于polyline-decorator的模式

      showFacilitiesDialog: false,
      facilitySearchLocation: '',
      facilityCategory: '',
      nearbyFacilities: [], // 存储从后端获取的设施数据

      currentLocationDialogVisible: false, // 控制位置通知对话框的显示
      currentLocationInfo: '', // 存储当前位置信息
    };
  },
  mounted() {
    this.initMap();


    // 获取 Vuex 中存储的当前位置并显示弹窗
    this.currentLocationInfo = this.$store.state.currentLocation;
    this.currentLocationDialogVisible = true; // 显示对话框
  },
  created() {
    this.fetchLocations(); // 组件创建时加载地点数据
  },
  computed: {
    // 格式化步骤，添加箭头并在适当位置换行
    formattedSteps() {
      if (!this.routePlan) return '';
      const steps = this.routePlan.steps.join(' → ');
      // 根据需要在适当的位置添加换行符
      return steps.replace(/( → [^→]* → [^→]*) → /g, '$1<br/>→ ');
    }
  },
  methods: {
    initMap() {
      this.map = L.map('map').setView([39.9611, 116.3519], 17);
      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
        attribution: '© OpenStreetMap'
      }).addTo(this.map);

      this.markerCluster = L.markerClusterGroup();
      this.map.addLayer(this.markerCluster);
    },

    fetchLocations() {
      axios.get('/api/innerlocations').then(response => {
        this.locations = response.data;
        this.addMarkers();
      }).catch(error => console.error('Error loading locations:', error));
    },



    fetchFacilities() {
      const requestData = {
        startLocationName: this.facilitySearchLocation,
        facilityType: this.facilityCategory
      };

      axios.post('/api/search-facilities', requestData)
        .then(response => {
          // 直接使用返回的数据，假设数据结构已正确
          this.nearbyFacilities = response.data;
        })
        .catch(error => {
          console.error('Error fetching facilities:', error);
        });
    },





    addMarkers() {
      this.markerCluster.clearLayers();
      this.locations.forEach(location => {
        const marker = L.marker([location.latitude, location.longitude]).bindPopup(`<b>${location.name}</b><br>Type: ${location.type}<br>Popularity: ${location.popularity}`);
        this.markerCluster.addLayer(marker);
      });
    },
    filterLocations() {
      this.filteredLocations = this.locations.filter(location =>
        location.name.toLowerCase().includes(this.searchQuery.toLowerCase())
      );
    },
    selectLocation(location) {
      this.searchQuery = location.name;
      this.filteredLocations = [];
      this.map.setView([location.latitude, location.longitude], 20);
    },
    searchLocation() {
      const location = this.locations.find(loc => loc.name === this.searchQuery);
      if (location) {
        this.map.setView([location.latitude, location.longitude], 20);
      } else {
        alert('未找到相关地点');
      }
    },
    navigateToMap() {
      router.push('/map');
    },

    // 查询匹配输入的地点名称
    querySearch(queryString, cb) {
      const results = this.locations.filter(location =>
        location.name.toLowerCase().includes(queryString.toLowerCase())
      );
      cb(results.map(location => ({ value: location.name })));
    },
    // 选择地点时的处理函数
    handleSelect(item) {
      console.log('Selected:', item);
    },
    // 添加途径地点
    addWaypoint() {
      this.waypoints.push('');
    },
    // 移除指定途径地点
    removeWaypoint(index) {
      this.waypoints.splice(index, 1);
    },
    // 提交表单时的处理函数，发送数据到后端以生成路线规划
    async handleSubmit(type) {
      const data = {
        startLocation: this.startLocation,
        waypoints: this.waypoints.filter(wp => wp.trim() !== ''),
        transportMode: this.transportMode
      };
      let apiUrl = type === 'shortestTime' ? '/api/innerplan1' : '/api/innerplan';
      try {
        const response = await axios.post(apiUrl, data);
        this.routePlan = response.data;
        this.pathPoints = this.getPathPoints(this.routePlan.steps); // 获取路径点
        this.mapCenter = this.pathPoints[0]; // 将地图中心设置为起点
        this.map.setView(this.mapCenter, 20);

        this.dialogVisible = true; // 显示悬浮框
        this.setPatterns(); // 设置路径装饰模式
      } catch (error) {
        console.error(`Error fetching route plan from ${apiUrl}:`, error);
        this.routePlan = null;
        this.pathPoints = [];
        this.dialogVisible = false; // 隐藏悬浮框
      }
    },
    // 根据步骤名称获取路径点的经纬度
    getPathPoints(steps) {
      return steps.map(step => {
        const location = this.locations.find(loc => loc.name === step);
        if (!location) return null;
        return [location.latitude, location.longitude];
      }).filter(point => point !== null);
    },
    // 设置路径装饰模式（有向路径的箭头）
    setPatterns() {
      this.patterns = [
        {
          offset: 10,
          repeat: 100,
          symbol: L.Symbol.arrowHead({
            pixelSize: 15,
            pathOptions: { color: 'blue', fillOpacity: 1, weight: 2 }
          })
        }
      ];
    },
    // 关闭悬浮框的处理函数
    handleClose() {
      this.dialogVisible = false;
    }
  }
};
</script>
<style scoped>
.content-wrapper {
  display: flex;
  height: 100vh;
  background-color: #f5f5f5;
  /* 轻微灰色背景 */
}

#map {
  width: 40vw;
  height: 100vh;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  /* 添加阴影 */
}

.controls-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding: 20px;
}

.search-container,
.route-planner {
  background: white;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  /* 更显著的阴影 */
  transition: all 0.3s ease;
  /* 平滑过渡效果 */
}

.search-input,
.search-button,
.exit-button {
  width: 100%;
  padding: 12px 15px;
  margin: 8px 0;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: all 0.2s ease;
  /* 输入框和按钮的过渡效果 */
}

.search-input:focus,
.search-button:hover,
.exit-button:hover {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
  /* 鼠标悬停时的阴影效果 */
}

.exit-button {
  background-color: #ff6347;
  /* Tomato color */
  color: white;
}

.suggestions-list {
  width: 100%;
  background: white;
  border: 1px solid #ddd;
  max-height: 200px;
  overflow-y: auto;
}

.suggestion-item {
  padding: 10px;
  transition: background-color 0.2s ease;
  /* 列表项背景过渡 */
}

.suggestion-item:hover {
  background-color: #f0f0f0;
}

.route-planner {
  margin-top: 20px;
}

.dialog-footer {
  text-align: right;
}

.current-location-dialog {
  /* 对话框的样式调整 */
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.current-location-content {
  /* 对话框内容的样式调整 */
  font-size: 16px;
  color: #333;
  padding: 15px;
}

.dialog-footer {
  /* 对话框底部按钮区域的样式调整 */
  text-align: right;
  padding: 10px 20px;
}

</style>
