import request from "@/api/request";

const rolePrefix = () => {
  return "/service-system/role";
};

export const getRoleList = (cur: number, size: number, form: { name: string }) => {
  return request({
    url: rolePrefix() + "/list",
    method: "post",
    params: {
      cur, size
    },
    data: form
  });
};

export const addRole = (form: {}) => {
  return request({
    url: rolePrefix() + "/role",
    method: "post",
    data: form
  });
};

export const updateRole = (form: {}) => {
  return request({
    url: rolePrefix() + "/role",
    method: "put",
    data: form
  });
};

export const getRole = (id: string) => {
  return request({
    url: rolePrefix() + "/role",
    method: "get",
    params: { id }
  });
};

export const deleteRole = (ids: Array<string>) => {
  return request({
    url: rolePrefix() + "/role",
    method: "delete",
    data: ids
  });
};

export const getRoleMenu = (id: string) => {
  return request({
    url: rolePrefix() + "/roleMenuBinding",
    method: "get",
    params: {
      id
    }
  });
};

export const configureRoleMenu = (id: string, ids: Array<string>) => {
  return request({
    url: rolePrefix() + "/configureRoleMenuBinding",
    method: "post",
    params: {
      id
    },
    data: ids
  });
};
