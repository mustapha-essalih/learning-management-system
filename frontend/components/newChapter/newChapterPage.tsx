import { Chapter, NewCourse, Resource } from "@/lib/newCourseContext/context";
import Title from "../newCourse/Title";
import Button from "../ui/Button";
import Resources from "./Resources";
import { useContext, useState } from "react";


export default function NewChapterPage({saveChapter}: {saveChapter: any}) {
	const context = useContext(NewCourse);	
	const [title, setTitle] = useState("");
	const [isFree, setIsFree] = useState(false);
	const [resources, setResources] = useState<Resource[]>([]);
	
	if (!context)
		throw new Error('MyComponent must be used within a MyProvider');
	
	const {course, setCourse} = context;

	const addResource = (resource: Resource) => {
		setResources((r) => [...r, resource]);
	}

	const handleSubmit = () => {
		setCourse((old) => {
			const tt = {...old};
			const newChapter: Chapter = {
				title,
				resources: resources,
				isFree,
				id: tt.chapters.length + 1
			}
			tt.chapters.push(newChapter);
			return (tt);
		})
		saveChapter('course')
	}

	return (
		<>
			<div className="flex flex-col items-center gap-5 relative">
				<div className="flex gap-5 w-full">
					<div className="flex flex-col w-1/2 gap-3">
						<Title setTitle={setTitle} />
						<div className="flex gap-3 items-center">
							<input type="checkbox" id="free" onChange={() => setIsFree(old => !old)} className="h-5 w-5"/>
							<label htmlFor="free" className=" text-xl">is free</label>
						</div>
					</div>
					<div className="flex flex-col w-1/2 gap-3">
						<Resources addResource={addResource} />
					</div>
				</div>
				<Button className="bg-main-blue w-fit" onClick={handleSubmit}>Save</Button>
			</div>
		</>
	);
}