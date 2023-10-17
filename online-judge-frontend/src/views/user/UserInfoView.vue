<template>
  <div id="user-info">
    <!--描述列表-->
    <a-descriptions-item>
      <a-avatar
        :size="100"
        shape="circle"
        style="margin-left: -40px"
        :imageUrl="loginUser.userAvatar"
      ></a-avatar>
    </a-descriptions-item>

    <!--Card区域-->
    <a-card title="我的信息" style="text-align: center; margin-top: 20px">
      <a-descriptions :data="userInfo" size="large" column="1" bordered />
      <template #extra>
        <a-badge status="success" text="在线" />
      </template>
    </a-card>

    <!--对话框-->
    <a-modal
      width="30%"
      v-model:visible="visible"
      placement="right"
      @ok="handleOk"
      @cancel="closeModel"
      unmountOnClose
    >
      <!--头像-->
      <div style="text-align: center">
        <!--文件上传-->
        <a-upload
          action="/"
          :fileList="file ? [file] : []"
          :show-file-list="false"
          @change="onChange"
          :custom-request="uploadAvatar"
        >
          <template #upload-button>
            <div
              class="arco-upload-list-picture custom-upload-avatar"
              v-if="updateForm.userAvatar"
            >
              <a-avatar :size="70" shape="circle">
                <img alt="头像" :src="userAvatarImg" />
                <template #trigger-icon>
                  <IconEdit :size="15" />
                </template>
              </a-avatar>
              >
            </div>
          </template>
        </a-upload>
      </div>

      <a-space></a-space>

      <!--表单-->
      <a-form
        :model="loginUser"
        label-align="right"
        title="个人信息"
        style="max-width: 480px; margin: 0 auto"
      >
        <a-form-item field="用户名称" label="用户名称 :">
          <a-input v-model="updateForm.userName" placeholder="请输入用户名称" />
        </a-form-item>
        <a-form-item field="邮箱" label="邮箱 :">
          <a-input v-model="updateForm.email" placeholder="请输入邮箱" />
        </a-form-item>
        <a-form-item field="电话" label="电话 :">
          <a-input v-model="updateForm.phone" placeholder="请输入电话号码" />
        </a-form-item>
        <a-form-item field="userProfile" label="简介 :">
          <a-textarea
            v-model="updateForm.userProfile"
            placeholder="请输入简介"
          />
        </a-form-item>
      </a-form>
    </a-modal>

    <!--按钮-->
    <div>
      <a-button
        shape="round"
        status="success"
        size="small"
        type="outline"
        style="margin: 10px"
      >
        <a-link @click="toIndex">首页</a-link>
      </a-button>
      <a-button
        shape="round"
        status="normal"
        size="medium"
        type="outline"
        style="margin: 10px"
        @click="openModalForm"
      >
        修改
      </a-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useStore } from "vuex";
import moment from "moment";
import { useRouter } from "vue-router";
import { ref } from "vue";
import { UserControllerService, UserUpdateMineRequest } from "../../../backapi";
import { Message } from "@arco-design/web-vue";
import USER_STATE_ENUM from "@/access/userStateEnum";

const router = useRouter();
const visible = ref(false);
const file = ref();

/**
 * 获取用户信息
 */
const store = useStore();
let loginUser = store.state.user.loginUser;

const userInfo = [
  { label: "用户名称：", value: loginUser.userName },
  { label: "账号名称：", value: loginUser.userAccount },
  {
    label: "我的简介：",
    value: loginUser.userProfile ? loginUser.userProfile : "暂无简介",
  },
  {
    label: "用户角色：",
    value: loginUser.userRole === "user" ? "普通用户" : "管理员",
  },
  {
    label: "邮箱：",
    value: loginUser.email !== "" ? loginUser.email : "未填写",
  },
  {
    label: "电话：",
    value: loginUser.phone !== "" ? loginUser.phone : "未填写",
  },
  {
    label: "当前状态：",
    value: USER_STATE_ENUM[loginUser.userState as keyof typeof USER_STATE_ENUM],
  },

  {
    label: "创建时间：",
    value: moment(loginUser.createTime).format("YYYY-MM-DD HH:mm:ss"),
  },
  {
    label: "修改时间：",
    value: moment(loginUser.updateTime).format("YYYY-MM-DD HH:mm:ss"),
  },
];

/**
 * 更新表单
 */
const updateForm = ref<UserUpdateMineRequest>({
  ...store.state.user?.loginUser,
});

/**
 * 从表单中获取的用户头像
 */
let userAvatarImg = updateForm.value.userAvatar;

/**
 * 上传头像
 */
const uploadAvatar = () => {
  Message.success("上传成功，点击确认即可修改头像");
};

/**
 * 跳转到首页
 */
const toIndex = () => {
  router.push({
    path: "/",
  });
};

/**
 * 对话框
 */
const openModalForm = () => {
  visible.value = true;
};

const onChange = () => {
  alert("onChange");
};
// const onProgress = () => {
//   alert(onProgress);
// };

/**
 * 确定修改按钮
 */
const handleOk = async () => {
  const res = await UserControllerService.updateMyUserUsingPost({
    ...updateForm.value,
    userAvatar: userAvatarImg,
  });
  if (res.code === 0) {
    Message.success("更新成功！");
    visible.value = false;
    location.reload();
  } else {
    Message.error("更新失败！", res.msg);
  }
};

/**
 * 关闭对话框
 */
const closeModel = () => {
  visible.value = false;
};
</script>

<style scoped>
#user-info {
  margin: 0 auto;
  padding: 10px;
  max-width: 1000px;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(35, 7, 7, 0.21);
}
</style>
