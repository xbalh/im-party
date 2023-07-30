type ServiceEnvType = 'dev' | 'test' | 'prod';

interface ServiceEnvConfig {
  url: string;
  urlPattern: string;
  secondUrl: string;
  secondUrlPattern: '/second-url-pattern';
  wsUrl: string;
  wsUrlPattern: string;
}

interface ImportMetaEnv {
  readonly VITE_BASE_URL: string;
  readonly VITE_APP_NAME: string;
  readonly VITE_APP_TITLE: string;
  readonly VITE_APP_DESC: string;
  readonly VITE_AUTH_ROUTE_MODE: 'static' | 'dynamic';
  readonly VITE_ROUTE_HOME_PATH: AuthRoute.RoutePath;
  readonly VITE_ICON_PREFFIX: string;
  readonly VITE_ICON_LOCAL_PREFFIX: string;
  readonly VITE_SERVICE_ENV?: ServiceEnvType;
  readonly VITE_HTTP_PROXY?: 'Y' | 'N';
  readonly VITE_VISUALIZER?: 'Y' | 'N';
  readonly VITE_COMPRESS?: 'Y' | 'N';
  readonly VITE_COMPRESS_TYPE?: 'gzip' | 'brotliCompress' | 'deflate' | 'deflateRaw';
  readonly VITE_PWA?: 'Y' | 'N';
  readonly VITE_HASH_ROUTE?: 'Y' | 'N';
  readonly VITE_VERCEL?: 'Y' | 'N';
}

interface ImportMeta {
  readonly env: ImportMetaEnv;
}
