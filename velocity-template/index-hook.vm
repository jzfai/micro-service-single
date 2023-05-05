#parse("index-extract.vm")
import { deleteReq, exportReq } from '@/api/${basicConfig.apiFileName}'
import { downLoadTemp } from '@/hooks/use-common'
import { ElMessage } from 'element-plus'
const single = ref(true)
const multiple = ref(true)
/*table 列表*/
const ids = ref([])
const totalNum = ref(0)
const loading = ref(false)
const ${basicConfig.apiFileName}List = ref([])
const showSearch = ref(true)
const refAddEditModal = ref('')
const refElTable = ref(null)
const refExport = ref(null)
export const handleImport = () => {
refExport.value.showModal()
}

let tableHeadColumns = ref([
#generalColumnFunc()
])
export const handleSelectionChange = (selection) => {
ids.value = selection.map((item) => item.${basicConfig.apiFileName}Id)
single.value = selection.length !== 1
multiple.value = !selection.length
}
export const colChange = (heardColsArr) => {
tableHeadColumns.value = heardColsArr
}

export const handleAdd = () => {
refAddEditModal.value.showModal({})
}

export const removeEmptyKey = (data) => {
return Object.keys(data)
.filter((key) => data[key] !== null && data[key] !== undefined && data[key] !== '')
.reduce((acc, key) => ({ ...acc, [key]: data[key] }), {})
}

watch(
() => tableHeadColumns,
() => {
if (refElTable) {
nextTick(refElTable.value.doLayout)
}
},
{ deep: true }
)

export const currentHook = (queryParams, getList) => {
const handleExport = () => {
exportReq(queryParams).then((res) => {
downLoadTemp(res)
})
}
const handleDelete = (row) => {
const ${basicConfig.apiFileName}Ids = row.${basicConfig.apiFileName}Id || ids.value
elConfirm(`是否确认删除用户编号为"${${basicConfig.apiFileName}Ids}"的数据项`)
.then(() => {
return deleteReq(${basicConfig.apiFileName}Ids)
})
.then(() => {
ElMessage({ message: '删除成功', type: 'success' })
getList()
})
}
return {
refAddEditModal,
handleDelete,
refElTable,
refExport,
multiple,
ids,
single,
totalNum,
loading,
${basicConfig.apiFileName}List,
showSearch,
tableHeadColumns,
handleExport
}
}
