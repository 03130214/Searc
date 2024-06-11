<template>
  <div class="background-image">
    <!-- <v-dialog v-model="fall" persistent max-width="300px">
      <v-card>
        <v-card-title class="text-h5">Warning</v-card-title>
        <v-card-text>出现了一些错误..</v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="green darken-1" text @click="fall = false">OK</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <v-dialog v-model="success" persistent max-width="300px">
      <v-card>
        <v-card-title class="text-h5">Success</v-card-title>
        <v-card-text>账号注册成功。</v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="green darken-1" text @click="issuccess()" >OK</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog> -->
    <div class="register-container">
      <h1 class="register">Register</h1>
      <form @submit.prevent="register">
        <div class="maininput">
          <div>
            <label for="username" class="label">Username:</label>
            <input id="username" v-model="username" type="text" required>
          </div>
          <div>
            <label for="email" class="label">Email:</label>
            <input id="email" v-model="email" type="email" required>
          </div>
          <div>
            <label for="password" class="label">Password:</label>
            <input id="password" v-model="password" type="password" required>
          </div>
        </div>
        <div>
          <button type="submit">Register</button>
          <button @click="changeRoute('/')">Cancel</button>
        </div>
        <p v-if="error" class="errormessage">{{ error }}</p>
      </form>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      username: '',
      email: '',  // Add email to the data properties
      password: '',
      error: '',
      fall: false,
      success: false
    };
  },
  methods: {
    issuccess(){
      this.success = false;
      this.$router.push('/login');
    },
    register() {
      this.error = '';  // Reset error message before a new attempt
      axios.post('/api/register', {
        username: this.username,
        email: this.email,  // Include email in the request
        password: this.password
      }).then(response => {
        if (response.data === "User registered successfully") {
          alert("User registered successfully");
          // this.success = true;
          this.$router.push('/login');
        } else {
          this.error = response.data.message;
          this.fall = true;
        }
      }).catch(error => {
        console.error('Error caught:', error);
        this.error = error.response && error.response.data.message ?
          error.response.data.message :
          'An error occurred during registration. Please try again.';
      });
    },
    changeRoute(route) {
      this.$router.push(route);
    }
  }
};
</script>

<style scoped>
.register {
  margin-left: 10vw;
  color: rgb(3, 139, 139);
}

.label {
  margin-left: 2vw;
}

.maininput {
  margin-top: 5vh;
}

.errormessage {
  color: #D8000C;
  background-color: #FFD2D2;
  border: 1px solid #D8000C;
  padding: 10px;
  margin: 10px 0;
  border-radius: 5px;
  font-size: 0.9em;
  text-align: center;
}

.background-image {
  display: flex;
  background-image: url('../assets/lonely.jpg');
  background-size: cover;
  height: 100vh;
  width: 100%;
  justify-content: center;
}

.register-container {
  background-color: rgba(255, 255, 255, 0.8);
  padding: 20px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  width: 30vw;
  height: 70vh;
  margin-top: 20vh;
}

input[type="text"],
input[type="email"],
input[type="password"] {
  border: 1px solid #000000;
  border-radius: 8px;
  background-color: white;
  width: 80%;
  padding: 8px;
  margin: 10px;
  margin-top: 1vh;
  margin-left: 2vw;
}

button {
  width: 60%;
  padding: 10px;
  background-color: #42b983;
  color: white;
  border: none;
  cursor: pointer;
  margin-top: 2vw;
  margin-left: 5vw;
}

button:hover {
  background-color: #368a77;
}
</style>
