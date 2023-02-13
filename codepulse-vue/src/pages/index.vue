<template>
    <div>
        <div class="section-container" style="background-color: #fff; margin-top: 0px;">
            <div style="height: 380px">
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
import { ref, onMounted, onBeforeUnmount, reactive, computed } from "vue";
import { getDailyCodePulseInfo, getWeeklyCodePulseInfo } from "/@/utils/http/codepulse"
import { getYearMonthDay, toHumanReadble } from "/@/utils/dataformt";
import BaseChart from "/@/components/echarts/BaseChart.vue";
import dayjs, { Dayjs } from 'dayjs';
import { daysWeek, dataWeek, getPunchCardOption, hours } from './data'

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

const titleWeek = ref("近一周编程趋势图");
const xAxisDataWeek = daysWeek;
const yAxisDataWeek = dataWeek;
const optionWeek = reactive(getPunchCardOption(xAxisDataWeek, yAxisDataWeek))
const token = '0x4af97338';
const dataWeekInit = ref([] as any);

// 获取最近一周的数据
const queryWeekly = () => {
    getWeeklyCodePulseInfo({ token })
        .then((res: any) => {
            const data = res.data;
            const daysWeek = data.map(item => item.title);
            const daysWeekChn = data.map(item => item.dayOfWeekChinese);
            console.log(data);
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
        })
}

const reload = (day: string = getYearMonthDay()) => {
    const today = getYearMonthDay();
    const desPrefix = today === day ? "今日" : day + "日"
    getDailyCodePulseInfo({ token, day: day })
        .then((res: any) => {
            const codeTime = res.data.codeTime;
            // console.log(res)
            if (codeTime != codeTimeRef.value) {
                const dayData = res.data.dayStaticByHour;
                option.series[0].data = dayData.map(i => i * 0.5);
                title.value = desPrefix + "编程趋势图 （共" + toHumanReadble(codeTime) + "）";
                codeTimeRef.value = res.data.codeTime;

                if (today === day) {
                    // 更新周表里当天的数据
                    for (let j = 0; j < 24; j++) {
                        dataWeekInit.value[6 * 24 + j] = [6, j, dayData[j] * 0.5]
                    }
                    const valueArray = dataWeekInit.value.map(item => item[2]);
                    // 注意这里是分钟单位，不是秒单位
                    const weekCodeTimeTotal = valueArray.reduce(
                        (accumulator:number, currentValue: number) => accumulator + currentValue,
                        0
                    );
                    titleWeek.value = "近一周编程趋势图（共" + toHumanReadble(weekCodeTimeTotal * 60) + ")"
                    optionWeek.series[0].data = dataWeekInit.value.map((item) => {
                        return [item[1], item[0], item[2]]; // 初始化数据
                    });
                }
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
    queryWeekly();
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
