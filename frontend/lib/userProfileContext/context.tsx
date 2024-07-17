'use client'

import { createContext, useState } from 'react';

type Profile = {
	fullName: string,
	email: string,
	jwt: string,
	id: number,
	role: 'STUDENT' | 'INSTRUCTOR'
}

interface ProfileState {
	profile: Profile | null,
	setProfile: React.Dispatch<React.SetStateAction<Profile | null>>
}

export const ProfileContext = createContext<ProfileState | null>(null);

export default function ProfileProvider({children}: {children: React.ReactNode}) {

	const [profile, setProfile] = useState<Profile | null>(null);

	return (
		<ProfileContext.Provider value={{profile, setProfile}}>
			{children}
		</ProfileContext.Provider>
	);
}