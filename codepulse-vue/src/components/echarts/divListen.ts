import {elementResizeDetectorMaker} from "element-resize-detector";

export function debounce(fn, t = 100) {
    let lastTime;
    return function () {
        clearTimeout(lastTime);
        // @ts-ignore
        const [that, args] = [this, arguments];
        lastTime = setTimeout(() => {
            fn.apply(that, args);
        }, t);
    }
}

export function divListen(div, chart) {
    // let that = this
    return new Promise(() => {
        let erd = elementResizeDetectorMaker();
        erd.listenTo(document.getElementById(div), debounce(async () => {
            chart && chart.resize();
        }));
    })
}
