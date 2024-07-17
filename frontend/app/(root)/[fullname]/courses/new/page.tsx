'use client'

import NewChapterPage from "@/components/newChapter/newChapterPage";
import { ChapterProperties } from "@/components/newCourse/Chapters";
import NewCoursePage from "@/components/newCourse/NewCoursePage";
import NewCourseProvider, { NewCourse } from "@/lib/newCourseContext/context";
import { useContext, useEffect, useState } from "react";

export default function UploadCourse() {
	const [page, setPage] = useState<'course' | 'chapter'>('course');
	const context = useContext(NewCourse);
	
	if (!context)
		throw new Error('MyComponent must be used within a MyProvider');
	
	const {setCourse} = context;


	useEffect(() => {setCourse({
		title: "",
		description: "",
		price: 0,
		isFree: false,
		chapters: [],
		thumbnail: null,
		category: 'lifestyle',
		language: 'ENGLISH',
		level: 'BEGINNERS'
	})}, [setCourse])

	return (
		<>
			{
				page === 'course' ? <NewCoursePage newChapter={setPage}/> : <NewChapterPage saveChapter={setPage}/>
			}
		</>
	);
}