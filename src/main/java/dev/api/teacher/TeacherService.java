package dev.api.teacher;

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

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import dev.api.enums.Category;
import dev.api.enums.Language;
import dev.api.enums.Level;
import dev.api.enums.Status;
import dev.api.exception.ResourceNotFoundException;
import dev.api.model.Categories;
import dev.api.model.Chapter;
import dev.api.model.Courses;
import dev.api.model.Resources;
import dev.api.model.Roles;
import dev.api.model.User;
import dev.api.repository.CategoryRepository;
import dev.api.repository.ChapterRepository;
import dev.api.repository.CoursesRepository;
import dev.api.repository.ResourcesRepository;
import dev.api.repository.UserRepository;
import dev.api.utils.FileStorageService;
import jakarta.servlet.ServletRequest;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class TeacherService {

    private FileStorageService fileStorageService;
    private CoursesRepository coursesRepository;
    private UserRepository userRepository;
    private ResourcesRepository resourcesRepository;
    private ChapterRepository chapterRepository;
    private CategoryRepository categoryRepository;

    public ResponseEntity<?> uploadCourse(MultipartHttpServletRequest request) {

        Integer userId = Integer.parseInt(request.getParameter("userId"));

        User user =  userRepository.findById(userId).orElseThrow(() -> new BadCredentialsException("user not found"));

        // getParameterMap() returns a Map<String, String[]>. This means the key is a
        // String representing the name of the form field, and the value is an array of
        // String containing all the submitted values for that field.
        Set<Map.Entry<String, String[]>> parameterEntries = request.getParameterMap().entrySet();
        // Set<Map.Entry<String, String[]>> create a Set containing pairs of key and
        // value

        List<Chapter> listOfChapters = new ArrayList<>();

        setAllChapters(listOfChapters, parameterEntries);
        setAllResources(listOfChapters, request);

        setCourse(listOfChapters, request, user);
 
        return ResponseEntity.status(201).body(new String());
    }

    private void setCourse(List<Chapter> listOfChapters, MultipartHttpServletRequest request, User user) 
    {
        List<Courses> listCourses = new ArrayList<>();

        Categories category = categoryRepository.findByCategory(request.getParameter("category")).orElseThrow(() -> new RuntimeException("upload course error, category not found"));
        Set<Categories> set = new HashSet<>();
        set.add(category);
        Courses newCourse = Courses.builder()
                .category(set)
                .description(request.getParameter("description"))
                .title(request.getParameter("title"))
                // .duration(request.getParameter("duration"))
                .language(Language.valueOf(request.getParameter("language")))
                .level(Level.valueOf(request.getParameter("level")))
                .price(new BigDecimal(request.getParameter("price")))
                .status(Status.PENDING)
                .isFree( Boolean.parseBoolean(request.getParameter("isFree")))
                .build();

        MultipartFile file = request.getFile("courseImage");

        if (file != null) {
            String filePath;
            try {
                filePath = fileStorageService.storeFileInLocaleStorage(file);
            } catch (IOException e) {
                throw new RuntimeException("error in upload course");
            }
            newCourse.setCourseImage(filePath);
            newCourse.setContentType(file.getContentType());

        } else
            throw new RuntimeException("error in upload course");

            // set course to user
            // set user to course
        if (user.getCourses().isEmpty() ) 
        {
            newCourse.setChapters(listOfChapters);
            listCourses.add(newCourse);
            user.setCourses(listCourses);
            newCourse.setUser(user);
        }
        else{
            newCourse.setUser(user);
            newCourse.setChapters(listOfChapters);
            user.getCourses().add(newCourse);
            listCourses.add(newCourse);
            user.getCourses().addAll(listCourses);
        }
        userRepository.save(user);
    }

    private void setAllResources(List<Chapter> listOfChapters, MultipartHttpServletRequest request) {

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
                        throw new RuntimeException("error in upload course");
                    }
                    Resources resources = Resources.builder()
                            .file(filePath)
                            .contentType(file.getContentType())
                            .build();
                    listOfResources.add(resources);
                } else {
                    throw new RuntimeException("error in upload course");
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

            if (key.contains("videoTitle")) {
                for (int j = 0; j < values.length; j++)
                    listOfChapters.get(i).getResources().get(j).setTitle(values[j]);
                i++;
            }
        }
    }

    private void setAllChapters(List<Chapter> listOfChapters, Set<Entry<String, String[]>> parameterEntries) {

        for (Map.Entry<String, String[]> entry : parameterEntries) {
            String key = entry.getKey();
            String[] values = entry.getValue();

            if (key.contains("chapterTitle")) {
                Chapter chapter = Chapter.builder().title(values[0]).build();
                listOfChapters.add(chapter);
            }
        }
    }
 
    public ResponseEntity<?> updateResourse(MultipartFile resource, String resourceTitle, Integer resourseId, Integer courseId, Authentication authenticatedUser) throws ResourceNotFoundException {

        Courses course = coursesRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("course not found"));

        User user = (User) authenticatedUser.getPrincipal();


        if (user.getUserId() != course.getUser().getUserId()) {
            throw new ResourceNotFoundException("course not found");
        }
        
        Resources updatedResource = resourcesRepository.findById(resourseId).orElseThrow(() -> new ResourceNotFoundException("course not found"));
        String filePath = "";

        if (!resource.isEmpty()) { // or null
            try {
                filePath = fileStorageService.storeFileInLocaleStorage(resource);
            } catch (IOException e) {
                throw new RuntimeException("error update resourse");
            }
            updatedResource.setContentType(resource.getContentType());
            updatedResource.setFile(filePath);
        }
        
        if (!resourceTitle.isEmpty()) {
            updatedResource.setTitle(resourceTitle);   
        }
        
        resourcesRepository.save(updatedResource);
        return ResponseEntity.ok().build();
    }

	public ResponseEntity<?> addResourse(MultipartFile resource, String resourceTitle, Integer chapterId, Integer courseId, Authentication authenticatedUser) throws ResourceNotFoundException {
       
        Courses course = coursesRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("course not found"));

        Chapter chapter = chapterRepository.findById(chapterId).orElseThrow(() -> new ResourceNotFoundException("course not found"));

        User user = (User) authenticatedUser.getPrincipal();


        if (user.getUserId() != course.getUser().getUserId()) {
            throw new ResourceNotFoundException("course not found");
        }

        String filePath = "";

        try {
            filePath = fileStorageService.storeFileInLocaleStorage(resource);
        } catch (IOException e) {
            throw new RuntimeException("error update resourse");
        }
        Resources resources = Resources.builder()
                            .file(filePath)
                            .contentType(resource.getContentType())
                            .title(resourceTitle)
                            .build(); 
        chapter.getResources().add(resources);
        chapterRepository.save(chapter);

        return ResponseEntity.status(201).build();
	}

    public ResponseEntity<?> deleteResource(Integer resourseId, Integer courseId, Authentication authenticatedUser) throws ResourceNotFoundException {
        
        Courses course = coursesRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("course not found"));

        User user = (User) authenticatedUser.getPrincipal();


        if (user.getUserId() != course.getUser().getUserId()) {
            throw new ResourceNotFoundException("course not found");
        }
        Resources resource = resourcesRepository.findById(resourseId).orElseThrow(() -> new ResourceNotFoundException("course not found"));

        resourcesRepository.delete(resource);
        return ResponseEntity.status(204).build();
    }

    public ResponseEntity<?> updateChapter(String chapterTitle, Integer chapterId, Integer courseId, Authentication authenticatedUser) throws ResourceNotFoundException {

        Courses course = coursesRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("course not found"));

        Chapter chapter = chapterRepository.findById(chapterId).orElseThrow(() -> new ResourceNotFoundException("course not found"));

        User user = (User) authenticatedUser.getPrincipal();


        if (user.getUserId() != course.getUser().getUserId()) {
            throw new ResourceNotFoundException("course not found");
        }
        

        chapter.setTitle(chapterTitle);

        chapterRepository.save(chapter);
        return ResponseEntity.status(204).build();
    }

    public ResponseEntity<?> addChapter(String chapterTitle, List<MultipartFile> files, List<String> resourseTitle,Integer courseId,  Authentication authenticatedUser) throws ResourceNotFoundException {
        User user = (User) authenticatedUser.getPrincipal();

        Courses course = coursesRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("course not found"));

        if (user.getUserId() != course.getUser().getUserId()) {
            throw new ResourceNotFoundException("course not found");
        }
        List<Resources> listOfResources = new ArrayList<>();
        Chapter chapter = Chapter.builder().title(chapterTitle).build();
        for (int i = 0; i < files.size(); i++) 
        {
            String filePath = "";
            try {
                filePath = fileStorageService.storeFileInLocaleStorage(files.get(i));
                Resources resource = Resources.builder()
                                    .title(resourseTitle.get(i))
                                    .contentType(files.get(i).getContentType())
                                    .file(filePath)
                                    .build();
                listOfResources.add(resource);
                } catch (IOException e) {
                throw new RuntimeException("error update resourse");
            }
        }
        chapter.setResources(listOfResources);
        course.getChapters().add(chapter);
        coursesRepository.save(course);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> deleteChapter(Integer chapterId, Integer courseId, Authentication authenticatedUser) throws ResourceNotFoundException {

        Courses course = coursesRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("course not found"));

        Chapter chapter = chapterRepository.findById(chapterId).orElseThrow(() -> new ResourceNotFoundException("course not found"));
        User user = (User) authenticatedUser.getPrincipal();


        if (user.getUserId() != course.getUser().getUserId()) {
            throw new ResourceNotFoundException("course not found");
        }
        
        chapterRepository.delete(chapter);

        return ResponseEntity.status(204).build();
    }

    public ResponseEntity<?> updateCourse(ServletRequest request, MultipartFile courseImage, String courseImageTitle, Authentication authenticatedUser) throws NumberFormatException, ResourceNotFoundException {
       
        User user = (User) authenticatedUser.getPrincipal();
        
        Set<Categories> set = new HashSet<>();
        Courses course = coursesRepository.findById( Integer.parseInt(request.getParameter("courseId"))).orElseThrow(() -> new ResourceNotFoundException("course not found"));
        if (user.getUserId() != course.getUser().getUserId()) {
            throw new ResourceNotFoundException("course not found");
        }
        
        String cat = request.getParameter("category");
        if (cat != null) {
            Categories category = categoryRepository.findByCategory(cat).orElseThrow(() -> new RuntimeException("upload course error, category not found"));
            
            set.add(category);
            course.setCategory(set);
        }

        String description= request.getParameter("description");
        String title = request.getParameter("title");
        String isFree = request.getParameter("isFree");
        // // Integer duration =  request.getParameter("duration")_;


        String language = request.getParameter("language");
        String level = request.getParameter("level");
        String price = request.getParameter("price");
 
        if (isFree != null) 
        {
            course.setFree(Boolean.parseBoolean(isFree));    
        }

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
            
        if (courseImage != null) {
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

	public ResponseEntity<?> deleteCourse(Integer courseId) throws ResourceNotFoundException {
        Courses course = coursesRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("course not found"));
        coursesRepository.delete(course);
        return ResponseEntity.ok().build();
	}

    public ResponseEntity<?> switchToStudentRole(Integer userId) {
    
        User user = userRepository.findById(userId).orElseThrow(() -> new BadCredentialsException("user not found"));

        user.setRoles(Roles.STUDENT);

        userRepository.save(user);
        return ResponseEntity.status(204).build();
    }


    


}

 