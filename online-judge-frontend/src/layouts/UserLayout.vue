<template>
  <div id="user-layout">
    <a-layout style="min-height: 100vh">
      <a-layout-header class="header">
        <img src="../assets/oj-logo.svg" alt="online-judge" class="logo" />
      </a-layout-header>
      <a-divider direction="vertical" />
      <!--主体内容-->
      <a-layout-content class="content">
        <router-view />
      </a-layout-content>
      <a-layout-footer class="footer">
        <a-link href="https://github.com/betterwuwangsheng" icon>
          小王子的 github</a-link
        >
      </a-layout-footer>
    </a-layout>
  </div>
</template>

<script setup lang="ts"></script>

<style scoped>
#user-layout {
  text-align: center;
  background: url("../assets/login-background.png") 0% 0% /100% 100%;
}

#user-layout .header {
}

#user-layout .logo {
  width: 200px;
}

#user-layout .content {
  margin-bottom: 16px;
  padding: 20px;
}

#user-layout .footer {
  background: #efefef;
  padding: 16px;
  /*绝对定位*/
  position: sticky;
  bottom: 0;
  left: 0;
  right: 0;
  text-align: center;
}
</style>
