<template>
  <div>
    <el-breadcrumb separator="/" style="padding-left: 10px; padding-bottom: 10px; font-size: 12px;">
      <el-breadcrumb-item :to="{ path: '/frame' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>日志管理</el-breadcrumb-item>
      <el-breadcrumb-item>登录日志</el-breadcrumb-item>
    </el-breadcrumb>

    <el-card class="box-card" :style="{height: getHeight()}">
      <el-form :inline="true" :model="loginForm" class="demo-form-inline">
        <el-form-item label="用户账号" size="small">
          <el-input clearable placeholder="请输入用户账号" v-model="loginForm.username" />
        </el-form-item>

        <el-form-item label="用户IP" size="small">
          <el-input clearable placeholder="请输入用户IP" v-model="loginForm.ip" />
        </el-form-item>

        <el-form-item label="是否成功" size="small">
          <el-select v-model="successValue" placeholder="请选择">
            <el-option v-for="item in selectOptions" :key="item.value" :label="item.label"
                       :value="item.value" />
          </el-select>
        </el-form-item>

        <el-form-item label="登录时间" size="small">
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
          <el-table-column prop="exception" label="失败原因" align="center" show-overflow-tooltip>
            <template slot-scope="scope">
              {{ scope.row.exception === "" ? "-" : scope.row.exception }}
            </template>
          </el-table-column>
          <el-table-column prop="loginTime" label="登录时间" width="200" align="center" show-overflow-tooltip>
            <template slot-scope="scope">
              {{ scope.row.loginTime === "" ? "-" : scope.row.loginTime }}
            </template>
          </el-table-column>
          <el-table-column prop="ip" label="用户IP" align="center" show-overflow-tooltip>
            <template slot-scope="scope">
              {{ scope.row.ip === "" ? "-" : scope.row.ip }}
            </template>
          </el-table-column>
          <el-table-column prop="browserName" label="浏览器名称" align="center" show-overflow-tooltip>
            <template slot-scope="scope">
              {{ scope.row.browserName == "" ? "-" : scope.row.browserName }}
            </template>
          </el-table-column>
          <el-table-column prop="browserType" label="浏览器类型" align="center" show-overflow-tooltip>
            <template slot-scope="scope">
              {{ scope.row.browserType == "" ? "-" : scope.row.browserType }}
            </template>
          </el-table-column>
          <el-table-column prop="browserVersion" label="浏览器版本" align="center" show-overflow-tooltip>
            <template slot-scope="scope">
              {{ scope.row.browserVersion == "" ? "-" : scope.row.browserVersion }}
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
                <el-form-item label="登录时间">
                                    <span :title="scope.row.loginTime">
                                        {{ scope.row.loginTime }}
                                    </span>
                </el-form-item>
                <el-form-item label="用户IP">
                                    <span :title="scope.row.userIp">
                                        {{ scope.row.ip }}
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
  </div>
</template>

<script>
import { getLoginLogList, deleteLogs } from "@/api/log/login/index";

export default {
  name: "Index",
  data() {
    return {
      height: "0px",
      tableHeight: "0px",
      logList: [],
      loading: false,
      cur: 1,
      size: 15,
      total: 0,
      loginForm: {
        username: "",
        ip: "",
        success: -1,
        start: "",
        end: ""
      },
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
      successValue: ""
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
  },
  methods: {
    getHeight() {
      return this.height + "px";
    },
    getTableHeight() {
      return this.tableHeight + "px";
    },
    reset() {
      this.loginForm = {
        username: "",
        ip: "",
        success: -1,
        start: "",
        end: ""
      };
      this.successValue = "";
      this.timespan = "";
      this.getLogList();
    },
    getLogList() {
      if (this.timespan.length === 2) {
        this.loginForm.start = this.timespan[0];
        this.loginForm.end = this.timespan[1];
      }
      this.loginForm.success = this.successValue;
      this.loading = true;
      getLoginLogList(this.cur, this.size, this.loginForm).then(res => {
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
    handleSizeChange(val) {
      this.size = val;
      this.getLogList();
    },
    handleCurrentChange(val) {
      this.cur = val;
      this.getLogList();
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