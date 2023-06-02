#parse("list-utils.vm")
<template>
    <div class="mt-10px query-page-style">
        <!--条件搜索-->
        <el-form ref="refSearchForm" :inline="true" :model="searchForm">
          #queryFormItemTmp()
            <el-form-item>
                <!--查询按钮-->
                <el-button type="primary" @click="resetPageReq">查询</el-button>
                <el-button type="primary" @click="resetForm">重置</el-button>
            </el-form-item>
        </el-form>
        <div class="rowES mb-10px">
          #tableGlobalOperatorTmp()
        </div>
        <!--表格和分页-->
        <el-table
                id="resetElementDialog"
                ref="refuserTable"
                :height="`calc(100vh - 260px)`"
                border
                :data="tableListData"
                @selection-change="handleSelectionChange"
        >
          #tableItemTmp()
            <!--点击操作-->
            <el-table-column fixed="right" align="center" label="操作" width="180">
                <template #default="{ row }">
                    <div class="table-operation-btn">
                      #tableOperatorTmp()
                    </div>
                </template>
            </el-table-column>
        </el-table>
        <!--分页-->
      #tablePaginationTmp()
    </div>
</template>
<script setup lang="ts" name="${basicConfig.apiFileName}">
    import {Delete, FolderAdd} from '@element-plus/icons-vue'
    import {getQueryParam, routerPush, routerReplace} from "@/hooks/use-self-router"
    import {useTable} from '@/hooks/use-table'
 
    const searchForm = reactive({#formKeyScrpt()})
    const selectPageReq = () => {
        const reqConfig = {
            url: '${apiConfig.queryApi}',
            method: '${apiConfig.queryMethod}'
        }
        tableListReq(reqConfig).then(({data}) => {
            tableListData.value = data.records
            totalPage.value = data.total
        })
    }
    //重置
    const refSearchForm = $ref()
    const resetForm = () => {
        refSearchForm.resetFields()
        dateRangePacking(['', ''])
        resetPageReq()
    }

    //批量删除
    const multiDelBtnClick = () => {
        let reqConfig = {
            url: '${apiConfig.multiDeleteApi}',
            method: '${apiConfig.multiDeleteMethod}',
            bfLoading: true
        }
        multiDelBtnDill(reqConfig)
    }

    //单个删除
    const tableDelClick = (row) => {
        const reqConfig = {
            url: '${apiConfig.deleteApi}',
            data: {id: row.id},
            isParams: true,
            method: '${apiConfig.deleteMethod}',
            bfLoading: true
        }
        tableDelDill(row, reqConfig)
    }

    //添加和修改详情

    const addBtnClick = () => {
        routerPush('${basicConfig.apiFileNameFirstCase}AddEdit')
    }
    const tableEditClick = (row) => {
        routerPush('${basicConfig.apiFileNameFirstCase}AddEdit', {isEdit: true, row})
    }
    const tableDetailClick = (row) => {
        routerPush('${basicConfig.apiFileNameFirstCase}Detail', {isDetail: true, row})
    }
      #onMountedScript()
      #apiReqScript()
    //引入table-query相关的hooks 方法
    let {
        pageNum,
        pageSize,
        totalPage,
        tableListData,
        tableListReq,
        dateRangePacking,
        handleSelectionChange,
        handleCurrentChange,
        handleSizeChange,
        resetPageReq,
        multiDelBtnDill,
        tableDelDill
    } = useTable(searchForm, selectPageReq)
</script>
