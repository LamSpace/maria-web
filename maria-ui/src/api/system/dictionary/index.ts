import request from "@/api/request";

const dictionaryPrefix = () => {
  return "/service-system/dictionary";
};

export const getDictionaryTypes = (cur: number, size: number, form: {
  dictionaryName: string, dictionaryType: string, remark: string
}) => {
  return request({
    url: dictionaryPrefix() + "/listType",
    method: "post",
    params: {
      cur, size
    },
    data: form
  });
};

export const addDictionaryType = (form: {}) => {
  return request({
    url: dictionaryPrefix() + "/dictionaryType",
    method: "post",
    data: form
  });
};

export const updateDictionaryType = (form: {}) => {
  return request({
    url: dictionaryPrefix() + "/dictionaryType",
    method: "put",
    data: form
  });
};

export const getDictionaryType = (id: string) => {
  return request({
    url: dictionaryPrefix() + "/dictionaryType",
    method: "get",
    params: {
      id
    }
  });
};

export const deleteDictionaryType = (ids: Array<string>) => {
  return request({
    url: dictionaryPrefix() + "/dictionaryType",
    method: "delete",
    data: ids
  });
};

export const getAllDictionaryTypes = () => {
  return request({
    url: dictionaryPrefix() + "/dictionaryTypes",
    method: "get"
  });
};

export const getDictionaryList = (cur: number, size: number, form: {
  dictionaryLabel: string, dictionaryValue: string, dictionaryType: string, description: string
}) => {
  return request({
    url: dictionaryPrefix() + "/list",
    method: "post",
    params: {
      cur, size
    },
    data: form
  });
};

export const addDictionary = (form: {}) => {
  return request({
    url: dictionaryPrefix() + "/dictionary",
    method: "post",
    data: form
  });
};

export const updateDictionary = (form: {}) => {
  return request({
    url: dictionaryPrefix() + "/dictionary",
    method: "put",
    data: form
  });
};

export const getDictionary = (id: string) => {
  return request({
    url: dictionaryPrefix() + "/dictionary",
    method: "get",
    params: {
      id
    }
  });
};

export const deleteDictionary = (ids: Array<string>) => {
  return request({
    url: dictionaryPrefix() + "/dictionary",
    method: "delete",
    data: ids
  });
};

export const getDictionaryListByType = (type: string) => {
  return request({
    url: dictionaryPrefix() + "/listByType",
    method: "get",
    params: { type }
  });
};
