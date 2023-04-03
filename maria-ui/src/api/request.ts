import axios from "axios";
import store from "@/store/index";
import router from "@/router/index";
import Vue from "vue";

const baseURL = () => {
  return "/api";
};

export const baseURLPrefix = () => {
  return baseURL();
};

const request = axios.create({
  baseURL: baseURL(),
  timeout: 3000,
  withCredentials: true
});

let flag: number;

request.interceptors.request.use(config => {
  if (store.state.token) {
    // eslint-disable-next-line @typescript-eslint/ban-ts-comment
    // @ts-ignore
    config.headers.common["Authorization"] = store.state.token;
    flag = 1;
  }
  return config;
}, error => {
  return Promise.reject(error);
});

request.interceptors.response.use(
  (res) => {
    return res;
  }, error => {
    const errorResponse = { ...error };
    console.log(errorResponse);
    if (errorResponse.response.status) {
      switch (errorResponse.response.status) {
        case 401:
          Vue.prototype.$confirm(errorResponse.response.data ? errorResponse.response.data : "认证失败, 请重新登录!", "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          });
          store.commit("delMenus");
          store.commit("delPerms");
          store.commit("delUser");
          store.commit("delToken");
          router.push({
            path: "/"
          }).then(r => {
            //
          });
          break;
        case 403:
          if (flag === 1) {
            Vue.prototype.$confirm(errorResponse.response.data ? errorResponse.response.data : "登录过期, 请重新登录!", "提示", {
              confirmButtonText: "确定",
              cancelButtonText: "取消",
              type: "warning"
            });
            store.commit("delMenus");
            store.commit("delPerms");
            store.commit("delUser");
            store.commit("delToken");
            router.push({
              path: "/"
            }).then(r => {
              //
            });
            flag = 0;
          }
          break;
        case 404:
          Vue.prototype.$confirm("请求无法处理, 请联系运维人员!", "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "error"
          });
          break;
        case 500:
          Vue.prototype.$confirm("服务器内部发生错误", "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "error"
          });
          break;
      }
    }
    return Promise.reject(errorResponse);
  });

export default request;