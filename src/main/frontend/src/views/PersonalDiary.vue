<template>
    <div class="content-wrapper">
        <SideBox />
        <!-- <v-dialog v-model="dialog" persistent max-width="300px">
            <v-card>
                <v-card-title class="text-h5">Warning</v-card-title>
                <v-card-text>请选择至少一篇日记。</v-card-text>
                <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn color="green darken-1" text @click="dialog = false">OK</v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>
        <v-dialog v-model="success" persistent max-width="300px">
            <v-card>
                <v-card-title class="text-h5">Success</v-card-title>
                <v-card-text>日记删除成功。</v-card-text>
                <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn color="green darken-1" text @click="success = false">OK</v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog> -->
        <div class="main-content">
            <div class="left-content">
                <div v-for="diary in diaries" :key="diary.id" class="diary-item" @dblclick="goToDiaryDetails(diary.id)">
                    <input class="choose" type="checkbox" :value="diary.id" v-model="selectedDiaries">
                    <div class="txtcontent">
                        <h2>{{ diary.title }}<span class="tag">{{ diary.location }}</span></h2>
                        <p class="content-preview">{{ formattedContent(diary.content) }}</p>
                        <p><strong>views:</strong> {{ diary.hot }} <strong>likes:</strong> {{ diary.likes }}</p>
                    </div>
                </div>
            </div>
            <div class="right-content">
                <div class="personal">
                    <button class="user"></button>
                    <button title="创建日记" class="circle-button" @click="$router.push('/editdiary')">✏️</button>
                    <button title="搜索日记" class="circle-button" @click="$router.push('/checkdiary')">🔍</button>
                    <button title="删除选中的日记" class="circle-button" @click="deleteSelectedDiaries()">🗑️</button>
                    <button title="返回" class="circle-button" @click="$router.push('/diary')">🔙</button>
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
            username: '',
            diaries: [],
            selectedDiaries: [],
            dialog: false,
            success: false
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
            return content.length > 40 ? content.slice(0, 40) + '...' : content;
        },
        changeRoute(route) {
            this.$router.push(route);
        },
        fetchDiaries() {
            axios.get('/api/diaries/by-user', { params: { username: this.username1 } })
                .then(response => {
                    this.diaries = response.data;
                })
                .catch(error => {
                    console.error('Failed to fetch diaries:', error);
                    this.diaries = [];
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
        },
        deleteSelectedDiaries() {
            if (this.selectedDiaries.length === 0) {
                // this.dialog = true;
                alert("请选择至少一篇日记");
                return;
            }
            axios.post('/api/diaries/delete', this.selectedDiaries)
                .then(() => {
                    this.fetchDiaries();
                    this.selectedDiaries = [];
                    this.success = true;
                })
                .catch(error => {
                    console.error('Failed to delete diaries:', error);
                });
        }
    }
};
</script>

<style scoped>
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
    border-radius: 50%;
    width: 70px;
    height: 70px;
    border: none;
    font-size: 20px;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-top: 1vh;
    transition: background-color 0.3s;
}

.circle-button {
    border-radius: 50%;
    width: 60px;
    height: 60px;
    border: none;
    font-size: 20px;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    transition: background-color 0.3s;
}

.circle-button:hover {
    background-color: #b8b6b6;
    /* 悬浮时背景色 */
}

.personal {
    padding: 10px;
    width: 60%;
    height: 50vh;
    background-color: rgba(247, 247, 247, 0.9);
    border: 1px solid #ccc;
    border-radius: 5px;
    margin: 5vh;
    position: relative;
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 20px;
}

.content-wrapper {
    display: flex;
    height: 100vh;
}

.main-content {
    display: flex;
    flex-grow: 1;
    padding: 20px;
}

.left-content {
    overflow-y: auto;
    width: 60vw;
    flex: 4;
    padding: 20px;
    background-color: rgba(247, 247, 247, 0.9);
    border: 1px solid #ccc;
    border-radius: 5px;
}

.right-content {
    flex: 1;
    padding: 20px;
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