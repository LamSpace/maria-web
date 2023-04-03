import Vue from "vue";
import "./plugins/axios.js";
import ElementUI from "element-ui";
import "element-ui/lib/theme-chalk/index.css";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import "./assets/css/global.css";
import * as echarts from "echarts";
import mavonEditor from "mavon-editor";
import "mavon-editor/dist/css/index.css";

Vue.config.productionTip = false;
Vue.use(ElementUI);
Vue.use(mavonEditor);

Vue.prototype.$echarts = echarts;

new Vue({
  router,
  store,
  render: (h) => h(App)
}).$mount("#app");

Vue.directive("hasPermission", {
  bind(el, binding, vnode) {
    let flag = false;
    // let user = vnode.context?.$store.state.user;
    const perms = vnode.context?.$store.state.perms;
    const value = binding.value;
    if (perms.indexOf(value) !== -1) {
      flag = true;
    }
    if (!flag) {
      el.setAttribute("disabled", String(true));
      el.classList.add("is-disabled");
    }
  }
});