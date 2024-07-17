'use client'

import { MdEdit } from "react-icons/md";
import { Typography } from "@mui/material";
import CourseInput from "../ui/CourseInput";

export default function Title({setTitle, title}: any) {
	return (
		<CourseInput title="title">
			<input type="text" value={title} placeholder="Enter Course Title" className="w-full outline-none px-2 py-1 rounded-full h-[40px]" onChange={(e) => setTitle(e.target.value)}/>
		</CourseInput>
	);
}