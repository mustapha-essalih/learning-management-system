import { Typography } from "@mui/material";
import Image from "next/image";

export default function Hero() {
	return (
		<div className="relative w-full p-5 rounded-3xl bg-red-500">
			<Typography variant="h1" className="text-white font-medium">
				hello
				<span className="text-main-yellow"> Ali</span>
			</Typography>
		</div>
	);
}