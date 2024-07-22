package api.dev.instructors.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import api.dev.courses.model.Courses;
import api.dev.instructors.dto.CourseTitleDTO;
import api.dev.instructors.dto.CoursesAnalyticsDTO;
import api.dev.instructors.model.Instructors;

@Mapper(componentModel = "spring")
public interface InstructorMapper {
 

    @Mapping(target = "numberOfStudents", expression = "java(course.getStudents().size())")
    @Mapping(target = "averageRating", expression = "java(mapAverageRating(course))")  
    @Mapping(target = "totalFeedbacks" , expression = "java(course.getFeedback().size())")
    CoursesAnalyticsDTO toCoursesAnalyticsDTO(Courses course);

    @Mapping(target = "title" , source = "title")
    List<CourseTitleDTO> toCourseTitleDTOList(List<Courses> courses);

    default Double mapAverageRating(Courses course) {
        return course.calculateAverageRating();
    }

}
