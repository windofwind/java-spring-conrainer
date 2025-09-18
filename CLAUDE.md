# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Spring Boot application named "acution" built for user management functionality including user registration, login, withdrawal, and profile management (CRUD operations).

**Tech Stack:**
- Java 17
- Spring Boot 3.5.5
- Gradle 8.14.3
- PostgreSQL (via Docker)
- JPA/Hibernate for ORM
- Environment variables (.env)
- Swagger/OpenAPI for API documentation

## Development Commands

### Build and Test
```bash
# Build the project
./gradlew build

# Run tests
./gradlew test

# Clean build
./gradlew clean build

# Run a single test class
./gradlew test --tests "info.addline.acution.AcutionApplicationTests"
```

### Database Setup
```bash
# Start PostgreSQL database
docker-compose up -d

# Stop database
docker-compose down

# View database logs
docker logs postgresdb
```

### Running the Application
```bash
# Start the Spring Boot application
./gradlew bootRun

# Run with specific profile
./gradlew bootRun --args='--spring.profiles.active=dev'

# Access Swagger UI (after application starts)
# http://localhost:8080/swagger-ui.html
```

## Project Architecture

### Package Structure
- **Base Package**: `info.addline.acution`
- **Main Class**: `AcutionApplication.java`
- **Application Name**: "acution" (configured in application.properties)

### Database Configuration
- **Database**: PostgreSQL
- **Container**: `postgresdb`
- **Default Credentials**: postgres/postgres
- **Database Name**: postgres
- **Port**: Uses host networking mode
- **ORM**: JPA with Hibernate implementation

### Environment Variables (.env)
Create a `.env` file in the root directory:
```env
# Database configuration
DB_URL=jdbc:postgresql://localhost:5432/postgres
DB_USERNAME=postgres
DB_PASSWORD=postgres

# Application settings
JWT_SECRET=your-secret-key-here
JWT_EXPIRATION=86400000

# Server configuration
SERVER_PORT=8080
```

### JPA Configuration
Add these properties to `application.properties`:
```properties
# Database connection (using .env variables)
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA/Hibernate settings
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Server configuration
server.port=${SERVER_PORT:8080}

# Swagger/OpenAPI configuration
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.operationsSorter=method
```

### Expected Module Structure
For implementing user management features with JPA, follow this structure:
```
src/main/java/info/addline/acution/
├── controller/     # REST controllers for user endpoints
├── service/        # Business logic layer
├── repository/     # JPA repositories extending JpaRepository
├── entity/         # JPA entities with @Entity annotations
├── dto/            # Data Transfer Objects for API requests/responses
├── config/         # Configuration classes (Security, JPA, etc.)
└── exception/      # Custom exception handling
```

### JPA Entity Guidelines
- Use `@Entity` annotation for domain objects
- Use `@Id` and `@GeneratedValue` for primary keys
- Use `@Column` for column mapping when needed
- Use `@Table` to specify table names
- Example entity structure:
```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    // getters, setters, constructors
}
```

### JPA Repository Pattern
- Extend `JpaRepository<Entity, ID>` for basic CRUD operations
- Use custom query methods with naming conventions
- Use `@Query` annotation for complex queries
- **Do not use enums in repository queries**: Use String values instead of enum constants for database operations
- Example repository:
```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.email = ?1")
    Optional<User> findByEmail(String email);

    // Use String values instead of enum in queries
    @Query("SELECT u FROM User u WHERE u.status = 'ACTIVE'")
    List<User> findActiveUsers();

    // Custom method using String parameter instead of enum
    List<User> findByStatus(String status);
}
```

### Development Notes

1. **Java Version**: Project requires Java 17 with toolchain configuration
2. **Gradle Wrapper**: Always use `./gradlew` instead of system gradle
3. **Database**: Ensure PostgreSQL container is running before starting the application
4. **Environment Setup**:
   - Create `.env` file with database credentials and application settings
   - Configure `application.properties` to use environment variables
5. **JPA Setup**: Configure application.properties with database connection and JPA settings
6. **Entity Scanning**: Main application class should have `@EnableJpaRepositories` if needed
7. **Swagger Documentation**: Access API docs at `/swagger-ui.html` after starting the application
8. **User Management**: Implementation should include:
   - User registration (회원가입) - User entity with JPA
   - User login/authentication (로그인) - UserRepository methods
   - User withdrawal/deletion (회원탈퇴) - JPA delete operations
   - Profile CRUD operations (프로필 추가/수정/삭제) - Profile entity with relationships
9. **Database Enum Handling**:
   - Do not use Java enums directly in repository/database operations
   - Use String values in @Query annotations and method parameters
   - Convert between enum and String in service layer when needed

### Dependencies (Already Added)
Current `build.gradle` includes:
```gradle
dependencies {
    // Core Spring Boot starters
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-security'
    implementation 'org.springframework.boot:spring-boot-starter-validation'

    // Database
    runtimeOnly 'org.postgresql:postgresql'

    // Environment variables support
    implementation 'me.paulschwarz:spring-dotenv:4.0.0'

    // Swagger/OpenAPI documentation
    implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0'

    // Testing
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testImplementation 'com.h2database:h2'
}
```

### Swagger API Documentation
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **API Docs JSON**: `http://localhost:8080/api-docs`
- Use `@Tag`, `@Operation`, `@ApiResponse` annotations for better documentation
- Example controller with Swagger annotations:
```java
@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "사용자 관리 API")
public class UserController {

    @PostMapping("/register")
    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다")
    @ApiResponse(responseCode = "201", description = "회원가입 성공")
    public ResponseEntity<UserDto> register(@RequestBody @Valid UserRegistrationDto dto) {
        // Implementation
    }
}
```

### Testing Strategy
- Use JUnit 5 (already configured)
- Test application context loads (basic test exists)
- Write unit tests for services and integration tests for controllers
- Use `@SpringBootTest` for integration testing
- Use `@DataJpaTest` for testing JPA repositories
- Use H2 in-memory database for testing (see dependencies above)
- Example repository test:
```java
@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByUsername_ShouldReturnUser() {
        // Test JPA repository methods
    }
}
```