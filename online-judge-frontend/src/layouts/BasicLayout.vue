<template>
  <div id="basic-layout">
    <a-layout style="min-height: 100vh">
      <a-layout-header class="header">
        <!--导航栏-->
        <GlobalHeader />
      </a-layout-header>

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

<script setup lang="ts">
import GlobalHeader from "@/components/GlobalHeader.vue";
</script>

<style scoped>
#basic-layout {
}

#basic-layout .header {
  box-shadow: #eee 1px 1px 5px;
}

#basic-layout .content {
  margin-bottom: 16px;
  padding: 20px;
}

#basic-layout .footer {
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
