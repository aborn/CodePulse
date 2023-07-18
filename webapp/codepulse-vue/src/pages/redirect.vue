<template>
    <div>
        <div class="section-container" style="background-color: #fff; margin-top: 0px;">
            用户登录中
            <a-divider />
        </div>
    </div>
</template>

<script lang="ts" setup>
import { getUserInfo } from '../utils/oauth2';

let uri = window.location.href.split('?');
if (uri.length == 2) {
    let uriParams = uri[1].endsWith("#/") ? uri[1].substring(0, uri[1].length - 2) : uri[1];
    let vars = uriParams.split('&');
    let getVars = {};
    let tmp;
    vars.forEach(function (v) {
        tmp = v.split('=');
        if (tmp.length == 2)
            getVars[tmp[0]] = tmp[1];
    });

    // 调后台接口获取登录用户信息
    getUserInfo(getVars).then((res: any) => {
        if (res.status) {
            // 登录成功
            const data = res.data;
            console.log('login success!', data)
            localStorage.setItem("token", data.token);
            localStorage.setItem("avatar", data.avatar);
            localStorage.setItem("name", data.name);
            localStorage.setItem("uid", data.uid);
            window.location.href = "/"; // 登录成功后回跳到首页
        } else {
            // 登录失败
            console.error('登录失败', res)
        }
    }).catch((err: any) => {
        console.error(err);
    })
}
</script>