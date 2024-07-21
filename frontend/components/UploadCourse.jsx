import React, { useState } from 'react';
import axios from 'axios';

const UploadCourse = () => {
  const [courseData, setCourseData] = useState({
    title: '',
    description: '',
    chapters: [{ title: '', resources: [{ title: '', file: null }] }],
  });

  const handleCourseChange = (e) => {
    setCourseData({ ...courseData, [e.target.name]: e.target.value });
  };

  const handleChapterChange = (index, e) => {
    const newChapters = [...courseData.chapters];
    newChapters[index][e.target.name] = e.target.value;
    setCourseData({ ...courseData, chapters: newChapters });
  };

  const handleResourceChange = (chapterIndex, resourceIndex, e) => {
    const newChapters = [...courseData.chapters];
    newChapters[chapterIndex].resources[resourceIndex][e.target.name] = e.target.value;
    if (e.target.name === 'file') {
      newChapters[chapterIndex].resources[resourceIndex][e.target.name] = e.target.files[0];
    }
    setCourseData({ ...courseData, chapters: newChapters });
  };

  const addChapter = () => {
    setCourseData({
      ...courseData,
      chapters: [...courseData.chapters, { title: '', resources: [{ title: '', file: null }] }],
    });
  };

  const addResource = (chapterIndex) => {
    const newChapters = [...courseData.chapters];
    newChapters[chapterIndex].resources.push({ title: '', file: null });
    setCourseData({ ...courseData, chapters: newChapters });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append('title', courseData.title);
    formData.append('description', courseData.description);
    courseData.chapters.forEach((chapter, index) => {
      formData.append(`chapters[${index}].title`, chapter.title);
      chapter.resources.forEach((resource, resIndex) => {
        formData.append(`chapters[${index}].resources[${resIndex}].title`, resource.title);
        formData.append(`chapters[${index}].resources[${resIndex}].file`, resource.file);
      });
    });

    try {
      await axios.post('/api/uploadCourse', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
          Authorization: `Bearer ${localStorage.getItem('token')}`,
        },
      });
      // handle success
    } catch (error) {
      // handle error
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <div>
        <label>Course Title:</label>
        <input type="text" name="title" value={courseData.title} onChange={handleCourseChange} required />
      </div>
      <div>
        <label>Course Description:</label>
        <textarea name="description" value={courseData.description} onChange={handleCourseChange} required></textarea>
      </div>
      {courseData.chapters.map((chapter, chapterIndex) => (
        <div key={chapterIndex}>
          <h3>Chapter {chapterIndex + 1}</h3>
          <div>
            <label>Chapter Title:</label>
            <input type="text" name="title" value={chapter.title} onChange={(e) => handleChapterChange(chapterIndex, e)} required />
          </div>
          {chapter.resources.map((resource, resourceIndex) => (
            <div key={resourceIndex}>
              <label>Resource Title:</label>
              <input type="text" name="title" value={resource.title} onChange={(e) => handleResourceChange(chapterIndex, resourceIndex, e)} required />
              <label>Resource File:</label>
              <input type="file" name="file" onChange={(e) => handleResourceChange(chapterIndex, resourceIndex, e)} required />
            </div>
          ))}
          <button type="button" onClick={() => addResource(chapterIndex)}>Add Resource</button>
        </div>
      ))}
      <button type="button" onClick={addChapter}>Add Chapter</button>
      <button type="submit">Submit</button>
    </form>
  );
};

export default UploadCourse;
