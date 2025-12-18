import type { UserConfig, ConfigEnv, PluginOption } from 'vite';
import pkg from './package.json';
import dayjs from 'dayjs';
import { loadEnv } from 'vite';
import { resolve } from 'path';
import { generateModifyVars } from './build/generate/generateModifyVars';
import { createProxy } from './build/vite/proxy';
import { wrapperEnv } from './build/utils';
import { createVitePlugins } from './build/vite/plugin';
import { OUTPUT_DIR } from './build/constant';

function pathResolve(dir: string) {
  return resolve(process.cwd(), '.', dir);
}

/**
 * SPA History Fallback 插件
 * 确保刷新页面时返回 index.html，支持 Vue Router History 模式
 */
function spaFallbackPlugin(): PluginOption {
  return {
    name: 'spa-fallback',
    configureServer(server) {
      server.middlewares.use((req, res, next) => {
        const url = req.url || '';
        // 跳过静态资源、API 请求和已有文件
        if (
          url.startsWith('/jeecgboot') ||
          url.startsWith('/upload') ||
          url.startsWith('/@') ||
          url.startsWith('/src') ||
          url.startsWith('/node_modules') ||
          url.includes('.') ||
          url.startsWith('/__')
        ) {
          return next();
        }
        // 对于其他请求，重写为 index.html
        req.url = '/index.html';
        next();
      });
    },
  };
}

const { dependencies, devDependencies, name, version } = pkg;
const __APP_INFO__ = {
  pkg: { dependencies, devDependencies, name, version },
  lastBuildTime: dayjs().format('YYYY-MM-DD HH:mm:ss'),
};

export default ({ command, mode }: ConfigEnv): UserConfig => {
  const root = process.cwd();

  const env = loadEnv(mode, root);

  // The boolean type read by loadEnv is a string. This function can be converted to boolean type
  const viteEnv = wrapperEnv(env);

  const { VITE_PORT, VITE_PUBLIC_PATH, VITE_PROXY } = viteEnv;

  const isBuild = command === 'build';

  return {
    // 显式指定为 SPA 应用，确保刷新时正确回退到 index.html
    appType: 'spa',
    base: VITE_PUBLIC_PATH,
    root,
    resolve: {
      alias: [
        {
          find: 'vue-i18n',
          replacement: 'vue-i18n/dist/vue-i18n.cjs.js',
        },
        // /@/xxxx => src/xxxx
        {
          find: /\/@\//,
          replacement: pathResolve('src') + '/',
        },
        // /#/xxxx => types/xxxx
        {
          find: /\/#\//,
          replacement: pathResolve('types') + '/',
        },
        {
          find: /@\//,
          replacement: pathResolve('src') + '/',
        },
        // /#/xxxx => types/xxxx
        {
          find: /#\//,
          replacement: pathResolve('types') + '/',
        },
      ],
    },
    server: {
      // Listening on all local IPs
      host: true,
      https: false,
      port: VITE_PORT,
      // Load proxy configuration from .env
      proxy: createProxy(VITE_PROXY),
    },
    build: {
      minify: 'esbuild',
      target: 'es2015',
      cssTarget: 'chrome80',
      outDir: OUTPUT_DIR,
      rollupOptions: {
        // 关闭除屑优化，防止删除重要代码，导致打包后功能出现异常
        treeshake: false,
        output: {
          chunkFileNames: 'js/[name]-[hash].js', // 引入文件名的名称
          entryFileNames: 'js/[name]-[hash].js', // 包的入口文件名称
          // manualChunks配置 (依赖包从大到小排列)
          manualChunks: {
            // vue vue-router合并打包
            'vue-vendor': ['vue', 'vue-router'],
            'antd-vue-vendor': ['ant-design-vue','@ant-design/icons-vue','@ant-design/colors'],
            'vxe-table-vendor': ['vxe-table','vxe-table-plugin-antd','xe-utils'],
            'emoji-mart-vue-fast': ['emoji-mart-vue-fast'],
            'china-area-data-vendor': ['china-area-data']
          },
        },
      },
      // 关闭brotliSize显示可以稍微减少打包时间
      reportCompressedSize: false,
      // 提高超大静态资源警告大小
      chunkSizeWarningLimit: 2000,
    },
    esbuild: {
      //清除全局的console.log和debug
      drop: isBuild ? ['console', 'debugger'] : [],
    },
    define: {
      // setting vue-i18-next
      // Suppress warning
      __INTLIFY_PROD_DEVTOOLS__: false,
      __APP_INFO__: JSON.stringify(__APP_INFO__),
    },
    css: {
      preprocessorOptions: {
        less: {
          modifyVars: generateModifyVars(),
          javascriptEnabled: true,
        },
      },
    },

    // The vite plugin used by the project. The quantity is large, so it is separately extracted and managed
    plugins: [
      // SPA History Fallback 插件，确保刷新页面时正确加载
      spaFallbackPlugin(),
      ...createVitePlugins(viteEnv, isBuild),
    ],
    // 预加载构建配置（首屏性能)
    optimizeDeps: {
      esbuildOptions: {
        target: 'es2020',
      },
      exclude: [
        //升级vite4后，需要排除online依赖
        '@jeecg/online',
      ],
    },
  };
};
