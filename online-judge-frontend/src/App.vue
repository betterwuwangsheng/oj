<template>
  <div id="app">
    <!--以 /user 开头的路径: 直接使用 vue-router 的默认逻辑【路由文件中配置的】 UserLayout.vue-->
    <template v-if="route.path.startsWith('/user')">
      <RouterView />
    </template>
    <!--其他路由-->
    <template v-else>
      <BasicLayout />
    </template>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from "vue";
import { useRoute } from "vue-router";
import BasicLayout from "@/layouts/BasicLayout.vue";

const route = useRoute();

// 全局初始化函数，有全局单词调用的代码都可以写在这里
const doInit = () => {
  console.log("doInit()");
};

// dom 渲染完毕后执行
onMounted(() => {
  doInit();
});
</script>

<style>
#app {
}
</style>
