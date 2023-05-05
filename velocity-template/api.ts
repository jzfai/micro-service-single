//查询列表页面接口
import request from '@/utils/axios-req'

export const listReq = (query) => {
//查询列表
    return request({
        url: '${apiConfig.queryApi}',
        method: '${apiConfig.queryMethod}',
        params: query
    })
}

#if(${tableConfig.isDelete})
//删除
export const deleteReq = (id) => {
    return request({
        url: `${apiConfig.deleteApi}`,
        method: '${apiConfig.deleteMethod}',
    })
}
#end

#if(${tableConfig.isMulDelete})
//批量删除
export const multiDeleteReq = (id) => {
    return request({
        url: `${apiConfig.multiDeleteApi}`,
        method: '${apiConfig.multiDeleteMethod}',
    })
}
#end

#if(${tableConfig.isImport})
//导入
export const importReq = (updateSupport) => {
    return request({
        url: `${apiConfig.importApi}`,
        method: '${apiConfig.importMethod}',
    })
}
#end

#if(${tableConfig.isExport})
//导出
export const exportReq = (reqConfig) => {
    return request({
        url: `${apiConfig.exportApi}`,
        responseType: 'blob',
        method: '${apiConfig.exportMethod}',
        params: Object.assign(reqConfig, {responseType: 'blob'})
    })
}
#end

//模板下载
export const downloadTemplateReq = () => {
    return request({
        url: `${apiConfig.templateDownApi}`,
        method: '${apiConfig.templateDownMethod}'
    })
}
