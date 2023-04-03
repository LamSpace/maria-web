import request from "@/api/request";

const userPrefix = () => {
  return "/service-system/user";
};

export const getUserList = (cur: number, size: number, form: {
  username: string, nickname: string, email: string,
  telephone: string, idCard: string, address: string,
  start: string, end: string
}) => {
  return request({
    url: userPrefix() + "/list",
    method: "post",
    params: {
      cur, size
    },
    data: form
  });
};

export const addUser = (form: {}) => {
  return request({
    url: userPrefix() + "/user",
    method: "post",
    data: form
  });
};

export const updateUser = (form: {}) => {
  return request({
    url: userPrefix() + "/user",
    method: "put",
    data: form
  });
};

export const getUser = (id: string) => {
  return request({
    url: userPrefix() + "/user",
    method: "get",
    params: {
      id
    }
  });
};

export const deleteUser = (ids: Array<string>) => {
  return request({
    url: userPrefix() + "/user",
    method: "delete",
    data: ids
  });
};

export const resetPassword = (id: string) => {
  return request({
    url: userPrefix() + "/resetPassword",
    method: "get",
    params: {
      id
    }
  });
};

export const getUserRole = (id: string) => {
  return request({
    url: userPrefix() + "/userRoleBinding",
    method: "get",
    params: {
      id
    }
  });
};

export const configureUserRole = (id: string, roleIds: Array<string>) => {
  return request({
    url: userPrefix() + "/configureUserRoleBinding",
    method: "post",
    params: {
      id
    },
    data: roleIds
  });
};
