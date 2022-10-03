#parse("add-edit-table-utils.vm")
<template>
    <div class="handling-fee hide-table-hover">
        <el-table :data="opeTableData" border>
            #tableItemTemp()
            <el-table-column label="操作" width="76" align="center">
                <template v-slot="{ row, $index }">
                    <i class="el-icon-delete" @click="deleteTable($index)" style="cursor: pointer" />
                </template>
            </el-table-column>
        </el-table>

        <div style="cursor: pointer" class="w80" @click="addTable">
            <i class="el-icon-circle-plus" style="color: #477ef5" />
            <span style="color: #477ef5" class="ml2">添加</span>
        </div>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                #formDataScript()
                opeTableData: [],
                mustLineNum: 5
            }
        },
        computed: {
            //最多几行
            isDisable() {
                return this.opeTableData.length === this.mustLineNum
            }
        },
        mounted(){
            #formMountedScript()
        },
        methods: {
            #formMethodScript()
            resetData() {
                this.opeTableData = []
            },
            reshowData(data) {
                this.opeTableData = data
            },
            // 新增表格
            addTable() {
                if (this.isDisable) {
                    return
                }
                this.opeTableData.push({
                    id: this.guidMixin(),
                    name: ''
                })
            },
            // 删除表格
            deleteTable(index) {
                this.opeTableData.splice(index, 1)
            },
            // 校验数据并返回数据
            returnData() {
                return new Promise(resolve => {
                    resolve(this.opeTableData)
                })
            }
        }
    }
</script>

<style lang="scss" scoped>
    .el-icon-delete {
        color: #8c8c8c;
    }
    .el-icon-delete:hover {
        color: #262626 !important;
    }
</style>

