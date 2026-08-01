import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from 'tailwindcss'
import autoprefixer from 'autoprefixer'

export default defineConfig({
  plugins: [react()],
  css: {
    postcss: {
      plugins: [tailwindcss, autoprefixer()],
    },
  },
  server: {
    proxy: {
      '/aluno': 'http://localhost:8080',
      '/disciplina': 'http://localhost:8080',
      '/matriculas': 'http://localhost:8080',
    },
  },
})
