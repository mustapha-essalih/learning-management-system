import * as React from "react";

interface CourseInputProps {
	title: string;
	className?: string;
	children: React.ReactNode;
}

export default function CourseInput(props: CourseInputProps) {
	return (
		<div className="px-4 py-3 flex flex-col items-start justify-start gap-3 bg-[#dddddd] rounded-3xl">
			<span className="text-xl ml-3 capitalize">{props.title}</span>
			{
				props.children
			}
		</div>
	);
}