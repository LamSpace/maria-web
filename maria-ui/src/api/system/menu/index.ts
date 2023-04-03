import request from "@/api/request";

const menuPrefix = () => {
  return "/service-system/menu";
};

export const getMenuTree = () => {
  return request({
    url: menuPrefix() + "/menuTree",
    method: "get"
  });
};

export const addMenu = (form: {}) => {
  return request({
    url: menuPrefix() + "/menu",
    method: "post",
    data: form
  });
};

export const updateMenu = (form: {}) => {
  return request({
    url: menuPrefix() + "/menu",
    method: "put",
    data: form
  });
};

export const getMenu = (id: string) => {
  return request({
    url: menuPrefix() + "/menu",
    method: "get",
    params: {
      id
    }
  });
};

export const deleteMenu = (ids: Array<string>) => {
  return request({
    url: menuPrefix() + "/menu",
    method: "delete",
    data: ids
  });
};
