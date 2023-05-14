const BasicLayout = () => import('./DefaultLayout.vue');
const BlankLayout = () => import('./BlankLayout/index.vue');
const AuthLayout = () => import('./AuthLayout.vue');
const ErrorLayout = () => import('./ErrorLayout.vue');
const TodoLayout= () => import('../views/todo/TodoLayout.vue');
const ChatLayout= () => import('../views/chart/ChatPage.vue');

export {BasicLayout, BlankLayout, AuthLayout, ErrorLayout, TodoLayout , ChatLayout};
