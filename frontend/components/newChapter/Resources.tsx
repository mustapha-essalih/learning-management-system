'use client'

import { useContext, useState } from "react";
import CourseInput from "../ui/CourseInput";
import {DndContext, DragEndEvent, closestCorners} from "@dnd-kit/core";
import Chapter from "../ui/Chapter";
import { SortableContext, arrayMove, verticalListSortingStrategy } from "@dnd-kit/sortable";
import Button from '../ui/Button';
import AddResource from "./AddResource";
import ResourceComponent from "../ui/Resource";
import { NewCourse, Resource } from "@/lib/newCourseContext/context";

export interface ResourceProperties {
	title: string,
	file: File | null,
	id: number,
	isFree: boolean
}

export default function Resources({addResource}: {addResource: (resource: Resource) => void}) {
	const context = useContext(NewCourse);
	const [open, setOpen] = useState(false);
	const [resources, setResources] = useState<ResourceProperties[]>([{
		title: "resource 1",
		file: null,
		isFree: false,
		id: 1,
	},
	{
		title: "resource 2",
		file: null,
		isFree: false,
		id: 2,
	},
	{
		title: "resource 3",
		file: null,
		isFree: false,
		id: 3,
	}

]);

	if (!context)
		throw new Error('MyComponent must be used within a MyProvider');
	
	const {course, setCourse} = context;

	const getResource = (id: number) => resources.findIndex(resource => resource.id === id);

	const handleOnDragEnd = (event: DragEndEvent) => {
		const {active, over} = event;
		
		if (over?.id == null) return ;
		if (active.id === over.id) return ;

		setResources((old) => {
			const originalResource = getResource(active.id as number);
			const newPos = getResource(over.id as number);

			const newsd = arrayMove(resources, originalResource, newPos);
			return (newsd);
		})
	}

	const appendResource = (newResource: Resource) => {
		newResource.id = resources.length + 1;
		setResources((old) => [...old, newResource]);
		addResource(newResource);
	}

	const deleteResource = (id: number) => {
		const index = resources.findIndex(chapter => chapter.id === id);
		setResources(chapters => {
			const newResources = Array.from(chapters);
			newResources.splice(index, 1);
			return (newResources);
		})
	}

	return (
		<CourseInput title="resources">
			<DndContext collisionDetection={closestCorners} onDragEnd={handleOnDragEnd}>
				<div className="flex flex-col items-center justify-center w-full gap-3">
					<SortableContext items={resources} strategy={verticalListSortingStrategy}>
						{
							resources.map(({title, id}, index) => (
								<ResourceComponent title={title} id={id} key={id} deleteResource={deleteResource}/>
							))
						}
					</SortableContext>
				</div>
			</DndContext>
			<Button className="bg-main-blue" onClick={() => setOpen(true)}>Add</Button>
			<AddResource open={open} setOpen={setOpen} addResource={appendResource}/>
		</CourseInput>
	);
}