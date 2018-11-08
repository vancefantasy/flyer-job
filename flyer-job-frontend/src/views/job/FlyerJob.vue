<template>
    <section>
        <!--工具条-->
        <el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
            <el-form :inline="true" :model="filters">
                <el-form-item>
                    <el-input v-model="filters.jobName" placeholder="任务名称"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-input v-model="filters.jobBeanId" placeholder="JobBeanId"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-select v-model="filters.appCode" placeholder="请选择应用" clearable filterable>
                        <el-option v-for="item in selectData" :label="item.label"
                                   :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" v-on:click="getFlyerJobs">查询</el-button>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" v-on:click="clearSearch">清除条件</el-button>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="handleAdd">新增</el-button>
                </el-form-item>
            </el-form>
        </el-col>

        <!--列表-->
        <el-table :data="flyerJobs" highlight-current-row v-loading="listLoading"
                  @selection-change="selsChange"
                  style="width: 100%;">
            <el-table-column type="selection" width="60">
            </el-table-column>
            <el-table-column prop="appCode" label="应用" min-width="120" sortable>
            </el-table-column>
            <el-table-column prop="jobBeanId" label="JobBeanId" min-width="120">
            </el-table-column>
            <el-table-column prop="vhost" label="vhost" min-width="60">
            </el-table-column>
            <el-table-column prop="jobName" label="任务名称" min-width="115" sortable>
            </el-table-column>
            <el-table-column prop="cronExpression" label="Cron" min-width="120">
            </el-table-column>
            <el-table-column prop="owner" label="所有者" min-width="100">
            </el-table-column>
            <el-table-column prop="status" label="状态" min-width="90">
                <template scope="scope">
                    <el-switch
                            v-model="scope.row.status"
                            active-color="#13ce66"
                            inactive-color="#ff4949"
                            :off-value="0"
                            :on-value="1"
                            @change="handleSwitch(scope.row, $event)">
                    </el-switch>
                </template>
            </el-table-column>

            <el-table-column label="运行状况" min-width="100">
                <template scope="scope">
                    <el-button size="small" @click="handleOpenLog(scope.row)">查看</el-button>
                </template>
            </el-table-column>
            <el-table-column label="客户端" min-width="90">
                <template scope="scope">
                    <el-button size="small" @click="handleOpenClient(scope.row)">客户端</el-button>
                </template>
            </el-table-column>

            <el-table-column label="运行" min-width="90">
                <template scope="scope">
                    <el-button size="small" @click="handleRunItNow(scope.row)">运行一次</el-button>
                </template>
            </el-table-column>

            <el-table-column label="停止" min-width="80">
                <template scope="scope">
                    <el-button size="small" @click="handleCancelItNow(scope.row)">停止</el-button>
                </template>
            </el-table-column>

            <el-table-column label="操作" width="150">
                <template scope="scope">
                    <el-button size="small" @click="handleEdit(scope.$index, scope.row)">编辑
                    </el-button>
                    <el-button type="danger" size="small"
                               @click="handleDel(scope.$index, scope.row)">删除
                    </el-button>
                </template>
            </el-table-column>
        </el-table>

        <!--工具条-->
        <el-col :span="24" class="toolbar">
            <el-button type="danger" @click="batchRemove" :disabled="this.sels.length===0">批量删除
            </el-button>
            <el-pagination layout="prev, pager, next" @current-change="handleCurrentChange"
                           :page-size="10"
                           :total="total" style="float:right;">
            </el-pagination>
        </el-col>

        <!--编辑界面-->
        <el-dialog title="编辑" v-model="editFormVisible" :close-on-click-modal="false">
            <el-form :model="editForm" label-width="80px" :rules="editFormRules" ref="editForm">
                <el-form-item label="任务名称" prop="jobName">
                    <el-input v-model="editForm.jobName" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="JobBeanId" prop="jobBeanId">
                    <el-input v-model="editForm.jobBeanId" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="应用" prop="appCode">
                    <el-select v-model="editForm.appCode" placeholder="请选择" filterable>
                        <el-option v-for="item in selectData" :label="item.label"
                                   :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="vhost" prop="vhost">
                    <el-input v-model="editForm.vhost" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="Cron" prop="cronExpression">
                    <el-input v-model="editForm.cronExpression" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="参数" prop="paramData">
                    <el-input type="textarea" v-model="editForm.paramData"></el-input>
                </el-form-item>
                <el-form-item label="所有者" prop="owner">
                    <el-input v-model="editForm.owner" auto-complete="off"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click.native="editFormVisible = false">取消</el-button>
                <el-button type="primary" @click.native="editSubmit" :loading="editLoading">提交
                </el-button>
            </div>
        </el-dialog>

        <!--新增界面-->
        <el-dialog title="新增" v-model="addFormVisible" :close-on-click-modal="false">
            <el-form :model="addForm" label-width="80px" :rules="addFormRules" ref="addForm">

                <el-form-item label="任务名称" prop="jobName">
                    <el-input v-model="addForm.jobName" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="JobBeanId" prop="jobBeanId">
                    <el-input v-model="addForm.jobBeanId" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="应用" prop="appCode">
                    <el-select v-model="addForm.appCode" placeholder="请选择" filterable>
                        <el-option v-for="item in selectData" :label="item.label"
                                   :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="vhost" prop="vhost">
                    <el-input v-model="addForm.vhost" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="Cron" prop="cronExpression">
                    <el-input v-model="addForm.cronExpression" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="参数" prop="paramData">
                    <el-input type="textarea" v-model="addForm.paramData"></el-input>
                </el-form-item>
                <el-form-item label="所有者" prop="owner">
                    <el-input v-model="addForm.owner" auto-complete="off"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click.native="addFormVisible = false">取消</el-button>
                <el-button type="primary" @click.native="addSubmit" :loading="addLoading">提交
                </el-button>
            </div>
        </el-dialog>

        <!--查看客户端-->
        <el-dialog title="客户端列表" v-model="openClientVisible" :close-on-click-modal="false">
            <el-table :data="clientList" highlight-current-row v-loading="listLoading"
                      style="width: 100%;">
                <el-table-column type="index" width="50">
                </el-table-column>
                <el-table-column prop="id" label="ID" min-width="160" sortable>
                </el-table-column>
                <el-table-column prop="vhost" label="vhost" min-width="50" sortable>
                </el-table-column>
                <el-table-column prop="client" label="客户端" min-width="100" sortable>
                </el-table-column>
                <el-table-column prop="createTime" label="创建时间" min-width="100">
                </el-table-column>
                <el-table-column prop="lastFlushTime" label="上次刷新时间" min-width="100" sortable>
                </el-table-column>
            </el-table>
        </el-dialog>

        <!--执行任务-->
        <el-dialog title="客户端列表" v-model="openClientVisibleForSend" :close-on-click-modal="false">
            <el-table :data="clientList" highlight-current-row v-loading="listLoading"
                      @selection-change="handleCurrentChangeForClient"
                      style="width: 100%;" ref="clientListTable">
                <el-table-column type="selection" width="50">
                </el-table-column>
                <el-table-column prop="id" label="ID" min-width="160" sortable>
                </el-table-column>
                <el-table-column prop="vhost" label="vhost" min-width="50" sortable>
                </el-table-column>
                <el-table-column prop="client" label="客户端" min-width="100" sortable>
                </el-table-column>
                <el-table-column prop="createTime" label="创建时间" min-width="100">
                </el-table-column>
                <el-table-column prop="lastFlushTime" label="上次刷新时间" min-width="100" sortable>
                </el-table-column>
            </el-table>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click.native="handleSendJob"
                           :disabled="this.seletedClient.length===0">执行
                </el-button>
            </div>
        </el-dialog>

        <!--日志-->
        <el-dialog title="运行状况" v-model="openLogVisible" :close-on-click-modal="false">
            <el-tabs type="card">
                <el-tab-pane label="即将执行">
                    <div v-for="triggerTime in triggerTimes" class="text item">
                        {{triggerTime}}
                    </div>
                </el-tab-pane>
                <el-tab-pane label="日志">
                    <el-collapse>
                        <el-collapse-item v-for="record in jobsRecord" v-bind:title="record.title"
                                          v-bind:name="record.id">
                            <div v-for="log in record.logs"> {{log}}</div>
                        </el-collapse-item>
                    </el-collapse>
                </el-tab-pane>
            </el-tabs>
        </el-dialog>
    </section>
</template>

<script>
    import {
        getFlyerJobListPage,
        removeFlyerJob,
        batchRemoveFlyerJob,
        editFlyerJob,
        addFlyerJob,
        operateFlyerJob,
        runFlyerJobNow,
        getAppList,
        listClient,
        cancelFlyerJobNow,
        listRecord
    } from '../../api/api';

    export default {
        data() {
            return {
                filters: {
                    jobName: '',
                    jobBeanId: '',
                    appCode: ''
                },
                flyerJobs: [],
                total: 0,
                page: 1,
                listLoading: false,
                sels: [],//列表选中列

                editFormVisible: false,//编辑界面是否显示
                editLoading: false,
                editFormRules: {
                    name: [
                        {required: true, message: '请输入姓名', trigger: 'blur'}
                    ]
                },
                //编辑界面数据
                editForm: {
                    jobName: '',
                    jobBeanId: '',
                    appCode: '',
                    vhost: '',
                    cronExpression: '',
                    owner: ''
                },

                addFormVisible: false,//新增界面是否显示
                addLoading: false,
                addFormRules: {
                    name: [
                        {required: true, message: '请输入姓名', trigger: 'blur'}
                    ]
                },
                //新增界面数据
                addForm: {
                    jobName: '',
                    jobBeanId: '',
                    vhost: '',
                    cronExpression: '',
                    owner: ''
                },
                selectData: {},
                openClientVisible: false,
                openClientVisibleForSend: false,
                clientList: [],
                openLogVisible: false,
                jobsRecord: [],
                triggerTimes: [],
                seletedJobIdForExeceue: 0,
                seletedClient: []
            }
        },
        methods: {
            handleCurrentChange(val) {
                this.page = val;
                this.getFlyerJobs();
            },

            getApps: function () {
                let para = {};
                getAppList(para).then((res) => {
                    this.selectData = res.data.data;
                });
            },

            clearSearch() {
                this.filters.jobBeanId = '';
                this.filters.appCode = '';
                this.filters.jobName = '';
                this.getFlyerJobs();
            },

            //获取apps列表
            getFlyerJobs() {
                let para = {
                    page: this.page,
                    jobName: this.filters.jobName,
                    jobBeanId: this.filters.jobBeanId,
                    appCode: this.filters.appCode
                };
                this.listLoading = true;
                getFlyerJobListPage(para).then((res) => {
                    this.total = res.data.data.total;
                    this.flyerJobs = res.data.data.list;
                    this.listLoading = false;
                });
                this.getApps();
            },
            //删除
            handleDel: function (index, row) {
                this.$confirm('删除任务同时也会删除Quartz调度器对应的任务，确认删除该任务吗？?', '提示', {
                    type: 'warning'
                }).then(() => {
                    this.listLoading = true;
                    let para = {id: row.id};
                    removeFlyerJob(para).then((res) => {
                        this.listLoading = false;
                        this.$message({
                            message: '删除成功',
                            type: 'success'
                        });
                        this.getFlyerJobs();
                    });
                }).catch(() => {

                });
            },
            //显示编辑界面
            handleEdit: function (index, row) {
                this.editFormVisible = true;
                this.editForm = Object.assign({}, row);
                this.getApps();
            },
            //显示新增界面
            handleAdd: function () {
                this.addFormVisible = true;
                this.addForm = {
                    jobName: '',
                    jobBeanId: '',
                    cronExpression: '',
                    owner: ''
                };
                this.getApps();
            },
            //编辑
            editSubmit: function () {
                this.$refs.editForm.validate((valid) => {
                    if (valid) {
                        this.$confirm('确认提交吗？', '提示', {}).then(() => {
                            this.editLoading = true;
                            var para = {
                                id: this.editForm.id,
                                jobBeanId: this.editForm.jobBeanId,
                                jobName: this.editForm.jobName,
                                appCode: this.editForm.appCode,
                                vhost: this.editForm.vhost,
                                jobDesc: this.editForm.jobDesc,
                                cronExpression: this.editForm.cronExpression,
                                paramData: this.editForm.paramData,
                                owner: this.editForm.owner
                            };
                            editFlyerJob(para).then((res) => {
                                this.editLoading = false;
                                this.$message({
                                    message: '提交成功',
                                    type: 'success'
                                });
                                this.$refs['editForm'].resetFields();
                                this.editFormVisible = false;
                                this.getFlyerJobs();
                            });
                        });
                    }
                });
            },
            //新增
            addSubmit: function () {
                this.$refs.addForm.validate((valid) => {
                    if (valid) {
                        this.$confirm('确认提交吗？', '提示', {}).then(() => {
                            this.addLoading = true;
                            //NProgress.start();
                            let para = Object.assign({}, this.addForm);
                            addFlyerJob(para).then((res) => {
                                this.addLoading = false;
                                //NProgress.done();
                                this.$message({
                                    message: '提交成功',
                                    type: 'success'
                                });
                                this.$refs['addForm'].resetFields();
                                this.addFormVisible = false;
                                this.getFlyerJobs();
                            });
                        });
                    }
                });
            },
            selsChange: function (sels) {
                this.sels = sels;
            },
            //批量删除
            batchRemove: function () {
                var ids = this.sels.map(item => item.id).toString();
                this.$confirm('删除任务同时也会删除Quartz调度器对应的任务，确认删除选中任务吗？', '提示', {
                    type: 'warning'
                }).then(() => {
                    this.listLoading = true;
                    let para = {ids: ids};
                    batchRemoveFlyerJob(para).then((res) => {
                        this.listLoading = false;
                        this.$message({
                            message: '删除成功',
                            type: 'success'
                        });
                        this.getFlyerJobs();
                    });
                }).catch(() => {

                });
            },
            handleSwitch: function (row, event) {
                var opt = '';
                if (event == 1) {
                    opt = "启动";
                } else {
                    opt = "停止";
                }

                this.$confirm('确认' + opt + '该任务吗?', '提示', {
                    type: 'warning'
                }).then(() => {
                    this.listLoading = true;
                    let para = {id: row.id, status: event};
                    operateFlyerJob(para).then((res) => {
                        this.listLoading = false;
                        this.$message({
                            message: opt + '成功',
                            type: 'success'
                        });
                        this.getFlyerJobs();
                    });
                }).catch(() => {

                });
            },
            handleRunItNow: function (row) {
                this.listLoading = true;
                let para = {id: row.id};
                listClient(para).then((res) => {
                    this.listLoading = false;
                    this.clientList = res.data.data;
                    this.seletedJobIdForExeceue = row.id;
                    this.openClientVisibleForSend = true;
                });
            },

            handleSendJob: function () {
                let para = {jobId: this.seletedJobIdForExeceue, connId: this.seletedClient.id};
                runFlyerJobNow(para).then((res) => {
                    this.$message({
                        message: '执行成功',
                        type: 'success'
                    });
                    this.openClientVisibleForSend = false;
                });
            },

            handleCancelItNow: function (row) {
                this.$confirm('确认要停止该任务吗?', '提示', {
                    type: 'warning'
                }).then(() => {
                    this.listLoading = true;
                    let para = {id: row.id};
                    cancelFlyerJobNow(para).then((res) => {
                        this.listLoading = false;
                        this.$message({
                            message: '停止成功',
                            type: 'success'
                        });
                        this.getFlyerJobs();
                    });
                }).catch(() => {

                });
            },
            handleOpenClient: function (row) {
                this.listLoading = true;
                let para = {id: row.id};
                listClient(para).then((res) => {
                    this.listLoading = false;
                    this.clientList = res.data.data;
                    this.openClientVisible = true;
                });

            },
            handleOpenLog: function (row) {
                this.listLoading = true;
                let para = {jobId: row.id};
                listRecord(para).then((res) => {
                    this.jobsRecord = res.data.data.records;
                    this.triggerTimes = res.data.data.triggerTimes;
                    this.listLoading = false;
                    this.openLogVisible = true;
                });
            },
            handleCurrentChangeForClient(val) {
                val.forEach((row, index) => {
                    if (index === val.length - 1)
                        return;
                    this.$refs.clientListTable.toggleRowSelection(row, false);
                });
                if (val[0] == undefined) {
                    this.seletedClient = [];
                } else {
                    this.seletedClient = val[0];
                }
            }
        },
        mounted() {
            this.getFlyerJobs();
        }
    }

</script>

<style scoped>

</style>
