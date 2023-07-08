import ElementResizeDetectorMaker from "element-resize-detector";

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

export function divListen(divElement, chart) {
    // let that = this
    return new Promise(() => {
        let erd = ElementResizeDetectorMaker();
        erd.listenTo(divElement, debounce(async () => {
            chart && chart.resize();
        }));
    })
}
