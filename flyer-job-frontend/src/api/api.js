import axios from 'axios';
import router from '../routes'

axios.interceptors.response.use(
    response => {
        return response;
    },
    error => {
        if (error.response) {
            switch (error.response.status) {
                case 401:
                    router.currentRoute.path !== 'login' &&
                    router.replace({
                        path: 'login'
                    });
            }
        }
        return Promise.reject(error.response);
    }
);

//let base = 'http://job.flyer.com/api';
let base = 'http://localhost:8088/api';

export const requestLogin = params => {
    return axios.post(`${base}/user/login`, params).then(res => res.data);
};

export const getFlyerJobListPage = params => {
    return axios.get(`${base}/flyerJob/listpage`, {params: params});
};

export const removeFlyerJob = params => {
    return axios.get(`${base}/flyerJob/remove`, {params: params});
};

export const operateFlyerJob = params => {
    return axios.get(`${base}/flyerJob/operate`, {params: params});
};

export const runFlyerJobNow = params => {
    return axios.get(`${base}/flyerJob/runnow`, {params: params});
};

export const cancelFlyerJobNow = params => {
    return axios.get(`${base}/flyerJob/cancel`, {params: params});
};

export const batchRemoveFlyerJob = params => {
    return axios.get(`${base}/flyerJob/batchremove`, {params: params});
};

export const editFlyerJob = params => {
    return axios.post(`${base}/flyerJob/edit`, params);
};

export const addFlyerJob = params => {
    return axios.get(`${base}/flyerJob/add`, {params: params});
};

export const listClient = params => {
    return axios.get(`${base}/flyerJob/clients`, {params: params});
};

//record
export const listRecord = params => {
    return axios.get(`${base}/record/list`, {params: params});
};

// app
export const getAppListPage = params => {
    return axios.get(`${base}/app/listpage`, {params: params});
};

export const getAppList = params => {
    return axios.get(`${base}/app/list`);
};

export const addApp = params => {
    return axios.get(`${base}/app/add`, {params: params});
};

export const editApp = params => {
    return axios.post(`${base}/app/edit`, params);
};

export const removeApp = params => {
    return axios.get(`${base}/app/remove`, {params: params});
};

export const batchRemoveApp = params => {
    return axios.get(`${base}/app/batchremove`, {params: params});
};

// user
export const getUserListPage = params => {
    return axios.get(`${base}/user/listpage`, {params: params});
};

export const getUserList = params => {
    return axios.get(`${base}/user/list`);
};

export const addUser = params => {
    return axios.get(`${base}/user/add`, {params: params});
};

export const editUser = params => {
    return axios.post(`${base}/user/edit`, params);
};

export const removeUser = params => {
    return axios.get(`${base}/user/remove`, {params: params});
};

//cluster
export const getCluster = params => {
    return axios.get(`${base}/cluster/show`);
};

export const editCluster = params => {
    return axios.get(`${base}/cluster/save`, {params: params});
};
