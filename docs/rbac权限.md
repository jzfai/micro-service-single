查询该用户下的角色权限

```sql
select sr.*
from sys_role sr
left join sys_user_role sur on sur.role_id = sr.role_id
```



查询按钮权限

```sql
select  distinct m.perms  from  sys_menu  m 
left join sys_role_menu rm on m.menu_id = rm.menu_id
left join sys_user_role sur on rm.role_id = sur.role_id
left join sys_role r on r.role_id = sur.role_id
where sur.user_id = 1 and m.menu_type="C"
```

>distinct去重



根据用户平台查询菜单

```sql
select distinct m.*  from  sys_menu  m 
left join sys_role_menu rm on m.menu_id = rm.menu_id
left join sys_user_role sur on rm.role_id = sur.role_id
left join sys_role r on r.role_id = sur.role_id
where sur.user_id = 1 and m.menu_type!="F" and m.platform_id=2
```



前端菜单筛选

```js
// @ts-ignore
import Layout from '@/layout/index.vue'
import ParentView from '@/components/ParentView/index.vue'
import InnerLink from '@/components/InnerLink/index.vue'
import {isExternal} from "@/hooks/use-layout";
// @ts-ignore
const modules = import.meta.glob('../views/**/**.vue')

/**
 * 根据公司情况处理路由
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
      route.meta = JSON.parse(JSON.parse(item.metaExtra))
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
  const handleTree1 = handleTree(data, id, parentId, children);
  return buildAsyncRouter(handleTree1)
}
```

