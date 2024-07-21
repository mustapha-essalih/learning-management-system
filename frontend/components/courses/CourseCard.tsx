'use client';

import { ProfileContext } from '@/lib/userProfileContext/context';
import { Alert, Snackbar } from '@mui/material';
import { loadStripe } from '@stripe/stripe-js';
import React, { FC, useContext, useEffect, useState } from 'react';
import { useCookies } from 'react-cookie';

interface courseCardProps {
  course: any;
}

const checkout = async (price: number, title: string, token: string, setResStatus: any) => {
    const stripe = loadStripe('pk_test_51POm8W2KoHjIbgzwdKvtvfQr8Uubb93kByKSOk94QXWNmqmZW391gyIxC5CV45097IX11dDIjqpcwVQN0dPGGGtI00o3wg817W');


    const response = await fetch('http://localhost:8080/api/students/enroll-course', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({ amount: price, courseName: title })
    }).then(res => res.json()).then(res => {
      console.log(res);
      if (res.status === 200)
        setResStatus({open: true, status: "success", text: res.body})
      else
        setResStatus({open: true, status: "error", text: res.body})
    }
    );
 
    const { sessionId, clientSecret } = await response.json();

      await stripe.then(res => {
        if (res)
          res.redirectToCheckout({sessionId: sessionId})
        }
);

    // Redirect the user 
}

const getImage = async (filePath: string, jwt: string | undefined, courseId: number, contentType: string, setImage: React.Dispatch<React.SetStateAction<string | null>>) => {
  try {
    const response = await fetch(`http://localhost:8080/api/courses/get-resource/${courseId}?filePath=${filePath}&contentType=${contentType}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + jwt,
      },
    });

 
    if (!response.ok) {
      throw new Error('Network response was not ok');
    }

    const blob = await response.blob();
    const reader = new FileReader();
    reader.readAsDataURL(blob);
    reader.onloadend = () => {
      setImage(reader.result as string);
    };
  } catch (error) {
    console.error('Failed to fetch image:', error);
  }
};

const CourseCard: FC<courseCardProps> = ({ course }) => {
  const profileContext = useContext(ProfileContext);
  const [cookies] = useCookies(['token']);

  const [image, setImage] = useState<string | null>(null);

  const [resStatus, setResStatus] = useState({open: false, status: "", text: ""})

  useEffect(() => {
    getImage(course.courseImage, cookies.token, course.courseId, course.contentType, setImage);
  }, [course.courseImage, cookies.token, course.courseId, course.contentType]);

  return (
    <>
    <span key={course.courseId} className="bg-slate-300 shadow-lg hover:scale-105 transition-all w-72 h-72 rounded-lg relative flex flex-col items-center p-2 gap-1">
      {image ? (
        <img src={image} alt="test" className="bg-cover h-[80%] rounded-xl" />
      ) : (
        <p>Loading image...</p>
      )}
      <span>{course.title}</span>
      <div className='flex justify-between items-center w-full px-4' >
      { course.free ?  "free" : <span className='text-sm text-center p-1 font-semibold '>{course.price}$</span> }
      <button className='text-center p-2 bg-gray-500 text-black font-bold rounded text-sm' onClick={() => checkout(course.price, course.title, cookies.token, setResStatus)} >enroll the course</button>

      </div>
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
};




export default CourseCard;
