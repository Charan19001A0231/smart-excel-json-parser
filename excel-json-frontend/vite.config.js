import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5174,         // Use port 5173
    strictPort: true,   // Prevent Vite from switching to another port
  },
});
