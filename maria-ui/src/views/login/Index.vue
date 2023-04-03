<template>
  <div style="width: 100%; height: 100%;">
    <div class="screen">
      <div class="login_container">
        <div class="login_box">
          <div class="avatar_box">
            <img src="../../assets/logo.png">
          </div>
          <el-form :model="loginForm" :rules="loginFormRules" ref="loginForm" label-width="0px"
                   class="login-form">
            <el-form-item prop="username">
              <el-input prefix-icon="el-icon-user-solid" v-model="loginForm.username"
                        @keyup.enter.native="triggerLogin" />
            </el-form-item>
            <el-form-item prop="password">
              <el-input prefix-icon="el-icon-lock" v-model="loginForm.password" type="password"
                        @keyup.enter.native="triggerLogin" />
            </el-form-item>
            <el-form-item prop="kaptcha">
              <el-row>
                <el-col :span="16">
                  <el-input prefix-icon="el-icon-s-ticket" @keyup.enter.native="triggerLogin"
                            maxlength="4"
                            v-model="loginForm.kaptcha" />
                </el-col>
                <el-col :span="8">
                  <img :src="kaptcha" @click="refreshKaptcha">
                </el-col>
              </el-row>
            </el-form-item>
            <el-form-item>
              <el-row>
                <el-col :span="12">
                  <el-checkbox v-model="loginForm.rememberMe">记住我</el-checkbox>
                </el-col>
                <el-col :span="12">
                  <div class="login_button">
                    <el-button type="primary" @click="login('loginForm')" ref="btn">登录</el-button>
                    <el-button @click="cancel('loginForm')">取消</el-button>
                  </div>
                </el-col>
              </el-row>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>
    <div class="bracket" />
    <div class="base" />
  </div>
</template>

<script>
import { doLogin } from "@/api/login/index";

export default {
  name: "Index",
  data() {
    return {
      loginForm: {
        username: "admin",
        password: "admin",
        kaptcha: "",
        rememberMe: false
      },
      loginFormRules: {
        username: [
          { required: true, message: "请输入账号", trigger: "blur" }
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" }
        ],
        kaptcha: [
          { required: true, message: "请输入校验码", trigger: "blur" }
        ]
      },
      kaptcha: "/api/service-auth/auth/kaptcha?" + Math.ceil(Math.random() * 10)
    };
  },
  created() {
    // created here
  },
  mounted() {
    this.$notify.info({
      title: "欢迎使用",
      dangerouslyUseHTMLString: true,
      message: "账号: admin, 密码: admin <br> 请谨慎使用"
    });
  },
  methods: {
    triggerLogin() {
      this.$refs.btn.$emit("click");
    },
    login(form) {
      // noinspection JSUnresolvedFunction
      this.$refs[form].validate((v) => {
        if (v) {
          // noinspection JSCheckFunctionSignatures
          doLogin(this.loginForm).then(res => {
            if (res.data.code === 200) {
              this.$message({
                message: "登录成功",
                type: "success"
              });
              this.$store.commit("setToken", res.headers.authorization);
              this.$store.commit("setUser", res.data.data.user.userInfo);
              this.$store.commit("setMenus", res.data.data.user.menus);
              this.$store.commit("setPerms", res.data.data.user.perms);
              this.$store.commit("setSocketToken", res.data.data.user.socketToken);
              this.$router.push("/frame");
            } else {
              this.$message({
                message: "登录失败, " + res.data.message,
                type: "error"
              });
              this.refreshKaptcha();
            }
          }).catch(error => {
            console.log(error);
            this.refreshKaptcha();
            this.cancel("loginForm");
          });
        } else {
          this.$notify.warning({
            title: "参数校验失败",
            dangerouslyUseHTMLString: true,
            message: "请校验输入信息是否合法"
          });
        }
      });
    },
    cancel(form) {
      // noinspection JSUnresolvedFunction
      this.$refs[form].resetFields();
    },
    refreshKaptcha() {
      // 添加一个随机数, 避免请求由于幂等而缓存请求(即不发送获取图片的请求)
      this.kaptcha = "/api/service-auth/auth/kaptcha?" + Math.ceil(Math.random() * 10);
    }
  }
};
</script>

<style lang="less" scoped>
.base {
  width: 20%;
  height: 2%;
  background-color: slategray;
  position: absolute;
  left: 50%;
  top: 95%;
  transform: translate(-50%, -50%);
}

.bracket {
  width: 5%;
  height: 10%;
  background-color: slategray;
  position: absolute;
  left: 50%;
  top: 90%;
  transform: translate(-50%, -50%);
}

.screen {
  width: 70%;
  height: 70%;
  background-color: black;
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
}

.login_container {
  width: 98%;
  height: 96%;
  background-color: aquamarine;
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
}

.login_box {
  width: 400px;
  height: 350px;
  background-color: white;
  border-radius: 3px;
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);

  .avatar_box {
    width: 130px;
    height: 130px;
    border: 1px solid #EEEEEE;
    border-radius: 50%;
    padding: 10px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    margin: -65px auto;
    background-color: #FFFFFF;

    img {
      width: 100%;
      height: 100%;
      border-radius: 50%;
      background-color: #EEEEEE;
    }
  }

  .login-form {
    position: absolute;
    bottom: 0;
    width: 100%;
    padding: 0 20px;
    box-sizing: border-box;

    .login_button {
      display: flex;
      justify-content: flex-end;
    }
  }
}
</style>