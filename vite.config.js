import { defineConfig, loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path'

export default defineConfig(({ command, mode }) => {
  // Load env file based on `mode`
  const env = loadEnv(mode, process.cwd())
  
  return {
    plugins: [vue()],
    css: {
      transformer: 'postcss'
    },
    resolve: {
      alias: {
        '@': resolve(__dirname, 'src'),
      },
    },
    server: {
      host: true,
      port: 5173,
      proxy: env.VITE_API_URL === '/api' ? {
        '/api': {
          target: 'http://localhost:8080',
          changeOrigin: true,
          secure: false,
        },
      } : {},
    },
    optimizeDeps: {
      include: ['vue', 'vue-router', 'axios', 'chart.js', 'vue-chartjs'],
      esbuildOptions: {
        target: 'esnext',
        platform: 'browser',
      }
    },
    build: {
      target: 'es2015',
      minify: 'esbuild',
      cssMinify: 'esbuild',
      rollupOptions: {
        output: {
          manualChunks: {
            'vue-vendor': ['vue', 'vue-router'],
            'chart-vendor': ['chart.js', 'vue-chartjs'],
          }
        }
      },
      sourcemap: mode === 'development',
      reportCompressedSize: false // Faster builds
    },
    esbuild: {
      platform: 'browser',
      target: 'esnext',
      legalComments: 'none'
    }
  }
})
