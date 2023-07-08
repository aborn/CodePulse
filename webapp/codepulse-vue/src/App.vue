<template>
  <div>
    <ConfigProvider :locale="locale">
      <AppProvider>
        <DefaultLayout>
          <RouterView />
        </DefaultLayout>
      </AppProvider>
    </ConfigProvider>
  </div>
</template>

<script lang="ts" setup>
import * as echarts from 'echarts'
import { provide } from 'vue'
import { ConfigProvider } from 'ant-design-vue';
import { AppProvider } from '/@/components/Application';
import DefaultLayout from './layouts/default.vue';
import zhCN from 'ant-design-vue/es/locale/zh_CN';
import dayjs from 'dayjs';
import 'dayjs/locale/zh-cn';
dayjs.locale('zh-cn');
const locale = zhCN;
provide('echarts', echarts);
</script>
