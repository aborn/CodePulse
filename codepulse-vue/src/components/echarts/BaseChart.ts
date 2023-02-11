import * as echarts from "echarts";
import { watch, ref, nextTick } from 'vue'
import { onMounted, onUnmounted } from "@vue/runtime-core";
import { divListen } from "./divListen";

export default {
    name: "echartsBox",
    data() {
        return {
            echart: {},
            chartDocument: {}
        };
    },
    watch: {},
    computed: {},
    props: [
        'echartsId',
        'eWidth',
        'eHeight',
        'theme',
        'options',
    ],
    components: {},
    setup(props) {
        const echart = echarts;
        let docEcharts = ref();
        onMounted(() => {
            initChart();
        });
        onUnmounted(() => {
            echart.dispose;
        });
        // nextTick是为了让dom渲染后再执行echats
        const initChart = () => {
            nextTick(() => {
                // @ts-ignore
                const chart = echart.init(document.getElementById(props.echartsId), props.theme);
                docEcharts.value = chart
                // @ts-ignore
                divListen(props.echartsId, chart, this)
                // 把配置和数据放这里
                chart && chart.clear();
                props.options && chart.setOption(props.options, true);
                // 监听数据变化后重置数据
                watch(props.options,
                    () => {
                        chart && chart.clear();
                        props.options && chart.setOption(props.options, true)
                    }
                )
            })
        }
        return {
            docEcharts
        }
    },
    methods: {},
};