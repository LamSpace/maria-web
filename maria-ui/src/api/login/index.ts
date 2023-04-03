import request from "@/api/request";

const authPrefix = () => {
  return "/service-auth/auth";
};

export const doLogin = (loginFormData: {
  account: string, password: string
}) => {
  return request({
    url: authPrefix() + "/login",
    method: "post",
    data: loginFormData
  });
};

export const doLogout = () => {
  return request({
    url: authPrefix() + "/logout",
    method: "get"
  });
};

export const doVerifyPwd = (userId: number, password: string) => {
  return request({
    url: authPrefix() + "/verify",
    method: "GET",
    params: {
      userId, password
    }
  });
};
