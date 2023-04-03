<template>
  <el-container class="main_container">
    <el-header>
      <div class="left_box">
        <span @click="collapse" class="systemName">{{ systemName }}</span>
      </div>

      <div class="right_box">
        <el-dropdown>
          <img src="../../public/avatar.png" alt="">
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item icon="el-icon-s-custom">
              <span @click="updateUserInfo">修改信息</span>
            </el-dropdown-item>
            <el-dropdown-item icon="el-icon-key">
              <span @click="updateUserPwd">修改密码</span>
            </el-dropdown-item>
            <el-dropdown-item icon="el-icon-lock">
              <span @click="lockScreen">锁屏休息</span>
            </el-dropdown-item>
            <el-dropdown-item icon="el-icon-switch-button">
              <span @click="logout">退出登录</span>
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </el-header>

    <el-container>
      <el-aside :width="isCollapsed ? '64px' : '200px'">
        <el-menu class="el-menu-vertical-demo" @open="handleOpen"
                 @close="handleClose" background-color="#0F6076" text-color="#fff"
                 active-text-color="#ffd04b" :collapse="isCollapsed" :collapse-transition="false"
                 :unique-opened="true" :router="true" :default-active="activePath">
          <MenuTree :menu-list="this.menuList" />
        </el-menu>
      </el-aside>
      <el-main>
        <!--路由子页面-->
        <router-view />
      </el-main>

      <!-- 用户锁屏 -->
      <el-drawer :visible.sync="screenLock" direction="ttb" size="100%" :with-header="false"
                 :close-on-press-escape="false" :modal="false" :show-close="false">
        <div style="width: 400px; position: fixed; top: 30%; left: 50%; transform: translateX(-50%)">
          <el-input v-model="pwd" placeholder="请输入密码" type="password"
                    @keyup.enter.native="triggerUnlock" />
          <div style="position:fixed; left: 60%; transform: translate(-100%, 100%)">
            <el-button type="primary" @click="screenUnlock" ref="unlockScreen">解锁
            </el-button>
          </div>
        </div>
      </el-drawer>

    </el-container>
  </el-container>
</template>

<script>
import MenuTree from "@/components/MenuTree.vue";
import { doLogout, doVerifyPwd } from "@/api/login/index";

export default {
  name: "Index",
  data() {
    return {
      systemName: "Maria",
      active: 0,
      user: {
        id: -1,
        username: "",
        nickname: "",
        email: "",
        phone: "",
        gender: "",
        birth: ""
      },
      isCollapsed: false,
      activePath: "",
      menuList: this.$store.state.menus,
      screenLock: false,
      pwd: ""
    };
  },
  components: { MenuTree },
  created() {
    // created here
  },
  mounted() {
    if ("WebSocket" in window) {
      // eslint-disable-next-line @typescript-eslint/no-this-alias
      let that = this;
      // 使用这种方式则 websocket 连接可以被 Vue 拦截并转发至 vue.config.js 配置的地址上, 进而进入网关实现路由转发。
      // noinspection JSValidateTypes
      const url = `ws://` + window.location.host + `/socketApi/socket?token=` + this.$store.state.socketToken;
      // noinspection JSValidateTypes
      this.$store.state.socket = new WebSocket(url);
      this.$store.state.socket.onopen = function() {
        console.log("浏览器已成功连接 WebSocket 服务器.");
      };
      this.$store.state.socket.onclose = function() {
        console.log("WebSocket 服务器断开连接.");
      };
      this.$store.state.socket.onerror = function(error) {
        console.log("浏览器与 WebSocket 服务器连接发生错误, 原因: " + JSON.stringify(error));
      };
      this.$store.state.socket.onmessage = function(event) {
        that.doOnMessage(event.data);
      };
    } else {
      console.log("当前浏览器不支持 WebSocket 通信.");
    }
  },
  methods: {
    triggerUnlock() {
      this.$refs.unlockScreen.$emit("click");
    },
    collapse() {
      this.isCollapsed = !this.isCollapsed;
    },
    handleOpen(key, keyPath) {
      // 菜单展开
    },
    handleClose(key, keyPath) {
      // 菜单关闭
    },
    logout() {
      doLogout().then(res => {
        if (res.data.code === 200) {
          this.$message({
            message: res.data.message,
            type: "success"
          });
          this.$router.push("/");
        } else {
          this.$message({
            message: res.data.message,
            type: "error"
          });
        }
      }).finally(() => {
        this.$store.commit("delToken");
        this.$store.commit("delUser");
        this.$store.commit("delPerms");
        this.$store.commit("delMenus");
        this.$store.state.socket.close();
        this.$store.state.socket = null;
      });
    },
    lockScreen() {
      this.screenLock = true;
    },
    // eslint-disable-next-line @typescript-eslint/no-empty-function
    updateUserPwd() {
    },
    // eslint-disable-next-line @typescript-eslint/no-empty-function
    updateUserInfo() {
    },
    screenUnlock() {
      doVerifyPwd(this.$store.state.user.id, this.pwd).then(res => {
        if (res.data.code === 200) {
          if (res.data.data.equals) {
            this.$message({
              message: "解锁成功",
              type: "success"
            });
            this.screenLock = false;
            this.pwd = "";
          } else {
            this.$message({
              message: "密码错误, 请重新输入",
              type: "error"
            });
            this.pwd = "";
          }
        }
      }).catch(error => console.log(error));
    },
    // WebSocket 消息处理逻辑
    doOnMessage(json) {
      try {
        let message = { ...JSON.parse(json) };
        this.$notify({
          title: "提示",
          message: message.value,
          position: "bottom-right"
        });
      } catch (e) {
        this.$notify({
          title: "提示",
          message: json,
          position: "bottom-right"
        });
      }
    }
  }
};
</script>
<style lang="less" scoped>
.main_container {
  height: 100%;
}

.el-header {
  background-color: #0F6076;
  display: flex;
  justify-content: space-between;
  color: #FFFFFF;
  align-items: center;
  font-size: 20px;

  .left_box {
    display: flex;
    align-items: center;

    img {
      width: 60px;
      height: 60px;
    }

    span {
      margin-left: 15px;
    }
  }

  .right_box {
    .el-dropdown > img {
      width: 60px;
      height: 60px;
      border-radius: 50%;
      background-color: white;
      margin: 0 15px 0 0;
      background-size: contain;
    }
  }
}

.el-aside {
  background-color: #0F6076;

  .el-menu {
    border-right: none;
  }

  .toggle_box {
    background-color: #0F6076;
    font-size: 15px;
    font-weight: bold;
    line-height: 25px;
    color: white;
    letter-spacing: 0.2em;
    text-align: center;
    cursor: pointer;
  }
}

.el-main {
  background-color: #E9EEF3;
}

body > .el-container {
  margin-bottom: 40px;
}

.el-dropdown-link {
  cursor: pointer;
  color: #409EFF;
}

.el-icon-arrow-down {
  font-size: 12px;
}

.demonstration {
  display: block;
  color: #8492a6;
  font-size: 14px;
  margin-bottom: 20px;
}

.systemName {
  cursor: pointer;
  font-size: 31px;
  font-family: 'Times New Roman';
  padding-left: 5px;
}
</style>