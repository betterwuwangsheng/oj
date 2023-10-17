<template>
  <div class="user-login">
    <h1 style="margin: 32px 0">用户登录</h1>
    <!--登录表单-->
    <a-form
      :model="form"
      @submit="handleLoginFormSubmit"
      label-align="left"
      auto-label-width
      style="max-width: 480px; margin: 0 auto"
      :rules="[
        { required: true, message: '账号不能为空' },
        { minLength: 4, message: '账号长度不能低于四位' },
      ]"
    >
      <!--用户账号-->
      <a-form-item field="userAccount" tooltip="请输入账号！" label="账号：">
        <a-input v-model="form.userAccount" placeholder="请输入账号" />
      </a-form-item>

      <!--密码-->
      <a-form-item
        field="userPassword"
        label="密码："
        tooltip="密码不少于 8 位！"
        :rules="[
          { required: true, message: '密码不能为空' },
          { minLength: 6, message: '密码长度不能低于六位' },
        ]"
      >
        <a-input-password
          v-model="form.userPassword"
          placeholder="请输入密码"
        />
      </a-form-item>

      <!--提交-->
      <a-form-item>
        <a-button
          html-type="submit"
          style="width: 120px; margin: 0 auto"
          type="primary"
        >
          登录</a-button
        >
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import { Message } from "@arco-design/web-vue";
import { reactive } from "vue";
import { UserControllerService, UserLoginRequest } from "../../../backapi";
import store from "@/store";

import { useRouter } from "vue-router";

const router = useRouter();

const form = reactive({
  userAccount: "",
  userPassword: "",
} as UserLoginRequest);

/**
 * 登录提交事件
 */
const handleLoginFormSubmit = async () => {
  // 执行登录请求
  const res = await UserControllerService.userLoginUsingPost(form);
  if (res.code === 0) {
    // 获取登录用户
    await store.dispatch("user/getLoginUser");
    Message.success(
      `登录成功！欢迎${
        res.data.userName ? `【${res.data.userName}】` : ""
      } 来到 Online-Judge 系统`
    );

    // 登录成功跳转到主页
    await router.push({
      path: "/",
      replace: true,
    });
  } else {
    Message.error("登录失败，" + res.message);
  }
};
</script>

<style scoped></style>
