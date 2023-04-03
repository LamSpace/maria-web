<template>
  <div>
    <el-breadcrumb separator="/" style="padding-left: 10px; padding-bottom: 10px; font-size: 12px;">
      <el-breadcrumb-item :to="{ path: '/frame' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>系统管理</el-breadcrumb-item>
      <el-breadcrumb-item>配置管理</el-breadcrumb-item>
    </el-breadcrumb>

    <el-card class="box-card" :style="{height: getHeight()}">
      <el-form :inline="true" :model="configForm" class="demo-form-inline">
        <el-form-item label="配置键" size="small">
          <el-input clearable v-model="configForm.configKey" placeholder="请输入配置键"
                    @keyup.enter.native="triggerLoadConfigList" />
        </el-form-item>

        <el-form-item label="配置名称" size="small">
          <el-input clearable v-model="configForm.configName" placeholder="请输入配置名称"
                    @keyup.enter.native="triggerLoadConfigList" />
        </el-form-item>

        <el-form-item label="配置描述" size="small">
          <el-input clearable v-model="configForm.configDescription" placeholder="请输入配置描述信息"
                    @keyup.enter.native="triggerLoadConfigList" />
        </el-form-item>
      </el-form>

      <el-form :inline="true" class="demo-form-inline">
        <el-form-item size="small">
          <el-button icon="el-icon-refresh" @click="reset">重置条件</el-button>
          <el-button icon="el-icon-search" type="success" @click="getConfigList" ref="loadConfigList">
            查询记录
          </el-button>
          <el-button icon="el-icon-plus" type="primary" @click="addRecord"
                     v-has-permission="'config@add'">添加记录
          </el-button>
          <el-button icon="el-icon-edit" type="warning" @click="updateRecord"
                     v-has-permission="'config@update'">修改记录
          </el-button>
          <el-button icon="el-icon-delete" type="danger" @click="deleteRecords"
                     v-has-permission="'config@delete'">删除记录
          </el-button>
        </el-form-item>
      </el-form>

      <el-table :data="configList" border v-loading="loading" style="width: 100%"
                :row-style="{height: '35px'}" :cell-style="{padding: '0px'}"
                :header-row-style="{height: '35px'}" :header-cell-style="{padding: '0px'}"
                @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" fixed align="center" />
        <el-table-column type="index" label="序号" width="60" fixed align="center" />
        <el-table-column prop="configKey" label="配置键" width="200" align="center" show-overflow-tooltip />
        <el-table-column prop="configName" label="配置名称" width="200" align="center" show-overflow-tooltip />
        <el-table-column prop="configValue" label="配置值" width="300" align="center" show-overflow-tooltip />
        <el-table-column prop="updatable" label="可修改" width="80" align="center">
          <template slot-scope="scope">
            <i :class="scope.row.updatable === 1 ? 'el-icon-circle-check' : 'el-icon-circle-close'" />
          </template>
        </el-table-column>
        <el-table-column prop="deletable" label="可删除" width="80" align="center">
          <template slot-scope="scope">
            <i :class="scope.row.deletable === 1 ? 'el-icon-circle-check' : 'el-icon-circle-close'" />
          </template>
        </el-table-column>
        <el-table-column prop="configDescription" label="配置描述" align="center" show-overflow-tooltip>
          <template slot-scope="scope">
            {{ scope.row.configDescription === "" ? "-" : scope.row.configDescription }}
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注信息" align="center" show-overflow-tooltip>
          <template slot-scope="scope">
            {{ scope.row.remark === "" ? "-" : scope.row.remark }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right" align="center">
          <template slot-scope="scope">
            <el-tooltip content="更新系统配置" placement="top">
              <el-button type="primary" size="mini" icon="el-icon-edit"
                         @click="updateConfig(scope.row)" v-has-permission="'config@update'" />
            </el-tooltip>
            <el-tooltip content="删除系统配置" placement="top">
              <el-button type="danger" size="mini" icon="el-icon-delete"
                         @click="deleteConfig(scope.row)" v-has-permission="'config@delete'" />
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination style="padding-top: 20px;" @size-change="handleSizeChange"
                     @current-change="handleCurrentChange"
                     :current-page="cur" :page-sizes="[15, 30]" :page-size="size"
                     layout="total, sizes, prev, pager, next, jumper" :total="total" />
    </el-card>

    <el-dialog :visible.sync="configFormFlag" :title="updateConfigFlag ? '修改系统配置信息':'添加系统配置信息'"
               :close-on-click-modal="false" :close-on-press-escape="false" :show-close="false">
      <el-form :model="configInfo" :rules="configInfoRule" ref="configInfoForm" label-width="100px"
               class="demo-ruleForm">
        <el-form-item label="配置键" prop="configKey" size="small">
          <el-input clearable v-model="configInfo.configKey" maxlength="300" :readonly="updateConfigFlag"
                    @keyup.enter.native="triggerSubmitConfigInfo" />
        </el-form-item>

        <el-form-item label="配置名称" prop="configName" size="small">
          <el-input clearable v-model="configInfo.configName" maxlength="300"
                    @keyup.enter.native="triggerSubmitConfigInfo" />
        </el-form-item>

        <el-form-item label="配置值" prop="configValue" size="small">
          <el-input clearable v-model="configInfo.configValue" maxlength="300"
                    @keyup.enter.native="triggerSubmitConfigInfo" />
        </el-form-item>

        <el-form-item label="配置描述" prop="configDescription" size="small">
          <el-input clearable v-model="configInfo.configDescription" maxlength="300"
                    @keyup.enter.native="triggerSubmitConfigInfo" />
        </el-form-item>

        <el-form-item label="备注信息" prop="remark" size="small">
          <el-input clearable v-model="configInfo.remark" maxlength="300"
                    @keyup.enter.native="triggerSubmitConfigInfo" />
        </el-form-item>

        <el-form-item label="可修改" size="small">
          <el-switch active-color="#13ce66" inactive-color="#ff4949" active-text="启用" inactive-text="禁用"
                     v-model="updateStatus" />
        </el-form-item>

        <el-form-item label="可删除" size="small">
          <el-switch active-color="#13ce66" inactive-color="#ff4949" active-text="启用" inactive-text="禁用"
                     v-model="deleteStatus" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="cancelSubmitConfigInfo">取消</el-button>
        <el-button type="primary" size="small" @click="submitConfigInfo('configInfoForm')"
                   ref="submitConfigInfo">提交
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getConfigList, addConfig, updateConfig, getConfig, deleteConfig } from "@/api/system/config/index";

export default {
  name: "Index",
  data() {
    const checkConfigKey = (rule, value, callback) => {
      if (!value || value.length === 0) {
        callback(new Error("系统配置键不能为空!"));
      }
      if (value.length > 0 && value.trim() === "") {
        callback(new Error("系统配置键不能为空格!"));
      }
      if (value.trim() !== value) {
        callback(new Error("系统配置键前后不能包含空格!"));
      }
      callback();
    };
    const checkConfigName = (rule, value, callback) => {
      if (!value || value.length === 0) {
        callback(new Error("系统配置名称不能为空!"));
      }
      if (value.length > 0 && value.trim() === "") {
        callback(new Error("系统配置名称不能为空格!"));
      }
      if (value.trim() !== value) {
        callback(new Error("系统配置名称前后不能包含空格!"));
      }
      callback();
    };
    const checkConfigValue = (rule, value, callback) => {
      if (!value || value.length === 0) {
        callback(new Error("系统配置值不能为空!"));
      }
      if (value.length > 0 && value.trim() === "") {
        callback(new Error("系统配置值不能为空格!"));
      }
      if (value.trim() !== value) {
        callback(new Error("系统配置值前后不能包含空格!"));
      }
      callback();
    };
    const checkConfigDescription = (rule, value, callback) => {
      if (!value || value.length === 0) {
        callback();
      }
      if (value.length > 0 && value.trim() === "") {
        callback(new Error("系统配置描述不能为空格!"));
      }
      if (value.trim() !== value) {
        callback(new Error("系统配置描述前后不能包含空格!"));
      }
      callback();
    };
    const checkRemark = (rule, value, callback) => {
      if (!value || value.length === 0) {
        callback();
      }
      if (value.length > 0 && value.trim() === "") {
        callback(new Error("系统配置备注不能为空格!"));
      }
      if (value.trim() !== value) {
        callback(new Error("系统配置备注前后不能包含空格!"));
      }
      callback();
    };
    return {
      height: "0px",
      configForm: {
        configKey: "",
        configName: "",
        configDescription: ""
      },
      configList: [],
      loading: false,
      cur: 1,
      size: 15,
      total: 0,
      selection: [],
      configFormFlag: false,
      updateConfigFlag: false,
      configInfo: {
        configKey: "",
        configName: "",
        configValue: "",
        updatable: 1,
        deletable: 1,
        configDescription: "",
        remark: ""
      },
      configInfoRule: {
        configKey: [
          {
            required: true, validator: checkConfigKey, trigger: "blur"
          }
        ],
        configName: [
          {
            required: true, validator: checkConfigName, trigger: "blur"
          }
        ],
        configValue: [
          {
            required: true, validator: checkConfigValue, trigger: "blur"
          }
        ],
        configDescription: [
          {
            required: false, validator: checkConfigDescription, trigger: "blur"
          }
        ],
        remark: [
          {
            required: false, validator: checkRemark, trigger: "blur"
          }
        ]
      },
      deleteStatus: true,
      updateStatus: true
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
    this.getConfigList();
  },
  methods: {
    getHeight() {
      return this.height + "px";
    },
    handleSelectionChange(val) {
      this.selection = val;
    },
    handleSizeChange(val) {
      this.size = val;
      this.getConfigList();
    },
    handleCurrentChange(val) {
      this.cur = val;
      this.getConfigList();
    },
    getConfigList() {
      this.loading = true;
      getConfigList(this.cur, this.size, this.configForm).then(res => {
        if (res.data.code === 200) {
          this.configList = res.data.data.list;
          this.total = res.data.data.total;
        } else {
          this.$message({
            message: res.data.message,
            type: res.data.code === 200 ? "success" : "error"
          });
        }
      }).catch(error => {
        console.log(error);
      }).finally(() => {
        this.loading = false;
      });
    },
    updateConfig(row) {
      if (row.updatable === 1) {
        getConfig(row.id).then(res => {
          if (res.data.code === 200) {
            this.configInfo = { ...res.data.data.config };
            this.deleteStatus = this.configInfo.deletable === 1;
            this.updateStatus = this.configInfo.updatable === 1;
          } else {
            this.$message({
              message: res.data.message,
              type: "error"
            });
          }
        }).catch(error => {
          console.log(error);
        }).finally(() => {
          this.configFormFlag = true;
          this.updateConfigFlag = true;
        });
      } else {
        this.$message({
          message: "系统配置不允许修改!",
          type: "info"
        });
      }
    },
    deleteConfig(row) {
      if (row.deletable === 1) {
        this.$confirm("此操作将删除系统配置信息记录, 是否继续?", "提示信息", {
          confirmButtonText: "确定",
          cancelButtonText: "取消"
        }).then(() => {
          deleteConfig([id]).then(res => {
            this.$message({
              message: res.data.message,
              type: res.data.code === 200 ? "success" : "error"
            });
          }).catch(error => {
            console.log(error);
          }).finally(() => {
            this.getConfigList();
          });
        }).catch(() => {
          this.$message({
            message: "取消删除系统配置信息记录!",
            type: "info"
          });
        });
      } else {
        this.$message({
          message: "系统配置不允许删除!",
          type: "info"
        });
      }
    },
    reset() {
      this.cur = 1;
      this.size = 15;
      this.configForm = {
        configKey: "",
        configName: "",
        configDescription: ""
      };
      this.getConfigList();
    },
    addRecord() {
      this.configFormFlag = true;
      this.updateConfigFlag = false;
    },
    updateRecord() {
      if (this.selection.length === 0) {
        this.$message({
          message: "请选择系统配置信息记录!",
          type: "info"
        });
      } else if (this.selection.length > 1) {
        this.$message({
          message: "勾选记录数大于1!",
          type: "info"
        });
      } else {
        this.updateConfig(this.selection[0].id);
      }
    },
    deleteRecords() {
      if (this.selection.length === 0) {
        this.$message({
          message: "请选择系统配置信息记录!",
          type: "info"
        });
      } else {
        this.$confirm("此操作将删除系统配置信息记录, 是否继续?", "提示信息", {
          confirmButtonText: "确定",
          cancelButtonText: "取消"
        }).then(() => {
          deleteConfig(this.selection.map(config => config.id)).then(res => {
            this.$message({
              message: res.data.message,
              type: res.data.code === 200 ? "success" : "error"
            });
          }).catch(error => {
            console.log(error);
          }).finally(() => {
            this.getConfigList();
          });
        }).catch(() => {
          this.$message({
            message: "取消删除系统配置信息记录!",
            type: "info"
          });
        });
      }
    },
    cancelSubmitConfigInfo() {
      this.configFormFlag = false;
      this.updateConfigFlag = false;
      this.configInfo = {
        configKey: "",
        configName: "",
        configValue: "",
        updatable: 1,
        deletable: 1,
        configDescription: "",
        remark: ""
      };
      this.deleteStatus = true;
      this.updateStatus = true;
    },
    submitConfigInfo(form) {
      this.$refs[form].validate(valid => {
        if (valid) {
          this.configInfo.deletable = this.deleteStatus ? 1 : 0;
          this.configInfo.updatable = this.updateStatus ? 1 : 0;
          this.doSubmitConfigInfo();
        } else {
          this.$message({
            message: "参数校验未通过, 请重新输入!",
            type: "info"
          });
        }
      });
    },
    doSubmitConfigInfo() {
      if (this.updateConfigFlag) {
        updateConfig(this.configInfo).then(res => {
          this.$message({
            message: res.data.message,
            type: res.data.code === 200 ? "success" : "error"
          });
        }).catch(error => {
          console.log(error);
        }).finally(() => {
          this.cancelSubmitConfigInfo();
          this.getConfigList();
        });
      } else {
        addConfig(this.configInfo).then(res => {
          this.$message({
            message: res.data.message,
            type: res.data.code === 200 ? "success" : "error"
          });
        }).catch(error => {
          console.log(error);
        }).finally(() => {
          this.cancelSubmitConfigInfo();
          this.getConfigList();
        });
      }
    },
    triggerLoadConfigList() {
      this.$refs.loadConfigList.$emit("click");
    },
    triggerSubmitConfigInfo() {
      this.$refs.submitConfigInfo.$emit("click");
    }
  }
};
</script>

<style scoped lang="less">

</style>