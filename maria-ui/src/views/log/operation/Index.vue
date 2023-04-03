<template>
  <div>
    <el-breadcrumb separator="/" style="padding-left: 10px; padding-bottom: 10px; font-size: 12px;">
      <el-breadcrumb-item :to="{ path: '/frame' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>日志管理</el-breadcrumb-item>
      <el-breadcrumb-item>操作日志</el-breadcrumb-item>
    </el-breadcrumb>

    <el-card class="box-card" :style="{height: getHeight()}">
      <el-form :inline="true" :model="operationForm" class="demo-form-inline">
        <el-form-item label="用户账号" size="small">
          <el-input clearable v-model="operationForm.username" placeholder="请输入用户账号" />
        </el-form-item>

        <el-form-item label="用户IP" size="small">
          <el-input clearable v-model="operationForm.userIp" placeholder="请输入用户IP" />
        </el-form-item>

        <el-form-item label="是否成功" size="small">
          <el-select v-model="successValue" placeholder="请选择">
            <el-option v-for="item in selectOptions" :key="item.value" :label="item.label"
                       :value="item.value" />
          </el-select>
        </el-form-item>

        <el-form-item label="用户操作" size="small">
          <el-select v-model="userOperation" placeholder="请选择">
            <el-option v-for="item in operationMappings" :key="item.key" :label="item.value"
                       :value="item.key" />
          </el-select>
        </el-form-item>

        <el-form-item label="操作时间" size="small">
          <el-date-picker type="datetimerange" v-model="timespan"
                          :picker-options="pickerOptions" range-separator="至" start-placeholder="开始日期"
                          end-placeholder="结束日期" format="yyyy-MM-dd HH:mm:ss"
                          value-format="yyyy-MM-dd HH:mm:ss" align="right" />
        </el-form-item>
      </el-form>

      <el-form :inline="true" class="demo-form-inline">
        <el-form-item size="small">
          <el-button icon="el-icon-refresh" @click="reset">重置条件</el-button>
          <el-button icon="el-icon-search" type="success" @click="getLogList">查询记录</el-button>
          <el-button icon="el-icon-delete" type="danger" @click="deleteLogs">彻底清除</el-button>
        </el-form-item>
      </el-form>

      <el-scrollbar :style="{height: getTableHeight()}">
        <el-table :data="logList" border v-loading="loading" style="width: 100%"
                  :row-style="{height: '35px'}" :cell-style="{padding: '0px'}"
                  :header-row-style="{height: '35px'}" :header-cell-style="{padding: '0px'}">
          <el-table-column type="index" label="序号" width="60" fixed align="center" />
          <el-table-column prop="username" label="用户账号" width="150" fixed align="center"
                           show-overflow-tooltip />
          <el-table-column prop="success" label="是否成功" width="100" align="center">
            <template slot-scope="scope">
              {{ scope.row.success && scope.row.success === 1 ? "成功" : "失败" }}
            </template>
          </el-table-column>
          <el-table-column prop="exception" label="异常原因" width="300" align="center"
                           show-overflow-tooltip>
            <template slot-scope="scope">
              {{ scope.row.exception === "" ? "-" : scope.row.exception }}
            </template>
          </el-table-column>
          <el-table-column prop="operationTime" label="操作时间" width="150" align="center"
                           show-overflow-tooltip />
          <el-table-column prop="recordId" label="记录主键" width="230" align="center"
                           show-overflow-tooltip>
            <template slot-scope="scope">
              <el-tooltip content="点击查看记录痕迹" placement="top">
                <el-button size="mini" type="info" @click="traceRecord(scope.row.recordId)">
                  {{ scope.row.recordId === -1 ? "-" : scope.row.recordId }}
                </el-button>
              </el-tooltip>
            </template>
          </el-table-column>
          <el-table-column prop="operation" label="操作" width="100" align="center">
            <template slot-scope="scope">
              {{ scope.row.operation | operationTypeConverter(operationJson) }}
            </template>
          </el-table-column>
          <el-table-column prop="userIp" label="用户IP" width="150" align="center" show-overflow-tooltip>
            <template slot-scope="scope">
              {{ scope.row.userIp === "" ? "-" : scope.row.userIp }}
            </template>
          </el-table-column>
          <el-table-column prop="description" label="操作描述" align="center" show-overflow-tooltip>
            <template slot-scope="scope">
              {{ scope.row.description === "" ? "-" : scope.row.description }}
            </template>
          </el-table-column>
          <el-table-column type="expand" label="详情" width="60" fixed="right" align="center">
            <template slot-scope="scope">
              <el-form label-position="left" :inline="true" class="demo-table-expand" label-width="100px"
                       label-suffix=":">
                <el-form-item label="用户账号">
                                    <span :title="scope.row.username">
                                        {{ scope.row.username }}
                                    </span>
                </el-form-item>
                <el-form-item label="是否成功">
                                    <span :title="scope.row.success">
                                        {{ scope.row.success && scope.row.success === 1 ? "成功" : "失败" }}
                                    </span>
                </el-form-item>
                <el-form-item label="失败原因">
                                    <span :title="scope.row.exception">
                                        {{ scope.row.exception === "" ? "-" : scope.row.exception }}
                                    </span>
                </el-form-item>
                <el-form-item label="操作时间">
                                    <span :title="scope.row.operationTime">
                                        {{ scope.row.operationTime }}
                                    </span>
                </el-form-item>
                <el-form-item label="记录主键">
                                    <span :title="scope.row.recordId">
                                        {{ scope.row.recordId }}
                                    </span>
                </el-form-item>
                <el-form-item label="操作">
                                    <span :title="scope.row.operation">
                                        {{ scope.row.operation | operationTypeConverter(operationJson) }}
                                    </span>
                </el-form-item>
                <el-form-item label="用户IP">
                                    <span :title="scope.row.userIp">
                                        {{ scope.row.userIp }}
                                    </span>
                </el-form-item>
                <el-form-item label="操作描述">
                                    <span :title="scope.row.description">
                                        {{ scope.row.description }}
                                    </span>
                </el-form-item>
                <el-form-item label="操作方法">
                                    <span :title="scope.row.method">
                                        {{ scope.row.method }}
                                    </span>
                </el-form-item>
                <el-form-item label="方法参数">
                                    <span :title="scope.row.parameters">
                                        {{ scope.row.parameters }}
                                    </span>
                </el-form-item>
                <el-form-item label="浏览器名称">
                                    <span :title="scope.row.browserName">
                                        {{ scope.row.browserName === "" ? "-" : scope.row.browserName }}
                                    </span>
                </el-form-item>
                <el-form-item label="浏览器类型">
                                    <span :title="scope.row.browserType">
                                        {{ scope.row.browserType === "" ? "-" : scope.row.browserType }}
                                    </span>
                </el-form-item>
                <el-form-item label="浏览器版本">
                                    <span :title="scope.row.browserVersion">
                                        {{ scope.row.browserVersion === "" ? "-" : scope.row.browserVersion }}
                                    </span>
                </el-form-item>
                <el-form-item label="浏览器厂商">
                                    <span :title="scope.row.browserManufacturer">
                                        {{ scope.row.browserManufacturer === "" ? "-" : scope.row.browserManufacturer }}
                                    </span>
                </el-form-item>
                <el-form-item label="操作系统名称">
                                    <span :title="scope.row.osName">
                                        {{ scope.row.osName === "" ? "-" : scope.row.osName }}
                                    </span>
                </el-form-item>
                <el-form-item label="操作系统厂商">
                                    <span :title="scope.row.osManufacturer">
                                        {{ scope.row.osManufacturer === "" ? "-" : scope.row.osManufacturer }}
                                    </span>
                </el-form-item>
              </el-form>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination :current-page="cur" :page-sizes="[15, 30]" :page-size="size" :total="total"
                       layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
                       @current-change="handleCurrentChange" />
      </el-scrollbar>
    </el-card>

    <el-dialog :title="'操作痕迹'" :visible.sync="showOperationHistory"
               :close-on-click-modal="false" :close-on-press-escape="false">
      <el-timeline>
        <el-timeline-item v-for="item in recordHistory" :timestamp="item.operationTime" placement="top">
          <el-card>
            {{ item.username }} 在 IP 地址为 {{ item.userIp }} 的 {{ item.osName }} 系统上使用
            {{ item.browserName }} 浏览器远程访问系统{{ item.success ? "成功" : "未成功" }}
            [{{ item.description }}].
          </el-card>
        </el-timeline-item>
      </el-timeline>
    </el-dialog>
  </div>
</template>

<script>
import { getOperationLogList, getOperationMappings, traceRecord, deleteLogs } from "@/api/log/operation/index";

export default {
  name: "Index",
  data() {
    return {
      height: "0px",
      tableHeight: "0px",
      cur: 1,
      size: 15,
      total: 0,
      operationForm: {
        username: "",
        success: -1,
        operation: "",
        userIp: "",
        start: "",
        end: ""
      },
      logList: [],
      loading: false,
      operationMappings: [],
      operationJson: {},
      successValue: "",
      selectOptions: [{
        value: "-1",
        label: "全部"
      }, {
        value: "0",
        label: "失败"
      }, {
        value: "1",
        label: "成功"
      }],
      userOperation: "",
      timespan: "",
      pickerOptions: {
        shortcuts: [{
          text: "最近一周",
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit("pick", [start, end]);
          }
        }, {
          text: "最近半个月",
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 15);
            picker.$emit("pick", [start, end]);
          }
        }, {
          text: "最近一个月",
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
            picker.$emit("pick", [start, end]);
          }
        }]
      },
      recordHistory: [],
      showOperationHistory: false
    };
  },
  created() {
    this.height = window.innerHeight - 60 - 20 - 32 - 14;
    window.onresize = (() => {
      return (() => {
        this.height = window.innerHeight - 60 - 20 - 32 - 14;
      })();
    });
    this.tableHeight = window.innerHeight - 60 - 20 - 32 - 14 - 120;
    window.onresize = (() => {
      return (() => {
        this.tableHeight = window.innerHeight - 60 - 20 - 32 - 14 - 120;
      })();
    });
  },
  mounted() {
    this.getLogList();
    this.getOperationMappings();
  },
  filters: {
    operationTypeConverter(operation, operations) {
      if (operations.length === 0) {
        return operation;
      } else {
        return operations[operation];
      }
    }
  },
  methods: {
    getHeight() {
      return this.height + "px";
    },
    getTableHeight() {
      return this.tableHeight + "px";
    },
    getLogList() {
      this.operationForm.success = this.successValue;
      this.operationForm.operation = this.userOperation;
      if (this.timespan.length === 2) {
        this.operationForm.start = this.timespan[0];
        this.operationForm.end = this.timespan[1];
      }
      this.loading = true;
      getOperationLogList(this.cur, this.size, this.operationForm).then(res => {
        if (res.data.code === 200) {
          this.logList = res.data.data.list;
          this.total = res.data.data.total;
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
      this.operationForm = {
        username: "",
        success: -1,
        operation: "",
        userIp: "",
        start: "",
        end: ""
      };
      this.successValue = "";
      this.userOperation = "";
      this.timespan = "";
      this.getLogList();
    },
    handleSizeChange(val) {
      this.size = val;
      this.getLogList();
    },
    handleCurrentChange(val) {
      this.cur = val;
      this.getLogList();
    },
    getOperationMappings() {
      getOperationMappings().then(res => {
        if (res.data.code === 200) {
          this.operationMappings = res.data.data.list;
          this.operationMappings.forEach(m => {
            this.operationJson[m.key] = m.value;
          });
        } else {
          this.$message({
            message: res.data.message,
            type: "error"
          });
        }
      }).catch(error => {
        console.log(error);
      }).finally(() => {
        // ignore
      });
    },
    traceRecord(id) {
      if (id === -1) {
        this.$message({
          message: "操作日志无记录主键, 无法查看记录痕迹!",
          type: "info"
        });
      } else {
        traceRecord(id).then(res => {
          if (res.data.code === 200) {
            this.recordHistory = res.data.data.list;
          } else {
            this.$message({
              message: res.data.message,
              type: "error"
            });
          }
        }).catch(error => {
          console.log(error);
        }).finally(() => {
          this.showOperationHistory = true;
        });
      }
    },
    deleteLogs() {
      this.$confirm("此操作将彻底删除登录日志, 是否继续?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消"
      }).then(() => {
        deleteLogs().then(res => {
          this.$message({
            message: res.data.message,
            type: res.data.code === 200 ? "success" : "error"
          });
        }).catch(error => {
          console.log(error);
        }).finally(() => {
          this.getLogList();
        });
      }).catch(() => {
        this.$message({
          message: "取消删除登录日志",
          type: "info"
        });
      });
    }
  }
};
</script>

<style scoped lang="less">
.demo-table-expand {
  font-size: 0;
}

.demo-table-expand label {
  width: 90px;
  color: #99a9bf;
}

.demo-table-expand .el-form-item {
  margin-right: 0;
  margin-bottom: 0;
  width: 50%;
}

.demo-table-expand .el-form-item span {
  display: inline-block;
  width: 300px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  word-break: keep-all;
}
</style>