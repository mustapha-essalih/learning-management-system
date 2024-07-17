'use client'

import { useSortable } from "@dnd-kit/sortable";
import { MdDragIndicator, MdModeEdit, MdDelete } from "react-icons/md";
import { CSS } from "@dnd-kit/utilities";
import { MouseEventHandler } from "react";

interface ResourceProps {
	title: string;
	id: number;
	deleteResource: (id: number) => void;
}

export default function ResourceComponent(props: ResourceProps) {
	const {attributes, listeners, setNodeRef, transform, transition} = useSortable({id: props.id});

	const style = {
		transition,
		transform: CSS.Transform.toString(transform)
	}

	return (
		<div className="w-full px-3 py-2 bg-white flex items-center justify-between rounded-xl" ref={setNodeRef} suppressHydrationWarning style={style}>
			<div className="flex items-center gap-2" {...listeners} {...attributes}>
				<MdDragIndicator />
				<span>{props.title}</span>
			</div>
			<div className="flex items-center gap-1">
				<button>
					<MdModeEdit />
				</button>
				<button onClick={() => props.deleteResource(props.id)}>
					<MdDelete />
				</button>
			</div>
		</div>
	)
}