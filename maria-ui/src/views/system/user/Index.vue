<template>
  <div>
    <el-breadcrumb separator="/" style="padding-left: 10px; padding-bottom: 10px; font-size: 12px;">
      <el-breadcrumb-item :to="{ path: '/frame' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>系统管理</el-breadcrumb-item>
      <el-breadcrumb-item>用户管理</el-breadcrumb-item>
    </el-breadcrumb>

    <el-card class="box-card" :style="{height: getHeight()}">
      <el-form :inline="true" :model="userForm" class="demo-form-inline">
        <el-form-item label="用户账号" size="small">
          <el-input clearable v-model="userForm.username" placeholder="请输入用户账号"
                    @keyup.enter.native="triggerLoadUserList" />
        </el-form-item>

        <el-form-item label="用户昵称" size="small">
          <el-input clearable v-model="userForm.nickname" placeholder="请输入用户昵称"
                    @keyup.enter.native="triggerLoadUserList" />
        </el-form-item>

        <el-form-item label="用户邮箱" size="small">
          <el-input clearable v-model="userForm.email" placeholder="请输入用户邮箱地址"
                    @keyup.enter.native="triggerLoadUserList" />
        </el-form-item>

        <el-form-item label="联系方式" size="small">
          <el-input clearable v-model="userForm.telephone" placeholder="请输入用户联系方式"
                    @keyup.enter.native="triggerLoadUserList" />
        </el-form-item>

        <el-form-item label="身份证号" size="small">
          <el-input clearable v-model="userForm.idCard" placeholder="请输入用户身份证号"
                    @keyup.enter.native="triggerLoadUserList" />
        </el-form-item>

        <el-form-item label="用户地址" size="small">
          <el-input clearable v-model="userForm.address" placeholder="请输入用户地址"
                    @keyup.enter.native="triggerLoadUserList" />
        </el-form-item>

        <el-form-item label="出生日期" size="small">
          <el-date-picker type="daterange" v-model="timespan"
                          range-separator="至" start-placeholder="开始日期"
                          end-placeholder="结束日期" format="yyyy-MM-dd"
                          value-format="yyyy-MM-dd" align="right" />
        </el-form-item>
      </el-form>

      <el-form :inline="true" class="demo-form-inline">
        <el-form-item size="small">
          <el-button icon="el-icon-refresh" @click="reset">重置条件</el-button>
          <el-button icon="el-icon-search" type="success" ref="loadUserList" @click="getUserList">查询记录
          </el-button>
          <el-button icon="el-icon-plus" type="primary" @click="addRecord"
                     v-has-permission="'user@add'">添加记录
          </el-button>
          <el-button icon="el-icon-edit" type="warning" @click="updateRecord"
                     v-has-permission="'user@update'">修改记录
          </el-button>
          <el-button icon="el-icon-delete" type="danger" @click="deleteRecords"
                     v-has-permission="'user@delete'">删除记录
          </el-button>
        </el-form-item>
      </el-form>

      <el-table :data="userList" border v-loading="loading" style="width: 100%"
                :row-style="{height: '35px'}" :cell-style="{padding: '0px'}"
                :header-row-style="{height: '35px'}" :header-cell-style="{padding: '0px'}"
                @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" fixed align="center" />
        <el-table-column type="index" label="序号" width="60" fixed align="center" />
        <el-table-column prop="username" label="用户账号" width="150" fixed align="center" />
        <el-table-column prop="nickname" label="用户昵称" width="150" align="center" />
        <el-table-column prop="gender" label="性别" width="60" align="center">
          <template slot-scope="scope">
            {{ scope.row.gender === 1 ? "男" : "女" }}
          </template>
        </el-table-column>
        <el-table-column prop="email" label="用户邮箱" width="210" align="center">
          <template slot-scope="scope">
            {{ scope.row.email === "" ? "-" : scope.row.email }}
          </template>
        </el-table-column>
        <el-table-column prop="telephone" label="联系方式" width="150" align="center">
          <template slot-scope="scope">
            {{ scope.row.telephone === "" ? "-" : scope.row.telephone }}
          </template>
        </el-table-column>
        <el-table-column prop="birthday" label="出生日期" width="120" align="center">
          <template slot-scope="scope">
            {{ scope.row.birthday === "" ? "-" : scope.row.birthday }}
          </template>
        </el-table-column>
        <el-table-column prop="idCard" label="身份证号" width="200" align="center">
          <template slot-scope="scope">
            {{ scope.row.idCard === "" ? "-" : scope.row.idCard }}
          </template>
        </el-table-column>
        <el-table-column prop="address" label="用户地址" width="300" align="center">
          <template slot-scope="scope">
            {{ scope.row.address === "" ? "-" : scope.row.address }}
          </template>
        </el-table-column>
        <el-table-column prop="enabled" label="用户状态" width="100" align="center">
          <template slot-scope="scope">
            {{ scope.row.enabled === 1 ? "启用" : "禁用" }}
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" width="230" align="center">
          <template slot-scope="scope">
            <el-tooltip content="重置密码" placement="top">
              <el-button type="success" size="mini" icon="el-icon-lock"
                         @click="resetPassword(scope.row.id)" v-has-permission="'user@reset'" />
            </el-tooltip>
            <el-tooltip content="修改信息" placement="top">
              <el-button type="primary" size="mini" icon="el-icon-edit"
                         @click="updateUser(scope.row.id)" v-has-permission="'user@update'" />
            </el-tooltip>
            <el-tooltip content="配置角色" placement="top">
              <el-button type="warning" size="mini" icon="el-icon-s-tools"
                         @click="configUser(scope.row.id)" v-has-permission="'user@configure'" />
            </el-tooltip>
            <el-tooltip content="删除用户" placement="top">
              <el-button type="danger" size="mini" icon="el-icon-delete"
                         @click="deleteUser(scope.row.id)" v-has-permission="'user@delete'" />
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination style="padding-top: 20px;" @size-change="handleSizeChange"
                     @current-change="handleCurrentChange"
                     :current-page="current" :page-sizes="[15, 30]" :page-size="pageSize"
                     layout="total, sizes, prev, pager, next, jumper" :total="total" />
    </el-card>

    <el-dialog :title="updateUserFlag ? '修改用户信息' : '添加用户信息'" :visible.sync="userFormFlag"
               :close-on-click-modal="false" :close-on-press-escape="false" :show-close="false">
      <el-form :model="userInfo" :rules="userInfoRule" ref="userInfoForm" label-width="100px"
               class="demo-ruleForm">
        <el-form-item label="用户账号" prop="username" size="small">
          <el-input v-model="userInfo.username" clearable maxlength="20"
                    @keyup.enter.native="triggerSubmitUserInfo" />
        </el-form-item>

        <el-form-item label="用户昵称" prop="nickname" size="small">
          <el-input v-model="userInfo.nickname" clearable maxlength="20"
                    @keyup.enter.native="triggerSubmitUserInfo" />
        </el-form-item>

        <el-form-item label="用户性别" prop="gender" size="small">
          <el-radio v-model="userInfo.gender" label="1">
            <span class="el-tag el-tag--success el-tag--mini el-tag--plain">男</span>
          </el-radio>
          <el-radio v-model="userInfo.gender" label="0">
            <span class="el-tag el-tag--success el-tag--mini el-tag--plain">女</span>
          </el-radio>
        </el-form-item>

        <el-form-item label="用户邮箱" prop="email" size="small">
          <el-input v-model="userInfo.email" maxlength="25" clearable
                    @keyup.enter.native="triggerSubmitUserInfo" />
        </el-form-item>

        <el-form-item label="联系方式" prop="telephone" size="small">
          <el-input v-model="userInfo.telephone" clearable maxlength="11"
                    @keyup.enter.native="triggerSubmitUserInfo" />
        </el-form-item>

        <el-form-item label="出生日期" prop="birthday" size="small">
          <el-date-picker type="date" placeholder="请选择出生日期" v-model="userInfo.birthday"
                          value-format="yyyy-MM-dd" format="yyyy-MM-dd" style="width: 100%;"
                          placement="bottom-start" />
        </el-form-item>

        <el-form-item label="身份证号" prop="idCard" size="small">
          <el-input v-model="userInfo.idCard" clearable maxlength="18"
                    @keyup.enter.native="triggerSubmitUserInfo" />
        </el-form-item>

        <el-form-item label="用户地址" prop="address" size="small">
          <el-input v-model="userInfo.address" clearable maxlength="70"
                    @keyup.enter.native="triggerSubmitUserInfo" />
        </el-form-item>

        <el-form-item label="用户状态" prop="enabled" size="small">
          <el-switch active-color="#13ce66" inactive-color="#ff4949" active-text="启用" inactive-text="禁用"
                     v-model="userStatus" />
        </el-form-item>

        <el-form-item size="small" v-if="updateUserFlag === false">
                    <span>
                        用户默认密码：11111111
                    </span>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="cancelSubmitUserInfo">取消</el-button>
        <el-button type="primary" size="small" @click="submitUserInfo('userInfoForm')" ref="submitUserInfo">
          提交
        </el-button>
      </div>
    </el-dialog>

    <el-dialog :title="'配置用户-角色关联关系'" :visible.sync="configureUserRoleBinding"
               :close-on-click-modal="false" :close-on-press-escape="false" :show-close="false">
      <div style="text-align: center">
        <el-transfer v-model="assignedRoles" :data="roleList" :titles="['角色列表', '已选角色']"
                     @change="handleRoleChange" />
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="cancelSubmitUserRoleInfo">取消</el-button>
        <el-button size="small" type="primary" @click="submitUserRoleInfo">提交</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getUserList,
  addUser,
  updateUser,
  getUser,
  deleteUser,
  resetPassword,
  getUserRole,
  configureUserRole
} from "@/api/system/user/index";

export default {
  name: "Index",
  data() {
    const checkUsername = (rule, value, callback) => {
      if (!value || value.length === 0) {
        callback(new Error("用户账号不能为空!"));
      }
      if (value.length > 0 && value.trim() === "") {
        callback(new Error("用户账号不能为空格!"));
      }
      if (value.trim() !== value) {
        callback(new Error("用户账号前后不能包含空格!"));
      }
      callback();
    };
    const checkNickname = (rule, value, callback) => {
      if (!value || value.length === 0) {
        callback(new Error("用户昵称不能为空!"));
      }
      if (value.length > 0 && value.trim() === "") {
        callback(new Error("用户昵称不能为空格!"));
      }
      if (value.trim() !== value) {
        callback(new Error("用户昵称前后不能包含空格!"));
      }
      callback();
    };
    const checkEmail = (rule, value, callback) => {
      if (!value || value.length === 0) {
        callback();
      }
      if (value.length > 0 && value.trim() === "") {
        callback(new Error("用户邮箱不能为空格!"));
      }
      if (value.trim() !== value) {
        callback(new Error("用户邮箱前后不能包含空格!"));
      }
      const reg = /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
      if (reg.test(value)) {
        callback();
      } else {
        callback(new Error("用户邮箱格式错误!"));
      }
    };
    const checkTelephone = (rule, value, callback) => {
      if (!value || value.length === "") {
        callback();
      }
      if (value.length > 0 && value.trim() === "") {
        callback(new Error("联系方式不能为空格!"));
      }
      if (value.trim() !== value) {
        callback(new Error("联系方式前后不能包含空格!"));
      }
      const phoneReg = /^1[3|4|5|7|8][0-9]{9}$/;
      setTimeout(() => {
        if (!Number.isInteger(+value)) {
          callback(new Error("请输入数字!"));
        } else {
          if (phoneReg.test(value)) {
            callback();
          } else {
            callback(new Error("联系方式格式错误!"));
          }
        }
      }, 100);
    };
    const checkIdCard = (rule, value, callback) => {
      if (!value || value.length === "") {
        callback();
      }
      if (value.length > 0 && value.trim() === "") {
        callback(new Error("身份证号不能为空格!"));
      }
      if (value.trim() !== value) {
        callback(new Error("身份证号前后不能包含空格!"));
      }
      if (value.length !== 18) {
        callback(new Error("身份证号长度不足18位!"));
      }
      callback();
    };
    const checkAddress = (rule, value, callback) => {
      if (!value || value.length === "") {
        callback();
      }
      if (value.length > 0 && value.trim() === "") {
        callback(new Error("用户地址不能为空格!"));
      }
      if (value.trim() !== value) {
        callback(new Error("用户地址前后不能包含空格!"));
      }
      callback();
    };
    return {
      height: "0px",
      userForm: {
        username: "",
        nickname: "",
        email: "",
        telephone: "",
        idCard: "",
        address: "",
        start: "",
        end: ""
      },
      userList: [],
      loading: false,
      current: 1,
      pageSize: 15,
      total: 0,
      userFormFlag: false,
      updateUserFlag: false,
      userInfo: {
        username: "",
        nickname: "",
        email: "",
        telephone: "",
        gender: "1",
        birthday: "",
        idCard: "",
        address: "",
        enabled: 1
      },
      userInfoRule: {
        username: [
          {
            required: true, validator: checkUsername, trigger: "blur"
          }
        ],
        nickname: [
          {
            required: true, validator: checkNickname, trigger: "blur"
          }
        ],
        email: [
          {
            required: false, validator: checkEmail, trigger: "blur"
          }
        ],
        telephone: [
          {
            required: false, validator: checkTelephone, trigger: "blur"
          }
        ],
        idCard: [
          {
            required: false, validator: checkIdCard, trigger: "blur"
          }
        ],
        address: [
          {
            required: false, validator: checkAddress, trigger: "blur"
          }
        ]
      },
      userStatus: true,
      selection: [],
      configureUserRoleBinding: false,
      assignedRoles: [],
      roleList: [],
      currentUser4UserRoleBinding: "-1",
      timespan: ""
    };
  },
  created() {
    this.height = window.innerHeight - 60 - 20 - 32 - 14;
    window.onresize = (() => {
      return (() => {
        this.height = window.innerHeight - 60 - 20 - 32 - 14;
      })();
    });
  },
  mounted() {
    this.getUserList();
  },
  methods: {
    getHeight() {
      return this.height + "px";
    },
    handleSizeChange(val) {
      this.pageSize = val;
      this.getUserList();
    },
    handleCurrentChange(val) {
      this.current = val;
      this.getUserList();
    },
    getUserList() {
      this.selection = [];
      this.loading = true;
      if (this.timespan.length === 2) {
        this.userForm.start = this.timespan[0];
        this.userForm.end = this.timespan[1];
      }
      getUserList(this.current, this.pageSize, this.userForm).then(res => {
        if (res.data.code === 200) {
          this.total = res.data.data.total;
          this.userList = res.data.data.list;
        } else {
          this.$message({
            message: res.data.message,
            type: "error"
          });
        }
      }).catch(error => {
        console.log(error);
      }).finally(() => {
        this.loading = false;
      });
    },
    reset() {
      this.current = 1;
      this.pageSize = 15;
      this.userForm = {
        username: "",
        nickname: "",
        email: "",
        telephone: "",
        idCard: "",
        address: ""
      };
      this.timespan = "";
      this.getUserList();
    },
    addRecord() {
      this.userFormFlag = true;
      this.updateUserFlag = false;
    },
    updateRecord() {
      if (this.selection.length === 0) {
        this.$message({
          message: "请选择用户信息记录!",
          type: "info"
        });
      } else if (this.selection.length > 1) {
        this.$message({
          message: "勾选记录数大于1!",
          type: "info"
        });
      } else {
        this.updateUser(this.selection[0].id);
      }
    },
    deleteRecords() {
      if (this.selection.length === 0) {
        this.$message({
          message: "请选择用户信息记录!",
          type: "info"
        });
      } else {
        this.$confirm("此操作将删除用户信息记录, 是否继续?", "提示信息", {
          confirmButtonText: "确定",
          cancelButtonText: "取消"
        }).then(() => {
          deleteUser(this.selection.map(user => user.id)).then(res => {
            this.$message({
              message: res.data.message,
              type: res.data.code === 200 ? "success" : "error"
            });
          }).catch(error => {
            console.log(error);
          }).finally(() => {
            this.getUserList();
          });
        }).catch(() => {
          this.$message({
            message: "取消删除用户信息记录!",
            type: "info"
          });
        });
      }
    },
    cancelSubmitUserInfo() {
      this.userInfo = {
        username: "",
        nickname: "",
        email: "",
        telephone: "",
        gender: "1",
        birthday: "",
        idCard: "",
        address: "",
        enabled: 1
      };
      this.userFormFlag = false;
      this.updateUserFlag = false;
    },
    submitUserInfo(form) {
      this.$refs[form].validate(valid => {
        if (valid) {
          if (this.userStatus) {
            this.userInfo.enabled = 1;
          } else {
            this.userInfo.enabled = 0;
          }
          this.doSubmitUserInfo();
        } else {
          this.$message({
            message: "参数校验未通过, 请重新输入!",
            type: "info"
          });
        }
      });
    },
    doSubmitUserInfo() {
      if (this.updateUserFlag) {
        updateUser(this.userInfo).then(res => {
          this.$message({
            message: res.data.message,
            type: res.data.code === 200 ? "success" : "error"
          });
        }).catch(error => {
          console.log(error);
        }).finally(() => {
          this.cancelSubmitUserInfo();
          this.getUserList();
        });
      } else {
        addUser(this.userInfo).then(res => {
          this.$message({
            message: res.data.message,
            type: res.data.code === 200 ? "success" : "error"
          });
        }).catch(error => {
          console.log(error);
        }).finally(() => {
          this.cancelSubmitUserInfo();
          this.getUserList();
        });
      }
    },
    resetPassword(id) {
      resetPassword(id).then(res => {
        this.$message({
          message: res.data.message,
          type: res.data.code === 200 ? "success" : "error"
        });
      }).catch(error => {
        console.log(error);
      });
    },
    updateUser(id) {
      getUser(id).then(res => {
        if (res.data.code === 200) {
          this.userInfo = { ...res.data.data.user };
        } else {
          this.$message({
            message: res.data.message,
            type: "error"
          });
        }
      }).catch(error => {
        console.log(error);
      }).finally(() => {
        this.userStatus = this.userInfo.enabled === 1;
        this.userFormFlag = true;
        this.updateUserFlag = true;
      });
    },
    configUser(id) {
      this.cancelSubmitUserRoleInfo();
      this.currentUser4UserRoleBinding = id;
      getUserRole(id).then(res => {
        if (res.data.code === 200) {
          this.assignedRoles = res.data.data.assigned;
          for (let i = 0; i < res.data.data.list.length; i++) {
            this.roleList.push({
              key: res.data.data.list[i].id,
              label: res.data.data.list[i].name
            });
          }
        } else {
          this.$message({
            message: res.data.message,
            type: "error"
          });
        }
      }).catch(error => {
        console.log(error);
      }).finally(() => {
        this.configureUserRoleBinding = true;
      });
    },
    deleteUser(id) {
      this.$confirm("此操作将删除用户信息记录, 是否继续?", "提示信息", {
        confirmButtonText: "确定",
        cancelButtonText: "取消"
      }).then(() => {
        deleteUser([id]).then(res => {
          this.$message({
            message: res.data.message,
            type: res.data.code === 200 ? "success" : "error"
          });
        }).catch(error => {
          console.log(error);
        }).finally(() => {
          this.getUserList();
        });
      }).catch(() => {
        this.$message({
          message: "取消删除用户信息记录!",
          type: "info"
        });
      });
    },
    handleSelectionChange(val) {
      this.selection = val;
    },
    handleRoleChange(currentValue, direction, key) {
      this.assignedRoles = [];
      console.log("currentValue = " + currentValue);
      for (let i = 0; i < currentValue.length; i++) {
        this.assignedRoles.push(currentValue[i]);
      }
    },
    submitUserRoleInfo() {
      configureUserRole(this.currentUser4UserRoleBinding, this.assignedRoles).then(res => {
        this.$message({
          message: res.data.message,
          type: res.data.code === 200 ? "success" : "error"
        });
      }).catch(error => {
        console.log(error);
      }).finally(() => {
        this.cancelSubmitUserRoleInfo();
        this.getUserList();
      });
    },
    cancelSubmitUserRoleInfo() {
      this.configureUserRoleBinding = false;
      this.assignedRoles = [];
      this.roleList = [];
      this.currentUser4UserRoleBinding = "-1";
    },
    triggerLoadUserList() {
      this.$refs.loadUserList.$emit("click");
    },
    triggerSubmitUserInfo() {
      this.$refs.submitUserInfo.$emit("click");
    }
  }
};
</script>

<style scoped lang="less">
.el-transfer {
  font-size: 14px;
  text-align: left;
  display: inline-block
}
</style>