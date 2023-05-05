#parse("add-edit-extract.vm")
<template>
    <!-- 添加或修改${basicConfig.modalName}配置对话框 -->
    <el-dialog :close-on-press-escape="false" :close-on-click-modal="false"
               :destroy-on-close="true" v-model="open" :title="title" width="600px" append-to-body @close="cancel">
        <el-form ref="${basicConfig.apiFileName}Ref" :model="addEditForm" label-width="80px">
            #generatFormItemFunc()
        </el-form>
        <template #footer>
            <div class="dialog-footer">
                <el-button type="primary" @click="submitForm">确 定</el-button>
                <el-button @click="cancel">取 消</el-button>
            </div>
        </template>
    </el-dialog>
</template>

<script setup>
    import { ElMessage } from 'element-plus'
    import {#generalImportReqApiString()} from '@/api/${basicConfig.apiFileName}'
    import { useDict } from '@/hooks/use-dict'
    import { resetData } from '@/hooks/use-common'

    //element valid
    const formRules = useElement().formRules
    /** 提交按钮 */
    const ${basicConfig.apiFileName}Ref = ref('')
    let open = ref(false)
    let title = ref('新增${basicConfig.modalName}')
    const emits = defineEmits([])
    // eslint-disable-next-line camelcase
    const { #generalEnumKey()} = useDict(#generalEnumString())
    let addEditForm = reactive({
        #generalFormKey()
    })
    const formString = JSON.stringify(addEditForm)
    const submitForm = () => {
        ${basicConfig.apiFileName}Ref.value.validate((valid) => {
            if (valid) {
                if (addEditForm.${basicConfig.apiFileName}Id !== '') {
                    update${basicConfig.apiFileNameFirstCase}(addEditForm).then(() => {
                        ElMessage({message: '修改成功', type: 'success'})
                        open.value = false
                        emits('getList')
                    })
                } else {
                    add${basicConfig.apiFileNameFirstCase}(addEditForm).then(() => {
                        ElMessage({ message: '新增成功', type: 'success' })
                        open.value = false
                        emits('getList')
                    })
                }
            }
        })
    }
    /** 取消按钮 */
    const cancel = () => {
        open.value = false
        resetData(addEditForm, formString)
    }
    const showModal = (row) => {
        if (row.${basicConfig.apiFileName}Id) {
            get${basicConfig.apiFileNameFirstCase}(row.${basicConfig.apiFileName}Id).then(({ data }) => {
                reshowData(addEditForm, data.${basicConfig.apiFileName})
                //edit modal
                title.value = '编辑${basicConfig.modalName}'
            })
        }
        title.value = '新增${basicConfig.modalName}'
        open.value = true
    }
    const reshowData = (addEditForm, detailData) => {
        Object.keys(addEditForm).forEach((fItem) => {
            if (detailData[fItem]) {
                addEditForm[fItem] = detailData[fItem]
            }
        })
    }
        #generalFormReq()
        #generalMountedReq()
    //导出给refs使用
    defineExpose({ cancel, showModal })
</script>

<style scoped lang="scss"></style>
