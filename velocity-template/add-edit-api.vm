#macro(generatFormItemFunc)
    #foreach($item in $tableList)
        #if($item.componentType=="input")
        <el-col :span="12">
            <el-form-item label="${item.desc}" prop="${item.field}" :rules="formRules.${item.rule}('${item.desc}不能为空')">
                <el-input v-model="addEditForm.${item.field}" class="wi-${item.width}px" placeholder="${item.desc}"/>
            </el-form-item>
        </el-col>
        #elseif($item.componentType=="textarea")
        <el-col :span="12">
            <el-form-item label="${item.desc}" prop="${item.field}" :rules="formRules.${item.rule}('${item.desc}不能为空')">
                <el-input
                    v-model="addEditForm.${item.field}"
                    type="textarea"
                    maxlength="100"
                    show-word-limit="show-word-limit"
                    autosize="autosize"
                    resize="none"
                    clearable="clearable"
                    class="wi-${item.width}px"
                    placeholder="请输入${item.desc}"/>
            </el-form-item>
        </el-col>
        #elseif($item.componentType=="enumSelect")
        <el-col :span="12">
            <el-form-item label="${item.desc}" :rules="formRules.${item.rule}('请选择${item.desc}')">
                <el-select v-model="addEditForm.${item.field}" placeholder="${item.desc}" class="wi-${item.width}px">
                    <el-option v-for="dict in ${item.enumKey}" :key="dict.value" :label="dict.label" :value="dict.value" />
                </el-select>
            </el-form-item>
        </el-col>
        #elseif($item.componentType=="treeSelectApi")
        <el-col :span="12">
            <el-form-item label="${item.desc}" prop="${item.field}" :rules="formRules.${item.rule}('用户昵称不能为空')">
                <el-tree-select
                    v-model="addEditForm.${item.field}"
                    :data="${item.field}Options"
                    :props="{ value:'${item.valueKey}', label: '${item.labelKey}', children: 'children' }"
                    value-key="${item.valueKey}"
                    placeholder="请选择${item.desc}"
                    check-strictly
                />
            </el-form-item>
        </el-col>
        #elseif($item.componentType=="selectRadio")
        <el-col :span="12">
            <el-form-item label="${item.desc}" :rules="formRules.${item.rule}('请选择${item.desc}')">
                <el-radio-group v-model="addEditForm.${item.field}">
                    <el-radio v-for="dict in ${item.enumKey}" :key="dict.value" :label="dict.value">
                        {{ dict.label }}
                    </el-radio>
                </el-radio-group>
            </el-form-item>
        </el-col>
        #elseif($item.componentType=="selectApi")
        <el-form-item label="${item.desc}" :rules="formRules.${item.rule}('请选择${item.desc}')">
            <el-select v-model="addEditForm.${item.field}" placeholder="${item.desc}" class="wi-${item.width}px">
                <el-option
                    v-for="item in ${item.field}Data"
                    :key="item.${item.valueKey}"
                    :label="item.${item.labelKey}"
                    :value="item.${item.valueKey}"/>
            </el-select>
        </el-form-item>
        #elseif($item.componentType=="radio") ##radio
        <el-form-item label="${item.desc}" prop="${item.field}" :rules="formRules.${item.rule}('请选择${item.desc}')">
            <el-radio-group v-model="addEditForm.${item.field}">
                #foreach($enum in $item.optionDataArr)
                    <el-radio :label="${enum.value}">${enum.label}</el-radio>
                #end
            </el-radio-group>
        </el-form-item>
        #elseif($item.componentType=="checkbox") ##checkbox
        <el-form-item label="${item.desc}" prop="${item.field}" ::rules="formRules.${item.rule}('请选择${item.desc}')">
            <el-checkbox-group v-model="addEditForm.${item.field}">
                #foreach($enum in $item.optionDataArr)
                    <el-checkbox label="${enum.value}"/>
                #end
            </el-checkbox-group>
        </el-form-item>
        #elseif($item.componentType=="switch") ##switch
        <el-form-item label="${item.desc}" prop="${item.field}" :rules="formRules.${item.rule}('请选择${item.desc}')">
            <el-switch
                v-model="addEditForm.${item.field}"
                inline-prompt="inline-prompt"
                active-color="#13ce66"
                inactive-color="#ff4949"
                active-text="0"
                inactive-text="1"/>
        </el-form-item>
        #elseif($item.componentType=="daterange")
        <el-form-item  label="${item.desc}" prop="${item.field}" :rules="formRules.${item.rule}('请选择${item.desc}')">
            <el-date-picker
                v-model="addEditForm.${item.field}Arr"
                type="daterange"
                format="YYYY-MM-DD HH:mm:ss"
                value-format="YYYY-MM-DD HH:mm:ss"
                @change="dateRangePacking"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                class="wi-${item.width}px"/>
        </el-form-item>
        #elseif($item.componentType=="date")
        <el-form-item label="${item.desc}" prop="${item.field}" :rules="formRules.${item.rule}('请选择${item.desc}')">
            <el-date-picker v-model="addEditForm.${item.field}" type="date" class="wi-${item.width}px"/>
        </el-form-item>
        #end
    #end
#end
#macro(generalFormKey)
    #foreach($item in $tableList)
        $item.field:"",
        #if($item.componentType=="daterange")
            ${item.field}:[],
        #end
    #end
#end
#macro(generalFormReq)
    #foreach($item in $tableList)
        #if(${item.api}&&($item.componentType=="treeSelectApi"||$item.componentType=="selectApi"))
        let ${item.field}Options = ref([])
        const get${item.fieldFirstWordCase}Data = () => {
            ${item.field}Req().then(({ data }) => {
            ${item.field}Options.value = data
        })
        }
        #end
    #end
#end
#macro(generalMountedReq)
onMounted(() => {
    #foreach($item in $tableList)
        #if(${item.api}&&($item.componentType=="treeSelectApi"||$item.componentType=="selectApi"))
        get${item.fieldFirstWordCase}Data()
        #end
    #end
})
#end
#macro(generalImportReqApiString)
    #set($apiString="add${basicConfig.apiFileNameFirstCase}, get${basicConfig.apiFileNameFirstCase}, update${basicConfig.apiFileNameFirstCase},")
    #foreach($item in $tableList)
        #if(${item.api}&&($item.componentType=="treeSelectApi"||$item.componentType=="selectApi"))
            #set($apiString=$apiString+"${item.field}Req")
        #end
    #end
    $apiString
#end
#macro(generalEnumKey)
    #set($apiString="")
    #foreach($item in $tableList)
        #if($item.componentType.indexOf("enum")!=-1)
            #set($apiString=$apiString+"${item.enumKey},")
        #end
    #end
    $apiString
#end
#macro(generalEnumString)
    #set($apiString="")
    #foreach($item in $tableList)
        #if($item.componentType.indexOf("enum")!=-1)
            #set($apiString=$apiString+"'${item.enumKey}',")
        #end
    #end
    $apiString
#end
