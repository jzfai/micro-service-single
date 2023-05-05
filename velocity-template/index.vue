#parse("index-extract.vm")
<template>
  <div class="p-10px">
    <el-form v-show="showSearch" :model="queryParams" :inline="true" label-width="68px">
      #generalQueryFormItemFunc()
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      #if(${tableConfig.isAdd})
      <el-button type="primary" plain icon="Plus" @click="handleAdd">新增</el-button>
      #end
      #if(${tableConfig.isGlobalEdit})
      <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate">修改</el-button>

      #end
      #if(${tableConfig.isMulDelete})
      <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete">删除</el-button>

      #end
      #if(${tableConfig.isImport})
      <el-button type="info" plain icon="Upload" @click="handleImport">导入</el-button>

      #end
      #if(${tableConfig.isExport})
      <el-button type="warning" plain icon="Download" @click="handleExport">导出</el-button>

      #end
      #if(${tableConfig.showSearch})
      <RightToolBar v-model:showSearch="showSearch" @queryTable="getList"/>
      #end
      #if(${tableConfig.columnSetting})
      <ColumnFilter v-if="${basicConfig.apiFileName}List.length" :is-operation="true" :cols="tableHeadColumns"
                    @colChange="colChange"/>
      #end
    </el-row>
    <el-table ref="refElTable" v-loading="loading" border :data="${basicConfig.apiFileName}List"
              @selection-change="handleSelectionChange">
      #if(${tableConfig.isTableMulChoose})
      <el-table-column type="selection" width="50" align="center"/>
      #end
      <!--column头字段-->
      <template v-for="item in tableHeadColumns">
        <el-table-column
            v-if="!item.isTemplate && item.showColumn"
            :key="item.prop"
            show-overflow-tooltip
            v-bind="item"
            :align="item.align || 'center'"
            :prop="item.prop"
            :label="item.label"
        />
        #generalTableColumnFunc()
      </template>

      <el-table-column label="操作" align="center" width="150" class-name="small-padding fixed-width">
        <template #default="{row}">
          #if(${tableConfig.isEdit})
          <el-tooltip content="修改" placement="top">
            <el-button link type="primary" icon="Edit" size="large" @click="handleUpdate(row)"/>
          </el-tooltip>
          #end
          #if(${tableConfig.isDelete})
          <el-tooltip content="删除" placement="top">
            <el-button link type="primary" icon="Delete" size="large" @click="handleDelete(row)"/>
          </el-tooltip>
          #end
        </template>
      </el-table-column>
    </el-table>
    #if(${tableConfig.isPagination})
    <div class="columnSE">
      <Pagination
          v-show="totalNum > 10"
          v-model:page="queryParams.pageNum"
          v-model:limit="queryParams.pageSize"
          :total="totalNum"
          @pagination="getList"
      />
    </div>
    #end
    #if(${tableConfig.isAdd}||${tableConfig.isEdit})
    <AddEditModal ref="refAddEditModal" @getList="getList"/>
    #end
    #if(${tableConfig.isImport})
    <Import ref="refExport" @getList="getList"/>
    #end
  </div>
</template>
<script setup>
import {listReq} from '@/api/'
import {useDict} from '@/hooks/use-dict'
import {onMounted, reactive, ref} from "vue"
//导入当前页面封装方法
import {currentHook, removeEmptyKey} from './index-hook'
import {resetData} from '@/hooks/use-common'

#if(${tableConfig.columnSetting})

#end

#if(${tableConfig.isAdd} ||${tableConfig.isEdit})

#end
#if(${tableConfig.isImport})

#end
/*查询模块*/
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  #generalQueryFormKey()
})
//备份数据
const bakQueryParams = JSON.stringify(queryParams)
const dateRange = ref([])
//查询
const handleQuery = () => {
  queryParams.pageNum = 1
  getList(queryParams)
}
//重置
const resetQuery = () => {
  resetData(queryParams, bakQueryParams)
  dateRange.value = []
  handleQuery()
}
#if(${tableConfig.isEdit})
const handleUpdate = (row) => {
  const ${basicConfig.apiFileName}Id = row.$
  {
    basicConfig.apiFileName
  }
  Id || ids[0]
  refAddEditModal.value.showModal({${basicConfig.apiFileName}Id})
}
#end
const getList = () => {
  loading.value = true
  if (dateRange.value?.length) {
    queryParams.beginTime = dateRange.value.at(-2)
    queryParams.endTime = dateRange.value.at(-1)
  } else {
    queryParams.beginTime = ""
    queryParams.endTime = ""
  }
  listReq(removeEmptyKey(queryParams)).then(({rows, total}) => {
    loading.value = false
    ${basicConfig.apiFileName}
    List.value = rows
    totalNum.value = total
  })
}
onMounted(() => {
  handleQuery()
})
//字典数据
// eslint-disable-next-line camelcase
const {#generalEnumKey()} = useDict(#generalEnumString())

const {
  refAddEditModal,
  refElTable,
  refExport,
  single,
  multiple,
  ids,
  totalNum,
  loading,
  ${basicConfig.apiFileName}List,
  showSearch,
  tableHeadColumns,
  handleExport,
  handleDelete
} = currentHook(queryParams, getList)
</script>
