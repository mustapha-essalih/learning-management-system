import Link from "next/link";

export default function Navbar() {
	return (
		<nav className="w-full h-[60px] flex justify-between items-center px-8 py-3 bg-white shadow-lg rounded-full mb-5">
			<span className="font-bold text-2xl text-main-blue">
				Ude<span className=" text-main-yellow">mok</span>
			</span>
			<div className="flex gap-5 items-center">
				<Link href="/">home</Link>
				<Link href="/contact">contact</Link>
				<Link href="/login" className="px-3 py-2 bg-main-blue text-white font-semibold rounded-full">login</Link>
				<Link href="/register" className="px-3 py-2 bg-main-yellow text-white font-semibold rounded-full">register</Link>
			</div>
		</nav>
	);
}
