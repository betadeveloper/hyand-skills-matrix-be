# Skills Matrix Framework Backend

The backend of the Skills Matrix Framework is a Java (Spring Framework) based application designed to do the server-side handling of the interface for managing skills, career paths, and feedback processes. The frontend uses the backend to retrieve, process and in other ways communicate data.

## Backend Technology Stack

- **Programming Language:** Java with Spring Framework
- **Database:** MySQL
- **Authentication:** JSON Web Tokens (JWT)

## Setup Instructions

### Prerequisites

- Java 17 or higher
- MySQL Server
- Docker

### Steps to Run

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/your-repo/hyand-skills-matrix.git
   cd hyand-skills-matrix
   ```

2. **Backend Setup:**
   - Navigate to the backend directory and build the project:
     ```bash
     cd backend
     ./mvnw clean install
     ```
   - You can also instead just press Run on the SkillsMatrixApplication.java class if you are using IntelliJ IDEA
   - Configure the `application.properties` file to match your MySQL database settings.
   - Start the backend server:
     ```bash
     ./mvnw spring-boot:run
     ```

3. **Backend available:**
   - Open your browser and navigate to `http://localhost:8080`, or whatever port you have set in the configuration.
