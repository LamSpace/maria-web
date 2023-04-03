import request from "@/api/request";

const configPrefix = () => {
  return "/service-system/config";
};

export const getConfigList = (cur: number, size: number, form: {
  configKey: string, configName: string, configDescription: string
}) => {
  return request({
    url: configPrefix() + "/list",
    method: "post",
    params: {
      cur, size
    },
    data: form
  });
};

export const addConfig = (form: {}) => {
  return request({
    url: configPrefix() + "/config",
    method: "post",
    data: form
  });
};

export const updateConfig = (form: {}) => {
  return request({
    url: configPrefix() + "/config",
    method: "put",
    data: form
  });
};

export const getConfig = (id: string) => {
  return request({
    url: configPrefix() + "/config",
    method: "get",
    params: { id }
  });
};

export const deleteConfig = (ids: Array<string>) => {
  return request({
    url: configPrefix() + "/config",
    method: "delete",
    data: ids
  });
};

export const getConfigByKey = (configKey: string) => {
  return request({
    url: configPrefix() + "/getConfig",
    method: "get",
    params: {
      configKey
    }
  });
};
