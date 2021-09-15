<template>
    <el-dialog
            v-if="dialogVisibleMixin"
            :title="dialogTitleMixin"
            v-model="dialogVisibleMixin"
            width="50vw"
            :close-on-click-modal="false"
            :before-close="beforeCloseModal">
        <el-form
                label-width="150px"
                ref="refForm"
                :inline="false"
                :model="subFormMixin"
                :rules="formRulesMixin"
                class="pr-5">
            <el-form-item
                    label="品牌名称"
                    prop="name"
                    :rules="formRulesMixin.isNotNull"
                    label-position="left">
                <el-input v-model="subFormMixin.name" class="widthPx-150"
                          placeholder="品牌名称"/>
            </el-form-item>
            <el-form-item
                    label="品牌图片地址"
                    prop="image"
                    :rules="formRulesMixin.isNotNull"
                    label-position="left">
                <el-input v-model="subFormMixin.image" class="widthPx-150"
                          placeholder="品牌图片地址"/>
            </el-form-item>
            <el-form-item
                    label="品牌的首字母"
                    prop="letter"
                    :rules="formRulesMixin.isNotNull"
                    label-position="left">
                <el-input v-model="subFormMixin.letter" class="widthPx-150"
                          placeholder="品牌的首字母"/>
            </el-form-item>
            <el-form-item
                    label="排序"
                    prop="seq"
                    :rules="formRulesMixin.isNotNull"
                    label-position="left">
                <el-input v-model="subFormMixin.seq" class="widthPx-150"
                          placeholder="排序"/>
            </el-form-item>
        </el-form>
        <template #footer>
      <span class="dialog-footer">
        <el-button @click="beforeCloseDetailModal">取 消</el-button>
        <el-button type="primary" @click="confirmBtnClick">确 定</el-button>
      </span>
        </template>
    </el-dialog>
</template>

<script setup>
    import {onMounted, getCurrentInstance, watch, ref, toRefs, reactive, computed} from 'vue'

    const refForm = ref(null)
    let {proxy} = getCurrentInstance()
    /*新增*/
    let subFormMixin = reactive({
        id:
            '',
        name:
            '',
        image:
            '',
        letter:
            '',
        seq:
            '',
    })
    let searchFormMixin = reactive({
        id:
            '',
        name:
            '',
        image:
            '',
        letter:
            '',
        seq:
            '',
    })
    let brandListData = ref([])
    const resetData = () => {
        proxy.subFormMixin = {
            id:
                '',
            name:
                '',
            image:
                '',
            letter:
                '',
            seq:
                '',
        }
        proxy.dialogVisibleMixin = false
        proxy.isDialogEditMixin = false
    }
    let confirmBtnClick = () => {
        refForm.value.validate((valid) => {
            if (valid) {
                if (subFormMixin.id) {
                    updateReq()
                } else {
                    insertReq()
                }
            } else {
                return false
            }
        })
    }
    const insertReq = () => {
        const data = JSON.parse(JSON.stringify(proxy.subFormMixin))
        delete data.id
        proxy.$axiosReq({
            url: '/ty-user/brand/insert',
            data: data,
            method: 'post',
            isParams: true,
            bfLoading: true
        }).then((res) => {
            proxy.elMessageMixin('保存成功')
            resetData()
            proxy.$emit('selectPageReq')
            proxy.dialogVisibleMixin = false
        })
    }
    /*修改*/
    const reshowData = (row) => {
        Object.keys(row).forEach((fItem) => {
            Object.keys(subFormMixin).forEach((sItem) => {
                if (fItem === sItem) {
                    subFormMixin[sItem] = row[sItem]
                }
            })
        })
    }
    let updateReq = () => {
        return proxy.$axiosReq({
            url: '/ty-user/brand/updateById',
            data: subFormMixin,
            isParams: true,
            method: 'update',
            bfLoading: true
        }).then(() => {
            proxy.elMessageMixin('更新成功')
            resetData()
            proxy.$emit('selectPageReq')
            proxy.dialogVisibleMixin = false
        })
    }
    let getDetailByIdReq = (id) => {
        return proxy.$axiosReq({
            url: '/ty-user/brand',
            id: id,
        })
    }
    let dialogConfirmBtn = () => {
        let validFuc = (valid) => {
            if (valid) {
                if (proxy.isDialogEditMixin) {
                    //编辑
                    updateReq()
                } else {
                    insertReq()
                }
            } else {
                return false
            }
        }
    }
    /*上传文件*/
    const tableImportReq = () => {
        const formData = new FormData()
        formData.append('file', proxy.$refs.refSettingFile.files[0])
        proxy.$axiosReq({
            url: '/ty-upload/uploadFile',
            data: formData,
            method: 'post',
            bfLoading: true,
            isUploadFile: true
        }).then((resData) => {
            let {ori, shot, name} = resData.data
            proxy.$refs.refSettingFile.value = ''
        })
            .catch((err) => {
                proxy.$refs.refSettingFile.value = ''
            })
    }
    /*关闭弹框*/
    let beforeCloseDetailModal = () => {
        resetData()
        proxy.dialogTitleMixin = '添加【brand】'
        proxy.dialogVisibleMixin = false
    }
    let showModal = (isEdit, row) => {
        if (isEdit) {
            getDetailByIdReq(row.id).then((resData) => {
                proxy.dialogTitleMixin = `编辑【${row.name}】`
                proxy.dialogVisibleMixin = true
                reshowData(resData.data)
            })
        } else {
            proxy.dialogVisibleMixin = true
        }
    }
    /*弹框关闭*/
    let beforeCloseModal = () => {
        resetData()
    }
    //导出给refs使用
    defineExpose({
        getDetailByIdReq,
        showModal
    })
    //导出属性到页面中使用
    // let {levelList} = toRefs(state);
</script>

<style scoped lang="scss"></style>

