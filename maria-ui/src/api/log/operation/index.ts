import request from "@/api/request";

const logPrefix = () => {
  return "/service-log/logOperation";
};

export const getOperationLogList = (cur: number, size: number, form: {}) => {
  return request({
    url: logPrefix() + "/list",
    method: "post",
    params: {
      cur, size
    },
    data: form
  });
};

export const getOperationMappings = () => {
  return request({
    url: logPrefix() + "/operationMap",
    method: "get"
  });
};

export const traceRecord = (id: string) => {
  return request({
    url: logPrefix() + "/trace",
    method: "get",
    params: {
      id
    }
  });
};

export const deleteLogs = () => {
  return request({
    url: logPrefix() + "/deleteAll",
    method: "delete"
  });
};
