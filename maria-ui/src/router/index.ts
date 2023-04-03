import Vue from "vue";
import VueRouter, { RouteConfig } from "vue-router";

Vue.use(VueRouter);

const routes: Array<RouteConfig> = [
  {
    path: "/",
    redirect: "login"
  },
  {
    path: "/login",
    name: "Login",
    component: () => import("../views/login/Index.vue")
  },
  {
    path: "/frame",
    name: "Frame",
    redirect: "/index",
    component: () => import("../views/Index.vue"),
    children: [
      {
        path: "/index",
        name: "Index",
        component: () => import("../views/index/Index.vue")
      },
      {
        path: "/user",
        name: "User",
        component: () => import("../views/system/user/Index.vue")
      },
      {
        path: "/role",
        name: "ROle",
        component: () => import("../views/system/role/Index.vue")
      },
      {
        path: "/menu",
        name: "Menu",
        component: () => import("../views/system/menu/Index.vue")
      },
      {
        path: "/dictionaryType",
        name: "DictionaryType",
        component: () => import("../views/system/dictionaryType/Index.vue")
      },
      {
        path: "/dictionary",
        name: "Dictionary",
        component: () => import("../views/system/dictionary/Index.vue")
      },
      {
        path: "/config",
        name: "Config",
        component: () => import("../views/system/config/Index.vue")
      },
      {
        path: "/loginLog",
        name: "LoginLog",
        component: () => import("../views/log/login/Index.vue")
      },
      {
        path: "/operationLog",
        name: "OperationLog",
        component: () => import("../views/log/operation/Index.vue")
      },
      {
        path: "/runtimeLog",
        name: "RuntimeLog",
        component: () => import("../views/log/runtime/Index.vue")
      }
    ]
  }
];

const router = new VueRouter({
  // mode: "history",
  // base: process.env.BASE_URL,
  routes
});

export default router;
