import axios from "axios";

const service = axios.create({
  timeout: 8000,
  headers: {
    "Content-Type": "application/json;charset=utf-8",
    "Access-Control-Allow-Origin": "*",
  },
});

service.interceptors.response.use(
  (response) => {
    const res = response.data;
    if (res.message) {
      console.error(res.message);
    } else {
      return res;
    }
  },
  async (error) => {
    // 未授权
    if (error.response.status === 401) {
      // 请求出错，弹出错误信息
      if (error.response) {
        const res = error.response.data;
        if (res.message) {
          console.error(res.message);
        } else if (res.error) {
          console.error(res.error);
        }
      } else {
        console.error("request timeout");
      }
    }
  }
);

export default service;
