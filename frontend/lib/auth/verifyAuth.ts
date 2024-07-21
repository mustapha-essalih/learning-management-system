type Access_Token = {
	token: string,
	value: string
}

export async function verifyAuth(jwt: string | undefined): Promise<boolean> {
	
	if (!jwt)
		return (false);

	console.log(jwt);

	const res = await fetch('http://127.0.0.1:8080/api/auth/checkJwt', {
		method: 'POST',
		body: jwt
	})

	console.log('response: ', res.ok);

	if (res.ok) {
		return (true);
	}

	return (false);
}