# Skills Matrix Framework Backend

The Skills Matrix Framework is an internal tool designed to help employees track their skills and career progression within the organization. This system is built to provide a comprehensive overview of skillsets, facilitate performance reviews, and offer feedback for personal and professional growth.

## Key Features

- **Skill Management:**
  Employees can document and update their skill levels, categorize skills, and view progression over time.

- **Career Path Tracking:**
  Allows employees to map out potential career paths within the organization and identify skills required for advancement.

- **Feedback System:**
  Collect feedback after meetings or reviews, helping employees improve based on actionable insights.

- **Security and Access Control:**
  Implements JWT authentication with role-based access control to ensure data security.

## Technology Stack

- **Frontend:** React
- **Backend:** Java with Spring Framework
- **Database:** MySQL
- **Authentication:** JSON Web Tokens (JWT)

## Setup Instructions

### Prerequisites

- Java 17 or higher
- Node.js 18 or higher
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
