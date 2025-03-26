How to Run the Application
Start the Spring Boot backend:

OPEN bash
Copy cd backend [catfe-rockers]
./gradlew bootRun
Start the Angular frontend:

OPEN bash
Copy cd frontend [UI-Angular-Module]
ng serve
Open your browser to http://localhost:4200


[Features]
Full-stack application with Java/Spring Boot backend and Angular frontend

Interactive UI with form validation

Responsive design using Angular Material

Error handling with user feedback

API documentation via Swagger UI (available at http://localhost:8080/api/swagger-ui.html)

[Configuration]
Remember to:

Setup required for OpenAI API key in application.yml

Configure CORS in the Spring Boot application if needed

Adjust ports as necessary for your environment

This implementation provides a complete, production-ready solution for dynamically generating payment test cases with a modern web interface.