<template>
    <section>
        <!--工具条-->
        <el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
            <el-form :inline="true" :model="filters">
                <el-form-item>
                    <el-input v-model="filters.appName" placeholder="名称"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" v-on:click="getApps">查询</el-button>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="handleAdd">新增</el-button>
                </el-form-item>
            </el-form>
        </el-col>

        <!--列表-->
        <el-table :data="apps" highlight-current-row v-loading="listLoading"
                  @selection-change="selsChange" style="width: 100%;">
            <el-table-column type="selection" width="55">
            </el-table-column>
            <el-table-column prop="id" label="ID" width="150" sortable>
            </el-table-column>
            <el-table-column prop="appCode" label="应用编码" min-width="150" sortable>
            </el-table-column>
            <el-table-column prop="appName" label="应用名称" min-width="150" sortable>
            </el-table-column>
            <el-table-column prop="appDesc" label="描述" min-width="320">
            </el-table-column>
            <el-table-column prop="createTime" label="添加时间" min-width="200" sortable>
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

        <!--底部工具条-->
        <el-col :span="24" class="toolbar">
            <el-button type="danger" @click="batchRemove" :disabled="this.sels.length===0">批量删除
            </el-button>
            <el-pagination layout="prev, pager, next" @current-change="handleCurrentChange"
                           :page-size="10" :total="total" style="float:right;">
            </el-pagination>
        </el-col>

        <!--编辑界面-->
        <el-dialog title="编辑" v-model="editFormVisible" :close-on-click-modal="false">
            <el-form :model="editForm" label-width="80px" :rules="editFormRules" ref="editForm">
                <el-form-item label="编码" prop="appCode">
                    <el-input v-model="editForm.appCode" auto-complete="off"></el-input>
                </el-form-item>

                <el-form-item label="应用名称" prop="appName">
                    <el-input v-model="editForm.appName" auto-complete="off"></el-input>
                </el-form-item>

                <el-form-item label="描述" prop="appDesc">
                    <el-input type="textarea" v-model="editForm.appDesc"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click.native="editFormVisible = false">取消</el-button>
                <el-button type="primary" @click.native="editSubmit" :loading="editLoading">提交
                </el-button>
            </div>
        </el-dialog>

        <!--新增界面-->
        <el-dialog title="新增应用" v-model="addFormVisible" :close-on-click-modal="false">
            <el-form :model="addForm" label-width="80px" :rules="addFormRules" ref="addForm">
                <el-form-item label="编码" prop="appCode">
                    <el-input v-model="addForm.appCode" placeholder="请输入编码"
                              auto-complete="off"></el-input>
                </el-form-item>

                <el-form-item label="应用名称" prop="appName">
                    <el-input v-model="addForm.appName" auto-complete="off"></el-input>
                </el-form-item>

                <el-form-item label="描述" prop="appDesc">
                    <el-input type="textarea" v-model="addForm.appDesc"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click.native="addFormVisible = false">取消</el-button>
                <el-button type="primary" @click.native="addSubmit" :loading="addLoading">提交
                </el-button>
            </div>
        </el-dialog>
    </section>
</template>

<script>
    import {getAppListPage, removeApp, batchRemoveApp, editApp, addApp} from '../../api/api';

    export default {
        data() {
            return {
                filters: {
                    appName: ''
                },
                apps: [],
                total: 0,
                page: 1,
                listLoading: false,
                sels: [],//列表选中列

                editFormVisible: false,//编辑界面是否显示
                editLoading: false,
                editFormRules: {
                    appName: [
                        {required: true, message: '请输入应用名称', trigger: 'blur'}
                    ],
                    appCode: [
                        {required: true, message: '请输入应用编码', trigger: 'blur'}
                    ],
                    appDesc: [
                        {required: true, message: '请输入应用描述信息', trigger: 'blur'}
                    ]
                },
                //编辑界面数据
                editForm: {
                    id: 0,
                    appName: '',
                    appCode: '',
                    appDesc: ''
                },

                addFormVisible: false,//新增界面是否显示
                addLoading: false,
                addFormRules: {
                    appName: [
                        {required: true, message: '请输入应用名称', trigger: 'blur'}
                    ],
                    appCode: [
                        {required: true, message: '请输入应用编码', trigger: 'blur'}
                    ],
                    appDesc: [
                        {required: true, message: '请输入应用描述信息', trigger: 'blur'}
                    ]
                },
                //新增界面数据
                addForm: {
                    appName: '',
                    appCode: '',
                    appDesc: ''
                }

            }
        },
        methods: {
            handleCurrentChange(val) {
                this.page = val;
                this.getApps();
            },
            //获取用户列表
            getApps() {
                let para = {
                    page: this.page,
                    appName: this.filters.appName
                };
                this.listLoading = true;
                getAppListPage(para).then((res) => {
                    this.total = res.data.data.total;
                    this.apps = res.data.data.list;
                    this.listLoading = false;
                });
            },
            //删除
            handleDel: function (index, row) {
                this.$confirm('确认删除该记录吗?', '提示', {
                    type: 'warning'
                }).then(() => {
                    this.listLoading = true;
                    //NProgress.start();
                    let para = {id: row.id};
                    removeApp(para).then((res) => {
                        this.listLoading = false;
                        //NProgress.done();
                        this.$message({
                            message: '删除成功',
                            type: 'success'
                        });
                        this.getApps();
                    });
                }).catch(() => {

                });
            },
            //显示编辑界面
            handleEdit: function (index, row) {
                this.editFormVisible = true;
                this.editForm = Object.assign({}, row);
            },
            //显示新增界面
            handleAdd: function () {
                this.addFormVisible = true;
                this.addForm = {
                    appName: '',
                    appCode: '',
                    appDesc: ''
                };
            },
            //编辑
            editSubmit: function () {
                this.$refs.editForm.validate((valid) => {
                    if (valid) {
                        this.$confirm('确认提交吗？', '提示', {}).then(() => {
                            this.editLoading = true;
                            //NProgress.start();
                            var para = {
                                id: this.editForm.id,
                                appCode: this.editForm.appCode,
                                appName: this.editForm.appName,
                                appDesc: this.editForm.appDesc
                            };
                            editApp(para).then((res) => {
                                this.editLoading = false;
                                //NProgress.done();
                                this.$message({
                                    message: '提交成功',
                                    type: 'success'
                                });
                                this.$refs['editForm'].resetFields();
                                this.editFormVisible = false;
                                this.getApps();
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
                            addApp(para).then((res) => {
                                this.addLoading = false;
                                //NProgress.done();

                                this.$message({
                                    message: '提交成功',
                                    type: 'success'
                                });
                                this.$refs['addForm'].resetFields();
                                this.addFormVisible = false;
                                this.getApps();
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
                this.$confirm('确认删除选中记录吗？', '提示', {
                    type: 'warning'
                }).then(() => {
                    this.listLoading = true;
                    //NProgress.start();
                    let para = {ids: ids};
                    batchRemoveApp(para).then((res) => {
                        this.listLoading = false;
                        //NProgress.done();
                        this.$message({
                            message: '删除成功',
                            type: 'success'
                        });
                        this.getApps();
                    });
                }).catch(() => {

                });
            }
        },
        mounted() {
            this.getApps();
        }
    }

</script>

<style scoped>

</style>


