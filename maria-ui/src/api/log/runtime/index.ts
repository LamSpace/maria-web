import request from "@/api/request";
const logPrefix = () => {
  return "/service-log/logRuntime";
};

export const getRuntimeLogList = (cur: number, size: number, form: {}) => {
  return request({
    url: logPrefix() + "/list",
    method: "post",
    params: {
      cur, size
    },
    data: form
  });
};
