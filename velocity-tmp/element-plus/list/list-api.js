import request from '@/lib/axios'
/**
* ${frontConfig.apiFileName}查询
* @param {*} data
*/
export const tableQuery = (data) => {
        return request('${apiConfig.queryApi}', {
                data:data||{},
                method: '${apiConfig.queryMethod}'
        })
}

/**
*  ${frontConfig.apiFileName}作废
* @param {*} params
*/
export const splitfeeInvalid = id => {
        return request(`/api/v1.0/feePolicy/${id}`, {
        method: '${apiConfig.invalidMethod}'
        })
}


##生成queryItem里的api查询接口
#foreach($item in $queryConfig)
        #if($item.componentType=="selectApi") ##输入框 input
        /**
        *  ${item.desc}接口
        * @param {*} data
        */
        export const ${item.field}Req = (data) => {
        return request('${item.api}', {
        data: data||{},
        method: '${item.method}'
        })
        }
        #end
#end




#foreach( $key in $context.keys )
    $key
#end  