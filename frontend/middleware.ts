import { NextRequest, NextResponse } from "next/server";
import { verifyAuth } from "./lib/auth/verifyAuth";

export const middleware = async (req: NextRequest) => {
	// const token = req.cookies.get('token');
	// const loggedin = await verifyAuth(token?.value);
	const response = NextResponse.next({
		request: {
			headers: req.headers
		}
	})

	// if (!loggedin) {
	// 	req.nextUrl.pathname = '/login';
	// 	return NextResponse.redirect(req.nextUrl);
	// }

	return (response);
}

export const config = {
	matcher: [
		/*
		 * Match all request paths except for the ones starting with:
		 * - _next/static (static files)
		 * - _next/image (image optimization files)
		 * - favicon.ico (favicon file)
		 * Feel free to modify this pattern to include more paths.
		 */
		"/((?!login|register|$|_next/static|_next/image|favicon.ico|.*\\.(?:svg|png|jpg|jpeg|gif|webp)$).*)"
	],
}