import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from "path";

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
        vue({
            reactivityTransform: true,
        })
    ],
  resolve: {
    alias: {
      "@": path.resolve(__dirname, "./src"),
    },
  },
  server: {
    proxy: {
      '^/register': {
        target: 'http://localhost:8080', // Spring boot backend address
        changeOrigin: true,
        secure: false,
        ws: true,
      },
      '^/login': {
        target: 'http://localhost:8080', // Spring boot backend address
        changeOrigin: true,
        secure: false,
        ws: true,
      },
      '^/logout': {
        target: 'http://localhost:8080', // Spring boot backend address
        changeOrigin: true,
        secure: false,
        ws: true,
      },
      '^/.*/images/.*': {
        target: 'http://localhost:8080', // Spring boot backend address
        changeOrigin: true,
        secure: false,
        ws: true,
      },
    },
  },
  build: {
    outDir: 'target/dist',
    assetsDir: 'static',
  },
})
