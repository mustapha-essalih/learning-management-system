'use client'

import { Box, Typography } from "@mui/material";
import { FormEventHandler, useContext } from "react";
import { useRouter } from "next/navigation";
import { ProfileContext } from "@/lib/userProfileContext/context";

type ResponseObj = {
	role: string,
	jwt: string,
	email: string,
	fullName: string,
	id: number
}

export default function Login() {
	const router = useRouter();
	const context = useContext(ProfileContext);

	if (!context)
		throw new Error("no profile context");

	const {profile, setProfile} = context;

	const setCookie = (name: string, value: string, days: number) => {
		let expires = "";
		if (days) {
			const date = new Date();
			date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
			expires = "; expires=" + date.toUTCString();
		}
		document.cookie = name + "=" + (value || "") + expires + "; path=/";
	}

	const submitHandler: FormEventHandler<HTMLFormElement> = async (event) => {
		event.preventDefault();
		const form = event.target as HTMLFormElement;
		const email = (form[0] as HTMLInputElement).value;
		const password = (form[1] as HTMLInputElement).value;

		try {
			const res = await fetch('http://localhost:8080/api/auth/signin', {
				method: 'POST',
				body: JSON.stringify({email, password}),
				headers: {'Content-Type': 'application/json'}
			})

			const data = await res.json() as ResponseObj;
			setCookie('token', data.jwt, 15);
			setProfile({...data, role: data.role as ("STUDENT" | "INSTRUCTOR")})
			router.push(`/${data.fullName}/courses/new`);

		} catch(e) {
			console.log(e);
		}
	}

	return (
		<Box className="flex flex-col items-center justify-center h-screen gap-7">
			<Typography variant="h2" className="capitalize font-semibold text-gra">login</Typography>
			<form method="post" className="flex flex-col items-center justify-center gap-4" onSubmit={submitHandler}>
				<label htmlFor="email" className="flex flex-col gap-1">
					email
					<input type="email" id="email" className="outline-none px-3 py-2 bg-gray-200 rounded-sm"/>
				</label>
				<label htmlFor="password" className="flex flex-col gap-1">
					password
					<input type="password" id="password" className="outline-none px-3 py-2 bg-gray-200 rounded-sm"/>
				</label>
				<button className="capitalize px-3 py-2 bg-blue-500 rounded-full text-white font-medium">submit</button>
			</form>
		</Box>
	);
}