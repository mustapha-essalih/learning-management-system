import Navbar from "@/components/ui/Navbar";
import NewCourseProvider, { Chapter, NewCourse } from "@/lib/newCourseContext/context";
import { useContext, useState } from "react";

export default async function Layout({
	children,
  }: {
	children: React.ReactNode;
  }) {

	return (
		<NewCourseProvider>
			<div className="w-screen h-screen bg-[#f5f5f5] p-4">
				<Navbar />
				{children}
			</div>
		</NewCourseProvider>
	)

  }