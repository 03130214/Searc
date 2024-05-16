<template>
    <div class="background-image">
      <el-container class="center-container">
        <el-main class="center-content">
          <h1>Welcome to Searc</h1>
          <el-button type="primary" @click="$router.push('/login')">Log In</el-button>
          <el-button type="primary" @click="$router.push('/register')">Register</el-button>
          <router-view/>
        </el-main>
      </el-container> 
    </div>
  </template>
  
  <style>
  .background-image {   
    background-image: url('../assets/mainpage.jpg'); /* Replace with your image path */
    background-size:cover;
    background-position: center;
    height: 98vh;
    width: 100%;
  }
  
  .center-container {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
}

  .center-content {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    text-align: center;
    background-color: rgb(235, 235, 235, 0.5);
  }
  
  h1 {
    margin-bottom: 20px; /* Adjust spacing as needed */
    color: white; /* Adjust color as needed */
    font-size: 2em; /* Adjust font size as needed */
  }
  
  .el-button {
    margin: 10px 0; /* Spacing between buttons */
  }
  </style>
  