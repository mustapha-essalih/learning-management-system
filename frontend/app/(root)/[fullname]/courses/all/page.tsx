'use client'

import CourseCard from '@/components/courses/CourseCard';
import { ProfileContext } from '@/lib/userProfileContext/context';
import React, { useContext, useEffect, useState } from 'react'
import { useCookies } from 'react-cookie';

const Page = () => {

    const profileContext = useContext(ProfileContext);
    
    const [allCourses, setAllCourses] = useState<any[]>([])
    
    const [cookies] = useCookies(['token']);
    useEffect(() => {
        
        try {
			fetch('http://localhost:8080/api/courses/get-courses', {
                method: 'GET',
				headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${cookies.token}`
                }
			}).then(res =>  res.json())
            .then(res => setAllCourses(res))
		} catch(e) {
			// console.log(e);
		}
    }, [])
    
  return (
    <div className='w-full h-max relative py-5'>
        <div className='w-full h-full rounded-xl shadow-2xl bg-gray-100 p-6 flex justify-center'>
        {allCourses.length ? 

            <div className='grid grid-cols-1 md:grid-cols-2 xl:grid-cols-4 2xl:grid-cols-5 gap-10 relative p-10 overflow-y-scroll justify-center'>
                {
                    allCourses.map(course => 
                        <CourseCard key={course.courseId} course={course} />
                    )
                    }
                
            </div>
        :    
        <div className='w-full font-bold flex items-center justify-center'>No courses available</div>        
        }
        </div>
    </div>
  )
}

export default Page