
export function getYearMonthDay(date: Date = new Date()) {
    let ye = new Intl.DateTimeFormat('en', { year: 'numeric' }).format(date);
    let mo = new Intl.DateTimeFormat('en', { month: '2-digit' }).format(date);
    let da = new Intl.DateTimeFormat('en', { day: '2-digit' }).format(date);
    return `${ye}-${mo}-${da}`
}

export function toHumanReadble(second: number): string {
    if (second < 60) {
        return second + "秒";
    } else if (second < 60 * 60) {
        const minutes = Math.floor(second / 60);
        return minutes + "分" + (second - minutes * 60) + "秒";
    } else {
        const hours = Math.floor(second / (60 * 60));
        const minutes = Math.floor((second - hours * 60 * 60) / 60);
        const sec = second - (hours * 60 * 60) - (minutes * 60);
        return hours + "时" + minutes + "分" + sec + "秒";
    }
}