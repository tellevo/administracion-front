/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        primary: '#1d4ed8', // Azul
        secondary: '#4f46e5', // Índigo
        accent: '#0ea5e9', // Celeste
        neutral: '#3d4451', // Gris oscuro
        'base-100': '#ffffff',
        'base-200': '#f8fafc',
        'base-300': '#f1f5f9',
        info: '#3b82f6',
        success: '#22c55e',
        warning: '#f59e0b',
        error: '#ef4444',
      },
    },
  },
  plugins: [require("daisyui")],
  daisyui: {
    themes: [
      {
        light: {
          primary: '#1d4ed8',
          secondary: '#4f46e5',
          accent: '#0ea5e9',
          neutral: '#3d4451',
          'base-100': '#ffffff',
          'base-200': '#f8fafc',
          'base-300': '#f1f5f9',
        },
      },
    ],
    base: true,
    styled: true,
    utils: true,
    prefix: "",
    logs: false,
    themeRoot: ":root"
  }
}
