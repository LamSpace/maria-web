<template>
  <div>
    <el-breadcrumb separator="/" style="padding-left: 10px; padding-bottom: 10px; font-size: 12px;">
      <el-breadcrumb-item :to="{ path: '/frame' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>系统管理</el-breadcrumb-item>
      <el-breadcrumb-item>字典类型</el-breadcrumb-item>
    </el-breadcrumb>

    <el-card class="box-card" :style="{height: getHeight()}">
      <el-form :inline="true" :model="dictionaryTypeForm" class="demo-form-inline">
        <el-form-item label="类型名称" size="small">
          <el-input clearable v-model="dictionaryTypeForm.dictionaryName"
                    placeholder="请输入数据字典类型名称"
                    @keyup.enter.native="triggerLoadDictionaryTypeList" />
        </el-form-item>

        <el-form-item label="类型值" size="small">
          <el-input clearable v-model="dictionaryTypeForm.dictionaryType"
                    placeholder="请输入数据字典类型值" @keyup.enter.native="triggerLoadDictionaryTypeList" />
        </el-form-item>

        <el-form-item label="备注信息" size="small">
          <el-input clearable v-model="dictionaryTypeForm.remark" placeholder="请输入数据字典描述信息"
                    @keyup.enter.native="triggerLoadDictionaryTypeList" />
        </el-form-item>
      </el-form>

      <el-form :inline="true" class="demo-form-inline">
        <el-form-item size="small">
          <el-button icon="el-icon-refresh" @click="reset">重置条件</el-button>
          <el-button icon="el-icon-search" type="success" @click="getDictionaryTypeList"
                     ref="loadDictionaryTypeList">查询记录
          </el-button>
          <el-button icon="el-icon-plus" type="primary" @click="addRecord"
                     v-has-permission="'dictionaryType@add'">添加记录
          </el-button>
          <el-button icon="el-icon-edit" type="warning" @click="updateRecord"
                     v-has-permission="'dictionaryType@update'">修改记录
          </el-button>
          <el-button icon="el-icon-delete" type="danger" @click="deleteRecords"
                     v-has-permission="'dictionaryType@delete'">删除记录
          </el-button>
        </el-form-item>
      </el-form>

      <el-table :data="dictionaryTypeList" border v-loading="loading" style="width: 100%"
                :row-style="{height: '35px'}" :cell-style="{padding: '0px'}"
                :header-row-style="{height: '35px'}" :header-cell-style="{padding: '0px'}"
                @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" fixed align="center" />
        <el-table-column type="index" label="序号" width="60" fixed align="center" />
        <el-table-column prop="dictionaryName" label="类型名称" width="300" fixed align="center" />
        <el-table-column prop="dictionaryType" label="类型值" width="300" align="center"
                         show-overflow-tooltip />
        <el-table-column prop="enabled" label="类型状态" width="100" align="center">
          <template slot-scope="scope">
            {{ scope.row.enabled === 1 ? "启用" : "禁用" }}
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注信息" align="center" show-overflow-tooltip />
        <el-table-column label="操作" width="120" fixed="right" align="center">
          <template slot-scope="scope">
            <el-tooltip content="修改类型信息" placement="top">
              <el-button size="mini" type="primary" icon="el-icon-edit"
                         @click="updateDictionaryType(scope.row.id)"
                         v-has-permission="'dictionaryType@update'" />
            </el-tooltip>
            <el-tooltip content="删除类型信息" placement="top">
              <el-button size="mini" type="danger" icon="el-icon-delete"
                         @click="deleteDictionaryType(scope.row.id)"
                         v-has-permission="'dictionaryType@delete'" />
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination style="padding-top: 20px;" @size-change="handleSizeChange"
                     @current-change="handleCurrentChange"
                     :current-page="cur" :page-sizes="[15, 30]" :page-size="size"
                     layout="total, sizes, prev, pager, next, jumper" :total="total" />
    </el-card>

    <el-dialog :visible.sync="dictionaryFormFlag"
               :title="updateDictionaryFlag ? '修改数据字典类型' : '添加数据字典类型'" :close-on-click-modal="false"
               :close-on-press-escape="false" :show-close="false">
      <el-form :model="dictionaryTypeInfo" :rules="dictionaryTypeInfoRule" ref="dictionaryTypeInfoForm"
               label-width="100px" class="demo-ruleForm">
        <el-form-item label="类型名称" prop="dictionaryName" size="small">
          <el-input clearable maxlength="30" v-model="dictionaryTypeInfo.dictionaryName"
                    @keyup.enter.native="triggerSubmitDictionaryTypeInfo" />
        </el-form-item>
        <el-form-item label="类型值" prop="dictionaryType" size="small">
          <el-input clearable maxlength="30" v-model="dictionaryTypeInfo.dictionaryType"
                    @keyup.enter.native="triggerSubmitDictionaryTypeInfo" />
        </el-form-item>
        <el-form-item label="备注信息" prop="remark" size="small">
          <el-input clearable maxlength="120" v-model="dictionaryTypeInfo.remark"
                    @keyup.enter.native="triggerSubmitDictionaryTypeInfo" />
        </el-form-item>
        <el-form-item label="类型状态" size="small">
          <el-switch active-color="#13ce66" inactive-color="#ff4949" active-text="启用" inactive-text="禁用"
                     v-model="dictionaryTypeStatus" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="cancelSubmitDictionaryTypeInfo">取消</el-button>
        <el-button type="primary" size="small" @click="submitDictionaryTypeInfo('dictionaryTypeInfoForm')"
                   ref="submitDictionaryTypeInfo">提交
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getDictionaryTypes,
  addDictionaryType,
  updateDictionaryType,
  getDictionaryType,
  deleteDictionaryType
} from "@/api/system/dictionary/index";

export default {
  name: "Index",
  data() {
    const checkDictionaryName = (rule, value, callback) => {
      if (!value || value.length === 0) {
        callback(new Error("数据字典类型名称不能为空!"));
      }
      if (value.length > 0 && value.trim() === "") {
        callback(new Error("数据字典类型名称不能为空格!"));
      }
      if (value.trim() !== value) {
        callback(new Error("数据字典类型名称前后不能包含空格!"));
      }
      callback();
    };
    const checkDictionaryType = (rule, value, callback) => {
      if (!value || value.length === 0) {
        callback(new Error("数据字典类型值不能为空!"));
      }
      if (value.length > 0 && value.trim() === "") {
        callback(new Error("数据字典类型值不能为空格!"));
      }
      if (value.trim() !== value) {
        callback(new Error("数据字典类型值前后不能包含空格!"));
      }
      callback();
    };
    const checkRemark = (rule, value, callback) => {
      if (!value || value.length === 0) {
        callback();
      }
      if (value.length > 0 && value.trim() === "") {
        callback(new Error("数据字典类型备注不能为空格!"));
      }
      if (value.trim() !== value) {
        callback(new Error("数据字典类型备注前后不能包含空格!"));
      }
      callback();
    };
    return {
      height: "0px",
      dictionaryTypeForm: {
        dictionaryName: "",
        dictionaryType: "",
        remark: ""
      },
      selection: [],
      dictionaryTypeList: [],
      loading: false,
      cur: 1,
      size: 15,
      total: 0,
      dictionaryFormFlag: false,
      updateDictionaryFlag: false,
      dictionaryTypeInfo: {
        dictionaryName: "",
        dictionaryType: "",
        enabled: 1,
        remark: ""
      },
      dictionaryTypeInfoRule: {
        dictionaryName: [
          {
            required: true, validator: checkDictionaryName, trigger: "blur"
          }
        ],
        dictionaryType: [
          {
            required: true, validator: checkDictionaryType, trigger: "blur"
          }
        ],
        remark: [
          {
            required: false, validator: checkRemark, trigger: "blur"
          }
        ]
      },
      dictionaryTypeStatus: true
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
    this.getDictionaryTypeList();
  },
  methods: {
    getHeight() {
      return this.height + "px";
    },
    reset() {
      this.dictionaryTypeForm = {
        dictionaryName: "",
        dictionaryType: "",
        remark: ""
      };
      this.getDictionaryTypeList();
    },
    getDictionaryTypeList() {
      this.loading = true;
      getDictionaryTypes(this.cur, this.size, this.dictionaryTypeForm).then(res => {
        if (res.data.code === 200) {
          this.total = res.data.data.total;
          this.dictionaryTypeList = res.data.data.list;
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
    addRecord() {
      this.dictionaryFormFlag = true;
      this.updateDictionaryFlag = false;
    },
    updateRecord() {
      if (this.selection.length === 0) {
        this.$message({
          message: "请选择数据字典类型信息记录!",
          type: "info"
        });
      } else if (this.selection.length > 1) {
        this.$message({
          message: "勾选记录数大于1!",
          type: "info"
        });
      } else {
        this.updateDictionaryType(this.selection[0].id);
      }
    },
    deleteRecords() {
      if (this.selection.length === 0) {
        this.$message({
          message: "请选择数据字典类型信息记录!",
          type: "info"
        });
      } else {
        this.$confirm("此操作将删除数据字典类型信息记录, 是否继续?", "提示信息", {
          confirmButtonText: "确定",
          cancelButtonText: "取消"
        }).then(() => {
          deleteDictionaryType(this.selection.map(type => type.id)).then(res => {
            this.$message({
              message: res.data.message,
              type: res.data.code === 200 ? "success" : "error"
            });
          }).catch(error => {
            console.log(error);
          }).finally(() => {
            this.getDictionaryTypeList();
          });
        }).catch(() => {
          this.$message({
            message: "取消删除数据字典信息记录!",
            type: "info"
          });
        });
      }
    },
    handleSelectionChange(val) {
      this.selection = val;
    },
    handleSizeChange(val) {
      this.size = val;
      this.getDictionaryTypeList();
    },
    handleCurrentChange(val) {
      this.cur = val;
      this.getDictionaryTypeList();
    },
    cancelSubmitDictionaryTypeInfo() {
      this.dictionaryFormFlag = false;
      this.updateDictionaryFlag = false;
      this.dictionaryTypeInfo = {
        dictionaryName: "",
        dictionaryType: "",
        enabled: 1,
        remark: ""
      };
      this.dictionaryTypeStatus = true;
    },
    submitDictionaryTypeInfo(form) {
      this.$refs[form].validate(valid => {
        if (valid) {
          this.dictionaryTypeInfo.enabled = this.dictionaryTypeStatus ? 1 : 0;
          this.doSubmitDictionaryTypeInfo();
        } else {
          this.$message({
            message: "参数校验未通过, 请重新输入!",
            type: "info"
          });
        }
      });
    },
    doSubmitDictionaryTypeInfo() {
      if (this.updateDictionaryFlag) {
        updateDictionaryType(this.dictionaryTypeInfo).then(res => {
          this.$message({
            message: res.data.message,
            type: res.data.code === 200 ? "success" : "error"
          });
        }).catch(error => {
          console.log(error);
        }).finally(() => {
          this.cancelSubmitDictionaryTypeInfo();
          this.getDictionaryTypeList();
        });
      } else {
        addDictionaryType(this.dictionaryTypeInfo).then(res => {
          this.$message({
            message: res.data.message,
            type: res.data.code === 200 ? "success" : "error"
          });
        }).catch(error => {
          console.log(error);
        }).finally(() => {
          this.cancelSubmitDictionaryTypeInfo();
          this.getDictionaryTypeList();
        });
      }
    },
    updateDictionaryType(id) {
      getDictionaryType(id).then(res => {
        if (res.data.code === 200) {
          this.dictionaryTypeInfo = { ...res.data.data.type };
          this.dictionaryTypeStatus = this.dictionaryTypeInfo.enabled === 1;
        } else {
          this.$message({
            message: res.data.message,
            type: "error"
          });
        }
      }).catch(error => {
        console.log(error);
      }).finally(() => {
        this.dictionaryFormFlag = true;
        this.updateDictionaryFlag = true;
      });
    },
    deleteDictionaryType(id) {
      this.$confirm("此操作将删除数据字典类型信息记录, 是否继续?", "提示信息", {
        confirmButtonText: "确定",
        cancelButtonText: "取消"
      }).then(() => {
        deleteDictionaryType([id]).then(res => {
          this.$message({
            message: res.data.message,
            type: res.data.code === 200 ? "success" : "error"
          });
        }).catch(error => {
          console.log(error);
        }).finally(() => {
          this.getDictionaryTypeList();
        });
      }).catch(() => {
        this.$message({
          message: "取消删除数据字典信息记录!",
          type: "info"
        });
      });
    },
    triggerLoadDictionaryTypeList() {
      this.$refs.loadDictionaryTypeList.$emit("click");
    },
    triggerSubmitDictionaryTypeInfo() {
      this.$refs.submitDictionaryTypeInfo.$emit("click");
    }
  }
};
</script>

<style scoped lang="less">

</style>