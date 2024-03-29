import type { RouteRecordRaw, RouteMeta } from 'vue-router';
import { Recordable } from '@nanta/ui';
import { defineComponent } from 'vue';
export type Component<T = any> = ReturnType<typeof defineComponent>;

export interface AppRouteRecordRaw extends Omit<RouteRecordRaw, 'meta'> {
    name: string;
    meta: RouteMeta;
    component?: Component | string;
    components?: Component;
    children?: AppRouteRecordRaw[];
    props?: Recordable;
    fullPath?: string;
    redirect?: any;
}

export type AppRouteModule = AppRouteRecordRaw;