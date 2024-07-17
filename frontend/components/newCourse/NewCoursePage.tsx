import { useContext, useEffect, useState } from "react";
import Chapters from "./Chapters";
import Description from "./Description";
import Thumbnail from "./Thumbnail";
import Title from "./Title";
import { Chapter, NewCourse } from "@/lib/newCourseContext/context";
import Button from "../ui/Button";
import CourseInput from "../ui/CourseInput";
import { ProfileContext } from "@/lib/userProfileContext/context";

export default function NewCoursePage({newChapter}: {newChapter: any}) {
	// const [title, setTitle] = useState("");
	// const [price, setPrice] = useState(0);
	// const [level, setLevel] = useState("beginner");
	// const [language, setLanguage] = useState("english");
	// const [isFree, setIsFree] = useState(false);
	// const [description, setDescription] = useState("");
	// const [thumbnail, setThumbnail] = useState<File | null>(null);
	const context = useContext(NewCourse);
	const profileContext = useContext(ProfileContext);
	
	if (!context || !profileContext)
		throw new Error('no Context');
	
	const {course, setCourse} = context;
	const {profile} = profileContext;
	
	const setTitle = (title: string) => {
		setCourse((old) => {
			const tt = {...old};
			tt.title = title;
			return tt;
		});
	}

	const setDescription = (des: string) => {
		setCourse((old) => {
			const tt = {...old};
			tt.description = des;
			return tt;
		});
	}

	const setIsFree = (free: boolean) => {
		setCourse((old) => {
			const tt = {...old};
			tt.isFree = free;
			return tt;
		});
	}

	const setThumbnail = (file: File | null) => {
		setCourse((old) => {
			const tt = {...old};
			tt.thumbnail = file;
			return tt;
		});
	}

	const setPrice = (p: number) => {
		setCourse((old) => {
			const tt = {...old};
			tt.price = p;
			return tt;
		});
	}

	const setLanguage = (l: string) => {
		setCourse((old) => {
			const tt = {...old};
			tt.language = l as ("ENGLISH" | "SPANISH" | "FRENCH");
			return tt;
		});
	}

	const setLevel = (l: string) => {
		setCourse((old) => {
			const tt = {...old};
			tt.level = l as ("BEGINNERS" | "INTERMEDIATE" | "EXPERT");
			return tt;
		});
	}

	const setCategory = (c: string) => {
		setCourse((old) => {
			const tt = {...old};
			tt.category = c;
			return tt;
		});
	}

	const getFormData = () => {
		const formData = new FormData();
		formData.append('title', course?.title!);
		formData.append('description', course?.description!);
		formData.append('price', `${course?.price!}`);
		formData.append('level', course?.level!);
		formData.append('isFree', `${course?.isFree!}`);
		formData.append('language', course?.language!);
		formData.append('category', course?.category!);
		formData.append('courseImage', course?.thumbnail!);

		course?.chapters.forEach((c, i) => {
			formData.append(`chapters[${i}].chapterTitle`, c.title);
			c.resources.forEach((r, j) => {
				console.log(i, j);
				formData.append(`chapters[${i}].resources[${j}].resourceTitle`, r.title);
				formData.append(`chapters[${i}].resources[${j}].videos`, r.file!, r.file?.name);
			})
		})

		return (formData);
	}

	const submitCourse = async () => {
		const formData = getFormData();
		const res = await fetch('http://localhost:8080/api/instructor/upload-course', {
			method: 'POST',
			headers: {
				'Authorization': 'Bearer ' + profile?.jwt
			},
			body: formData
		})
		console.log(profile);
		console.log(formData);
		console.log(res);
	}

	return (
		<>
			<div className="flex gap-5">
				<div className="flex flex-col w-1/2 gap-3">
					<Title setTitle={setTitle} title={course?.title}/>
					<Description setDescription={setDescription} description={course?.description}/>
					<Thumbnail setThumbnail={setThumbnail}/>
				</div>
				<div className="flex flex-col w-1/2 gap-3">
					<Chapters newChapter={newChapter}/>
					<CourseInput title="price, language and level">
						<div>
							<label htmlFor="price">price</label>
							<input type="text" value={course?.price} id="price" placeholder="Enter Course Price" className="w-full outline-none px-2 py-1 rounded-full h-[40px]" onChange={(e) => setPrice(Number(e.target.value))}/>
						</div>
						<div>
							<label htmlFor="language">language</label>
							<select id="language" className="w-full outline-none px-2 py-1 rounded-full h-[40px]" onChange={(e) => setLanguage(e.target.value)}>
								<option value="ENGLISH">english</option>
								<option value="SPANISH">spanish</option>
								<option value="FRENSH">frensh</option>
							</select>
						</div>
						<div>
							<label htmlFor="level">level</label>
							<select id="level" className="w-full outline-none px-2 py-1 rounded-full h-[40px]" onChange={(e) => setLevel(e.target.value)}>
								<option value="BEGINNERS">beginner</option>
								<option value="INTERMEDIATE">intermediate</option>
								<option value="EXPERT">advanced</option>
							</select>
						</div>
						<div>
							<label htmlFor="cat">category</label>
							<select id="cat" className="w-full outline-none px-2 py-1 rounded-full h-[40px]" onChange={(e) => setCategory(e.target.value)}>
								<option value="lifestyle">lifestyle</option>
								<option value="it">it</option>
								<option value="health">health</option>
							</select>
						</div>
						<div className="flex items-center gap-3">
							<input type="checkbox" id="free" className="w-5 h-5" onChange={(e) => setIsFree(e.target.checked)}/>
							<label htmlFor="free">is Free</label>
						</div>
					</CourseInput>
				</div>
			</div>
			<Button className="bg-main-blue mt-6" onClick={submitCourse}>submit</Button>
		</>
	);
}
