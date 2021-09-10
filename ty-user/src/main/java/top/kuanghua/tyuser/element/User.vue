<template>
    <div id="iDuser">
        <!--操作和搜索-->
        <div class="rowBC">
            <!--操作-->
            <div style="margin-bottom: 18px">
                <el-button type="primary" icon="el-icon-plus" @click.stop.prevent="addBtnClick">新增</el-button>
                <el-button type="primary" icon="el-icon-delete" @click.stop.prevent="deleteBatchIdsReq()">删除</el-button>
                <el-button type="primary" @click="goUploadFileMixin">
                    <i class="el-icon-upload2"/>上传
                    <input type="file" style="display: none;" ref="refSettingFile" id="file" @change="tableImportReq"/>
                </el-button>
            </div>
            <!--条件搜索-->
            <el-form ref="refsearchFormMixin" :inline="true" class="demo-searchFormMixin">
                <el-form-item
                        label-width="0px"
                        label=""
                        prop="username"
                        label-position="left">
                    <el-input v-model="searchFormMixin.username" class="widthPx-150" placeholder="用户名"/>
                </el-form-item>
                <el-form-item
                        label-width="0px"
                        label=""
                        prop="password"
                        label-position="left">
                    <el-input v-model="searchFormMixin.password" class="widthPx-150" placeholder="密码"/>
                </el-form-item>
                <el-form-item
                        label-width="0px"
                        label=""
                        prop="phone"
                        label-position="left">
                    <el-input v-model="searchFormMixin.phone" class="widthPx-150" placeholder="电话"/>
                </el-form-item>
                <el-form-item
                        label-width="0px"
                        label=""
                        prop="createTime"
                        label-position="left">
                    <el-date-picker
                            v-model="startEndArrMixin"
                            type="datetimerange"
                            format="yyyy-MM-dd HH:mm:ss"
                            @change="searchFormMixinTimeRangePacking"
                            value-format="yyyy-MM-dd HH:mm:ss"
                            class="widthPx-335"
                            range-separator="-"
                            start-placeholder="开始日期"
                            end-placeholder="结束日期"/>
                </el-form-item>
                <el-form-item
                        label-width="0px"
                        label=""
                        prop="updateTime"
                        label-position="left">
                    <el-date-picker
                            v-model="startEndArrMixin"
                            type="datetimerange"
                            format="yyyy-MM-dd HH:mm:ss"
                            @change="searchFormMixinTimeRangePacking"
                            value-format="yyyy-MM-dd HH:mm:ss"
                            class="widthPx-335"
                            range-separator="-"
                            start-placeholder="开始日期"
                            end-placeholder="结束日期"/>
                </el-form-item>
                <el-form-item
                        label-width="0px"
                        label=""
                        prop="isDelete"
                        label-position="left">
                    <el-select
                            v-model="searchFormMixin.isDelete"
                            clearable
                            default-first-option
                            class="widthPx-100"
                            placeholder="是否删除">
                        <!-- <el-option v-for="(item,index) in timeOption" :key="index" :value="item.value" :label="item.label"/>-->
                        <el-option :key="1" :value="1" label="已删除"/>
                        <el-option :key="2" :value="2" label="未删除"/>
                    </el-select>
                </el-form-item>
                <!--查询按钮-->
                <el-button type="primary" @click.stop="searchBtnClick">查询</el-button>
            </el-form>
        </div>
        <!--表格和分页-->
        <el-table
                id="resetElementDialog"
                ref="refuserTable"
                v-loading="tableLoadingMixin"
                size="mini"
                border
                :data="userListData"
                style="width: 100%"
                element-loading-background="rgba(3, 3, 3, 0.1)"
                element-loading-text="加载中"
                element-loading-spinner="el-icon-loading">
            <!--多选框-->
            <el-table-column
                    type="selection"
                    align="center"
                    width="50"/>
            <el-table-column
                    align="center"
                    prop="username"
                    label="用户名"
                    min-width="100"/>
            <el-table-column
                    align="center"
                    prop="password"
                    label="密码"
                    min-width="100"/>
            <el-table-column
                    align="center"
                    prop="phone"
                    label="电话"
                    min-width="100"/>
            <el-table-column
                    align="center"
                    prop="createTime"
                    label="创建时间"
                    min-width="100"/>
            <el-table-column
                    align="center"
                    prop="updateTime"
                    label="更新时间"
                    min-width="100"/>
            <el-table-column
                    align="center"
                    prop="isDelete"
                    label="是否删除"
                    width="100">
                <template slot-scope="{row,$index}">
                    <span v-if="row.isDelete===1">已删除</span>
                    <span v-if="row.isDelete===2">未删除</span>
                </template>
            </el-table-column>
            <!--点击操作-->
            <el-table-column
                    fixed="right"
                    align="center"
                    label="操作"
                    width="120">
                <template slot-scope="{row,$index}">
                    <el-button type="text" size="small" @click="tableHandleEditClick(row)">编辑</el-button>
                    <el-button type="text" size="small" @click="tableDetailClick(row)">详情</el-button>
                    <el-button type="text" size="small" @click="deleteByIdReq(row)">删除</el-button>
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
                    @current-change="handleCurrentChangeMixin"/>
        </div>
        <!--编辑和删除-->
        <el-dialog
                :title="dialogTitleMixin"
                :visible.sync="dialogVisibleMixin"
                width="35%"
                :close-on-click-modal="false"
                class="elODialogModalBodyH60vh elODialogModal"
                :before-close="beforeCloseModal"
        >
            <el-form
                    ref="refForm"
                    :inline="true"
                    :model="subFormMixin"
                    :rules="subFormRulesMixin"
                    label-width="140px"
                    class="pr-5">
                <el-form-item
                        class="d-block"
                        label-width="100px"
                        label="用户名"
                        prop="username"
                        :rules="subFormRulesMixin.isNotNull"
                        label-position="left">
                    <el-input v-model="subFormMixin.username" class="widthPx-150" placeholder="用户名"/>
                </el-form-item>
                <el-form-item
                        class="d-block"
                        label-width="100px"
                        label="密码"
                        prop="password"
                        :rules="subFormRulesMixin.isNotNull"
                        label-position="left">
                    <el-input v-model="subFormMixin.password" class="widthPx-150" placeholder="密码"/>
                </el-form-item>
                <el-form-item
                        class="d-block"
                        label-width="100px"
                        label="电话"
                        prop="phone"
                        :rules="subFormRulesMixin.isNotNull"
                        label-position="left">
                    <el-input v-model="subFormMixin.phone" class="widthPx-150" placeholder="电话"/>
                </el-form-item>
                <el-form-item
                        class="d-block"
                        label-width="100px"
                        label="创建时间"
                        prop="createTime"
                        :rules="subFormRulesMixin.isNotNull"
                        label-position="left">
                    <el-date-picker
                            v-model="subFormMixin.timeArrMixin"
                            type="datetimerange"
                            format="yyyy-MM-dd HH:mm:ss"
                            @change="searchFormMixinTimeRangePackingSub"
                            value-format="yyyy-MM-dd HH:mm:ss"
                            class="widthPx-330"
                            range-separator="-"
                            start-placeholder="开始日期"
                            end-placeholder="结束日期"/>
                </el-form-item>
                <el-form-item
                        class="d-block"
                        label-width="100px"
                        label="更新时间"
                        prop="updateTime"
                        :rules="subFormRulesMixin.isNotNull"
                        label-position="left">
                    <el-date-picker
                            v-model="subFormMixin.timeArrMixin"
                            type="datetimerange"
                            format="yyyy-MM-dd HH:mm:ss"
                            @change="searchFormMixinTimeRangePackingSub"
                            value-format="yyyy-MM-dd HH:mm:ss"
                            class="widthPx-330"
                            range-separator="-"
                            start-placeholder="开始日期"
                            end-placeholder="结束日期"/>
                </el-form-item>
                <el-form-item
                        class="d-block"
                        label-width="100px"
                        label="是否删除"
                        prop="isDelete"
                        :rules="subFormRulesMixin.isNotNull"
                        label-position="left">
                    <el-select
                            v-model="subFormMixin.isDelete"
                            clearable
                            default-first-option
                            class="widthPx-120"
                            placeholder="状态">
                        <!-- <el-option v-for="(item,index) in timeOption" :key="index" :value="item.value" :label="item.label"/>-->
                        <el-option :key="1" :value="1" label="已删除"/>
                        <el-option :key="2" :value="2" label="未删除"/>
                    </el-select>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer" style="text-align: right">
        <el-button type="primary" @click="beforeCloseModal">取 消</el-button>
        <el-button type="primary" @click="dialogConfirmBtn">确 定</el-button>
      </span>
        </el-dialog>
        <!--详情-->
        <el-dialog
                v-if="detailDialogMixin"
                :title="detailDialogTitleMixin"
                :visible.sync="detailDialogMixin"
                width="35%"
                class="elODialogModalBodyH60vh elODialogModal"
                :close-on-click-modal="false"
                :before-close="beforeCloseDetailModal">
            <div class="detailDialog-title">这里是标题</div>
            <div class="detail-container rowBC">
                <div class="detail-container-item">用户名：{{detailData.username}}</div>
                <div class="detail-container-item">密码：{{detailData.password}}</div>
                <div class="detail-container-item">电话：{{detailData.phone}}</div>
                <div class="detail-container-item">创建时间：{{detailData.createTime}}</div>
                <div class="detail-container-item">更新时间：{{detailData.updateTime}}</div>
                <div class="detail-container-item">是否删除：{{detailData.isDelete}}</div>
            </div>
            <span slot="footer" class="dialog-footer" style="text-align: right">
        <el-button type="primary" @click="beforeCloseDetailModal">确 定</el-button>
      </span>
        </el-dialog>
    </div>
</template>

<script>
    // import { mapState, mapMutations, mapActions } from 'vuex'
    export default {
        name: 'Cuser',
        components: {
            // TablesGZCJ,
        },
        // mixins: [elementUiCDataOrMethod],
        props: {
            // treeData: Array,
        },
        // mixins: [validMixins],
        data() {
            return {
                // form表单和rule验证
                subFormMixin: {
                    id: '',
                    username: '',
                    password: '',
                    phone: '',
                    createTime: '',
                    updateTime: '',
                    isDelete: '',
                },
                searchFormMixin: {
                    id: '',
                    username: '',
                    password: '',
                    phone: '',
                    createTime: '',
                    updateTime: '',
                    isDelete: '',
                },
                /*modal*/
                userListData: [],
            }
        },
        computed: {
            // ...mapState('user', ['token', 'userInfo'])
        },
        watch: {
            // 'cmAddForm.enginePlatform': function(val) {
            //   console.log('enginePlatformval', val)
            //   if (val) {
            //     this.emissionOptionReq()
            //   }
            // }
        },
        created() {

        },
        mounted() {
            this.selectPageReq()
        },
        methods: {
            // ...mapActions('user', ['closeSideBar']),
            // ...mapMutations('user', ['CHAGE_TOKEN']),
            /*operate和searchForm*/
            addBtnClick() {
                this.dialogVisibleMixin = true
            },

            searchBtnClick() {
                this.pageNumMixin = 1;
                this.selectPageReq()
            },
            searchFormMixinTimeRangePacking(timeArr) {
                if (timeArr && timeArr.length === 2) {
                    this.subFormMixin.startTime = timeArr[0]
                    this.subFormMixin.endTime = timeArr[1]
                } else {
                    this.subFormMixin.startTime = ""
                    this.subFormMixin.endTime = ""
                }
            },
            searchFormCasPacking(dataArr) {
                if (dataArr && dataArr.length) {
                    this.searchFormMixin.modelId = dataArr[dataArr.length - 1]
                } else {
                    this.searchFormMixin.modelId = "";
                }
            },
            /*table和分页*/
            async tableHandleEditClick(row) {
                this.dialogTitleMixin = `编辑【${row.name}】`
                //获取详情数据
                let detailData = await this.selectByIdReq(row.id)
                this.reshowData(detailData)
                //显示弹框
                this.isDialogEditMixin = true
                this.dialogVisibleMixin = true
            },
            async tableDetailClick(row) {
                this.detailDialogTitleMixin = `用户详情【${row.fullName}】`
                this.detailData = await this.selectByIdReq(row.id)
                //显示弹框
                this.detailDialogMixin = true
            },
            /*dialog*/
            searchFormMixinTimeRangePackingSub(timeArr) {
                if (timeArr && timeArr.length === 2) {
                    this.subFormMixin.startTime = timeArr[0]
                    this.subFormMixin.endTime = timeArr[1]
                } else {
                    this.subFormMixin.startTime = ""
                    this.subFormMixin.endTime = ""
                }
            },
            casHandleVehicleChangeSub(dataArr) {
                if (dataArr && dataArr.length) {
                    this.subFormMixin.modelId = dataArr[dataArr.length - 1]
                } else {
                    this.subFormMixin.modelId = "";
                }
            },
            beforeCloseModal() {
                this.resetData()
            },
            dialogConfirmBtn() {
                let validFuc = (valid) => {
                    if (valid) {
                        if (this.isDialogEditMixin) {
                            //编辑
                            this.updateByIdReq()
                        } else {
                            this.insertReq()
                        }
                    }
                }
                this.$refs.refForm.validate(validFuc)
            },
            beforeCloseDetailModal() {
                this.detailDialogMixin = false;
            },
            resetData() {
                // 重置数据
                this.subFormMixin = {
                    id: '',
                    username: '',
                    password: '',
                    phone: '',
                    createTime: '',
                    updateTime: '',
                    isDelete: '',
                }
                this.dialogVisibleMixin = false
                this.isDialogEditMixin = false
            },
            reshowData(row) {
                Object.keys(row).forEach(fItem => {
                    Object.keys(this.subFormMixin).forEach(sItem => {
                        if (fItem === sItem) {
                            // console.log('复制了', sItem)
                            this.subFormMixin[sItem] = row[sItem]
                        }
                    })
                })
                if (this.subFormMixin.startTime && this.subFormMixin.endTime) {
                    this.subFormMixin.timeArrMixin = [row.startTime, row.endTime]
                }
            },
            /* 请求*/
            selectPageReq() {
                const data = Object.assign(this.searchFormMixin, {
                    pageNum: this.pageNumMixin,
                    pageSize: this.pageSizeMixin
                })
                Object.keys(data).forEach(fItem => {
                    if (data[fItem] === '' || data[fItem] === null || data[fItem] === undefined) delete data[fItem]
                })
                this.$axiosReq({
                    url: '/good-service/user/selectPage', data,
                    method: 'get', bfLoading: true, isParams: true,
                }).then(res => {
                    this.userListData = res.data.records
                    this.pageTotalMixin = res.data.total
                })
            },
            insertReq() {
                // 删除formModal id
                const data = JSON.parse(JSON.stringify(this.subFormMixin))
                delete data.id
                this.$axiosReq({
                    url: '/good-service/user', data,
                    method: 'post', bfLoading: true
                }).then(res => {
                    this.elMessage('添加成功')
                    this.dialogVisibleMixin = false
                    this.resetData()
                    this.selectPageReq()
                })
            },
            updateByIdReq() {
                const data = JSON.parse(JSON.stringify(this.subFormMixin))
                this.$axiosReq({
                    url: '/good-service/user/updateById', data,
                    method: 'PUT', bfLoading: true
                }).then(res => {
                    this.elMessage('更新成功')
                    this.dialogVisibleMixin = false
                    this.resetData()
                    this.selectPageReq()
                })
            },
            //根据id查看详情
            selectByIdReq(id) {
                return new Promise((resolve) => {
                    const data = {id: id}
                    this.$axiosReq({
                        url: '/good-service/user/selectById', data,
                        method: 'get', bfLoading: true, isParams: true
                    }).then(res => {
                        resolve(res.data)
                    })
                })
            },
            //删除单个
            async deleteByIdReq(row) {
                if (!(row && row.id)) {
                    this.elMessage('删除单个的id不能为空', 'warning')
                    return
                }
                let deleteNameTitle = row.name
                await this.elConfirm('删除', `您确定要删除${deleteNameTitle}吗`)
                let data = {id: row.id}
                console.log('data', data)
                this.$axiosReq({
                    url: `/good-service/user/deleteById`, data, isParams: true,
                    method: 'DELETE', bfLoading: true
                }).then(res => {
                    this.elMessage('删除成功')
                    this.selectPageReq()
                })
            },
            //批量删除和单个删除
            async deleteBatchIdsReq() {
                let rowDeleteIdArrMixin = []
                let selectionArr = this.$refs.refuserTable
                let deleteNameTitle = ""
                rowDeleteIdArrMixin = selectionArr.selection.map(mItem => {
                    deleteNameTitle = deleteNameTitle + mItem.name + ',';
                    return mItem.id
                })
                if (rowDeleteIdArrMixin.length === 0) {
                    this.elMessage('表格选项不能为空', 'warning')
                    return
                }
                let stringLength = deleteNameTitle.length - 1
                await this.elConfirm('删除', `您确定要删除${deleteNameTitle.slice(0, stringLength)}吗`)
                const data = rowDeleteIdArrMixin
                console.log('data', data)
                this.$axiosReq({
                    url: `/good-service/user/deleteBatchIds`, data,
                    method: 'DELETE', bfLoading: true
                }).then(res => {
                    this.elMessage('删除成功')
                    this.selectPageReq()
                })
            },

            tableImportReq() {
                let formData = new FormData();
                formData.append('excelFile', this.$refs.refSettingFile.files[0])
                this.$refs.refSettingFile.value = ""
                this.$axiosReq({
                    url: 'rxdrepairsaas-usercenter/uploadExcel',
                    data: formData, method: 'post', bfLoading: true, isUploadFile: true
                }).then(res => {
                    this.$refs.refSettingFile.value = ""
                    this.elMessage("文件导入成功")
                }).catch(err => {
                    this.$refs.refSettingFile.value = ""
                })
            },
            //文件上传
            fileUploadSave() {
                let formData = new FormData();
                formData.append('file', this.$refs.refSettingFile.files[0])
                this.$axiosReq({
                    url: '/ty-upload/uploadGetMetaData',
                    data: formData, method: 'POST', bfLoading: true, isUploadFile: true
                }).then(res => {
                    //存储文件名称
                    let filename = this.$refs.refSettingFile.value
                    this.chooseFileNameMixin = filename.slice(filename.lastIndexOf('\\') + 1)
                    this.$refs.refSettingFile.value = ""
                    //获取图片的地址
                    this.subFormMixin.url = res.data.fullPath;
                })
            },
        }
    }
</script>

<style lang="scss" scoped>
    #iDuser {
        padding: 10px 10px;
    }

    .main_content {
        padding: 10px !important;
        background-color: #fff !important;
    }

    /*详情*/
    .detail-container {
        flex-wrap: wrap;
        .detail-container-item {
            width: 40%;
            margin-bottom: 20px;
        }
    }

    .detailDialog-title {
        margin-bottom: 14px;
        font-weight: bold;
        font-size: 16px;
    }
</style>


<style lang="scss">


</style>