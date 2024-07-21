package api.dev.instructors;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import api.dev.courses.dto.CourseDTO;
import api.dev.courses.mapper.CourseMapper;
import api.dev.courses.model.Categories;
import api.dev.courses.model.Chapter;
import api.dev.courses.model.Courses;
import api.dev.courses.model.Resources;
import api.dev.courses.repository.CartRepository;
import api.dev.courses.repository.CategoryRepository;
import api.dev.courses.repository.ChapterRepository;
import api.dev.courses.repository.CoursesRepository;
import api.dev.courses.repository.FeedbackRepository;
import api.dev.courses.repository.ResourcesRepository;
import api.dev.enums.Language;
import api.dev.enums.Level;
import api.dev.enums.Status;
import api.dev.exceptions.ResourceNotFoundException;
import api.dev.instructors.dto.CourseTitleDTO;
import api.dev.instructors.dto.CoursesAnalyticsDTO;
import api.dev.instructors.dto.InstructorAnalyticsDTO;
import api.dev.instructors.dto.request.UpdateChapterTitleDto;
import api.dev.instructors.mapper.InstructorMapper;
import api.dev.instructors.model.Instructors;
import api.dev.instructors.repository.InstructorsRepository;
import api.dev.students.StudentsService;
import api.dev.students.model.Students;
import api.dev.students.repository.StudentsRepository;
import api.dev.utils.FileStorageService;
import jakarta.servlet.ServletRequest;




@Service
public class InstructorsService {

    private FileStorageService fileStorageService;
    private CoursesRepository coursesRepository;
    private InstructorsRepository instructorRepository;
    private ResourcesRepository resourcesRepository;
    private ChapterRepository chapterRepository;
    private CategoryRepository categoryRepository;
    private ResourcesRepository resourceRepository; 
    private CourseMapper courseMapper;
    private InstructorMapper instructorMapper;
    private FeedbackRepository feedbackRepository;
    private StudentsService studentsService;

    public InstructorsService(FileStorageService fileStorageService, CoursesRepository coursesRepository,
            InstructorsRepository instructorRepository, ResourcesRepository resourcesRepository,
            ChapterRepository chapterRepository, CategoryRepository categoryRepository,
            ResourcesRepository resourceRepository, CourseMapper courseMapper, InstructorMapper instructorMapper,
            FeedbackRepository feedbackRepository, StudentsRepository studentsRepository, CartRepository cartRepository,
            StudentsService studentsService) {
        this.fileStorageService = fileStorageService;
        this.coursesRepository = coursesRepository;
        this.instructorRepository = instructorRepository;
        this.resourcesRepository = resourcesRepository;
        this.chapterRepository = chapterRepository;
        this.categoryRepository = categoryRepository;
        this.resourceRepository = resourceRepository;
        this.courseMapper = courseMapper;
        this.instructorMapper = instructorMapper;
        this.feedbackRepository = feedbackRepository;
        this.studentsService = studentsService;
    }


    public ResponseEntity<?> uploadCourse(MultipartHttpServletRequest request, String email) throws ResourceNotFoundException, BadRequestException {
      
        Instructors instructor =  instructorRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("instructor not found"));

        Set<Map.Entry<String, String[]>> parameterEntries = request.getParameterMap().entrySet();
    
        List<Chapter> listOfChapters = new ArrayList<>();
        if (request.getParameter("description") == null || request.getParameter("title") == null) {
            return ResponseEntity.badRequest().body("error in upload course");
        }

        Courses newCourses = createNewCourse(request, instructor);

        setAllChapters(listOfChapters, parameterEntries, newCourses);
        
        if(setAllResources(listOfChapters, request) == null)
            return ResponseEntity.badRequest().body("error in upload course");

        if(saveCourse(listOfChapters,request,instructor,newCourses) == null)
            return ResponseEntity.badRequest().body("error in upload course");
 
        return ResponseEntity.status(HttpStatus.CREATED).body("Course uploaded successfully. The status is now pending and it will be reviewed by a manager. Once verified and published, students will be able to view and enroll in the course.");
    }
 

    private Courses createNewCourse(MultipartHttpServletRequest request, Instructors instructor) 
    {
        Categories category = categoryRepository.findByCategory(request.getParameter("category")).orElseThrow(() -> new RuntimeException("upload course error, category not found"));
        Set<Categories> set = new HashSet<>();
        set.add(category);
 
        Boolean isFree = Boolean.parseBoolean(request.getParameter("isFree"));
        BigDecimal price;
        if (isFree) 
        {
            price = null;
        }
        else
            price = new BigDecimal(request.getParameter("price"));

        Courses newCourse = new Courses(set,request.getParameter("description"),request.getParameter("title"),
        Language.valueOf(request.getParameter("language")), Level.valueOf(request.getParameter("level")),
        price, Status.PENDING,  isFree);
                 
        return newCourse;
    }

    private void setAllChapters(List<Chapter> listOfChapters, Set<Entry<String, String[]>> parameterEntries, Courses newCourses) {

        for (Map.Entry<String, String[]> entry : parameterEntries) {
            String key = entry.getKey();
            String[] values = entry.getValue();

            if (key.contains("chapterTitle")) {
                Chapter chapter = new Chapter(values[0]);
                chapter.setCourse(newCourses);
                listOfChapters.add(chapter);
            }
        }
    }


    private String setAllResources(List<Chapter> listOfChapters, MultipartHttpServletRequest request) throws BadRequestException {

        int i = 0;

        Iterator<String> fileNames = request.getFileNames();

        while (fileNames.hasNext()) {
            String fileName = fileNames.next();
            List<MultipartFile> files = request.getFiles(fileName);
            List<Resources> listOfResources = new ArrayList<>();
            String filePath;
            if (fileName.equals("courseImage"))
                continue;
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    try {
                        filePath = fileStorageService.storeFileInLocaleStorage(file);
                    } catch (IOException e) {
                        return null;
                    }
                    Resources resources = new Resources(filePath, file.getContentType());
                    resources.setChapter(listOfChapters.get(i));
                    listOfResources.add(resources);
                } else {
                    return null;
                }
            }
            listOfChapters.get(i).setResources(listOfResources);
            i++;
        }
        Set<Map.Entry<String, String[]>> parameterEntries = request.getParameterMap().entrySet();

        i = 0;
        for (Map.Entry<String, String[]> entry : parameterEntries) {
            String key = entry.getKey();
            String[] values = entry.getValue();

            if (key.contains("resourceTitle")) {
                for (int j = 0; j < values.length; j++)
                {
                    listOfChapters.get(i).getResources().get(j).setTitle(values[j]);
                }
                i++;
            }
        }
        return "";
    }

    private String saveCourse(List<Chapter> listOfChapters, MultipartHttpServletRequest request, Instructors instructor, Courses newCourse) throws BadRequestException 
    {
        List<Courses> listCourses = new ArrayList<>();
    
        MultipartFile file = request.getFile("courseImage");

        if (file != null && !file.isEmpty()) {
            String filePath;
            try {
                filePath = fileStorageService.storeFileInLocaleStorage(file);
            } catch (IOException e) {
                return null;
            }
            newCourse.setCourseImage(filePath);
            newCourse.setContentType(file.getContentType());

        } 
        else
            return null;

        if (instructor.getCourses().isEmpty() ) 
        {
            newCourse.setChapters(listOfChapters);
            listCourses.add(newCourse);
            instructor.setCourses(listCourses);
            newCourse.setInstructor(instructor);
        }
        else{
            newCourse.setInstructor(instructor);
            newCourse.setChapters(listOfChapters);
            instructor.getCourses().add(newCourse);
            listCourses.add(newCourse);
            instructor.getCourses().addAll(listCourses);
        }
        instructorRepository.save(instructor);
        return "";
    }


    public ResponseEntity<?> updateCourse(Integer courseId, ServletRequest request, MultipartFile courseImage, Principal principal) throws NumberFormatException, ResourceNotFoundException, BadRequestException 
    {
        Instructors user = instructorRepository.findByEmail(principal.getName()).get();
        
        Courses course = coursesRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("course not found"));
        
        if (user.getUserId() != course.getInstructor().getUserId()) 
            return ResponseEntity.badRequest().body("course is not related with this instructor");
        
        Set<Categories> set = new HashSet<>();
        String cat = request.getParameter("category");
        if (cat != null) {
            Categories category = categoryRepository.findByCategory(cat).orElseThrow(() -> new RuntimeException("upload course error, category not found"));
            
            set.add(category);
            course.setCategory(set);
        }

        String description= request.getParameter("description");
        String title = request.getParameter("title");
        String isFree = request.getParameter("isFree");

        String language = request.getParameter("language");
        String level = request.getParameter("level");
        String price = request.getParameter("price");
        String courseImageTitle = request.getParameter("courseImageTitle");

        if (isFree != null) 
            course.setFree(Boolean.parseBoolean(isFree));    

        if (description != null) 
            course.setDescription(description);
            
        if (title != null) 
            course.setTitle(title);
            
        if(language != null)
            course.setLanguage(Language.valueOf(language));
        if (level != null) 
            course.setLevel(Level.valueOf(level));
        
        if (price != null) 
            course.setPrice(new BigDecimal(price));

        course.setStatus(Status.PENDING);
        
        if (courseImageTitle != null) 
            course.setCourseImage(courseImageTitle);
            
        if (courseImage != null && !courseImage.isEmpty()) {
            String filePath;
            try {
                filePath = fileStorageService.storeFileInLocaleStorage(courseImage);
            } catch (IOException e) {
                throw new RuntimeException("error in upload course");
            }
            course.setCourseImage(filePath);
            course.setContentType(courseImage.getContentType());

        }
        coursesRepository.save(course);
        return ResponseEntity.status(204).build();
    }

    
    public ResponseEntity<?> deleteCourse(Integer courseId, Principal principal) throws ResourceNotFoundException {
    
        Instructors instructor = instructorRepository.findByEmail(principal.getName()).get();

        Courses course = coursesRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("course not found"));
        
        if (instructor.getUserId() != course.getInstructor().getUserId()) 
            return ResponseEntity.badRequest().body("course is not related with this instructor");
       

        instructorRepository.deleteCourseCategoriesJoinTableByCourseId(courseId);
        instructorRepository.deleteCartCoursesJoinTableByCourseId(courseId);
        instructorRepository.deleteStudentCoursesJoinTableByCourseId(courseId);
        coursesRepository.delete(course);
        return ResponseEntity.status(204).build();
    }


    public ResponseEntity<?> addChapter(String chapterTitle, List<MultipartFile> files, List<String> resourseTitle,Integer courseId,  Principal principal) throws ResourceNotFoundException {

        Instructors user = instructorRepository.findByEmail(principal.getName()).get();

        Courses course = coursesRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("course not found"));

        if (user.getUserId() != course.getInstructor().getUserId()) 
            return ResponseEntity.badRequest().body("course is not related with this instructor");
        
        List<Resources> listOfResources = new ArrayList<>();
        Chapter chapter = new Chapter(chapterTitle);
        chapter.setCourse(course);
        for (int i = 0; i < files.size(); i++) 
        {
            String filePath = "";
            try {
                if (files.get(i).isEmpty()) {
                    return ResponseEntity.badRequest().body("error update course");
                }
                filePath = fileStorageService.storeFileInLocaleStorage(files.get(i));
                Resources resource = new Resources(filePath, files.get(i).getContentType(), resourseTitle.get(i));
                resource.setChapter(chapter);
                listOfResources.add(resource);
                } 
            catch (IOException e) {
                return ResponseEntity.badRequest().body("error update course");
            }
        }
        chapter.setResources(listOfResources);
        course.getChapters().add(chapter);
        coursesRepository.save(course);
        return ResponseEntity.status(204).build();
    }

    
    public ResponseEntity<?> updateChapter(UpdateChapterTitleDto dto, Principal principal) throws ResourceNotFoundException {

        Chapter chapter = chapterRepository.findById(dto.getChapterId()).orElseThrow(() -> new ResourceNotFoundException("course not found"));

        Instructors user = instructorRepository.findByEmail(principal.getName()).get();
        
        if(!user.getCourses().stream().anyMatch(course -> course.getChapters().contains(chapter)))
            return ResponseEntity.badRequest().body("course is not related with this instructor");

        chapter.setTitle(dto.getChapterTitle());
        chapterRepository.save(chapter);
        return ResponseEntity.status(204).build();
    }

    public ResponseEntity<?> deleteChapter(Integer chapterId, Principal principal) throws ResourceNotFoundException {

        Chapter chapter = chapterRepository.findById(chapterId).orElseThrow(() -> new ResourceNotFoundException("course not found"));
         
        Instructors user = instructorRepository.findByEmail(principal.getName()).get();
        if(!chapter.getCourse().getInstructor().equals(user))
            return ResponseEntity.badRequest().body("course is not related with this instructor");

        chapterRepository.delete(chapter);

        return ResponseEntity.status(204).build();
    }

    public ResponseEntity<?> addResourse(MultipartFile file, String resourceTitle, Integer chapterId, Principal principal) throws ResourceNotFoundException {
       
        Chapter chapter = chapterRepository.findById(chapterId).orElseThrow(() -> new ResourceNotFoundException("course not found"));
        Instructors user = instructorRepository.findByEmail(principal.getName()).get();


        if(!chapter.getCourse().getInstructor().equals(user))
            return ResponseEntity.badRequest().body("course is not related with this instructor");

        String filePath = "";

        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("error add new resource");
            }
            filePath = fileStorageService.storeFileInLocaleStorage(file);
        } catch (IOException e) {
            throw new RuntimeException("error update resourse");
        }
        Resources newRsource = new Resources(filePath, file.getContentType(), resourceTitle);
        newRsource.setChapter(chapter);
        chapter.getResources().add(newRsource);
        chapterRepository.save(chapter);

        return ResponseEntity.status(204).build();
	}

    public ResponseEntity<?> updateResourse(MultipartFile file, String resourceTitle, Integer resourseId,  Principal principal) throws ResourceNotFoundException {

        Resources resource = resourceRepository.findById(resourseId).orElseThrow(() -> new ResourceNotFoundException("course not found"));
         
        Instructors user = instructorRepository.findByEmail(principal.getName()).get();

        if(!user.getUserId().equals(resource.getChapter().getCourse().getInstructor().getUserId()))
            return ResponseEntity.badRequest().body("course is not related with this instructor");

        
        String filePath = "";

        if (file != null) 
        {
            try {
                filePath = fileStorageService.storeFileInLocaleStorage(file);
            } catch (IOException e) {
                throw new RuntimeException("error update resourse");
            }
            resource.setContentType(resource.getContentType());
            resource.setFile(filePath);
        }
        
        if (resourceTitle != null) 
            resource.setTitle(resourceTitle);   
        
        
        resourcesRepository.save(resource);
        return ResponseEntity.status(204).build();
    }

    public ResponseEntity<?> deleteResource(Integer resourseId, Principal principal) throws ResourceNotFoundException {
 
        Resources resource = resourceRepository.findById(resourseId).orElseThrow(() -> new ResourceNotFoundException("course not found"));
         
        Instructors user = instructorRepository.findByEmail(principal.getName()).get();

        if(!user.getUserId().equals(resource.getChapter().getCourse().getInstructor().getUserId()))
            return ResponseEntity.badRequest().body("course is not related with this instructor");

        resourcesRepository.delete(resource);
        return ResponseEntity.status(204).build();
    }


    public ResponseEntity<?> getCourses(String email) 
    {
        Instructors instructor = instructorRepository.findByEmail(email).get();
        
        List<CourseDTO> courses = courseMapper.coursesToCourseDTOsWithDetails(instructor.getCourses(), true);
        
        return ResponseEntity.ok().body(courses);
    }


	public ResponseEntity<?> getCourseAnalytics(Integer courseId, String email) throws ResourceNotFoundException {
        
        Instructors instructor = instructorRepository.findByEmail(email).get(); 
        Courses course = coursesRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("course not found"));

        if (!instructor.getCourses().contains(course)) 
            return ResponseEntity.badRequest().body("course is not related with this instructor");

        CoursesAnalyticsDTO coursesAnalyticsDTO = instructorMapper.toCoursesAnalyticsDTO(course);
        coursesAnalyticsDTO.setTotalRevenue(coursesRepository.calculateCourseRevenue(courseId));
        return ResponseEntity.ok(coursesAnalyticsDTO);
	}


    public ResponseEntity<InstructorAnalyticsDTO> getInstructorAnalytics(String email) {
        
        Instructors instructor = instructorRepository.findByEmail(email).get(); 

        BigDecimal totalRevenue = coursesRepository.calculateTotalRevenueForInstructor(instructor.getUserId());
        int totalStudents = coursesRepository.countTotalStudentsForInstructor(instructor.getUserId());
        int totalFeedback = feedbackRepository.countTotalFeedbackForInstructor(instructor.getUserId());
        List<CourseTitleDTO> coursesTitle = instructorMapper.toCourseTitleDTOList(instructor.getCourses());
        InstructorAnalyticsDTO dto =  new InstructorAnalyticsDTO(totalRevenue, totalStudents, totalFeedback, coursesTitle);
        
        return ResponseEntity.ok(dto);
    }


    public ResponseEntity<Void> updateInfos(String newEmail, String fullName, String bio, String email) 
    {
        Instructors instructor = instructorRepository.findByEmail(email).get(); 
        if (newEmail != null) 
            instructor.setEmail(email);
        if (fullName != null) 
            instructor.setFullName(fullName);
        if (bio != null) 
            instructor.setBio(bio);
        
        instructorRepository.save(instructor);
        return ResponseEntity.noContent().build();
    }




}
