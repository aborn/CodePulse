
<template>
    <div ref="chartRef"></div>
</template>

<script setup lang="ts">
import { onBeforeUnmount, onMounted, ref, inject, nextTick, watch } from "vue";
import { divListen } from "./divListen";
const echarts = inject('echarts') as any

const props = defineProps(["options"]);
const chartRef = ref<HTMLDivElement>();
let chart: echarts.ECharts | null = null;
const resizeHandler = () => {
    chart?.resize();
};

onMounted(() => {
    initChart();
    window.addEventListener("resize", resizeHandler);
});

const initChart = () => {
    nextTick(() => {

        const divElement = chartRef.value as HTMLDivElement;
        chart = echarts.init(divElement);
        // @ts-ignore
        divListen(divElement, chart, this)
        props.options && chart && chart.setOption({
            ...props.options,
        }, true);
    })
};

// 监听数据变化后重置数据
watch(props.options,
    () => {
        chart && chart.clear();
        props.options && chart && chart.setOption({
            ...props.options,
        }, true)
    }
)

onBeforeUnmount(() => {
    window.removeEventListener("resize", resizeHandler);
    chart?.dispose();
});
</script>

<style lang="scss" scoped>

</style>
