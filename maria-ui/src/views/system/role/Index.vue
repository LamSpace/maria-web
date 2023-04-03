<template>
  <div>
    <el-breadcrumb separator="/" style="padding-left: 10px; padding-bottom: 10px; font-size: 12px;">
      <el-breadcrumb-item :to="{ path: '/frame' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>系统管理</el-breadcrumb-item>
      <el-breadcrumb-item>角色管理</el-breadcrumb-item>
    </el-breadcrumb>

    <el-card class="box-card" :style="{height: getHeight()}">
      <el-form :inline="true" :model="roleForm" class="demo-form-inline">
        <el-form-item label="角色名称" size="small">
          <el-input clearable v-model="roleForm.name" placeholder="请输入角色名称"
                    @keyup.enter.native="triggerLoadRoleList" />
        </el-form-item>
      </el-form>

      <el-form :inline="true" class="demo-form-inline">
        <el-form-item size="small">
          <el-button icon="el-icon-refresh" @click="reset">重置条件</el-button>
          <el-button icon="el-icon-search" type="success" @click="getRoleList"
                     ref="loadRoleList">查询记录
          </el-button>
          <el-button icon="el-icon-plus" type="primary" @click="addRecord"
                     v-has-permission="'role@add'">添加记录
          </el-button>
          <el-button icon="el-icon-edit" type="warning" @click="updateRecord"
                     v-has-permission="'role@update'">修改记录
          </el-button>
          <el-button icon="el-icon-delete" type="danger" @click="deleteRecords"
                     v-has-permission="'role@delete'">删除记录
          </el-button>
        </el-form-item>
      </el-form>

      <el-table :data="roleList" border v-loading="loading" style="width: 100%"
                :row-style="{height: '35px'}" :cell-style="{padding: '0px'}"
                :header-row-style="{height: '35px'}" :header-cell-style="{padding: '0px'}"
                @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" fixed align="center" />
        <el-table-column type="index" label="序号" width="60" fixed align="center" />
        <el-table-column prop="name" label="角色名称" width="200" fixed align="center" />
        <el-table-column prop="enabled" label="角色状态" width="100" align="center">
          <template slot-scope="scope">
            {{ scope.row.enabled === 1 ? "启用" : "禁用" }}
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="角色描述" align="center" show-overflow-tooltip>
          <template slot-scope="scope">
            {{ scope.row.remark === "" ? "-" : scope.row.remark }}
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="175">
          <template slot-scope="scope">
            <el-tooltip content="修改角色" placement="top">
              <el-button size="mini" icon="el-icon-edit" type="primary"
                         @click="updateRole(scope.row.id)" v-has-permission="'role@update'" />
            </el-tooltip>
            <el-tooltip content="配置权限" placement="top">
              <el-button size="mini" icon="el-icon-s-tools" type="warning"
                         @click="configRole(scope.row.id)" v-has-permission="'role@configure'" />
            </el-tooltip>
            <el-tooltip content="删除角色" placement="top">
              <el-button size="mini" icon="el-icon-delete" type="danger"
                         @click="deleteRole(scope.row.id)" v-has-permission="'role@delete'" />
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination style="padding-top: 20px;" @size-change="handleSizeChange"
                     @current-change="handleCurrentChange"
                     :current-page="current" :page-sizes="[15, 30]" :page-size="pageSize"
                     layout="total, sizes, prev, pager, next, jumper" :total="total" />
    </el-card>

    <el-dialog :visible.sync="roleFormFlag" :title="updateRoleFlag ? '修改角色信息' : '添加角色信息'"
               :close-on-click-modal="false" :close-on-press-escape="false" :show-close="false">
      <el-form label-width="100px" :model="roleInfo" :rules="roleInfoRule" ref="roleInfoForm"
               class="demo-ruleForm">
        <el-form-item label="角色名称" prop="name" size="small">
          <el-input clearable v-model="roleInfo.name" maxlength="15"
                    @keyup.enter.native="triggerSubmitRoleInfo" />
        </el-form-item>
        <el-form-item label="角色描述" prop="remark" size="small">
          <el-input clearable v-model="roleInfo.remark" maxlength="50"
                    @keyup.enter.native="triggerSubmitRoleInfo" />
        </el-form-item>
        <el-form-item label="角色状态" prop="enabled" size="small">
          <el-switch active-color="#13ce66" inactive-color="#ff4949" active-text="启用" inactive-text="禁用"
                     v-model="roleStatus" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="cancelSubmitRoleInfo">取消</el-button>
        <el-button size="small" type="primary" @click="submitRoleInfo('roleInfoForm')" ref="submitRoleInfo">
          提交
        </el-button>
      </div>
    </el-dialog>

    <el-dialog :visible.sync="configureRoleMenuBinding" :title="'配置角色-菜单关联关系'"
               :close-on-click-modal="false" :close-on-press-escape="false" :show-close="false">
      <el-tree :data="menuTree" :props="defaultProps" ref="menuTree"
               show-checkbox accordion :default-checked-keys="defaultKeys" node-key="id" />
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="cancelSubmitRoleMenuInfo">取消</el-button>
        <el-button size="small" type="primary" @click="submitRoleMenuInfo">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getRoleList,
  addRole,
  updateRole,
  getRole,
  deleteRole,
  getRoleMenu,
  configureRoleMenu
} from "@/api/system/role/index";

export default {
  name: "Index",
  data() {
    const checkName = (rule, value, callback) => {
      if (!value || value.length === 0) {
        callback(new Error("角色名称不能为空!"));
      }
      if (value.length > 0 && value.trim() === "") {
        callback(new Error("角色名称不能为空格!"));
      }
      if (value.trim() !== value) {
        callback(new Error("角色名称前后不能包含空格!"));
      }
      callback();
    };
    const checkRemark = (rule, value, callback) => {
      if (!value || value.length === 0) {
        callback();
      }
      if (value.length > 0 && value.trim() === "") {
        callback(new Error("角色描述不能为空格!"));
      }
      if (value.trim() !== value) {
        callback(new Error("角色名称前后不能包含空格!"));
      }
      callback();
    };
    return {
      height: "0px",
      roleForm: {
        name: ""
      },
      roleList: [],
      loading: false,
      selection: [],
      current: 1,
      pageSize: 15,
      total: 0,
      roleFormFlag: false,
      updateRoleFlag: false,
      roleInfo: {
        name: "",
        enabled: 1,
        remark: ""
      },
      roleInfoRule: {
        name: [
          {
            required: true, validator: checkName, trigger: "blur"
          }
        ],
        remark: [
          {
            required: false, validator: checkRemark, trigger: "blur"
          }
        ]
      },
      roleStatus: true,
      configureRoleMenuBinding: false,
      menuTree: [],
      defaultProps: {
        children: "children",
        label: "menuName"
      },
      defaultKeys: [],
      currentRole4RoleMenuBinding: "-1"
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
  // eslint-disable-next-line @typescript-eslint/no-empty-function
  mounted() {
    this.getRoleList();
  },
  methods: {
    getHeight() {
      return this.height + "px";
    },
    handleSelectionChange(val) {
      this.selection = val;
    },
    getRoleList() {
      this.selection = [];
      this.loading = true;
      getRoleList(this.current, this.pageSize, this.roleForm).then(res => {
        if (res.data.code === 200) {
          this.total = res.data.data.total;
          this.roleList = res.data.data.list;
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
    handleSizeChange(val) {
      this.pageSize = val;
      this.getRoleList();
    },
    handleCurrentChange(val) {
      this.current = val;
      this.getRoleList();
    },
    reset() {
      this.current = 1;
      this.pageSize = 15;
      this.roleForm = {
        name: ""
      };
      this.getRoleList();
    },
    addRecord() {
      this.roleFormFlag = true;
      this.updateRoleFlag = false;
    },
    updateRecord() {
      if (this.selection.length === 0) {
        this.$message({
          message: "请选择角色信息记录!",
          type: "info"
        });
      } else if (this.selection.length > 1) {
        this.$message({
          message: "勾选记录数大于1!",
          type: "info"
        });
      } else {
        this.updateRole(this.selection[0].id);
      }
    },
    deleteRecords() {
      if (this.selection.length === 0) {
        this.$message({
          message: "请选择角色信息记录!",
          type: "info"
        });
      } else {
        this.$confirm("此操作将删除角色信息记录, 是否继续?", "提示信息", {
          confirmButtonText: "确定",
          cancelButtonText: "取消"
        }).then(() => {
          deleteRole(this.selection.map(role => role.id)).then(res => {
            this.$message({
              message: res.data.message,
              type: res.data.code === 200 ? "success" : "error"
            });
          }).catch(error => {
            console.log(error);
          }).finally(() => {
            this.getRoleList();
          });
        }).catch(() => {
          this.$message({
            message: "取消删除角色信息记录!",
            type: "info"
          });
        });
      }
    },
    cancelSubmitRoleInfo() {
      this.roleInfo = {
        name: "",
        enabled: 1,
        remark: ""
      };
      this.roleFormFlag = false;
      this.updateRoleFlag = false;
    },
    submitRoleInfo(form) {
      this.$refs[form].validate(valid => {
        if (valid) {
          if (this.roleStatus) {
            this.roleInfo.enabled = 1;
          } else {
            this.roleInfo.enabled = 0;
          }
          this.doSubmitRoleInfo();
        } else {
          this.$message({
            message: "参数校验未通过, 请重新输入!",
            type: "info"
          });
        }
      });
    },
    doSubmitRoleInfo() {
      if (this.updateRoleFlag) {
        updateRole(this.roleInfo).then(res => {
          this.$message({
            message: res.data.message,
            type: res.data.code === 200 ? "success" : "error"
          });
        }).catch(error => {
          console.log(error);
        }).finally(() => {
          this.cancelSubmitRoleInfo();
          this.getRoleList();
        });
      } else {
        addRole(this.roleInfo).then(res => {
          this.$message({
            message: res.data.message,
            type: res.data.code === 200 ? "success" : "error"
          });
        }).catch(error => {
          console.log(error);
        }).finally(() => {
          this.cancelSubmitRoleInfo();
          this.getRoleList();
        });
      }
    },
    updateRole(id) {
      getRole(id).then(res => {
        if (res.data.code === 200) {
          this.roleInfo = { ...res.data.data.role };
        } else {
          this.$message({
            message: res.data.message,
            type: "error"
          });
        }
      }).catch(error => {
        console.log(error);
      }).finally(() => {
        this.roleStatus = this.roleInfo.enabled === 1;
        this.roleFormFlag = true;
        this.updateRoleFlag = true;
      });
    },
    configRole(id) {
      this.currentRole4RoleMenuBinding = id;
      getRoleMenu(id).then(res => {
        if (res.data.code === 200) {
          this.menuTree = res.data.data.tree;
          this.defaultKeys = res.data.data.list;
        } else {
          this.$message({
            message: res.data.message,
            type: "error"
          });
        }
      }).catch(error => {
        console.log(error);
      }).finally(() => {
        this.configureRoleMenuBinding = true;
        console.log(this.menuTree);
        console.log(this.defaultKeys);
      });
    },
    deleteRole(id) {
      this.$confirm("此操作将删除角色信息记录, 是否继续?", "提示信息", {
        confirmButtonText: "确定",
        cancelButtonText: "取消"
      }).then(() => {
        deleteRole([id]).then(res => {
          this.$message({
            message: res.data.message,
            type: res.data.code === 200 ? "success" : "error"
          });
        }).catch(error => {
          console.log(error);
        }).finally(() => {
          this.getRoleList();
        });
      }).catch(() => {
        this.$message({
          message: "取消删除角色信息记录!",
          type: "info"
        });
      });
    },
    cancelSubmitRoleMenuInfo() {
      this.configureRoleMenuBinding = false;
      this.menuTree = [];
      this.defaultKeys = [];
    },
    submitRoleMenuInfo() {
      let ids = this.$refs.menuTree.getCheckedKeys().concat(this.$refs.menuTree.getHalfCheckedKeys());
      configureRoleMenu(this.currentRole4RoleMenuBinding, ids).then(res => {
        this.$message({
          message: res.data.message,
          type: res.data.code === 200 ? "success" : "error"
        });
      }).catch(error => {
        console.log(error);
      }).finally(() => {
        this.cancelSubmitRoleMenuInfo();
      });
    },
    triggerLoadRoleList() {
      this.$refs.loadRoleList.$emit("click");
    },
    triggerSubmitRoleInfo() {
      this.$refs.submitRoleInfo.$emit("click");
    }
  }
};
</script>

<style scoped>

</style>