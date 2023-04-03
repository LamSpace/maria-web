import request from "@/api/request";

const logPrefix = () => {
  return "/service-log/logLogin";
};

export const getLoginLogList = (cur: number, size: number, form: {}) => {
  return request({
    url: logPrefix() + "/list",
    method: "post",
    params: {
      cur, size
    },
    data: form
  });
};

export const deleteLogs = () => {
  return request({
    url: logPrefix() + "/deleteAll",
    method: "delete"
  });
};
