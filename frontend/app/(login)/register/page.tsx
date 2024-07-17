'use client'

import { Box, Typography } from "@mui/material";
import { FormEventHandler } from "react";

export default function Register() {

	const submitHandler: FormEventHandler<HTMLFormElement> = async (event) => {
		event.preventDefault();
		const form = event.target as HTMLFormElement;
		const fullName = (form[0] as HTMLInputElement).value; 
		const email = (form[1] as HTMLInputElement).value;
		const role = (form[2] as HTMLSelectElement).value.toUpperCase();
		const password = (form[3] as HTMLInputElement).value;

		console.log(role);

		try {
			const res = await fetch('http://localhost:8080/api/auth/signup', {
				method: 'POST',
				body: JSON.stringify({fullName, email, password, role}),
				headers: {'Content-Type': 'application/json'}
			})

			const {data} = await res.json();
			console.log(data);
		} catch(e) {
			console.log(e);
		}
	}

	return (
		<Box className="flex flex-col items-center justify-center h-screen gap-7">
			<Typography variant="h2" className="capitalize font-semibold text-gra">Register</Typography>
			<form method="post" className="flex flex-col items-center justify-center gap-4" onSubmit={submitHandler}>
				<label htmlFor="name" className="flex flex-col gap-1">
					fullname
					<input type="text" id="name" className="outline-none px-3 py-2 bg-gray-200 rounded-sm"/>
				</label>
				<label htmlFor="email" className="flex flex-col gap-1">
					email
					<input type="email" id="email" className="outline-none px-3 py-2 bg-gray-200 rounded-sm"/>
				</label>
				<label htmlFor="role" className="flex flex-col gap-1 w-full">
					role
					<select name="role" id="role" className="outline-none px-3 py-2 bg-gray-200 rounded-sm">
						<option value="student" defaultChecked>student</option>
						<option value="instructor">instructor</option>
					</select>
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