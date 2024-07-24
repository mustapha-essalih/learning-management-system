
#  A Full-Stack Comprehensive Online Learning Management System (Udemy Clone)

- a comprehensive web application designed to enhance the online education experience. This platform empowers students to enroll in courses, enables instructors to create and manage their courses, and provides administrators with tools to oversee and manage the entire platform. Here’s a quick overview of the project and its features:

## Tech Stack
- **Backend:** Spring Boot
- **Database:** PostgreSQL
- **Frontend:** Nextjs (for registration and login, If the user is a student, it will display all courses for enrollment. If an instructor, it redirects to the course upload page after login)
- **Authentication:** JWT (JSON Web Token)
- **File Storage:** Local storage
- **Development Tools:** Git, Maven, Docker
- **Database Interaction:** JPA & Hibernate

## Key Features
### User Roles
1. **Admin:** The super user with full access to the platform. Admins can manage instructors, managers, students, and categories, and view platform analytics.
   - **Admin Login:** Username: `admin`, Password: `admin`
   - Admins can create managers and perform CRUD operations on categories.
2. **Manager:** Responsible for verifying courses and managing course statuses.
   - Managers change the status of courses uploaded from pending to rejected or published.
3. **Instructor:** Can upload, update, and delete courses, and view analytics of their courses.
   - Instructors upload the course, each course has many chapters (sections), each chapter has many resources which can be videos, images, or files. After uploading, the course status will be pending and should be reviewed by a manager to be published. If published, students can see the course and enroll in it.
4. **Student:** Can enroll in courses, pay for enrollment, access course content, and find courses.

### Core Functionalities
#### User Authentication and Authorization
- Secure registration and login using JWT and bcrypt for password hashing.
- Role-based access control to ensure appropriate access for different users.

#### Course Management
- **Instructors:**
  - Create, update, and delete courses.
  - Organize courses into chapters (sections), each containing multiple resources such as videos and documents.
  - Upload and manage videos and documents for each section.
  - View detailed analytics for their courses.
- **Admins:**
  - Manage all courses on the platform.
  - Oversee platform analytics for comprehensive insights.

#### Content Upload and Management
- Efficiently upload and manage videos and documents.
- Dynamic rendering of videos and documents ensures smooth access.

#### Enrollment and Access
- Students can easily enroll in courses and gain access to the content.
- Access control ensures that only enrolled students can view course resources.

#### Analytics and Reporting
- Instructors can view detailed analytics for their courses.
- Admins have access to comprehensive platform analytics.

### Additional Features
- **Stripe Integration:** Facilitates secure payment processing for course enrollments.
- **API Documentation:** Utilized Swagger for comprehensive API documentation.
- **Error Handling:** Implemented robust error handling for improved reliability.
- **Shopping Cart:** Features for adding, removing, and updating items in the cart.

## Installation and Setup
### Prerequisites
- Java 11 or higher
- Node.js and npm
- PostgreSQL
- Docker (optional, for containerization)

### Backend Setup
1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/online-learning-management-system.git
   ```
2. Navigate to the backend directory:
   ```bash
   cd online-learning-management-system/backend
   ```
3. Install dependencies and build the project:
   ```bash
   ./mvnw clean install
   ```
4. Configure the PostgreSQL database in `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/yourdbname
   spring.datasource.username=yourusername
   spring.datasource.password=yourpassword
   ```
5. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

### Frontend Setup
1. Navigate to the frontend directory:
   ```bash
   cd ../frontend
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Start the React application:
   ```bash
   npm run dev
   ```
 
## API Documentation
Swagger is used for API documentation. Once the backend is running, you can access the API documentation at:
```
http://localhost:8080/swagger-ui.html
```
 
## How the Project Works
- **Admin:** The admin user (username: `admin`, password: `admin`) creates managers and manages categories. Admins have full control over the platform and can view analytics.
- **Manager:** Managers are responsible for verifying the courses uploaded by instructors. They change the course status from pending to rejected or published.
- **Instructor:** Instructors upload courses, which consist of multiple chapters (sections). Each chapter can have various resources like videos, images, or files. Courses remain in a pending status until reviewed and published by a manager.
- **Student:** Students can browse, enroll in courses, and pay for the enrollment using Stripe. Once enrolled, they can access all the course content.

 
