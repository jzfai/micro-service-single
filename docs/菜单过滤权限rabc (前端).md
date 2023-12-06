权限过滤文件

use-menu.ts

```typescript
// @ts-ignore
import Layout from '@/layout/index.vue'
import ParentView from '@/components/ParentView/index.vue'
import InnerLink from '@/components/InnerLink/index.vue'
import {isExternal} from "@/hooks/use-layout";
// @ts-ignore
const modules = import.meta.glob('../views/**/**.vue')
/**
 * 根据公司情况处理，生成路由
 * @return
 */
const buildAsyncRouter = (asyncRouterMap) => {
  return asyncRouterMap.map((item) => {
    const route:any={}
    //component
    route.component=getComponent(item)
    route.children=item.children
    if (route.component) {
      // Layout ParentView 组件特殊处理
      if (route.component === 'Layout') {
        route.component = Layout
      } else if (route.component === 'ParentView') {
        route.component = ParentView
      } else if (route.component === 'InnerLink') {
        route.component = InnerLink
      } else {
        route.component = modules[`../views/${route.component}`]
      }
    }
    if (route.children?.length) {
      route.children = buildAsyncRouter(route.children)
    } else {
      delete route['children']
    }

    //name
    if (item.routeName) {
      route.name = item.routeName
    }
    //alwaysShow
    route.alwaysShow = item.alwaysShow === 1
    //hidden
    route.hidden = item.visible === "1"
    //title、icon
    route.meta={title: item.menuName,icon:item.icon}
    //meta
    if (item.metaExtra && JSON.parse(item.metaExtra) !== '{}') {
      route.meta = Object.assign(route.meta,JSON.parse(JSON.parse(item.metaExtra)))
    }
    //path
    route.path = item.path
    return route
  })
}

/**
 * 构造树型结构数据
 * @param {*} data 数据源
 * @param {*} id id字段 默认 'id'
 * @param {*} parentId 父节点字段 默认 'parentId'
 * @param {*} children 孩子节点字段 默认 'children'
 */
export function handleTree(data, id, parentId?, children?) {
  const config = {
    id: id || 'id',
    parentId: parentId || 'parentId',
    childrenList: children || 'children'
  }

  const childrenListMap = {}
  const nodeIds = {}
  const tree:any = []

  for (const d of data) {
    const parentId = d[config.parentId]
    if (childrenListMap[parentId] == null) {
      childrenListMap[parentId] = []
    }
    nodeIds[d[config.id]] = d
    childrenListMap[parentId].push(d)
  }

  for (const d of data) {
    const parentId = d[config.parentId]
    if (nodeIds[parentId] == null) {
      tree.push(d)
    }
  }

  for (const t of tree) {
    adaptToChildrenList(t)
  }

  function adaptToChildrenList(o) {
    if (childrenListMap[o[config.id]] !== null) {
      o[config.childrenList] = childrenListMap[o[config.id]]
    }
    if (o[config.childrenList]) {
      for (const c of o[config.childrenList]) {
        adaptToChildrenList(c)
      }
    }
  }
  return tree
}
/*处理component*/
const getComponent=(menu)=>{
  let component = "Layout"
  if(menu.component&&!isMenuFrame(menu)){
    component = menu.component;
  }else if(!menu.component &&menu.parentId!==0 && isInnerLink(menu)){
    component = "InnerLink";
  }else if(!menu.component&& isParentView(menu)){
    component = "ParentView";
  }
  return component;
}
/**
 * 是否为菜单内部跳转
 *
 * @param menu 菜单信息
 * @return 结果
 */
const isMenuFrame=(menu)=>{
  return menu.parentId===0&& "C"===menu.menuType &&  1===menu.isFrame
}
/**
 * 是否为内链组件
 *
 * @param menu 菜单信息
 * @return 结果
 */
const isInnerLink=(menu)=> {
  return 1===menu.isFrame && isExternal(menu.path);
}
/**
 * 是否为parent_view组件
 *
 * @param menu 菜单信息
 * @return 结果
 */
const isParentView=(menu)=> {
  return menu.parentId!==0 && "M"===menu.menuType;
}
export  const generatorRouter=(data, id, parentId?, children?)=>{
  //将菜单转换为树形结构
  const routerTree = handleTree(data, id, parentId, children);
  //构建路由（此处可以根据公司的选择）  
  return buildAsyncRouter(routerTree)
}
```

如何使用

use-permission.ts

```js
export function filterAsyncRouter(data) {
  const fileAfterRouter = generatorRouter(data, 'menuId');
  fileAfterRouter.forEach((route) => router.addRoute(route))
}
```



请求api

rbac.ts

```typescript
//获取用户信息
export const userInfoReq = () => {
  return new Promise((resolve) => {
    const reqConfig = {
      url: 'rbac/getInfo',
      data:{platformId: settings.platformId},
      method: 'get'
    }
    request(reqConfig).then(({ data }) => {
      resolve(data)
    })
  })
}
//获取路由信息
export const getRouterReq = () => {
  return new Promise((resolve) => {
    const reqConfig = {
      url: 'rbac/getMenu',
      params: { platformId: settings.platformId },
      reqLoading: false,
      method: 'get'
    }
    request(reqConfig).then(({ data }) => {
      resolve(data)
    })
  })
}
//登录
export const loginReq = (subForm) => {
  return request({
    url: 'rbac/login',
    data: subForm,
    method: 'post',
    isNotTipErrorMsg: true
  })
}
// 注册方法
export const register = (data) => {
  return request({
    url: 'rbac/register',
    method: 'post',
    data
  })
}
//获取验证码
export const getCodeImg = () => {
  return request({
    url: 'rbac/getCode',
    params:{width:85,height:50},
    method: 'get'
  })
}
```

