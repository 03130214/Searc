<template>
  <div class="background-image">
    <div class="register-container">
      <h1 class="register">Register</h1>
      <form @submit.prevent="register">
        <div class="maininput">
          <div>
            <label for="username"  class="label">Username:</label>
            <input id="username" v-model="username" type="text" required>
          </div>
          <div>
            <label for="email"  class="label">Email:</label>
            <input id="email" v-model="email" type="email" required>
          </div>
          <div>
            <label for="password"  class="label">Password:</label>
            <input id="password" v-model="password" type="password" required>
          </div>
        </div>
        <div>
          <button type="submit">Register</button>
          <button @click="changeRoute('/')">Cancel</button>
        </div>
        <p v-if="error"  class="errormessage">{{ error }}</p>
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
      error: ''
    };
  },
  methods: {
    register() {
      console.log('Register method is called');
      this.error = '';  // Reset error message before a new attempt
      axios.post('/api/register', {
        username: this.username,
        email: this.email,  // Include email in the request
        password: this.password
      }).then(response => {
        if (response.data === "User registered successfully") {
          this.$router.push('/login');  // Redirect to login on successful registration
          alert('Registration successful. You can now log in.');
        } else {
          this.error = response.data.message;  // Show error from server
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
  background-image: url('../assets/mainpage.jpg');
  background-size: cover;
  height: 98vh;
  width: 100%;
  justify-content: center;
}

.register-container {
  background-color: rgba(255, 255, 255, 0.8);
  padding: 20px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  width: 30vw;
  height: 60vh;
  margin-top: 20vh;
}

input[type="text"],
input[type="email"],
input[type="password"] {
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
