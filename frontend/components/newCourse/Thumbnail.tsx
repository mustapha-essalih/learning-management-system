'use client'

import Image from "next/image";
import { ChangeEventHandler, useState } from "react";
import { MdOutlineFileUpload } from "react-icons/md";
import CourseInput from "../ui/CourseInput";
import { filesize } from 'filesize';

export default function Thumbnail({setThumbnail, thumbnail}: any) {
	const [selectedImage, setSelectedImage] = useState<File | null>(null);

	const handleChange: ChangeEventHandler<HTMLInputElement> = (e) => {
		const file = e.target.files![0] as File;
		setSelectedImage(file);
		setThumbnail(file);
	}

	return (
		<CourseInput title="course image">
			<label htmlFor="image" className="relative w-full aspect-[3/1] bg-white rounded-2xl cursor-pointer overflow-hidden">
				<div className="absolute top-1/2 left-1/2 flex flex-col items-center translate-x-[-50%] translate-y-[-50%]">
					<MdOutlineFileUpload className="text-[30px] text-gray-400"/>
					<span className="text-gray-400 text-center">upload Course Image</span>
					{selectedImage && (
						<p>
							selected file is:{' '}
							<span className='text-main-blue'>
								{filesize(selectedImage.size, { standard: 'jedec' })}
							</span>
						</p>
					)}
				</div>
				<input type="file" name="image" id="image" className="hidden" onChange={handleChange}/>
			</label>
		</CourseInput>
	);
}