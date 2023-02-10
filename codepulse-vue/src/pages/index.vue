<template>    
    <div class="section-container" style="background-color: #fff; margin-top: 10px;">
        <div id="home-page-traffic_chart" style="width: 600px; height: 280px"/>
    </div>
</template>

<script lang="ts" setup>
import { getMenus, getMenuList } from "/@/layouts/menu"
import { NantaButton, useTable, NantaTable, BasicColumn, FormSchema } from "@nanta/ui";
import { version as docsVersion, dependencies } from '../../package.json'
import { version as nantaVersion, dependencies as nantaDeps } from '../../node_modules/@nanta/ui/package.json'
import { h, ref, inject, onMounted } from "vue";

const vueVersion = nantaDeps["vue"];
const antdVersion = nantaDeps["ant-design-vue"];
const versions = [{ nantaVersion }, { vueVersion }, { antdVersion }, { antdVersion }];
console.log(versions);

const menus = getMenuList(getMenus());
const trafficData = ref({})
const echarts = inject('echarts') as any

onMounted(() => {    
    const myChart = echarts.init(document.getElementById('home-page-traffic_chart'))
      // 绘制图表
      myChart.setOption({
        title: {
          text: '今日编程时间统计'
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: [
          {
            type: 'category',
            data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
            axisTick: {
              alignWithLabel: true
            }
          }
        ],
        yAxis: [
          {
            type: 'value'
          }
        ],
        series: [
          {
            name: '直接访问',
            type: 'bar',
            barWidth: '60%',
            data: [10, 52, 200, 334, 390, 330, 220]
          }
        ]
      })
      window.onresize = function () {
        myChart.resize()
      }
})

const columns: BasicColumn[] = [
    {
        title: "Package Name",
        dataIndex: "name",
        key: "name",
    },
    {
        title: "Package Version",
        dataIndex: "version",
        key: "version",
        format: (text) => {
            return h('span', { class: 'version-style', style: "font-size: 1.5rem;color:#faad14" }, text)
        }
    },
]

const data = [
    {
        "name": "Docs version",
        "version": docsVersion
    },
    {
        "name": "@nanta/ui",
        "version": nantaVersion
    }
]

const searchFormSchema: FormSchema[] = [
    {
        field: 'name',
        label: 'Package Name',
        component: 'Input',
        colProps: { span: 12 },
    },
];

const [registerTable] = useTable({
    title: '@nant/ui version list.',
    columns,
    dataSource: data,
    pagination: false,
    useSearchForm: true,
    searchFormConfig: {
        labelWidth: 120,
        schemas: searchFormSchema,
        autoSubmitOnEnter: true,
        colon: true,
        submitButtonOptions: { text: 'search' },
        actionColOptions: {
            span: 12,
            style: {
                'text-align': 'left'
            }
        },
    },
})

for (let [key, value] of Object.entries(nantaDeps)) {
    data.push({
        "name": key,
        "version": value,
    })
}


</script>

<style scoped>
.fbox-line {
    display: flex;
    margin-right: 1rem;
    margin-bottom: 1rem;
}

.version-line {
    display: flex;
    margin-right: 1rem;
    margin-bottom: 1rem;
    flex-direction: column;
}

.version-style {
    font-size: 1.5rem;
    color: #faad14;
}

.fbox-line div {
    margin-right: .5rem;
}
</style>
