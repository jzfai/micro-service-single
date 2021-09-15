<template>
    <!--操作-->
    <div class="mr-3 rowSS">
        <el-button type="primary" icon="el-icon-plus" @click="addBtnClick">新增</el-button>
        <el-button type="primary" icon="el-icon-delete" @click="multiDelBtnClick">删除</el-button>
        <!--条件搜索-->
        <el-form ref="refsearchFormMixin" :inline="true" class="demo-searchFormMixin ml-2">
            <el-form-item label-width="0px" label="" prop="username" label-position="left">
                <el-input v-model="searchFormMixin.username" class="widthPx-150" placeholder="用户名"/>
            </el-form-item>
            <el-form-item label-width="0px" label="" prop="createTime" label-position="left">
                <el-date-picker
                        v-model="startEndArrMixin"
                        type="datetimerange"
                        format="YYYY-MM-DD"
                        value-format="YYYY-MM-DD HH:mm:ss"
                        @change="dateTimePacking"
                        class="widthPx-250"
                        range-separator="-"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期"
                />
            </el-form-item>
            <!--查询按钮-->
            <el-button @click="searchBtnClick">查询</el-button>
        </el-form>
    </div>
    <!--表格和分页-->
    <el-table id="resetElementDialog" ref="refuserTable" size="mini" border :data="usertableData">
        <el-table-column
                type="selection"
                align="center"
                width="50"/>
        <el-table-column
                align="center"
                prop="name"
                label="品牌名称"
                min-width="100"/>
        <el-table-column
                align="center"
                prop="image"
                label="品牌图片地址"
                min-width="100"/>
        <el-table-column
                align="center"
                prop="letter"
                label="品牌的首字母"
                min-width="100"/>
        <el-table-column
                align="center"
                prop="seq"
                label="排序"
                min-width="100"/>
        <!--点击操作-->
        <el-table-column fixed="right" align="center" label="操作" width="180">
            <template #default="{ row }">
                <el-button type="text" size="small" @click="tableEditClick(row)">编辑</el-button>
                <el-button type="text" size="small" @click="tableDetailClick(row)">详情</el-button>
                <el-button type="text" size="small" @click="tableDelClick(row)">删除</el-button>
            </template>
        </el-table-column>
    </el-table>
    <!--分页-->
    <div class="block columnCC mt-2 mb-5">
        <el-pagination
                :current-page="pageNumMixin"
                :page-sizes="[10, 20, 50, 100]"
                :page-size="pageSizeMixin"
                layout="total, sizes, prev, pager, next, jumper"
                :total="pageTotalMixin"
                @size-change="handleSizeChangeMixin"
                @current-change="handleCurrentChangeMixin"
        />
    </div>
    <!--详情-->
    <el-dialog
            v-if="detailDialogMixin"
            :title="dialogTitleMixin"
            v-model="detailDialogMixin"
            width="40vw"
            :close-on-click-modal="false"
    >
        <div class="detail-container rowBC elODialogModalBodyH60vh">
            <div class="detail-container-item">DBC文件名：{{ detailData.username }}</div>
        </div>
        <div class="detail-container rowBC elODialogModalBodyH60vh">
            <div class="detail-container-item" style="color: green" v-if="detailData.status === 1">状态： 启用</div>
            <div class="detail-container-item" v-else>状态： 停用</div>
        </div>
        <div class="detail-container rowBC elODialogModalBodyH60vh">
            <div class="detail-container-item">说明：{{ detailData.remark }}</div>
        </div>
        <template #footer>
      <span class="dialog-footer">
        <el-button type="primary" @click="detailDialogMixin = false">确 定</el-button>
      </span>
        </template>
    </el-dialog>
    <BrandEForm ref="refBrandEForm"/>
</template>

<script setup>
    import {onMounted, getCurrentInstance, watch, ref, reactive, computed, toRefs} from 'vue'

    let {proxy} = getCurrentInstance()
    /*获取子组件实例*/
    import BrandEForm from './BrandEForm.vue'

    const refBrandEForm = ref(null)
    /*
     * 一般根据页面层次来排序 如此页面 表格查询和筛选->table的操作
     * 每个模块按：响应数据定义->公用方法->请求方法->页面按钮操作方法 进行排序
     * */
    /*表格查询和筛选*/
    let usertableData = ref([])
    let searchFormMixin = reactive({
        username: '',
        startTime: '',
        endTime: '',
        pageNumber: 0,
        pageSize: 10
    })
    let selectPageReq = () => {
        const data = Object.assign(searchFormMixin, {
            pageNum: proxy.pageNumMixin,
            pageSize: proxy.pageSizeMixin
        })
        Object.keys(data).forEach((fItem) => {
            if (data[fItem] === '' || data[fItem] === null || data[fItem] === undefined) delete data[fItem]
        })
        let reqConfig = {
            url: '/ty-user/brandE/selectPage',
            method: 'get',
            data,
            isParams: true,
            isAlertErrorMsg: false
        }
        proxy.$axiosReq(reqConfig).then((resData) => {
            usertableData.value = resData.data?.records
            proxy.pageTotalMixin = resData.data?.total
        })
    }
    const dateTimePacking = (timeArr) => {
        if (timeArr && timeArr.length === 2) {
            searchFormMixin.startTime = timeArr[0]
            searchFormMixin.endTime = timeArr[1]
        } else {
            searchFormMixin.startTime = ''
            searchFormMixin.endTime = ''
        }
    }
    onMounted(() => {
        selectPageReq()
    })
    const searchBtnClick = () => {
        proxy.pageNumMixin = 1
        selectPageReq()
    }

    /*添加和修改*/
    let addBtnClick = () => {
        refBrandEForm.value.showModal()
    }

    let tableEditClick = (row) => {
        refBrandEForm.value.showModal(true, row)
    }
    /*详情*/
    let detailData = ref({})
    let tableDetailClick = (row) => {
        proxy.dialogTitleMixin = `详情【${row.username}】`
        refBrandEForm.value.getDetailByIdReq(row.id).then((resData) => {
            detailData.value = resData.data
            proxy.detailDialogMixin = true
        })
    }

    /*删除*/
    let deleteByIdReq = (id) => {
        return proxy.$axiosReq({
            url: '/ty-user/brandE/deleteById',
            data: {id: id},
            isParams: true,
            method: 'delete',
            bfLoading: true
        })
    }
    let tableDelClick = async (row) => {
        await proxy.elConfirmMixin('确定', `您确定要删除【${row.needReplace}】吗？`)
        deleteByIdReq(row.id).then(() => {
            selectPageReq()
            proxy.elMessageMixin(`【${row.needReplace}】删除成功`)
        })
    }

    /*批量删除*/
    const multiDelBtnClick = async () => {
        let rowDeleteIdArrMixin = []
        let selectionArr = proxy.$refs.refuserTable
        let
            deleteNameTitle = ''
        rowDeleteIdArrMixin = selectionArr.selection.map((mItem) => {
            deleteNameTitle = deleteNameTitle + mItem.needReplace + ','
            return mItem.id
        })
        if (rowDeleteIdArrMixin.length === 0) {
            proxy.elMessageMixin('表格选项不能为空', 'warning')
            return
        }
        let stringLength = deleteNameTitle.length - 1
        await proxy.elConfirmMixin('删除', `您确定要删除${deleteNameTitle.slice(0, stringLength)}吗`)
        const data = rowDeleteIdArrMixin
        proxy.$axiosReq({
            url: `/ty-user/brandE/deleteBatchIds`,
            data,
            method: 'DELETE',
            bfLoading: true
        }).then((res) => {
            proxy.elMessageMixin('删除成功')
            proxy.selectPageReq()
        })
    }
    /*导出*/
    let downloadReq = (row) => {
        let reqConfig = {
            url: '/ty-user/brandE/download',
            method: 'get',
            data: {
                id: row.id
            },
            isDownLoadFile: true,
            isParams: true,
            isAlertErrorMsg: false
        }
        proxy.$axiosReq(reqConfig).then((resData) => {
            const url = window.URL.createObjectURL(new Blob([resData]))
            const link = document.createElement('a')
            link.href = url
            link.setAttribute('download', `${row.username}.xls`)
            document.body.appendChild(link)
            link.click()
        })
    }
    let tableDownloadClick = (row) => {
        downloadReq(row)
    }
</script>

<style scoped lang="scss">
    /*详情*/
    .detail-container {
        flex-wrap: wrap;
    }

    .detail-container-item {
        min-width: 40%;
        margin-bottom: 20px;
    }

    .detailDialog-title {
        margin-bottom: 14px;
        font-weight: bold;
        font-size: 16px;
    }
</style>

