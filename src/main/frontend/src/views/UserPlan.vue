<template>
  <div class="content-wrapper">
    <SideBox />
    <div class="route-planner">
      <el-row type="flex" justify="center">
        <el-col :span="12">
          <h1>路线规划</h1>
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
            <!-- 添加途径地点按钮和提交表单按钮 -->
            <el-form-item label="交通方式">
              <el-radio-group v-model="transportMode">
                <el-radio :label="0">步行</el-radio>
                <el-radio :label="1">电动车</el-radio>
              </el-radio-group>
            </el-form-item>

            <el-form-item>
              <el-button type="success" icon="el-icon-plus" @click.prevent="addWaypoint">添加途径地点</el-button>
              <el-button type="primary" native-type="submit">生成解决方案</el-button>
            </el-form-item>
          </el-form>
          <!-- 路线规划结果展示区 -->
          <el-dialog title="路线规划结果" :visible.sync="dialogVisible" width="50%" :before-close="handleClose">
            <div v-if="routePlan">
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
  </div>
</template>

<script>
import axios from 'axios';
import SideBox from '../components/SideBox.vue';
export default {
  components: {
    SideBox
  },
  data() {
    return {
      startLocation: '',     // 起始地点
      waypoints: [],         // 途径地点列表
      routePlan: null,       // 路线规划结果
      locations: [],         // 存储从服务器加载的所有地点数据
      transportMode: 0,      // 默认为步行
      dialogVisible: false   // 控制悬浮框的可见性
    };
  },
  created() {
    this.loadLocations(); // 组件创建时加载地点数据
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
    // 加载所有地点数据
    loadLocations() {
      axios.get('/api/map').then(response => {
        this.locations = response.data;
      }).catch(error => console.error('Error loading locations:', error));
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
    async handleSubmit() {
      const data = {
        startLocation: this.startLocation,
        waypoints: this.waypoints.filter(wp => wp.trim() !== ''),
        transportMode: this.transportMode
      };
      try {
        const response = await axios.post('/api/plan', data);
        this.routePlan = response.data;
        this.dialogVisible = true; // 显示悬浮框
      } catch (error) {
        console.error('Error fetching route plan:', error);
        this.routePlan = null;
        this.dialogVisible = false; // 隐藏悬浮框
      }
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
  /* 视图高度全屏 */
}
.route-planner {
  width: 60vw;
  padding: 20px;
}

.dialog-footer {
  text-align: right;
}
</style>
