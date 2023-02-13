import request from './request';

// 查询当天的编程信息
export function getDailyCodePulseInfo(params) {
    return request({
        url: '/api/v1/codepulse/admin/getUserAction',
        method: 'get',
        params: params
    });
}

export function getWeeklyCodePulseInfo(params) {
    return request({
        url: '/api/v1/codepulse/admin/getWeekUserAction',
        method: 'get',
        params: params
    });
}
