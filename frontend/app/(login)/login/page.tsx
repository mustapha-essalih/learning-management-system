'use client'

import { Box, Typography } from "@mui/material";
import { FormEventHandler, useContext } from "react";
import { useRouter } from "next/navigation";
import { ProfileContext } from "@/lib/userProfileContext/context";
import styles from "./styles.module.css";
import Link from "next/link";

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
			if (data.role === "INSTRUCTOR")
				router.push(`/${data.fullName}/courses/new`);
			else
			router.push(`/${data.fullName}/courses/all`);

		} catch(e) {
			console.log(e);
		}
	}

	return (
		<div className={styles.login_container}>
			<div className={styles.login_form_container}>
				<div className={styles.left}>
					<form className={styles.form_container} onSubmit={submitHandler}>
						<h1>Login to Your Account</h1>
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
						{/* {error && <div className={styles.error_msg}>{error}</div>} */}
						<button type="submit" className={styles.green_btn}>
							signin
						</button>
					</form>
				</div>
				<div className={styles.right}>
					<h1>New Here ?</h1>
					<Link href="/register">
						<button type="button" className={styles.white_btn}>
							Sign Up
						</button>
					</Link>
				</div>
			</div>
		</div>
	);
}