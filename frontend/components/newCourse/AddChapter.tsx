'use client';

import * as React from 'react';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Modal from '@mui/material/Modal';
import { MdOutlineFileUpload } from 'react-icons/md';
import { ChangeEventHandler, useState } from 'react';
import { filesize } from 'filesize';
import Button from '../ui/Button';
import { ChapterProperties } from './Chapters';

interface AddChapterProps {
	open: boolean;
	setOpen: React.Dispatch<React.SetStateAction<boolean>>;
	addChapter: (newChapter: ChapterProperties) => void;
}

export default function AddChapter({ open, setOpen, addChapter }: AddChapterProps) {
	const [selectedFile, setSelectedFile] = useState<File | null>(null);
	const [title, setTitle] = useState<string>("");
	const [description, setDescription] = useState<string>("");

	const handleChange: ChangeEventHandler<HTMLInputElement> = (e) => {
		const file = e.target.files![0] as File;
		setSelectedFile(file);
	};

	const handleClose = () => {
		setOpen((open) => !open);
	};

	const handleFormSubmit:React.FormEventHandler<HTMLFormElement>  = (e) => {
		e.preventDefault();
		const newChapter: ChapterProperties = {
			title,
			description,
			file: selectedFile,
			id: 0
		}

		addChapter(newChapter);
		setOpen(false);
	}

	const style = {
		position: 'absolute' as 'absolute',
		top: '50%',
		left: '50%',
		transform: 'translate(-50%, -50%)',
		maxWidth: '600px',
		width: '80%',
		bgcolor: '#dddddd',
		borderRadius: '20px',
		boxShadow: 24,
		p: 3,
	};

	return (
		<Modal
			open={open}
			onClose={handleClose}
			aria-labelledby="modal-modal-title"
			aria-describedby="modal-modal-description"
		>
			<Box sx={style}>
				<Typography id="modal-modal-title" variant="h6" component="h2">
					Create new Course
				</Typography>
				<form onSubmit={handleFormSubmit}>
					<label htmlFor="title">
						<Typography id="modal-modal-description" sx={{ mt: 2, ml: 2 }}>
							Title
						</Typography>
						<input
							type="text"
							placeholder="Enter Course Title"
							id="title"
							className="mt-2 w-full outline-none px-3 py-1 rounded-full h-[40px]"
							value={title}
							onChange={(e) => setTitle(e.target.value)}
						/>
					</label>
					<label htmlFor="description">
						<Typography id="modal-modal-description" sx={{ mt: 2, ml: 2 }}>
							Description
						</Typography>
						<input
							type="text"
							placeholder="Enter Course Description"
							id="title"
							className="mt-2 w-full outline-none px-3 py-1 rounded-full h-[40px]"
							value={description}
							onChange={(e) => setDescription(e.target.value)}
						/>
					</label>
					<div className="relative flex flex-col gap-2">
						<Typography id="modal-modal-description" sx={{ mt: 2, ml: 2 }}>
							Course Video
						</Typography>
						<label
							htmlFor="video"
							className="relative w-full aspect-[3/1] bg-white rounded-2xl cursor-pointer overflow-hidden"
						>
							<div className="absolute top-1/2 left-1/2 flex flex-col items-center translate-x-[-50%] translate-y-[-50%]">
								<MdOutlineFileUpload className="text-[30px] text-gray-400" />
								<span className="text-gray-400 text-center">
									upload Course Image
								</span>
								{selectedFile && (
									<p>
										selected file is:{' '}
										<span className='text-main-blue'>
											{filesize(selectedFile.size, { standard: 'jedec' })}
										</span>
									</p>
								)}
							</div>
							<input
								type="file"
								name="image"
								id="video"
								className="hidden"
								onChange={handleChange}
							/>
						</label>
					</div>
					<Button className='bg-main-yellow mt-4' type='submit'>submit</Button>
				</form>
			</Box>
		</Modal>
	);
}
