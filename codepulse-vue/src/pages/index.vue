<template>
    <div class="section-container" style="background-color: #fff; margin-top: 10px;">
        <div style="height: 280px">
            <BaseChart :options="option" class="h-right-three-chart"></BaseChart>
        </div>
    </div>
</template>

<script lang="ts" setup>
import { version as docsVersion } from '../../package.json'
import { version as nantaVersion, dependencies as nantaDeps } from '../../node_modules/@nanta/ui/package.json'
import { ref, onMounted, onBeforeUnmount, reactive } from "vue";
import { getDailyCodePulseInfo } from "/@/utils/http/codepulse"
import { getYearMonthDay, toHumanReadble } from "/@/utils/dataformt";
import BaseChart from "/@/components/echarts/BaseChart.vue";

const xAxisData = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23'];
const yAxisData = ref([0]);
const codeTimeRef = ref(0);

const option = reactive({
    title: {
        text: '今日编程时间趋势图'
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
            data: xAxisData,
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
            name: '编程时间(分钟)',
            type: 'bar',
            barWidth: '60%',
            data: yAxisData.value
        }
    ]
})

const reload = () => {
    getDailyCodePulseInfo({ token: '0x4af97338', day: getYearMonthDay() })
        .then((res: any) => {
            const codeTime = res.data.codeTime;
            // console.log(res)
            if (codeTime != codeTimeRef.value) {
                option.series[0].data = res.data.dayStaticByHour.map(i => i * 0.5);
                option.title.text = "今日编程时间趋势图 （共" + toHumanReadble(codeTime) + "）";
                codeTimeRef.value = res.data.codeTime;

            }
        });
}
reload();

let timer: any = null;
onBeforeUnmount(() => {
    if (timer) {
        clearInterval(timer);
    }
})

onMounted(() => {
    timer = setInterval(() => {
        reload();
    }, 30 * 1000);
})

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

.h-right-three-chart {
    width: 100%;
    height: 352px;
}
</style>
