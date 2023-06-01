import Components from 'unplugin-vue-components/vite';
import AutoImport from 'unplugin-auto-import/vite';
import Icons from 'unplugin-icons/vite'
import {getSrcPath} from '../utils';
import {FileSystemIconLoader} from "unplugin-icons/loaders";
import IconsResolver from 'unplugin-icons/resolver'
import {createSvgIconsPlugin} from 'vite-plugin-svg-icons';

import vueJsx from '@vitejs/plugin-vue-jsx'


export default function unplugin(viteEnv: ImportMetaEnv) {
  const {VITE_ICON_PREFFIX} = viteEnv;

  const srcPath = getSrcPath();
  const localIconPath = `${srcPath}/assets/images/svgs`;

  /** 本地svg图标集合名称 */
  const collectionName = 'local';

  return [
    AutoImport({
      imports: ['vue', {
        'vuetify': ['useTheme'],
        'pinia': ['storeToRefs'],
      }],
      dirs: [`${srcPath}/plugins`, `${srcPath}/filters`, `${srcPath}/store/**`, `${srcPath}/router`],
      resolvers: [
        IconsResolver({
          customCollections: [collectionName],
          componentPrefix: VITE_ICON_PREFFIX,
          enabledCollections: [collectionName],
        }),

      ],
      dts: "src/typings/auto-imports.d.ts",
    }),
    Icons(
      {
        compiler: 'vue3',
        customCollections: {
          [collectionName]: FileSystemIconLoader(localIconPath)
        }
      }
    ),
    Components({
      dts: 'src/typings/components.d.ts',
      dirs: ['src/components'],
      extensions: ['vue'],
      resolvers: [
        IconsResolver(
          {
            customCollections: [collectionName],
            prefix: VITE_ICON_PREFFIX
          }
        ),
        (componentName) => {
          // where `componentName` is always CapitalCase
          if (componentName.startsWith('V'))
            return {name: componentName, from: 'vuetify/lib/labs/components'}
        },

        (componentName) => {
          // where `componentName` is always CapitalCase
          if (componentName.endsWith('Provider'))
            return {name: componentName, from: '@/components/provider'}
        }
      ]
    }),
    createSvgIconsPlugin({
      iconDirs: [localIconPath],
      symbolId: `${VITE_ICON_PREFFIX}-[dir]-[name]`,
      inject: 'body-last',
      customDomId: '__SVG_ICON_LOCAL__'
    }),
    vueJsx({optimize: false, enableObjectSlots: true}),

  ];
}
