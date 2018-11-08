<template>
    <section>
        <!--工具条-->
        <el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
            <el-form :inline="true" :model="filters"> <!--todo-->
                <el-form-item>
                    <el-button type="primary" @click="handleAdd">新增</el-button>
                </el-form-item>
            </el-form>
        </el-col>

        <!--列表-->
        <el-table :data="users" highlight-current-row v-loading="listLoading"
                  @selection-change="selsChange" style="width: 100%;">
            <el-table-column type="selection" width="150">
            </el-table-column>
            <el-table-column prop="id" label="ID" width="150" sortable>
            </el-table-column>
            <el-table-column prop="userName" label="用户名" min-width="150" sortable>
            </el-table-column>
            <el-table-column prop="addTime" label="添加时间" min-width="200" sortable>
            </el-table-column>
            <el-table-column label="操作" min-width="150">
                <template scope="scope">
                    <el-button size="small" @click="handleEdit(scope.$index, scope.row)">编辑
                    </el-button>
                    <el-button type="danger" size="small"
                               @click="handleDel(scope.$index, scope.row)">删除
                    </el-button>
                </template>
            </el-table-column>
        </el-table>

        <!--编辑界面-->
        <el-dialog title="编辑" v-model="editFormVisible" :close-on-click-modal="false">
            <el-form :model="editForm" label-width="80px" :rules="editFormRules" ref="editForm">
                <el-form-item label="用户名" prop="userName">
                    <el-input v-model="editForm.userName" auto-complete="off"></el-input>
                </el-form-item>

                <el-form-item label="密码" prop="userPwd">
                    <el-input v-model="editForm.userPwd" auto-complete="off"></el-input>
                </el-form-item>

            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click.native="editFormVisible = false">取消</el-button>
                <el-button type="primary" @click.native="editSubmit" :loading="editLoading">提交
                </el-button>
            </div>
        </el-dialog>

        <!--新增界面-->
        <el-dialog title="新增用户" v-model="addFormVisible" :close-on-click-modal="false">
            <el-form :model="addForm" label-width="80px" :rules="addFormRules" ref="addForm">
                <el-form-item label="用户名" prop="userName">
                    <el-input v-model="addForm.userName" placeholder="请输入用户名"
                              auto-complete="off"></el-input>
                </el-form-item>

                <el-form-item label="密码" prop="userPwd">
                    <el-input v-model="addForm.userPwd" auto-complete="off"></el-input>
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
    import {getUserListPage, removeUser, editUser, addUser} from '../../api/api';

    export default {
        data() {
            return {
                filters: {
                    userName: '' /*todo*/
                },
                users: [],
                total: 0,
                page: 1,
                listLoading: false,
                sels: [],//列表选中列

                editFormVisible: false,//编辑界面是否显示
                editLoading: false,
                editFormRules: {
                    userName: [
                        {required: true, message: '请输入用户名', trigger: 'blur'}
                    ],
                    userPwd: [
                        {required: true, message: '请输入登录密码', trigger: 'blur'}
                    ]
                },
                //编辑界面数据
                editForm: {
                    id: 0,
                    userName: '',
                    userPwd: ''
                },
                addFormVisible: false,//新增界面是否显示
                addLoading: false,
                addFormRules: {
                    userName: [
                        {required: true, message: '请输入用户名', trigger: 'blur'}
                    ],
                    userPwd: [
                        {required: true, message: '请输入登录密码', trigger: 'blur'}
                    ]
                },
                //新增界面数据
                addForm: {
                    userName: '',
                    userPwd: '',
                }
            }
        },
        methods: {
            //获取用户列表
            getUsers() {
                let para = {
                    page: this.page,
                    userName: this.filters.userName
                };
                this.listLoading = true;
                getUserListPage(para).then((res) => {
                    this.total = res.data.data.total;
                    this.users = res.data.data.list;
                    this.listLoading = false;
                });
            },
            //删除
            handleDel: function (index, row) {
                this.$confirm('确认删除该记录吗?', '提示', {
                    type: 'warning'
                }).then(() => {
                    this.listLoading = true;
                    let para = {id: row.id};
                    removeUser(para).then((res) => {
                        this.listLoading = false;
                        this.$message({
                            message: '删除成功',
                            type: 'success'
                        });
                        this.getUsers();
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
                    userName: '',
                    userPwd: ''
                };
            },
            //编辑
            editSubmit: function () {
                this.$refs.editForm.validate((valid) => {
                    if (valid) {
                        this.$confirm('确认提交吗？', '提示', {}).then(() => {
                            this.editLoading = true;
                            var para = {
                                id: this.editForm.id,
                                userName: this.editForm.userName,
                                userPwd: this.editForm.userPwd
                            };
                            editUser(para).then((res) => {
                                this.editLoading = false;
                                if (res.data.code !== 0) {
                                    this.$message({
                                        message: res.data.msg,
                                        type: 'error'
                                    });
                                } else {
                                    this.$message({
                                        message: '提交成功',
                                        type: 'success'
                                    });
                                }
                                this.$refs['editForm'].resetFields();
                                this.editFormVisible = false;
                                this.getUsers();
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
                            let para = Object.assign({}, this.addForm);
                            addUser(para).then((res) => {
                                this.addLoading = false;
                                if (res.data.code !== 0) {
                                    this.$message({
                                        message: res.data.msg,
                                        type: 'error'
                                    });
                                } else {
                                    this.$message({
                                        message: '提交成功',
                                        type: 'success'
                                    });
                                }
                                this.$refs['addForm'].resetFields();
                                this.addFormVisible = false;
                                this.getUsers();
                            });
                        });
                    }
                });
            },
            selsChange: function (sels) {
                this.sels = sels;
            },
        },
        mounted() {
            this.getUsers();
        }
    }

</script>
