import CourseInput from "../ui/CourseInput"

export default function Description({setDescription, description}: any) {
	return (
		<CourseInput title="description">
			<textarea value={description} className="w-full outline-none h-[100px] resize-none rounded-2xl px-3 py-2" placeholder="Enter Course Description" onChange={(e) => setDescription(e.target.value)}/>
		</CourseInput>
	);
}