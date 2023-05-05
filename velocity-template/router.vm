{
path: '/${basicConfig.routerParentDir}',
component: Layout,
alwaysShow: true,
meta: { title: '${basicConfig.modalName}父目录', icon: 'example' },
children: [
{
path: '${basicConfig.apiFileName}/index',
component: () => import('@/views/${basicConfig.routerParentDir}/${basicConfig.apiFileName}/index.vue'),
name: '${basicConfig.apiFileNameFirstCase}',
meta: { title: '${basicConfig.modalName}', icon: 'Fold' }
}
]
},
