package api.dev.courses.mapper;

import org.mapstruct.Mapper;

import api.dev.courses.dto.ChapterDTO;
import api.dev.courses.dto.CourseDTO;
import api.dev.courses.dto.ResourceDTO;
import api.dev.courses.model.Chapter;
import api.dev.courses.model.Courses;
import api.dev.courses.model.Resources;

@Mapper(componentModel = "spring") // Spring integration
public interface CourseMapper { 
 
    CourseDTO courseToCourseDTO(Courses course);

    ChapterDTO chapterToChapterDTO(Chapter chapter);

    ResourceDTO resourceToResourceDTO(Resources resource);
}