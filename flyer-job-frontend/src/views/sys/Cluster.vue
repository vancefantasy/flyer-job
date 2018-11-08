<template>
    <el-form ref="cluster" :model="cluster" label-width="80px"
             style="margin:20px;width:60%;min-width:600px;">
        <el-form-item label="集群设置">
            <el-input type="textarea" v-model="cluster.servers"></el-input>
        </el-form-item>
        <el-form-item label="版本号">
            <label>{{cluster.version}}</label>
        </el-form-item>
        <el-form-item label="修改时间">
            <label>{{cluster.lastUpdateTime}}</label>
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click.native="onSubmit">保存</el-button>
        </el-form-item>
    </el-form>
</template>

<script>
    import {getCluster, editCluster} from '../../api/api';

    export default {
        data() {
            return {
                cluster: {
                    servers: '',
                    version: 0,
                    lastUpdateTime: ''
                }
            }
        },
        methods: {
            onSubmit() {
                this.$confirm('确认修改吗？', '提示', {}).then(() => {
                    let para = {servers: this.cluster.servers};
                    editCluster(para).then((res) => {
                        this.$message({
                            message: '提交成功',
                            type: 'success'
                        });
                        this.getCluster();
                    });
                });
            },
            getCluster: function () {
                this.loading = true;
                getCluster().then((res) => {
                    if (res.data.data) {
                        this.cluster = res.data.data;
                    }
                    this.loading = false;
                });
            }
        },
        mounted() {
            this.getCluster();
        }
    }
</script>
