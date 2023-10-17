<template>
  <div id="home">
    <MdEditor :value="value" :handle-change="handleChange" />
    <CodeEditor />
    <div class="home">题目</div>
  </div>
</template>

<script setup lang="ts">
import MdEditor from "@/components/MdEditor.vue";
import { ref } from "vue";
import CodeEditor from "@/components/CodeEditor.vue";

const value = ref("");
const handleChange = (v: string) => {
  value.value = v;
};
</script>

<style scoped></style>
