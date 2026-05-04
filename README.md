- TopSet: Workout Tracking API 💪

   TopSet is a backend-driven fitness-tracking application that allows users to log and manage structured workout sessions, with detailed exercise and set-level data. 
   The application is designed with a focus on clean architecture, secure user access, and real-world API design.

- Features ✅

   Create and manage workout sessions (e.g., Push A, Pull B)
   Add exercises to each workout
   Track individual sets (weight, reps, personal records)
   Secure authentication using JWT
   User-specific data access (users can only access their own workouts)
   Input validation and error handling
   Unit and integration testing
   Tech Stack
   Backend: Java, Spring Boot
   Security: Spring Security + JWT
   Database: PostgreSQL 
   Build Tool: Gradle
   Testing: JUnit, MockMvc

- Architecture ⚙️
  
   TopSet follows a layered architecture:

   Controller Layer – Handles HTTP requests
   Service Layer – Contains business logic and user ownership enforcement
   Repository Layer – Handles database operations
   DTOs & Mappers – Separate API contracts from persistence models

- Getting Started 🏃
1. Clone the repository
   git clone https://github.com/your-username/TopSet.git
   cd topset

2. Configure environment
   Create a local config file:
   src/main/resources/application-local.properties
   

3. Run the application
   ./gradlew bootRun (or run from your IDE)


- Project Motivation 🏆

   TopSet was built to combine my interest in fitness with backend development. 
   The project focuses on designing scalable APIs, handling real-world relational data, and implementing secure user-based access control.

- Future Improvements 🎯
  
   Mobile frontend integration
   Progress analytics and visualization
   Exercise library and templates
   Cloud deployment (AWS/Docker)
     
