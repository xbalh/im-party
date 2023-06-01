import unplugin from "./unplugin";
import vuetify from "./vuetify";
import vue from '@vitejs/plugin-vue'
import mock from './mock';
import type {PluginOption} from 'vite';

export function setupVitePlugins(viteEnv: ImportMetaEnv): (PluginOption | PluginOption[])[] {
  return [vue(), vuetify(), ...unplugin(viteEnv), mock(viteEnv)];
}
