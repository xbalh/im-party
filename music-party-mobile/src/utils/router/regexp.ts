/** 获取登录页面模块的动态路由的正则 */
export function getLoginModuleRegExp() {
  const modules: EnumType.LoginModuleKey[] = ['forgot', 'verify-email', 'sign-up', 'reset', 'sign-in'];
  return modules.join('|');
}
