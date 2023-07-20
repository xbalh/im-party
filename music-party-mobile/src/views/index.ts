import type {RouteComponent} from 'vue-router';

export const views: Record<PageRoute.LastDegreeRouteKey,
  RouteComponent | (() => Promise<{ default: RouteComponent }>)> = {
  404: () => import('@/views/_builtin/error/NotFoundPage.vue'),
  403: () => import('@/views/_builtin/error/NotFoundPage.vue'),
  500: () => import('@/views/_builtin/error/UnexpectedPage.vue'),
  login: () => import('@/views/_builtin/auth/index.vue'),
  'not-found': () => import('@/views/_builtin/error/NotFoundPage.vue'),
  "apps_manager-user_edit": () => import('@/views/users/EditUserPage.vue'),
  "apps_board": () => import('@/views/board/pages/BoardPage.vue'),
  "apps_todo_tasks": () => import('@/views/todo/pages/TasksPage.vue'),
  "apps_todo_completed": () => import('@/views/todo/pages/CompletedPage.vue'),
  "apps_todo_label": () => import('@/views/todo/pages/LabelPage.vue'),
  "apps_chat-channel": () => import('@/views/chat/ChatChannel.vue'),
  "pages_error_notfound": () => import('@/views/_builtin/error/NotFoundPage.vue'),
  "pages_error_unexpected": () => import('@/views/_builtin/error/UnexpectedPage.vue'),
};
