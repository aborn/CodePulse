import { EXCEPTION_COMPONENT, PAGE_NOT_FOUND_NAME, E404_COMPONENT } from '/@/router/constant';

export const basicRouteItems = [
    { path: '/', name: 'Index', component: () => import('/@/pages/dashboard.vue') },
    { path: '/dashboard', name: 'Dashboard', component: () => import('/@/pages/dashboard.vue') },      
    { path: '/trending', name: 'Trending', component: () => import('/@/pages/trending.vue') },      
    { path: '/redirect', name: 'Redirect', component: () => import('/@/pages/redirect.vue') },
    { path: '/404', name: '404Page', component: E404_COMPONENT },
    { path: '/:path(.*)*', name: PAGE_NOT_FOUND_NAME, component: EXCEPTION_COMPONENT, }
]