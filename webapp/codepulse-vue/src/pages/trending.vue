<template>
    <div>
        <div class="section-container" style="background-color: #fff; margin-top: 0px;">
            <div style="height: 560px">
                <div class="cp-box-title">
                    <span class="cp-title">{{ title }}</span>
                    <div>
                        <span style="font-size:medium">Date：</span>
                        <a-date-picker v-model:value="date" :format="dateFormat" @change="dateChange"
                            :disabledDate="disabledDate">
                            <template #dateRender="{ current }">
                                <div class="ant-picker-cell-inner" :style="getCurrentStyle(current)">
                                    {{ current.date() }}
                                </div>
                            </template>
                        </a-date-picker>
                    </div>
                </div>
                <BaseChart :options="option" class="td-daily-chart"></BaseChart>
            </div>
        </div>
        <a-divider />
        <div class="section-container" style="background-color: #fff; margin-top: 0px;">
            <div style="height: 380px">
                <div class="cp-box-title">
                    <span class="cp-title">{{ titleWeek }}</span>
                </div>
                <BaseChart :options="optionWeek" class="cp-daily-chart"></BaseChart>
            </div>
        </div>
    </div>
</template>

<script lang="ts" setup>
import { ref, onMounted, onBeforeUnmount, reactive } from "vue";
import type { CSSProperties } from 'vue';
import { getDailyTrendingInfo, getWeeklyCodePulseInfo, getMonthCodePulseInfo } from "/@/utils/http/codepulse"
import { getYearMonthDay, toHumanReadble } from "/@/utils/dataformt";
import BaseChart from "/@/components/echarts/BaseChart.vue";
import dayjs, { Dayjs } from 'dayjs';
import { daysWeek, dataWeek, getPunchCardOption, hours } from './data'

const token = localStorage.getItem('token');
// console.log('login token:', token)

const xAxisData = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23'];
const yAxisData = ref([0]);
const codeTimeRef = ref(0);
const dateFormat = 'YYYY-MM-DD';
const date = ref<Dayjs>(dayjs());
const title = ref('Global Trending');
const dateChange = (date: Dayjs | string, dateString: string) => {
    console.log('date chaged!', dateString)
    reload(dateString)
}
const disabledDate = (currentDate: Dayjs) => {
    return currentDate.isAfter(dayjs())
}

const data: number[] = [];
for (let i = 0; i < 5; ++i) {
    data.push(Math.round(Math.random() * 200));
}

const option = reactive({
    xAxis: {
        max: 'dataMax'
    },
    yAxis: {
        type: 'category',
        data: ['A', 'B', 'C', 'D', 'E'],
        inverse: true,
        animationDuration: 300,
        animationDurationUpdate: 300,
        max: 10 // only the largest 3 bars will be displayed
    },
    series: [
        {
            realtimeSort: true,
            name: 'Coding Time (Minutes)',
            type: 'bar',
            data: data,
            label: {
                show: true,
                position: 'right',
                valueAnimation: true
            }
        }
    ],
    legend: {
        show: true
    },
    animationDuration: 0,
    animationDurationUpdate: 3000,
    animationEasing: 'linear',
    animationEasingUpdate: 'linear'
})

const titleWeek = ref("近一周编程趋势图");
const xAxisDataWeek = daysWeek;
const yAxisDataWeek = dataWeek;
const optionWeek = reactive(getPunchCardOption(xAxisDataWeek, yAxisDataWeek))
const dataWeekInit = ref([] as any);
const dataMonthInit = ref([] as any);

// 获取最近一周的数据
function queryWeekly(day: string = getYearMonthDay()) {
    return new Promise((resolve, reject) => {
        getWeeklyCodePulseInfo({ token, day: day }).then((res: any) => {
            const data = res.data;
            const daysWeek = data.map(item => item.title);
            const daysWeekChn = data.map(item => item.dayOfWeekChinese);
            optionWeek.yAxis.data = daysWeek;
            for (let i = 0; i < 7; i++) {
                const dayData = data[i].action.dayStaticByHour;
                for (let j = 0; j < 24; j++) {
                    dataWeekInit.value[i * 24 + j] = [i, j, dayData[j] * 0.5]
                }
            }
            optionWeek.series[0].data = dataWeekInit.value.map((item) => {
                return [item[1], item[0], item[2]]; // 初始化数据
            });
            optionWeek.tooltip.formatter = (params: any) => {
                return (
                    '周' + daysWeekChn[params.value[1]] + hours[params.value[0]] + '时，编程时间：' + params.value[2] + "分钟"
                );
            }
            resolve("finished");
        }).catch(() => {
            reject("error");
        })
    })

}

const reload = (day: string = getYearMonthDay()) => {
    const today = getYearMonthDay();
    const desPrefix = today === day ? "Today " : day + " "
    getDailyTrendingInfo({ day: day })
        .then((res: any) => {
            console.log(res)
            const data = res.data;
            console.log('max=' + data.trendNameList.length)
            option.series[0].data = data.trendTimeList;
            option.yAxis.data = data.trendNameList;
            option.yAxis.max = data.trendNameList.length;
        });
}

let timer: any = null;
onBeforeUnmount(() => {
    if (timer) {
        clearInterval(timer);
    }
})

const getCurrentStyle = (current: Dayjs) => {
    const style: CSSProperties = {};
    const curTime = dayjs();
    // console.log(current.date(), current.month(), curTime.month())
    if (curTime.month() === current.month()
        && current.date() !== curTime.date()
        && dataMonthInit.value.length > 0) {
        if (dataMonthInit.value[current.date() - 1].dot > 0) {
            style.border = '1px solid #1890ff';
            style.borderRadius = '50%';
        }
    }

    return style;
};

onMounted(() => {
    if (!token) {
        console.error('not login, pls login first!')
        return;
    }
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

.td-daily-chart {
    width: 100%;
    height: 552px;
}

.cp-box-title {
    display: flex;
    justify-content: space-between;
}

.cp-title {
    font-size: 1.35rem;
    font-weight: 400;
}

@media (max-width: 800px) {
    .cp-title {
        font-size: 1.2rem;
        font-weight: 400;
    }

    .cp-box-title {
        display: flex;
        flex-direction: column;
    }

    .cp-daily-chart {
        width: 120%;
        height: 352px;
    }
}
</style>
../utils/oauth2