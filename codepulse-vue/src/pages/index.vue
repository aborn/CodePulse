<template>
    <div class="section-container" style="background-color: #fff; margin-top: 10px;">
        <div style="height: 280px">
            <div class="cp-box-title">
                <span class="cp-title">{{ title }}</span>
                <div>
                    <span style="font-size:medium">日期：</span>
                    <a-date-picker v-model:value="date" :format="dateFormat" @change="dateChange"
                        :disabledDate="disabledDate" />
                </div>
            </div>
            <BaseChart :options="option" class="cp-daily-chart"></BaseChart>
        </div>
    </div>
</template>

<script lang="ts" setup>
import { ref, onMounted, onBeforeUnmount, reactive } from "vue";
import { getDailyCodePulseInfo } from "/@/utils/http/codepulse"
import { getYearMonthDay, toHumanReadble } from "/@/utils/dataformt";
import BaseChart from "/@/components/echarts/BaseChart.vue";
import dayjs, { Dayjs } from 'dayjs';

const xAxisData = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23'];
const yAxisData = ref([0]);
const codeTimeRef = ref(0);
const dateFormat = 'YYYY-MM-DD';
const date = ref<Dayjs>(dayjs());
const title = ref('编程趋势图');
const dateChange = (date: Dayjs | string, dateString: string) => {
    console.log('date chaged!', dateString)
    reload(dateString)
}
const disabledDate = (currentDate: Dayjs) => {
    return currentDate.isAfter(dayjs())
}

const option = reactive({
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
            type: 'value',
            max: 60
        }
    ],
    series: [
        {
            name: '编程时间(分钟)',
            type: 'bar',
            showBackground: true,
            barWidth: '60%',
            data: yAxisData.value
        }
    ]
})

const reload = (day: string = getYearMonthDay()) => {
    const today = getYearMonthDay();
    const desPrefix = today === day ? "今日" : day + "日"
    getDailyCodePulseInfo({ token: '0x4af97338', day: day })
        .then((res: any) => {
            const codeTime = res.data.codeTime;
            // console.log(res)
            if (codeTime != codeTimeRef.value) {
                option.series[0].data = res.data.dayStaticByHour.map(i => i * 0.5);
                title.value = desPrefix + "编程趋势图 （共" + toHumanReadble(codeTime) + "）";
                codeTimeRef.value = res.data.codeTime;

            }
        });
}

let timer: any = null;
onBeforeUnmount(() => {
    if (timer) {
        clearInterval(timer);
    }
})

onMounted(() => {
    reload();
    timer = setInterval(() => {
        reload();
    }, 30 * 1000);
})
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

.cp-daily-chart {
    width: 100%;
    height: 352px;
}

.cp-box-title {
    display: flex;
    justify-content: space-between;
}

.cp-title {
    font-size: 1.35rem;
    font-weight: 400;
}
</style>
