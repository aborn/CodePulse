import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'
import vueJsx from '@vitejs/plugin-vue-jsx'
import Icons from 'unplugin-icons/vite'

// https://vitejs.dev/config/
export default defineConfig(({ command, mode, ssrBuild }) => {
  return {
    plugins: [vue(), vueJsx(), Icons({ /* options */ })],
    base: command === 'build' ? '/nanta-ui/' : '',
    resolve: {
      alias: {
        "/@": path.resolve(__dirname, "./src"),
      }
    },
  }
})