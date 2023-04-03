<template>
  <div>
    <el-breadcrumb separator="/" style="padding-left: 10px; padding-bottom: 10px; font-size: 12px;">
      <el-breadcrumb-item :to="{ path: '/frame' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>系统管理</el-breadcrumb-item>
      <el-breadcrumb-item>菜单管理</el-breadcrumb-item>
    </el-breadcrumb>

    <el-card class="box-card" :style="{height: getHeight()}">
      <el-form :inline="true" class="demo-form-inline">
        <el-form-item size="small">
          <el-button type="primary" icon="el-icon-plus" @click="addRecord" v-has-permission="'menu@add'">
            添加记录
          </el-button>
          <el-button type="warning" icon="el-icon-edit" @click="updateRecord"
                     v-has-permission="'menu@update'">修改记录
          </el-button>
          <el-button type="danger" icon="el-icon-delete" @click="deleteRecords"
                     v-has-permission="'menu@delete'">删除记录
          </el-button>
        </el-form-item>
      </el-form>

      <el-scrollbar :style="{height: getTableHeight()}">
        <el-table :data="menuTree" border v-loading="loading" style="width: 100%;margin-bottom: 20px;"
                  :row-style="{height: '35px'}" :cell-style="{padding: '0px'}"
                  :header-row-style="{height: '35px'}" :header-cell-style="{padding:  '0px'}"
                  row-key="id" :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
                  @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55" fixed align="center" />
          <el-table-column prop="menuName" label="菜单名称" width="200" fixed align="center"
                           show-overflow-tooltip />
          <el-table-column prop="url" label="URL" width="200" align="center" show-overflow-tooltip>
            <template slot-scope="scope">
              {{ scope.row.url === "" ? "-" : scope.row.url }}
            </template>
          </el-table-column>
          <el-table-column prop="icon" label="图标名称" width="250" align="center" show-overflow-tooltip>
            <template slot-scope="scope">
              {{ scope.row.icon === "" ? "-" : scope.row.icon }}
            </template>
          </el-table-column>
          <el-table-column prop="icon" label="图标样式" width="100" align="center">
            <template slot-scope="scope">
              <i :class="scope.row.icon" />
            </template>
          </el-table-column>
          <el-table-column prop="orderNum" label="排序" width="100" align="center" />
          <el-table-column prop="type" label="菜单类型" width="100" align="center">
            <template slot-scope="scope">
              {{ scope.row.type === 0 ? "页面" : "按钮" }}
            </template>
          </el-table-column>
          <el-table-column prop="status" label="菜单状态" width="100" align="center">
            <template slot-scope="scope">
              {{ scope.row.status === 0 ? "禁用" : "启用" }}
            </template>
          </el-table-column>
          <el-table-column prop="perms" label="菜单权限" align="center">
            <template slot-scope="scope">
              {{ scope.row.perms === "" ? "-" : scope.row.perms }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="120" fixed="right" align="center">
            <template slot-scope="scope">
              <el-tooltip content="更新菜单" placement="top">
                <el-button type="primary" size="mini" icon="el-icon-edit"
                           @click="updateMenu(scope.row.id)" v-has-permission="'menu@update'" />
              </el-tooltip>
              <el-tooltip content="删除菜单" placement="top">
                <el-button type="danger" size="mini" icon="el-icon-delete"
                           @click="deleteMenu(scope.row.id)" v-has-permission="'menu@delete'" />
              </el-tooltip>
            </template>
          </el-table-column>
        </el-table>
      </el-scrollbar>
    </el-card>

    <el-dialog :visible.sync="menuFormFlag" :title="updateMenuFlag ? '修改菜单信息' : '添加菜单信息'"
               :close-on-click-modal="false" :close-on-press-escape="false" :show-close="false">
      <el-form label-width="100px" :model="menuInfo" :rules="roleInfoRule" ref="menuInfoForm"
               class="demo-ruleForm">
        <el-form-item label="菜单名称" prop="menuName" size="small">
          <el-input clearable v-model="menuInfo.menuName" maxlength="13"
                    @keyup.enter.native="triggerSubmitMenuInfo" />
        </el-form-item>
        <el-form-item label="上级菜单" prop="parentId" size="small">
          <el-select v-model="menuInfo.parentId" placeholder="请选择上级菜单" style="width: 100%;">
            <el-option v-for="item in menuList" :key="item.id" :label="item.menuName" :value="item.id">
              <span style="float: left">{{ item.menuName }}</span>
              <span style="float: right;color: #8492a6; font-size: 13px; margin-top: 6px;"
                    class="el-tag tl-tag--success el-tag--mini el-tag--plain">{{ item.id }}</span>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="菜单类型" size="small">
          <el-radio v-model="menuInfo.type" label="0">页面</el-radio>
          <el-radio v-model="menuInfo.type" label="1">按钮</el-radio>
        </el-form-item>
        <el-form-item label="菜单URL" prop="url" size="small">
          <el-input clearable v-model="menuInfo.url" maxlength="30"
                    @keyup.enter.native="triggerSubmitMenuInfo" />
        </el-form-item>
        <el-form-item label="图标名称" prop="icon" size="small">
          <el-input clearable v-model="menuInfo.icon" maxlength="30"
                    @keyup.enter.native="triggerSubmitMenuInfo" />
        </el-form-item>
        <el-form-item label="菜单权限" prop="perms" size="small">
          <el-input clearable v-model="menuInfo.perms" maxlength="80"
                    @keyup.enter.native="triggerSubmitMenuInfo" />
        </el-form-item>
        <el-form-item label="菜单状态" size="small">
          <el-switch active-color="#13ce66" inactive-color="#ff4949" active-text="启用" inactive-text="禁用"
                     v-model="menuStatus" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="cancelSubmitMenuInfo">取消</el-button>
        <el-button size="small" type="primary" @click="submitMenuInfo('menuInfoForm')" ref="submitMenuInfo">
          提交
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getMenuTree, addMenu, updateMenu, deleteMenu, getMenu } from "@/api/system/menu/index";

export default {
  name: "Index",
  data() {
    const checkMenuName = (rule, value, callback) => {
      if (!value || value.length === 0) {
        callback(new Error("菜单名称不能为空!"));
      }
      if (value.length > 0 && value.trim() === "") {
        callback(new Error("菜单名称不能为空格!"));
      }
      if (value.trim() !== value) {
        callback(new Error("菜单名称前后不能包含空格!"));
      }
      callback();
    };
    const checkParentId = (rule, value, callback) => {
      if (this.menuInfo.parentId === -1) {
        callback(new Error("上级菜单不能为-1!"));
      }
      callback();
    };
    const checkURL = (rule, value, callback) => {
      if (this.menuInfo.type === "0") {
        if ((!value || value.length === 0) && this.menuInfo.parentId !== "0") {
          callback(new Error("菜单URL不能为空!"));
        }
        if (value.length > 0 && value.trim() === "" && this.menuInfo.parentId !== "0") {
          callback(new Error("菜单URL不能为空格!"));
        }
        if (value.trim() !== value && this.menuInfo.parentId !== "0") {
          callback(new Error("菜单URL前后不能包含空格!"));
        }
        if (!value.startsWith("/") && this.menuInfo.parentId !== "0") {
          callback(new Error("菜单URL必须以 / 开头"));
        }
        callback();
      }
      callback();
    };
    const checkPerms = (rule, value, callback) => {
      if (this.menuInfo.type === "1") {
        if (!value || value.length === 0) {
          callback(new Error("菜单权限不能为空!"));
        }
        if (value.length > 0 && value.trim() === "") {
          callback(new Error("菜单权限不能为空格!"));
        }
        if (value.trim() !== value) {
          callback(new Error("菜单权限前后不能包含空格!"));
        }
        callback();
      }
      callback();
    };
    return {
      height: "0px",
      tableHeight: "0px",
      menuTree: [],
      menuList: [],
      loading: false,
      selection: [],
      menuFormFlag: false,
      updateMenuFlag: false,
      menuInfo: {
        parentId: -1,
        menuName: "",
        url: "",
        perms: "",
        icon: "",
        type: 0 + "",
        status: 1
      },
      roleInfoRule: {
        menuName: [
          {
            required: true, validator: checkMenuName, trigger: "blur"
          }
        ],
        parentId: [
          {
            required: true, validator: checkParentId
          }
        ],
        url: [
          {
            required: false, validator: checkURL, trigger: "blur"
          }
        ],
        perms: [
          {
            required: false, validator: checkPerms, trigger: "blur"
          }
        ]
      },
      menuStatus: false
    };
  },
  created() {
    this.height = window.innerHeight - 60 - 20 - 32 - 14;
    window.onresize = (() => {
      return (() => {
        this.height = window.innerHeight - 60 - 20 - 32 - 14;
      })();
    });
    this.tableHeight = window.innerHeight - 60 - 20 - 32 - 50;
    window.onresize = (() => {
      return (() => {
        this.tableHeight = window.innerHeight - 60 - 20 - 32 - 50;
      })();
    });
  },
  // eslint-disable-next-line @typescript-eslint/no-empty-function
  mounted() {
    this.getMenuTree();
  },
  methods: {
    getHeight() {
      return this.height + "px";
    },
    getTableHeight() {
      return this.tableHeight + "px";
    },
    getMenuTree() {
      this.loading = true;
      getMenuTree().then(res => {
        if (res.data.code === 200) {
          this.menuTree = res.data.data.tree;
          this.menuList = res.data.data.list;
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
    handleSelectionChange(val) {
      this.selection = val;
    },
    updateMenu(id) {
      getMenu(id).then(res => {
        if (res.data.code === 200) {
          this.menuInfo = { ...res.data.data.menu };
          this.menuInfo.type += "";
        } else {
          this.$message({
            message: res.data.message,
            type: "error"
          });
        }
      }).catch(error => {
        console.log(error);
      }).finally(() => {
        this.menuStatus = this.menuInfo.status === 1;
        this.menuFormFlag = true;
        this.updateMenuFlag = true;
      });
    },
    deleteMenu(id) {
      this.$confirm("此操作将删除菜单信息记录, 是否继续?", "提示信息", {
        confirmButtonText: "确定",
        cancelButtonText: "取消"
      }).then(() => {
        deleteMenu([id]).then(res => {
          this.$message({
            message: res.data.message,
            type: res.data.code === 200 ? "success" : "error"
          });
        }).catch(error => {
          console.log(error);
        }).finally(() => {
          this.getMenuTree();
        });
      }).catch(() => {
        this.$message({
          message: "取消删除菜单信息记录!",
          type: "info"
        });
      });
    },
    addRecord() {
      this.menuFormFlag = true;
      this.updateMenuFlag = false;
    },
    updateRecord() {
      if (this.selection.length === 0) {
        this.$message({
          message: "请选择菜单信息记录!",
          type: "info"
        });
      } else if (this.selection.length > 1) {
        this.$message({
          message: "勾选记录数大于1!",
          type: "info"
        });
      } else {
        this.updateMenu(this.selection[0].id);
      }
    },
    deleteRecords() {
      if (this.selection.length === 0) {
        this.$message({
          message: "请选择菜单信息记录",
          type: "info"
        });
      } else {
        this.$confirm("此操作将删除菜单信息记录, 是否继续?", "提示信息", {
          confirmButtonText: "确定",
          cancelButtonText: "取消"
        }).then(() => {
          deleteMenu(this.selection.map(menu => menu.id)).then(res => {
            this.$message({
              message: res.data.message,
              type: res.data.code === 200 ? "success" : "error"
            });
          }).catch(error => {
            console.log(error);
          }).finally(() => {
            this.getMenuTree();
          });
        }).catch(() => {
          this.$message({
            message: "取消删除菜单信息记录!",
            type: "info"
          });
        });
      }
    },
    cancelSubmitMenuInfo() {
      this.menuInfo = {
        parentId: -1,
        menuName: "",
        url: "",
        perms: "",
        icon: "",
        type: 0 + "",
        status: 1
      };
      this.menuFormFlag = false;
      this.updateMenuFlag = false;
    },
    submitMenuInfo(form) {
      this.$refs[form].validate(valid => {
        if (valid) {
          this.menuInfo.status = this.menuStatus ? 1 : 0;
          this.doSubmitMenuInfo();
        } else {
          this.$message({
            message: "参数校验未通过, 请重新输入!",
            type: "info"
          });
        }
      });
    },
    doSubmitMenuInfo() {
      if (this.updateMenuFlag) {
        updateMenu(this.menuInfo).then(res => {
          this.$message({
            message: res.data.message,
            type: res.data.code === 200 ? "success" : "error"
          });
        }).catch(error => {
          console.log(error);
        }).finally(() => {
          this.cancelSubmitMenuInfo();
          this.getMenuTree();
        });
      } else {
        addMenu(this.menuInfo).then(res => {
          this.$message({
            message: res.data.message,
            type: res.data.code === 200 ? "success" : "error"
          });
        }).catch(error => {
          console.log(error);
        }).finally(() => {
          this.cancelSubmitMenuInfo();
          this.getMenuTree();
        });
      }
    },
    triggerSubmitMenuInfo() {
      this.$refs.submitMenuInfo.$emit("click");
    }
  }
};
</script>

<style scoped>

</style>