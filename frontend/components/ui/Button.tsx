import * as React from 'react';

interface ButtonProps {
	className?: string;
	children: React.ReactNode;
	type?: "button" | "submit" | "reset" | undefined;
	onClick?: React.MouseEventHandler<HTMLButtonElement> | undefined;
}

export default function Button(props: ButtonProps) {
	return (
		<button type={props.type} onClick={props.onClick} className={`px-3 py-2 text-white font-semibold rounded-full capitalize ${props.className}`}>
			{
				props.children
			}
		</button>
	);
}