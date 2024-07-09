package api.dev.courses.mapper;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import api.dev.courses.dto.ChapterDTO;
import api.dev.courses.dto.CourseDTO;
import api.dev.courses.dto.InstructorDto;
import api.dev.courses.dto.ResourceDTO;
import api.dev.courses.model.Chapter;
import api.dev.courses.model.Courses;
import api.dev.courses.model.Resources;
import api.dev.instructors.model.Instructors;
import api.dev.students.model.Students;

@Mapper(componentModel = "spring") // Spring integration
public interface CourseMapper { 
 

    List<CourseDTO> courseToCourseDTO(List<Courses> list);

    List<ChapterDTO> chapterToChapterDTO(List<Chapter> chapter);

    List<ResourceDTO> resourceToResourceDTO(List<Resources> resource);
    
    InstructorDto instructorToInstructorDto(Instructors instructor);

    @Mapping(target = "numberOfStudents", expression = "java(course.getStudents().size())")
    @Mapping(target = "createdBy", source  = "instructor.fullName") 
    @Mapping(target = "averageRating", expression = "java(mapAverageRating(course))")
    CourseDTO courseToCourseDTO(Courses course);


     default Double mapAverageRating(Courses course) {
        return course.calculateAverageRating();
    }

}