import { useContext, useState } from "react";
import Chapters from "./Chapters";
import Description from "./Description";
import Thumbnail from "./Thumbnail";
import { NewCourse } from "@/lib/newCourseContext/context";
import Button from "../ui/Button";
import CourseInput from "../ui/CourseInput";
import { ProfileContext } from "@/lib/userProfileContext/context";
import Title from "./Title";
import { Alert, Snackbar } from "@mui/material";


export default function NewCoursePage({ newChapter }: { newChapter: any }) {
    const context = useContext(NewCourse);
    const profileContext = useContext(ProfileContext);

    if (!context || !profileContext)
        throw new Error('no Context');

    const { course, setCourse } = context;
    const { profile } = profileContext;

    const setTitle = (title: string) => {
        setCourse((old) => ({ ...old, title }));
    }

    const setDescription = (description: string) => {
        setCourse((old) => ({ ...old, description }));
    }

    const setIsFree = (isFree: boolean) => {
        setCourse((old) => ({ ...old, isFree }));
    }

    const setThumbnail = (thumbnail: File | null) => {
        setCourse((old) => ({ ...old, thumbnail }));
    }

    const setPrice = (price: number) => {
        setCourse((old) => ({ ...old, price }));
    }

    const setLanguage = (language: "ENGLISH" | "SPANISH" | "FRENCH") => {
        setCourse((old) => ({ ...old, language }));
    }

    const setLevel = (level: "BEGINNERS" | "INTERMEDIATE" | "EXPERT") => {
        setCourse((old) => ({ ...old, level }));
    }

    const setCategory = (category: string) => {
        setCourse((old) => ({ ...old, category }));
    }

    const getFormData = async () => {
        const formData = new FormData();
        formData.append("title", course?.title || '');
        formData.append("description", course?.description || '');
        formData.append("price", `${course?.price || 0}`);
        formData.append("language", course?.language || '');
        formData.append("level", course?.level || '');
        formData.append("isFree", `${course?.isFree || false}`);
        formData.append("category", course?.category || '');
        formData.append("courseImage", course?.thumbnail || new Blob, course?.thumbnail?.name || 'course-image.jpg');
		let i = 0;
        for (; i < (course?.chapters || []).length; i++) {
            const chapter = course!.chapters[i];
            formData.append(`chapters[${i}].chapterTitle`, chapter.title);

            for (let j = 0; j < (chapter.resources || []).length; j++) {

                const resource = chapter.resources[j];
                formData.append(`chapters[${i}].resources[${i}].resourceTitle`, resource.title);

                if (resource.file) {
					const buffer = Buffer.from(await resource.file.arrayBuffer());
                    const blob = new Blob([buffer]);
                    formData.append(`chapters[${i}].resources[${i}].videos`, resource.file);
                    console.log(i);
                }
            }
        }

        return formData;
    }

    const [resStatus, setResStatus] = useState({open: false, status: "", text: ""})

    const submitCourse = async () => {
        const formData = await getFormData();
        const res = await fetch('http://localhost:8080/api/instructor/upload-course', {
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + profile?.jwt
            },
            body: formData
        });
        console.log(profile?.jwt);
        console.log(res);
        if (res.status === 201)
            setResStatus({open: true, status: "success", text: "Course uploaded successfully. The status is now pending and it will be reviewed by a manager. Once verified and published, students will be able to view and enroll in the course."})
        else
            setResStatus({open: true, status: "error", text: "Error when uploading course"})
    }

    const [_isFree, _setIsFree] = useState(false)

    return (
        <>
            <div className="flex gap-5">
                <div className="flex flex-col w-1/2 gap-3">
                    <Title setTitle={setTitle} title={course?.title || ''} />
                    <Description setDescription={setDescription} description={course?.description || ''} />
                    <Thumbnail setThumbnail={setThumbnail} />
                </div>
                <div className="flex flex-col w-1/2 gap-3">
                    <Chapters newChapter={newChapter} />
                    <CourseInput title="price, language and level">
                        {!_isFree && <div>
                            <label htmlFor="price">price</label>
                            <input type="text" value={course?.price} id="price" placeholder="Enter Course Price" className="w-full outline-none px-2 py-1 rounded-full h-[40px]" onChange={(e) => setPrice(Number(e.target.value))} />
                        </div>}
                        <div>
                            <label htmlFor="language">language</label>
                            <select id="language" className="w-full outline-none px-2 py-1 rounded-full h-[40px]" onChange={(e) => setLanguage(e.target.value as ("ENGLISH" | "SPANISH" | "FRENCH"))}>
                                <option value="ENGLISH">english</option>
                                <option value="SPANISH">spanish</option>
                                <option value="FRENCH">french</option>
                            </select>
                        </div>
                        <div>
                            <label htmlFor="level">level</label>
                            <select id="level" className="w-full outline-none px-2 py-1 rounded-full h-[40px]" onChange={(e) => setLevel(e.target.value as ("BEGINNERS" | "INTERMEDIATE" | "EXPERT"))}>
                                <option value="BEGINNERS">beginner</option>
                                <option value="INTERMEDIATE">intermediate</option>
                                <option value="EXPERT">expert</option>
                            </select>
                        </div>
                        <div>
                            <label htmlFor="category">category</label>
                            <select id="category" className="w-full outline-none px-2 py-1 rounded-full h-[40px]" onChange={(e) => setCategory(e.target.value)}>
                                <option value="it">it</option>
                                <option value="health">health</option>
                                <option value="lifestyle">lifestyle</option>
                            </select>
                        </div>
                        <div className="flex items-center gap-3">
                            <input type="checkbox" id="free" className="w-5 h-5" onChange={(e) => {setIsFree(e.target.checked); _setIsFree(e.target.checked)}} />
                            <label htmlFor="free">is Free</label>
                        </div>
                    </CourseInput>
                </div>
            </div>
            <span className="w-full flex items-center justify-center">
                <Button className="bg-main-blue mt-6 mb-2" onClick={submitCourse}>submit</Button>
            </span>
            <Snackbar open={resStatus.open} autoHideDuration={5000} onClose={() => setResStatus({open: false, status: "", text: ""})}>
                <Alert
                    onClose={() => setResStatus({open: false, status: "", text: ""})}
                    severity={resStatus.status}
                    variant="filled"
                    sx={{ width: '100%' }}
                >
                    {resStatus.text}
                </Alert>
            </Snackbar>
        </>
    );
}
