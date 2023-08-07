import { Menu, Nav } from './types/type'
import { basicRouteItems } from '../router/routes'

// menu datas
const localMenus: Menu[] = [
    {
        name: 'Dashboard',
        icon: 'ic:baseline-home',
        key: 'dashboard',
        path: '/',
        group: 'index'
    }
]

const noMenus: Menu[] = []

export const navItems: Nav[] = [
    {
        name: "Trending",
        key: "trending",
        path: "/trending",
        group: 'api'
    },
    {
        name: "Dashboard",
        key: "dashboard",
        path: "/dashboard",
        group: 'index',
    },
    {
        name: "Guide",
        key: "guide",
        group: "components",
    }
];

export function iteratorMenu(
    item: Menu,
    fn: (item: Menu, keyPath?: string[]) => void,
    keyPath?: string[]
) {
    fn(item, keyPath);
    if (item.children && item.children.length > 0) {
        item.children.forEach((i) => {
            iteratorMenu(i, fn, item.keyPath);
        });
    }
}

export function getMenus() {
    return noMenus;
}

export function getMenuList(rootMenus: Menu[]): Menu[] {
    const menuList = [] as Menu[];
    rootMenus.forEach((item: Menu) => {
        if (!item.children || item.children.length == 0) {
            menuList.push(item)
        } else {
            const childMenuList = getMenuList(item.children);
            childMenuList.forEach((item: Menu) => {
                menuList.push(item);
            })
        }

    })
    return menuList;
}

export function getMenuNode(rootMenus: Menu[]): Menu[] {
    const menuList = [] as Menu[];
    rootMenus.forEach((item: Menu) => {
        if (item.children && item.children.length > 0) {
            menuList.push(item);
            const childMenuList = getMenuNode(item.children);
            childMenuList.forEach((item: Menu) => {
                menuList.push(item);
            })
        }
    })
    return menuList;
}

export function findItemByPath(menus: Menu[], path: string | null, key?: string) {
    let res: Menu | undefined;
    menus.forEach((item) => {
        iteratorMenu(item, (i) => {
            if ((path && i.path === path) || i.key == key) {
                res = i;
                return;
            }
        })
    })
    return res;
};