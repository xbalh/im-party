/** 请求服务的环境配置 */
type ServiceEnv = Record<ServiceEnvType, ServiceEnvConfig>;

/** 不同请求服务的环境配置 */
const serviceEnv: ServiceEnv = {
  dev: {
    url: 'http://124.221.34.38:8081',
    urlPattern: '/api',
    secondUrl: 'http://124.221.34.38:8081',
    secondUrlPattern: '/second-url-pattern',
    wsUrl: 'ws://124.221.34.38:8081',
    wsUrlPattern: ''
  },
  test: {
    url: 'http://localhost:8080',
    urlPattern: '/api',
    secondUrl: 'http://124.221.34.38:8081',
    secondUrlPattern: '/second-url-pattern',
    wsUrl: 'ws://localhost:8080',
    wsUrlPattern: '/musicParty'
  },
  prod: {
    url: 'http://124.221.34.38:8082',
    urlPattern: '/api',
    secondUrl: 'http://124.221.34.38:8082',
    secondUrlPattern: '/second-url-pattern',
    wsUrl: 'ws://124.221.34.38:8081',
    wsUrlPattern: ''
  }
};

/**
 * 获取当前环境模式下的请求服务的配置
 * @param env 环境
 */
export function getServiceEnvConfig(env: ImportMetaEnv) {
  const { VITE_SERVICE_ENV = 'dev' } = env;

  const config = serviceEnv[VITE_SERVICE_ENV];

  return config;
}
