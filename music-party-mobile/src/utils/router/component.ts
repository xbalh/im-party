import type {RouteComponent} from 'vue-router';
import {views} from '@/views';
import {isFunction} from '@/utils';
import {BasicLayout, BlankLayout, AuthLayout, ErrorLayout, TodoLayout, ChatLayout} from '@/layouts';

type Lazy<T> = () => Promise<T>;

interface ModuleComponent {
  default: RouteComponent;
}

type LayoutComponent = Record<EnumType.LayoutComponentName, Lazy<ModuleComponent>>;

/**
 * 获取布局的vue文件(懒加载的方式)
 * @param layoutType - 布局类型
 */
export function getLayoutComponent(layoutType: EnumType.LayoutComponentName) {
  const layoutComponent: LayoutComponent = {
    basic: BasicLayout,
    blank: BlankLayout,
    auth: AuthLayout,
    error: ErrorLayout,
    todo: TodoLayout,
    chat: ChatLayout,
  };
  return layoutComponent[layoutType];
}

/**
 * 获取页面导入的vue文件
 * @param routeKey - 路由key
 */
export function getViewComponent(routeKey: AuthRoute.LastDegreeRouteKey) {
  if (!views[routeKey]) {
    throw new Error(`route “${routeKey}” miss corresponding component file`);
  }
  return setViewComponentName(views[routeKey], routeKey);
}

function setViewComponentName(component: RouteComponent | Lazy<ModuleComponent>, name: string) {
  if (isAsyncComponent(component)) {
    return async () => {
      const result = await component();
      Object.assign(result.default, {name});
      return result;
    };
  }

  Object.assign(component, {name});

  return component;
}

function isAsyncComponent(component: RouteComponent | Lazy<ModuleComponent>): component is Lazy<ModuleComponent> {
  return isFunction(component);
}
