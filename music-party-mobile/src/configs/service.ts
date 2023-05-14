export const REQUEST_TIMEOUT = 60 * 1000;

export const ERROR_MSG_DURATION = 3 * 1000;

export const DEFAULT_REQUEST_ERROR_CODE = 'DEFAULT';

export const DEFAULT_REQUEST_ERROR_MSG = '请求错误~';

export const REQUEST_TIMEOUT_CODE = 'ECONNABORTED';

export const REQUEST_TIMEOUT_MSG = '请求超时~';

export const NETWORK_ERROR_CODE = 'NETWORK_ERROR';

export const NETWORK_ERROR_MSG = '网络不可用~';

export const ERROR_STATUS = {
  400: '400: 请求出现语法错误~',
  401: '401: 用户未授权~',
  403: '403: 服务器拒绝访问~',
  404: '404: 请求的资源不存在~',
  405: '405: 请求方法未允许~',
  408: '408: 网络请求超时~',
  500: '500: 服务器内部错误~',
  501: '501: 服务器未实现请求功能~',
  502: '502: 错误网关~',
  503: '503: 服务不可用~',
  504: '504: 网关超时~',
  505: '505: http版本不支持该请求~',
  [DEFAULT_REQUEST_ERROR_CODE]: DEFAULT_REQUEST_ERROR_MSG
};

export const NO_ERROR_MSG_CODE: (string | number)[] = [];

export const REFRESH_TOKEN_CODE: (string | number)[] = [66666];
