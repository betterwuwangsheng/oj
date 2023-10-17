// Add a request interceptor
// 全局请求拦截器
import axios from "axios";

// 添加请求拦截器
axios.interceptors.request.use(
  function (config: any) {
    // 在发送请求之前做些什么
    return config;
  },
  function (error: any) {
    // 对请求错误做些什么
    return Promise.reject(error);
  }
);
// 携带token方式登录
// axios.interceptors.request.use(
//   (config) => {
//     // 假设token存在localStorage中
//     const token = localStorage.getItem("token");
//     console.log("token:", token);
//     if (token) {
//       config.headers.Authorization = token;
//     }
//     return config;
//   },
//   (error) => {
//     return Promise.reject(error);
//   }
// );

// 添加响应拦截器
axios.interceptors.response.use(
  function (response: any) {
    // console.log("全局响应: ", response);
    // 2xx 范围内的状态码都会触发该函数。
    // 对响应数据做点什么
    return response;
  },
  function (error: any) {
    // 超出 2xx 范围的状态码都会触发该函数。
    // 对响应错误做点什么
    return Promise.reject(error);
  }
);
