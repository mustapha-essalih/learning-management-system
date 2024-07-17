'use client'

import { createContext, useState } from 'react';

export type Resource = {
	title: string,
	file: File | null,
	isFree: boolean,
	duration: number,
	id: number
}

export type Chapter = {
	title: string,
	isFree: boolean,
	id: number,
	resources: Resource[]
}

type Course = {
	title: string,
	description: string,
	price: number,
	thumbnail: File | null,
	isFree: boolean,
	category: string,
	level: "BEGINNERS" | "INTERMEDIATE" | "EXPERT",
	language: 'ENGLISH' | 'SPANISH' | 'FRENCH',
	chapters: Chapter[]
}

interface NewCourseContext {
	course: Course | null,
	setCourse: React.Dispatch<React.SetStateAction<Course>>
}

export const NewCourse = createContext<NewCourseContext | null>(null);

export default function NewCourseProvider({children}: {children: React.ReactNode}) {

	const [newCourse, setNewCourse] = useState<Course>({
		title: "",
		description: "",
		price: 0,
		isFree: false,
		chapters: [],
		thumbnail: null,
		category: 'lifestyle',
		language: 'ENGLISH',
		level: 'BEGINNERS'
	});

	return (
		<NewCourse.Provider value={{course: newCourse, setCourse: setNewCourse}}>
			{children}
		</NewCourse.Provider>
	);
}