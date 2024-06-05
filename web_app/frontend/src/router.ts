import { createWebHistory, createRouter } from "vue-router";
import { RouteRecordRaw } from "vue-router";

export const routes: Array<RouteRecordRaw> = [
    {
        path: "/",
        name: "login",
        component: () => import("@/pages/Login.vue"),
        props: true
    },
    {
        path: "/home",
        name: "home",
        component: () => import("@/pages/Gallery.vue"),
        props: true
    },

    {
        path: "/gallery",
        name: "gallery",
        component: () => import("@/pages/Gallery.vue"),
        props: true
    },
    {
        path: "/post",
        name: "post",
        component: () => import("@/pages/Post.vue"),
        props: true
    },
    {
        path: "/select",
        name: "select",
        component: () => import("@/pages/Select.vue"),
        props: true
    },
    {
        path: "/edit",
        name: "upload",
        component: () => import("@/pages/Edit.vue"),
        props:  route =>{
            return ({ id : route.query.id });
        },
    }
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;
