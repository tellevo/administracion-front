/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      // Custom extensions can go here, but colors should be handled by DaisyUI themes
    },
  },
  plugins: [require("daisyui")],
  daisyui: {
    themes: [
      {
        dark: {
          primary: '#1d4ed8',
          secondary: '#4f46e5',
          accent: '#0ea5e9',
          neutral: '#3d4451',
          'base-100': '#1f2937', // Darker background
          'base-200': '#111827',
          'base-300': '#374151',
          info: '#3b82f6',
          success: '#22c55e',
          warning: '#f59e0b',
          error: '#ef4444',
        },
      },
    ],
    darkTheme: "dark",
    base: true,
    styled: true,
    utils: true,
    prefix: "",
    logs: false,
    themeRoot: ":root"
  }
}
