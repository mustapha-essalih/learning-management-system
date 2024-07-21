'use client'

import { ProfileContext } from "@/lib/userProfileContext/context";
import Link from "next/link";
import { useContext } from "react";

export default function Navbar() {

	const context = useContext(ProfileContext)

	// const {profile, setProfile} = context;

	return (
		<nav className="w-full h-[60px] flex justify-between items-center px-8 py-3 bg-white shadow-lg rounded-full mb-5">
			<span className="font-bold text-2xl text-main-blue">
				Ude<span className=" text-main-yellow">mok</span>
			</span>
			<div className="flex gap-5 items-center">
				<div>{context?.profile?.fullName}</div>
				<Link href={"/login"} onClick={async () => {
					const res = await fetch('http://localhost:8080/api/auth/logout', {
						method: 'GET',
						headers: {
							'Authorization': 'Bearer ' + context?.profile?.jwt
						},
					})
				}} className="px-3 py-2 bg-main-yellow text-white font-semibold rounded-full">logout</Link>
			</div>
		</nav>
	);
}
