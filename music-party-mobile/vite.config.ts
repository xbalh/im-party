import { setupVitePlugins, getSrcPath, getRootPath } from './build';

// Utilities
import { defineConfig, loadEnv } from 'vite'

// https://vitejs.dev/config/
export default defineConfig(configEnv => {
  const viteEnv = loadEnv(configEnv.mode, process.cwd()) as unknown as ImportMetaEnv;
  const rootPath = getRootPath();
  const srcPath = getSrcPath();

  return {
    base: './',
    plugins: setupVitePlugins(viteEnv),
    define: { 'process.env': {} },
    resolve: {
      alias: {
        '~': rootPath,
        '@': srcPath,
      },
      extensions: [
        '.js',
        '.json',
        '.jsx',
        '.mjs',
        '.ts',
        '.tsx',
        '.vue',
      ],
    },
    server: {
      port: 3322,
      open: true,
      host: '0.0.0.0',
      proxy: {
        '/api': {
          /* 目标代理服务器地址 */
          target: 'http://124.221.34.38:8081',
          /* 允许跨域 */
          changeOrigin: true,
          rewrite: (path) => path.replace(/^\/api/, ""),
        }
      }
    },
    proxyTable: {
      '/api': {
        target: 'http://124.221.34.38:8081',  //目标接口域
        changeOrigin: true,  //是否跨域
        secure: true,//当是https的时候改成false
        pathRewrite: {
          '^/api': '/'  //重写接口
        }
      },
    },

    // devServer: {
    //   port: 3322,
    //   open: true,
    //   proxy: {
    //     '/api': {
    //       target: 'http://124.221.34.38:8081',
    //       changeOrigin: true,
    //       pathRewrite: {
    //         '^/api': ''
    //       }
    //     }
    //   }
    // },
    build: {
      reportCompressedSize: false,
      sourcemap: false,
      commonjsOptions: {
        ignoreTryCatch: false
      }
    },
    assetsInclude: ["**/*.bpmn"]
  }
})
