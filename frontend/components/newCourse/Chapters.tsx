'use client'

import { useContext, useEffect, useState } from "react";
import CourseInput from "../ui/CourseInput";
import {DndContext, DragEndEvent, closestCorners} from "@dnd-kit/core";
import ChapterComponent from "../ui/Chapter";
import { SortableContext, arrayMove, verticalListSortingStrategy } from "@dnd-kit/sortable";
import Button from '../ui/Button';
import AddChapter from "./AddChapter";
import { Chapter , NewCourse, Resource } from "@/lib/newCourseContext/context";

export interface ChapterProperties {
	title: string;
	resources: Resource[];
	id: number;
	isFree: boolean;
}

export default function Chapters({newChapter}: {newChapter: any}) {
	const context = useContext(NewCourse);
	
	if (!context)
		throw new Error('MyComponent must be used within a MyProvider');
	
	const {course, setCourse} = context;

	const getChapter = (id: number) => course!.chapters.findIndex(chapter => chapter.id === id);

	const handleOnDragEnd = (event: DragEndEvent) => {
		const {active, over} = event;
		
		if (over?.id == null) return ;
		if (active.id === over.id) return ;

		setCourse((old) => {
			const tt = {...old};
			const originalChapter = getChapter(active.id as number);
			const newPos = getChapter(over.id as number);

			const newsd = arrayMove(tt.chapters, originalChapter, newPos);
			tt.chapters = [...newsd];
			return (tt);
		});
	}

	const deleteChapter = (id: number) => {
		const index = course!.chapters.findIndex(chapter => chapter.id === id);
		console.log(index);
		setCourse((old) => {
			const tt = {...old};
			old?.chapters.splice(index, 1);
			return (tt);
		});
	}

	console.log("chapter: ", course?.chapters.length);

	return (
		<CourseInput title="chapters">
			<DndContext collisionDetection={closestCorners} onDragEnd={handleOnDragEnd}>
				<div className="flex flex-col items-center justify-center w-full gap-3">
					<SortableContext items={course?.chapters!} strategy={verticalListSortingStrategy}>
						{
							course!.chapters.length > 0 ? course!.chapters.map(({title, id}, index) => {
								console.log(title);
								return (
								<ChapterComponent title={title} id={id} key={id} deleteChapter={deleteChapter}/>
							)}) : <h1>add chapters</h1>
						}
					</SortableContext>
				</div>
			</DndContext>
			<Button className="bg-main-blue" onClick={() => newChapter('chapter')}>Add</Button>
		</CourseInput>
	);
}