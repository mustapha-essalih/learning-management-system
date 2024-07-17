import type { Config } from "tailwindcss";

const config: Config = {
  content: [
    "./pages/**/*.{js,ts,jsx,tsx,mdx}",
    "./components/**/*.{js,ts,jsx,tsx,mdx}",
    "./app/**/*.{js,ts,jsx,tsx,mdx}",
  ],
  theme: {
    extend: {
      backgroundImage: {
        "gradient-radial": "radial-gradient(var(--tw-gradient-stops))",
        "gradient-conic":
          "conic-gradient(from 180deg at 50% 50%, var(--tw-gradient-stops))",
		"blue-tulips": "url('/blue-tulips.jpg')",
      },
	  colors: {
		"main-blue": "#114d97",
		"main-yellow": "#efb020",
		"soft-blue": "#b6d0fa"
	  }
    },
  },
  plugins: [],
};
export default config;
