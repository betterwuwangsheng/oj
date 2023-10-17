import { RouteRecordRaw } from "vue-router";
import ACCESS_ENUM from "@/access/accessEnum";
import Login from "@/views/user/UserLoginView.vue";
import Register from "@/views/user/UserRegisterView.vue";
import UserInfo from "@/views/user/UserInfoView.vue";
import UserLayout from "@/layouts/UserLayout.vue";
import UserManageView from "@/views/user/UserManageView.vue";
import AddQuestionView from "@/views/question/AddQuestionView.vue";
import ManageQuestionView from "@/views/question/ManageQuestionView.vue";
import QuestionsView from "@/views/question/QuestionsView.vue";
import SolveQuestionsView from "@/views/question/SolveQuestionsView.vue";
import QuestionSubmitView from "@/views/question/QuestionSubmitView.vue";

// 通用路由文件
const routes: Array<RouteRecordRaw> = [
  {
    path: "/user",
    name: "用户",
    component: UserLayout,
    meta: {
      hideInMenu: true,
    },
    children: [
      { path: "/user/login", name: "用户登录", component: Login },
      { path: "/user/register", name: "用户注册", component: Register },
      { path: "/user/info", name: "用户信息", component: UserInfo },
    ],
  },
  { path: "/", name: "首页", component: QuestionsView },
  {
    path: "/question_submit",
    name: "已提交题目",
    component: QuestionSubmitView,
    meta: {
      access: ACCESS_ENUM.USER,
    },
  },
  {
    path: "/question/add",
    name: "创建问题",
    meta: { access: ACCESS_ENUM.USER },
    component: AddQuestionView,
  },
  {
    path: "/question/update",
    name: "更新问题",
    meta: {
      access: ACCESS_ENUM.USER,
      hideInMenu: true,
    },
    component: AddQuestionView,
  },
  {
    path: "/question/view/:id",
    name: "在线做题",
    meta: {
      access: ACCESS_ENUM.USER,
      hideInMenu: true,
    },
    props: true,
    component: SolveQuestionsView,
  },
  {
    path: "/question/manage",
    name: "管理问题",
    component: ManageQuestionView,
    meta: { access: ACCESS_ENUM.ADMIN },
  },
  {
    path: "/manage/user",
    name: "用户管理",
    component: UserManageView,
    meta: {
      access: ACCESS_ENUM.ADMIN,
    },
  },
];

export default routes;
