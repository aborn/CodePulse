
<template>
    <div ref="chartRef"></div>
</template>

<script setup lang="ts">
import { defineProps, onBeforeUnmount, onMounted, ref, inject, nextTick, watch } from "vue";
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
        // @ts-ignore
        chart = echarts.init(chartRef.value as HTMLDivElement);
        props.options && chart && chart.setOption({
            ...props.options,
        }, true);
    })
};

// 监听数据变化后重置数据
watch(props.options,
    () => {
        console.log('options 参数发生了变化！')
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
