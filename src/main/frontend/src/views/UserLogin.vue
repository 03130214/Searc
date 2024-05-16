<template>
  <div class="background-image">
    <div class="login-container">
      <h1 class="login">Login</h1>
      <form @submit.prevent="performLogin">
        <div class="maininput">
          <div>
            <label for="username" class="label">Username:</label>
            <input id="username" v-model="username" type="text" required>
          </div>
          <div>
            <label for="password" class="label">Password:</label>
            <input id="password" type="password" v-model="password" required>
          </div>
        </div>
        <div>
          <button type="submit">Login</button>
          <button @click="changeRoute('/register')">Register</button>
        </div>
        <p v-if="error" class="errormessage">{{ error }}</p>
      </form>
    </div>
  </div>
</template>


<script>
import axios from 'axios';
import { mapActions } from 'vuex';

export default {
  data() {
    return {
      username: '',
      password: '',
      error: ''
    };
  },
  methods: {
    ...mapActions(['login']),
    performLogin() {
      this.error = '';  // Reset error message before a new login attempt
      axios.post('/api/login', {
        username: this.username,
        password: this.password
      }).then(response => {
        if (response.data === "Login successful. Redirecting to home.") {
          this.login({ username: this.username, isAuthenticated: true });
          this.$router.push('/home');  // Redirect to home route
        } else {
          this.error = response.data;  // Show the server returned error message
        }
      }).catch(error => {
        if (error.response && error.response.status === 401) {
          this.error = 'Invalid username or password.';
        } else {
          this.error = 'An error occurred. Please try again.';
        }
      });
    },
    changeRoute(route) {
    this.$router.push(route);
  }
  }
};
</script>

<style scoped>
.login {
  margin-left: 12vw;
  color: rgb(3, 139, 139);
}

.label {
  margin-left: 2vw;
}

.maininput {
  margin-top: 5vh;
}

.errormessage {
  color: #D8000C; /* Bright red color for visibility and importance */
  background-color: #FFD2D2; /* Light red background for contrast */
  border: 1px solid #D8000C; /* Red border to emphasize the error */
  padding: 10px; /* Padding to ensure text does not touch the edges */
  margin: 10px 0; /* Margin to separate the error message from other elements */
  border-radius: 5px; /* Rounded corners for a softer look */
  font-size: 0.9em; /* Slightly smaller text */
  text-align: center; /* Center the text within the error message box */
}

.background-image {
  display: flex;
  background-image: url('../assets/mainpage.jpg');
  background-size: cover;
  height: 98vh;
  width: 100%;
  justify-content: center;
}

.login-container {
  background-color: rgba(255, 255, 255, 0.8);
  padding: 20px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  width: 30vw;
  height: 60vh;
  margin-top: 20vh;
}

input[type="text"],
input[type="password"] {
  width: 80%;
  padding: 8px;
  margin: 10px;
  margin-top: 2vh;
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
