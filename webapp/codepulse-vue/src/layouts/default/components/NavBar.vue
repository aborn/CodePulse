<template>
  <a-layout-header class="header">
    <div class="logo" style="font-size: x-large; color: azure">
      <router-link to="/" aria-label="Back home">
        <img src="/logo.png" class="cp-logo" /> CodePulse
      </router-link>
    </div>

    <a-row>
      <a-col :span="12">
        <a-menu :selectedKeys="selectedKeys" theme="dark" mode="horizontal" @select="onSelect"
          :style="{ lineHeight: '64px', fontSize: 'large' }">
          <a-menu-item v-for="item in navItems" :key="item.key">{{ item.name }}</a-menu-item>
        </a-menu>
      </a-col>
      <a-col :span="12">

        <div class="badge-items">
          <!--
            <span class="badge-item">
              <a href="https://www.npmjs.com/package/@nanta/ui"><img src="https://badgen.net/npm/dm/@nanta/ui"
                  alt="Download"></a>
            </span>
            <span class="badge-item">
              <a href="https://github.com/nantajs/nanta-ui"><img src="https://badgen.net/github/stars/nantajs/nanta-ui"
                  alt="Stars"></a>
            </span>
            -->
          <span class="badge-item">
            <a v-if="token" @click="showDrawer" :alt="uid">
              <img v-if="avatar" :src="avatar" style="height: 50px; max-width: 100%; border-radius: 50%" />
              <NantaIcon v-else icon="mdi:github" size="36" style="margin-top: 14px;"></NantaIcon>
            </a>
            <a v-else @click="loginAction" style="font-size: large; color: azure">Sign in</a>
          </span>
        </div>
      </a-col>
    </a-row>

    <a-drawer :width="320" title="CodePulse" placement="right" v-model:visible="open" :closable="false">
      <div class="author-info-block">
        <img v-if="avatar" :src="avatar" style="height: 50px; max-width: 100%; border-radius: 50%" />
        <div class="author-info-box">
          <div class="author-meta">{{ uid }}</div>
          <div class="author-info-meta">
            {{ name }}
          </div>
        </div>
      </div>
      <template #extra>
        <NantaIcon @click="onClose" icon="ant-design:close-outlined" color="#000000" />
      </template>
      <a-divider>Personal Configiration Info</a-divider>
      <p>token = {{ token }}</p>
      <p>id = {{ uid }}</p>
      <p>url = https://cp.popkit.org/api/v1/codepulse/</p>
      <a-divider />
      <p>
        <a-button @click="userLogout">Sign out</a-button>
      </p>
    </a-drawer>

  </a-layout-header>
</template>

<script lang="ts" setup>
import { Nav } from "../../types/type";
import { ref } from 'vue';
import { PropType, Icon as NantaIcon } from "@nanta/ui";
import { loginWithGithubOauth2, userLogout } from '../../../utils/oauth2';

const open = ref<boolean>(false);

const showDrawer = () => {
  open.value = true;
};

const onClose = () => {
  open.value = false;
};

const loginAction = () => {
  loginWithGithubOauth2()
}

const props = defineProps({
  navItems: Array as PropType<Array<Nav>>,
  selectedKeys: Array as PropType<Array<string>>,
});

const emits = defineEmits(["selectd"])

const avatar = localStorage.getItem('avatar');
const token = localStorage.getItem('token');
const uid = localStorage.getItem('uid') || 'aborn';
const name = localStorage.getItem("name");

const onSelect = ({ item, key, selectedKeys }) => {
  // console.log("selectd");
  // console.log(item, key);
  emits("selectd", item, key);
};
</script>

<style scoped>
.logo {
  float: left;
  width: 188px;
  height: 64px;
  /*background: rgba(255, 255, 255, 0.3);*/
}

.site-layout-background {
  background: #fff;
}

.ant-layout-header {
  padding: 0 12px;
}

.cp-logo {
  height: 55px;
}

.badge-items {
  display: flex;
  justify-content: right;
  align-items: center;
}

.badge-item {
  margin-right: 1.5rem;
}

.author-info-block {
  display: flex;
  align-items: left;
  justify-content: left;
  margin-bottom: 1rem;
  font-size: 1.25rem;
}

.author-info-box {
  min-width: 0;
  min-height: 43px;
  margin-left: 0.5rem;
}

.author-info-meta {
  display: flex;
  color: #8a919f;
  margin-top: 2px;
  line-height: 22px;
  text-align: left;
}

.author-meta {
  font-size: 1.37rem;
  text-align: left;
  color: #8a919f;
  line-height: 22px;
  vertical-align: middle;
  margin-bottom: 0.3rem;
}
</style>
