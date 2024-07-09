package api.dev.courses.mapper;

import java.util.List;
import java.util.stream.Collectors;

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

@Mapper(componentModel = "spring")
public interface CourseMapper {

    @Mapping(target = "numberOfStudents", expression = "java(course.getStudents().size())")
    @Mapping(target = "createdBy", source = "instructor.fullName") 
    @Mapping(target = "averageRating", expression = "java(mapAverageRating(course))")
    CourseDTO courseToCourseDTO(Courses course);

    ChapterDTO chapterToChapterDTO(Chapter chapter);


    @Mapping(target = "file", ignore = true)
    @Mapping(target = "contentType", ignore = true)
    ResourceDTO resourceToResourceDTO(Resources resource);

    default CourseDTO courseToCourseDTOWithDetails(Courses course, boolean includeFile) {


        CourseDTO dto = courseToCourseDTO(course);
        if (dto.getChapters() != null) {
            dto.setChapters(course.getChapters().stream()
                .map(chapter -> chapterToChapterDTOWithDetails(chapter, includeFile))
                .collect(Collectors.toList()));
        }
        return dto;
    }

    default ChapterDTO chapterToChapterDTOWithDetails(Chapter chapter, boolean includeFile) {
        ChapterDTO dto = chapterToChapterDTO(chapter);
        if (dto.getResources() != null) {

            dto.setResources(chapter.getResources().stream()
                .map(resource -> resourceToResourceDTOWithFile(resource, includeFile))
                .collect(Collectors.toList()));
        }
        return dto;
    }

    default ResourceDTO resourceToResourceDTOWithFile(Resources resource, boolean includeFile) {
        ResourceDTO dto = resourceToResourceDTO(resource);
        if (includeFile == true) {
            dto.setFile(resource.getFile());
            dto.setContentType(resource.getContentType());
        }
        return dto;
    }


    default List<CourseDTO> coursesToCourseDTOsWithDetails(List<Courses> courses, boolean includeFile) {
        return courses.stream()
            .map(course -> courseToCourseDTOWithDetails(course, includeFile))
            .collect(Collectors.toList());
    }

    default Double mapAverageRating(Courses course) {
        return course.calculateAverageRating();
    }
}