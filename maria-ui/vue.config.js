const { defineConfig } = require("@vue/cli-service");
module.exports = defineConfig({

  transpileDependencies: true,

  lintOnSave: false,

  devServer: {
    proxy: {
      "/api": {
        target: "http://localhost:12000",
        changeOrigin: true,
        pathRewrite: {
          "^/api": ""
        }
      },
      "/socketApi": {
        target: "ws://localhost:12000",
        ws: true,
        changeOrigin: true,
        pathRewrite: {
          "^/socketApi": ""
        }
      }
    }
  },

  pluginOptions: {}

});
