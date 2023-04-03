<template>
  <div>
    <el-breadcrumb separator="/" style="padding-left: 10px; padding-bottom: 10px; font-size: 12px;">
      <el-breadcrumb-item :to="{ path: '/frame' }">首页</el-breadcrumb-item>
    </el-breadcrumb>

    <el-card class="box-card" :style="{height: getHeight()}">

    </el-card>
  </div>
</template>

<script>
export default {
  name: "Index",
  data() {
    return {
      height: "0px"
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

  },
  watch: {},
  methods: {
    getHeight() {
      return this.height + "px";
    }
  }
};
</script>

<style lang="less" scoped>

</style>