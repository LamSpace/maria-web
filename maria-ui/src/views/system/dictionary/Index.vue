<template>
  <div>
    <el-breadcrumb separator="/" style="padding-left: 10px; padding-bottom: 10px; font-size: 12px;">
      <el-breadcrumb-item :to="{ path: '/frame' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>系统管理</el-breadcrumb-item>
      <el-breadcrumb-item>数据字典</el-breadcrumb-item>
    </el-breadcrumb>

    <el-card class="box-card" :style="{height: getHeight()}">
      <el-form :inline="true" :model="dictionaryForm" class="demo-form-inline">
        <el-form-item label="字典标签" size="small">
          <el-input clearable v-model="dictionaryForm.dictionaryLabel" placeholder="请输入数据字典标签"
                    @keyup.enter.native="triggerLoadDictionaryList" />
        </el-form-item>

        <el-form-item label="字典键值" size="small">
          <el-input clearable v-model="dictionaryForm.dictionaryName" placeholder="请输入数据字典键值"
                    @keyup.enter.native="triggerLoadDictionaryList" />
        </el-form-item>

        <el-form-item label="字典类型" size="small">
          <el-select v-model="dictionaryForm.dictionaryType" placeholder="请选择数据字典类型">
            <!--suppress JSUnresolvedVariable -->
            <el-option v-for="item in dictionaryTypes" :key="item.dictionary_type"
                       :label="item.dictionary_name"
                       :value="item.dictionary_type" />
          </el-select>
        </el-form-item>

        <el-form-item label="字典描述" size="small">
          <el-input clearable v-model="dictionaryForm.description"
                    placeholder="请输入数据字典描述信息" @keyup.enter.native="triggerLoadDictionaryList" />
        </el-form-item>
      </el-form>

      <el-form :inline="true" class="demo-form-inline">
        <el-form-item size="small">
          <el-button icon="el-icon-refresh" @click="reset">重置条件</el-button>
          <el-button icon="el-icon-search" type="success" @click="getDictionaryList" ref="loadDictionaryList">
            查询记录
          </el-button>
          <el-button icon="el-icon-plus" type="primary" @click="addRecord"
                     v-has-permission="'dictionary@add'">添加记录
          </el-button>
          <el-button icon="el-icon-edit" type="warning" @click="updateRecord"
                     v-has-permission="'dictionary@update'">修改记录
          </el-button>
          <el-button icon="el-icon-delete" type="danger" @click="deleteRecords"
                     v-has-permission="'dictionary@delete'">删除记录
          </el-button>
        </el-form-item>
      </el-form>

      <el-table :data="dictionaryList" border v-loading="loading" style="width: 100%"
                :row-style="{height: '35px'}" :cell-style="{padding: '0px'}"
                :header-row-style="{height: '35px'}" :header-cell-style="{padding: '0px'}"
                @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" fixed align="center" />
        <el-table-column type="index" label="序号" width="60" fixed align="center" />
        <el-table-column prop="dictionaryLabel" label="字典标签" fixed width="200" align="center"
                         show-overflow-tooltip />
        <el-table-column prop="dictionaryValue" label="字典键值" width="250" align="center"
                         show-overflow-tooltip />
        <el-table-column prop="dictionaryType" label="字典类型" width="200" align="center"
                         show-overflow-tooltip>
          <template slot-scope="scope">
            {{ scope.row.dictionaryType | typeHandler(dictionaryTypes) }}
          </template>
        </el-table-column>
        <el-table-column prop="orderNum" label="字典排序" width="100" align="center" />
        <el-table-column prop="description" label="字典描述" width="300" align="center">
          <template slot-scope="scope">
            {{ scope.row.description === "" ? "-" : scope.row.description }}
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注信息" width="200" align="center" show-overflow-tooltip>
          <template slot-scope="scope">
            {{ scope.row.remark === "" ? "-" : scope.row.remark }}
          </template>
        </el-table-column>
        <el-table-column prop="enabled" label="字典状态" width="90" align="center">
          <tamplate slot-scope="scope">
            {{ scope.row.enabled === 1 ? "启用" : "禁用" }}
          </tamplate>
        </el-table-column>
        <el-table-column prop="cssClass" label="字典样式" width="200" align="center" show-overflow-tooltip>
          <template slot-scope="scope">
            {{ scope.row.cssClass === "" ? "暂无样式" : scope.row.cssClass }}
          </template>
        </el-table-column>
        <el-table-column prop="isDefault" label="是否默认" width="100" align="center">
          <template slot-scope="scope">
            {{ scope.row.isDefault === 1 ? "是" : "否" }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right" align="center">
          <template slot-scope="scope">
            <el-tooltip content="修改数据字典" placement="top">
              <el-button size="mini" icon="el-icon-edit" type="primary"
                         @click="updateDictionary(scope.row.id)" v-has-permission="'dictionary@update'" />
            </el-tooltip>
            <el-tooltip content="删除数据字典" placement="top">
              <el-button size="mini" icon="el-icon-delete" type="danger"
                         @click="deleteDictionary(scope.row.id)" v-has-permission="'dictionary@delete'" />
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
               :title="updateDictionaryFlag ? '修改数据字典信息' : '添加数据字典信息'" :close-on-click-modal="false"
               :close-on-press-escape="false" :show-close="false">
      <el-form :model="dictionaryInfo" :rules="dictionaryInfoRule" ref="dictionaryInfoForm" label-width="100px"
               class="demo-ruleForm">
        <el-form-item label="字典标签" prop="dictionaryLabel" size="small">
          <el-input clearable maxlength="100" v-model="dictionaryInfo.dictionaryLabel"
                    @keyup.enter.native="triggerSubmitDictionaryInfo" />
        </el-form-item>
        <el-form-item label="字典键值" prop="dictionaryValue" size="small">
          <el-input clearable maxlength="200" v-model="dictionaryInfo.dictionaryValue"
                    @keyup.enter.native="triggerSubmitDictionaryInfo" />
        </el-form-item>
        <el-form-item label="字典类型" prop="dictionaryType" size="small">
          <el-select placeholder="请选择数据字典类型" style="width: 100%;"
                     v-model="dictionaryInfo.dictionaryType">
            <!--suppress JSUnresolvedVariable -->
            <el-option v-for="item in dictionaryTypes" :key="item.dictionary_type"
                       :label="item.dictionary_name"
                       :value="item.dictionary_type" />
          </el-select>
        </el-form-item>
        <el-form-item label="字典描述" prop="description" size="small">
          <el-input clearable maxlength="300" v-model="dictionaryInfo.description"
                    @keyup.enter.native="triggerSubmitDictionaryInfo" />
        </el-form-item>
        <el-form-item label="备注信息" prop="remark" size="small">
          <el-input clearable maxlength="300" v-model="dictionaryInfo.remark"
                    @keyup.enter.native="triggerSubmitDictionaryInfo" />
        </el-form-item>
        <el-form-item label="CSS样式" size="small">
          <el-input :readonly="true" placeholder="暂不支持" v-model="dictionaryInfo.cssClass" />
        </el-form-item>
        <el-form-item label="是否默认" size="small">
          <el-switch active-color="#13ce66" inactive-color="#ff4949" active-text="是" inactive-text="否"
                     v-model="isDefaultStatus" disabled />
        </el-form-item>
        <el-form-item label="字典状态" size="small">
          <el-switch active-color="#13ce66" inactive-color="#ff4949" active-text="启用" inactive-text="禁用"
                     v-model="dictionaryStatus" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="cancelSubmitDictionaryInfo">取消</el-button>
        <el-button type="primary" size="small" @click="submitDictionaryInfo('dictionaryInfoForm')"
                   ref="submitDictionaryInfo">提交
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  addDictionary,
  getAllDictionaryTypes,
  updateDictionary,
  getDictionary,
  deleteDictionary,
  getDictionaryList
} from "@/api/system/dictionary/index";

export default {
  name: "Index",
  data() {
    const checkDictionaryLabel = (rule, value, callback) => {
      if (!value || value.length === 0) {
        callback(new Error("字典标签不能为空!"));
      }
      if (value.length > 0 && value.trim() === "") {
        callback(new Error("字典标签不能为空格!"));
      }
      if (value.trim() !== value) {
        callback(new Error("字典标签键前后不能包含空格!"));
      }
      callback();
    };
    const checkDictionaryValue = (rule, value, callback) => {
      if (!value || value.length === 0) {
        callback(new Error("字典键值不能为空!"));
      }
      if (value.length > 0 && value.trim() === "") {
        callback(new Error("字典键值不能为空格!"));
      }
      if (value.trim() !== value) {
        callback(new Error("字典键值键前后不能包含空格!"));
      }
      callback();
    };
    const checkDictionaryType = (rule, value, callback) => {
      if (!value || value.length === 0) {
        callback(new Error("字典类型不能为空!"));
      }
      callback();
    };
    const checkDescription = (rule, value, callback) => {
      if (!value || value.length === 0) {
        callback();
      }
      if (value.length > 0 && value.trim() === "") {
        callback(new Error("字典描述不能为空格!"));
      }
      if (value.trim() !== value) {
        callback(new Error("字典描述前后不能包含空格!"));
      }
      callback();
    };
    const checkRemark = (rule, value, callback) => {
      if (!value || value.length === 0) {
        callback();
      }
      if (value.length > 0 && value.trim() === "") {
        callback(new Error("字典备注不能为空格!"));
      }
      if (value.trim() !== value) {
        callback(new Error("字典备注前后不能包含空格!"));
      }
      callback();
    };
    return {
      height: "0px",
      dictionaryForm: {
        dictionaryLabel: "",
        dictionaryValue: "",
        dictionaryType: "",
        description: ""
      },
      dictionaryList: [],
      dictionaryTypes: [],
      loading: false,
      selection: [],
      size: 15,
      cur: 1,
      total: 0,
      dictionaryFormFlag: false,
      updateDictionaryFlag: false,
      dictionaryInfo: {
        dictionaryLabel: "",
        dictionaryValue: "",
        dictionaryType: "",
        description: "",
        remark: "",
        cssClass: "",
        isDefault: 1,
        enabled: 1
      },
      dictionaryInfoRule: {
        dictionaryLabel: [
          {
            required: true, validator: checkDictionaryLabel, trigger: "blur"
          }
        ],
        dictionaryValue: [
          {
            required: true, validator: checkDictionaryValue, trigger: "blur"
          }
        ],
        dictionaryType: [
          {
            required: true, validator: checkDictionaryType, trigger: "change"
          }
        ],
        description: [
          {
            required: false, validator: checkDescription, trigger: "blur"
          }
        ],
        remark: [
          {
            required: false, validator: checkRemark, trigger: "blur"
          }
        ]
      },
      isDefaultStatus: true,
      dictionaryStatus: true
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
    getAllDictionaryTypes().then(res => {
      if (res.data.code === 200) {
        this.dictionaryTypes = res.data.data.list;
      } else {
        this.$message({
          message: res.data.message,
          type: "error"
        });
      }
    }).catch(error => {
      console.log(error);
    }).finally(() => {
      this.getDictionaryList();
    });
  },
  filters: {
    typeHandler(type, types) {
      if (types.length === 0) {
        return type;
      } else {
        for (let index = 0; index < types.length; index++) {
          // noinspection JSUnresolvedVariable
          if (type === types[index].dictionary_type) {
            // noinspection JSUnresolvedVariable
            return types[index].dictionary_name;
          }
        }
      }
    }
  },
  methods: {
    getHeight() {
      return this.height + "px";
    },
    reset() {
      this.dictionaryForm = {
        dictionaryLabel: "",
        dictionaryValue: "",
        dictionaryType: "",
        description: ""
      };
      this.getDictionaryList();
    },
    getDictionaryList() {
      this.loading = true;
      getDictionaryList(this.cur, this.size, this.dictionaryForm).then(res => {
        if (res.data.code === 200) {
          this.total = res.data.data.total;
          this.dictionaryList = res.data.data.list;
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
          message: "请选择数据字典信息记录!",
          type: "info"
        });
      } else if (this.selection.length > 1) {
        this.$message({
          message: "勾选记录数大于1!",
          type: "info"
        });
      } else {
        this.updateDictionary(this.selection[0].id);
      }
    },
    deleteRecords() {
      if (this.selection.length === 0) {
        this.$message({
          message: "请选择数据字典信息记录!",
          type: "info"
        });
      } else {
        this.$confirm("此操作将删除数据字典信息记录, 是否继续?", "提示信息", {
          confirmButtonText: "确定",
          cancelButtonText: "取消"
        }).then(() => {
          deleteDictionary(this.selection.map(dictionary => dictionary.id)).then(res => {
            this.$message({
              message: res.data.message,
              type: res.data.code === 200 ? "success" : "error"
            });
          }).catch(error => {
            console.log(error);
          }).finally(() => {
            this.getDictionaryList();
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
      this.getDictionaryList();
    },
    handleCurrentChange(val) {
      this.cur = val;
      this.getDictionaryList();
    },
    updateDictionary(id) {
      getDictionary(id).then(res => {
        if (res.data.code === 200) {
          this.dictionaryInfo = { ...res.data.data.dictionary };
          this.dictionaryStatus = this.dictionaryInfo.enabled === 1;
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
    deleteDictionary(id) {
      this.$confirm("此操作将删除数据字典信息记录, 是否继续?", "提示信息", {
        confirmButtonText: "确定",
        cancelButtonText: "取消"
      }).then(() => {
        deleteDictionary([id]).then(res => {
          this.$message({
            message: res.data.message,
            type: res.data.code === 200 ? "success" : "error"
          });
        }).catch(error => {
          console.log(error);
        }).finally(() => {
          this.getDictionaryList();
        });
      }).catch(() => {
        this.$message({
          message: "取消删除数据字典信息记录!",
          type: "info"
        });
      });
    },
    cancelSubmitDictionaryInfo() {
      this.dictionaryFormFlag = false;
      this.updateDictionaryFlag = false;
      this.dictionaryInfo = {
        dictionaryLabel: "",
        dictionaryValue: "",
        dictionaryType: "",
        description: "",
        remark: "",
        cssClass: "",
        isDefault: 1,
        enabled: 1
      };
      this.isDefaultStatus = true;
      this.dictionaryStatus = true;
    },
    submitDictionaryInfo(form) {
      this.$refs[form].validate(valid => {
        if (valid) {
          this.dictionaryInfo.enabled = this.dictionaryStatus ? 1 : 0;
          this.doSubmitDictionaryInfo();
        } else {
          this.$message({
            message: "参数校验我通过, 请重新输入!",
            type: "info"
          });
        }
      });
    },
    doSubmitDictionaryInfo() {
      if (this.updateDictionaryFlag) {
        updateDictionary(this.dictionaryInfo).then(res => {
          this.$message({
            message: res.data.message,
            type: res.data.code === 200 ? "success" : "error"
          });
        }).catch(error => {
          console.log(error);
        }).finally(() => {
          this.cancelSubmitDictionaryInfo();
          this.getDictionaryList();
        });
      } else {
        addDictionary(this.dictionaryInfo).then(res => {
          this.$message({
            message: res.data.message,
            type: res.data.code === 200 ? "success" : "error"
          });
        }).catch(error => {
          console.log(error);
        }).finally(() => {
          this.cancelSubmitDictionaryInfo();
          this.getDictionaryList();
        });
      }
    },
    triggerLoadDictionaryList() {
      this.$refs.loadDictionaryList.$emit("click");
    },
    triggerSubmitDictionaryInfo() {
      this.$refs.submitDictionaryInfo.$emit("click");
    }
  }
};
</script>

<style scoped lang="less">

</style>