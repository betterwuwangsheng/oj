// 用户模块
// 初始化 state
import { StoreOptions } from "vuex";
import ACCESS_ENUM from "@/access/accessEnum";
import { UserControllerService } from "../../../backapi";

export default {
  namespaced: true,
  state: () => ({
    loginUser: {
      userName: "未登录!",
    },
  }),
  actions: {
    async getLoginUser({ commit, state }) {
      // 从远程请求获取登录信息
      const res = await UserControllerService.getLoginUserUsingGet();

      // 请求成功
      if (res.code === 0) {
        // 更新 vuex 中的状态信息
        commit("updateUser", res.data);
      } else {
        // 没有查找到登录用户
        commit("updateUser", {
          ...state.loginUser,
          userRole: ACCESS_ENUM.NOT_LOGIN,
        });
      }
    },
  },
  mutations: {
    updateUser(state, payload) {
      state.loginUser = payload;
    },
  },
} as StoreOptions<any>;
