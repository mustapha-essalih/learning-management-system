'use client'

import { Box, Typography } from "@mui/material";
import styles from "./styles.module.css";
import { FormEventHandler, useState } from "react";
import Link from "next/link";

import Snackbar from '@mui/material/Snackbar';
import Alert from '@mui/material/Alert';

export default function Register() {

	const [resStatus, setResStatus] = useState({open: false, status: "", text: ""})


	const submitHandler: FormEventHandler<HTMLFormElement> = async (event) => {
		event.preventDefault();
		const form = event.target as HTMLFormElement;
		const fullName = (form[0] as HTMLInputElement).value; 
		const email = (form[1] as HTMLInputElement).value;
		const role = (form[3] as HTMLSelectElement).value.toUpperCase();
		const password = (form[2] as HTMLInputElement).value;


		form.reset()
		try {
			const res = await fetch('http://localhost:8080/api/auth/signup', {
				method: 'POST',
				body: JSON.stringify({fullName, email, password, role}),
				headers: {'Content-Type': 'application/json'}
			}).then(res => res.json()).then(res => {
				if (res.status === 200)
				setResStatus({open: true, status: "success", text: res.body})
				else
				setResStatus({open: true, status: "error", text: res.body})
			})

			const {data} = await res.json();
		} catch(e) {
		}
	}

	return (
		// <Box className="flex flex-col items-center justify-center h-screen gap-7">
		// 	<Typography variant="h2" className="capitalize font-semibold text-gra">Register</Typography>
		// 	<form method="post" className="flex flex-col items-center justify-center gap-4" onSubmit={submitHandler}>
		// 		<label htmlFor="name" className="flex flex-col gap-1">
		// 			fullname
		// 			<input type="text" id="name" className="outline-none px-3 py-2 bg-gray-200 rounded-sm"/>
		// 		</label>
		// 		<label htmlFor="email" className="flex flex-col gap-1">
		// 			email
		// 			<input type="email" id="email" className="outline-none px-3 py-2 bg-gray-200 rounded-sm"/>
		// 		</label>
		// 		<label htmlFor="role" className="flex flex-col gap-1 w-full">
		// 			role
		// 			<select name="role" id="role" className="outline-none px-3 py-2 bg-gray-200 rounded-sm">
		// 				<option value="student" defaultChecked>student</option>
		// 				<option value="instructor">instructor</option>
		// 			</select>
		// 		</label>
		// 		<label htmlFor="password" className="flex flex-col gap-1">
		// 			password
		// 			<input type="password" id="password" className="outline-none px-3 py-2 bg-gray-200 rounded-sm"/>
		// 		</label>
		// 		<button className="capitalize px-3 py-2 bg-blue-500 rounded-full text-white font-medium">submit</button>
		// 	</form>
		// </Box>
		<>
		<div className={styles.signup_container}>
			<div className={styles.signup_form_container}>
				<div className={styles.left}>
					<h1>Welcome Back</h1>
					<Link href="/login">
						<button type="button" className={styles.white_btn}>
							Sign in
						</button>
					</Link>
				</div>
				<div className={styles.right}>
					<form className={styles.form_container} onSubmit={submitHandler}>
						<h1>Create Account</h1>
						<input
							type="text"
							placeholder="Full name"
							name="fullname"
							// onChange={handleChange}
							// value={data.username}
							required
							className={styles.input}
						/>
						<input
							type="email"
							placeholder="Email"
							name="email"
							// onChange={handleChange}
							// value={data.email}
							required
							className={styles.input}
						/>
						<input
							type="password"
							placeholder="Password"
							name="password"
							// onChange={handleChange}
							// value={data.password}
							required
							className={styles.input}
						/>
						<select name="role" id="role" className="outline-none px-3 py-2 bg-gray-200 rounded-sm">
			 				<option value="student" defaultChecked>student</option>
			 				<option value="instructor">instructor</option>
			 			</select>
						{/* {error && <div className={styles.error_msg}>{error}</div>} */}
						<button type="submit" className={styles.green_btn}>
							signup
						</button>
					</form>
				</div>
			</div>
		</div>
		<Snackbar open={resStatus.open} autoHideDuration={5000} onClose={() => setResStatus({open: false, status: "", text: ""})}>
		<Alert
			onClose={() => setResStatus({open: false, status: "", text: ""})}
			severity={resStatus.status}
			variant="filled"
			sx={{ width: '100%' }}
		>
			{resStatus.text}
		</Alert>
	  </Snackbar>
	  </>
	);
}