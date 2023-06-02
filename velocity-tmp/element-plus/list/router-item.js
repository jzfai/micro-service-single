{
path: '/${basicConfig.routerParentDir}',
component: Layout,
meta: { title: '${basicConfig.routerParentDir}', elSvgIcon: 'Fold' },
alwaysShow: true,
children: [
{
path: '${basicConfig.apiFileName}-list',
component: () => import('@/views/${basicConfig.routerParentDir}/${basicConfig.apiFileName}/${basicConfig.apiFileName}-list.vue'),
name: '${basicConfig.apiFileNameFirstCase}List',
meta: { title: '${basicConfig.apiFileName}列表' }
},
#if($tableConfig.isAdd||$tableConfig.isEdit)
{
path: '${basicConfig.apiFileName}-add-edit',
component: () => import('@/views/${basicConfig.routerParentDir}/${basicConfig.apiFileName}/${basicConfig.apiFileName}-add-edit.vue'),
name: '${basicConfig.apiFileNameFirstCase}AddEdit',
hidden: true,
meta: { title: '新增编辑', activeMenu: '/${basicConfig.routerParentDir}/${basicConfig.apiFileName}-list' }
},
#end
#if($tableConfig.isDetail)
{
path: '${basicConfig.apiFileName}-detail',
component: () => import('@/views/${basicConfig.routerParentDir}/${basicConfig.apiFileName}/${basicConfig.apiFileName}-detail.vue'),
name: '${basicConfig.apiFileNameFirstCase}Detail',
hidden: true,
meta: { title: '详情', activeMenu: '/${basicConfig.routerParentDir}/${basicConfig.apiFileName}-list' }
}
#end
]
}