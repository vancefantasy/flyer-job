import Login from './views/Login.vue'
import NotFound from './views/404.vue'
import Home from './views/Home.vue'
import FlyerJob from './views/job/FlyerJob.vue'
import OutJob from './views/job/OutJob.vue'
import User from './views/sys/User.vue'
import App from './views/sys/App.vue'
import Cluster from './views/sys/Cluster.vue'
import VueRouter from 'vue-router'

let routes = [
    {
        path: '/login',
        component: Login,
        name: 'login',
        hidden: true
    },
    {
        path: '/404',
        component: NotFound,
        name: '',
        hidden: true
    },
    {
        path: '/',
        component: Home,
        name: '任务管理',
        iconCls: 'el-icon-menu',
        children: [
            {path: '/jobs', component: FlyerJob, name: '内置任务'},
            {path: '/outjob', component: OutJob, name: '外部任务'}
        ]
    },
    {
        path: '/',
        component: Home,
        name: '系统设置',
        iconCls: 'el-icon-setting',
        children: [
            {path: '/app', component: App, name: '应用管理'},
            {path: '/user', component: User, name: '用户管理'},
            {path: '/cluster', component: Cluster, name: '集群设置'}
        ]
    },
    {
        path: '*',
        hidden: true,
        redirect: {path: '/404'}
    }
];

const router = new VueRouter({
    routes
});

export default router;
