<template>
    <div class="diary-container">
        <button class="back-button" @click="changeRoute('/diary')">&#8592;</button>
        <h1 class="header">写日记</h1>
        <form @submit.prevent="saveDiary" class="diary-form">
            <label for="title" class="label">标题：</label>
            <input type="text" id="title" v-model="title" class="input" />
            <label for="location" class="label">地点：</label>
            <input type="text" id="location" v-model="location" class="input" />
            <br />
            <label for="content" class="label">内容：</label>
            <textarea id="content" v-model="content" class="textarea"></textarea>
            <br />
            <button type="submit" class="button">保存</button>
            <button class="button_cancel" @click="changeRoute('/diary')">取消</button>
        </form>
    </div>
</template>

<script>
import axios from 'axios';
import { mapState } from 'vuex';

export default {
    data() {
        return {username:'',
            title: '',
            content: '',
            location:''
        };
    },
    computed: {
        ...mapState({
            username1: state => state.username
        })
    },
    methods: {
        changeRoute(route) {
            this.$router.push(route);
        },
        saveDiary() {
            const diary = {
                username: this.username1,  // 将用户名添加到日记对象中
                title: this.title,
                content: this.content,
                location: this.location
            };
            console.log('Sending diary data:', diary);
            axios.post('/api/diaries', diary)
                .then(response => {
                    // 保存成功后的操作
                    console.log('日记保存成功：', response.data);
                    alert("日记创建成功！");
                    // 重置表单字段
                    this.title = '';
                    this.content = '';
                    this.changeRoute('/diary');
                })
                .catch(error => {
                    console.error('保存日记失败：', error);
                });
        }
    }
};
</script>

<style scoped>
.diary-container {
    max-width: 800px;
    max-height: 800px;
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
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #c3c4c6;
}

.back-button:hover {
  color: #0056b3;
}
.diary-form {
    /* display: flex; */
    flex-direction: column;
}

.label {
    width: 40%;
    font-size: 16px;
    margin-bottom: 5px;
    margin: 5px;
    color: #555;
}

.input {
    width: 40%;
    max-height: 300px;
    padding: 10px;
    margin-bottom: 15px;
    border: 1px solid #ccc;
    border-radius: 4px;
    font-size: 14px;
}

.textarea {
    width: 95%;
    max-height: 300px;
    padding: 10px;
    margin-left: 1vw;
    margin-bottom: 15px;
    border: 1px solid #ccc;
    border-radius: 4px;
    font-size: 14px;
}

.input:focus,
.textarea:focus {
    border-color: #007BFF;
    outline: none;
    box-shadow: 0 0 5px rgba(0, 123, 255, 0.5);
}

.textarea {
    resize: vertical;
    min-height: 100px;
}

.button {
    padding: 10px 15px;
    font-size: 16px;
    margin-left: 24vw;   
    color: #fff;
    background-color: #007BFF;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    transition: background-color 0.3s ease;
}
.button_cancel {
    padding: 10px 15px;
    font-size: 16px;
    color: #000000;
    background-color: #ffffff;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    transition: background-color 0.3s ease;
}
.button_cancel:hover {
    background-color: #d0d0d0;
}
.button:hover {
    background-color: #0056b3;
}
</style>