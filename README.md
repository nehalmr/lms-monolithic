# Library Management System

A comprehensive full-stack Library Management System built with **Spring Boot** (Backend) and **React.js** (Frontend). This system provides complete functionality for managing books, members, borrowing transactions, fines, and notifications in a modern library environment.

## 🚀 Features

### Core Functionality
- **Book Management**: Add, update, delete, and search books with availability tracking
- **Member Management**: Register members, manage profiles, and track membership status
- **Borrowing System**: Handle book borrowing and returns with due date tracking
- **Overdue Management**: Automatic overdue detection with fine calculation
- **Notification System**: Real-time notifications for borrowing confirmations and overdue notices
- **Dashboard Analytics**: Comprehensive overview with statistics and system status

### Technical Features
- **RESTful API**: Clean REST endpoints with proper HTTP status codes
- **Real-time Updates**: Dynamic UI updates without page refresh
- **Responsive Design**: Mobile-friendly interface using Tailwind CSS
- **Error Handling**: Graceful error handling with user-friendly messages
- **Health Monitoring**: Backend health checks and connection status
- **Demo Mode**: Offline demo with sample data when backend is unavailable

## 🏗️ Architecture

### Backend (Spring Boot)
\`\`\`
src/main/java/com/library/
├── entity/              # JPA Entities
├── repository/          # Data Access Layer
├── service/            # Business Logic Layer
├── controller/         # REST API Controllers
├── config/             # Configuration Classes
└── LibraryManagementApplication.java
\`\`\`

### Frontend (React.js)
\`\`\`
src/
├── components/         # React Components
├── services/          # API Service Layer
├── App.tsx           # Main Application Component
└── index.tsx         # Application Entry Point
\`\`\`

### Database Schema
- **Books**: Book information and availability
- **Members**: Member profiles and status
- **Borrowing Transactions**: Borrowing history and status
- **Fines**: Fine management and payment tracking
- **Notifications**: System notifications and alerts

## 🛠️ Technology Stack

### Backend
- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **Spring Web**
- **H2 Database** (Development)
- **Maven** (Build Tool)
- **Lombok** (Code Generation)

### Frontend
- **React 18**
- **TypeScript**
- **Tailwind CSS**
- **shadcn/ui Components**
- **Axios** (HTTP Client)
- **Lucide React** (Icons)

## 📋 Prerequisites

- **Java 17** or higher
- **Node.js 16** or higher
- **npm** or **yarn**
- **Maven 3.6** or higher
- **Git**

## 🚀 Quick Start

### 1. Clone the Repository
\`\`\`bash
git clone https://github.com/your-username/library-management-system.git
cd library-management-system
\`\`\`

### 2. Backend Setup
\`\`\`bash
# Navigate to backend directory
cd backend

# Install dependencies and run
mvn clean install
mvn spring-boot:run
\`\`\`

The backend will start on \`http://localhost:8080\`

### 3. Frontend Setup
\`\`\`bash
# Navigate to frontend directory (in a new terminal)
cd frontend

# Install dependencies
npm install

# Start development server
npm start
\`\`\`

The frontend will start on \`http://localhost:3000\`

### 4. Access the Application
- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8080/api
- **H2 Database Console**: http://localhost:8080/h2-console
- **Health Check**: http://localhost:8080/api/health

### 5. Access API Documentation
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/api-docs

The Swagger UI provides an interactive interface to:
- Explore all available API endpoints
- Test API calls directly from the browser
- View request/response schemas and examples
- Understand API authentication requirements

## 📊 API Documentation

### Interactive API Documentation (Swagger UI)
Once the backend is running, you can access the interactive API documentation at:
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs
- **OpenAPI YAML**: http://localhost:8080/api-docs.yaml

### API Endpoints Overview

#### Books API
\`\`\`
GET    /api/books              # Get all books
GET    /api/books/{id}         # Get book by ID
POST   /api/books              # Create new book
PUT    /api/books/{id}         # Update book
DELETE /api/books/{id}         # Delete book
GET    /api/books/search       # Search books
GET    /api/books/available    # Get available books
\`\`\`

#### Members API
\`\`\`
GET    /api/members            # Get all members
GET    /api/members/{id}       # Get member by ID
POST   /api/members            # Create new member
PUT    /api/members/{id}       # Update member
DELETE /api/members/{id}       # Delete member
GET    /api/members/search     # Search members
GET    /api/members/active     # Get active members
\`\`\`

#### Borrowing API
\`\`\`
GET    /api/borrowing                    # Get all transactions
POST   /api/borrowing/borrow            # Borrow a book
POST   /api/borrowing/return/{id}       # Return a book
GET    /api/borrowing/member/{id}       # Get member borrowings
GET    /api/borrowing/overdue           # Get overdue transactions
\`\`\`

#### Health API
\`\`\`
GET    /api/health             # Application health status
GET    /api/health/database    # Database health status
\`\`\`

### API Features
- **Interactive Testing**: Try out API endpoints directly from the Swagger UI
- **Request/Response Examples**: See sample requests and responses for each endpoint
- **Schema Documentation**: Detailed information about request and response models
- **Authentication Ready**: Prepared for future authentication integration
- **Error Handling**: Comprehensive error response documentation
- **Validation**: Input validation with detailed error messages

### Using the API

#### Example: Create a New Book
\`\`\`bash
curl -X POST "http://localhost:8080/api/books" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "New Book Title",
    "author": "Author Name",
    "genre": "Fiction",
    "isbn": "978-1234567890",
    "yearPublished": 2024,
    "availableCopies": 5,
    "totalCopies": 5
  }'
\`\`\`

#### Example: Search Books
\`\`\`bash
curl -X GET "http://localhost:8080/api/books/search?keyword=Harry%20Potter"
\`\`\`

#### Example: Borrow a Book
\`\`\`bash
curl -X POST "http://localhost:8080/api/borrowing/borrow?bookId=1&memberId=1"
\`\`\`

## 🗄️ Database Configuration

### Development (H2 In-Memory)
\`\`\`properties
spring.datasource.url=jdbc:h2:mem:librarydb
spring.datasource.username=sa
spring.datasource.password=
\`\`\`

### Production (MySQL)
\`\`\`properties
spring.datasource.url=jdbc:mysql://localhost:3306/library_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
\`\`\`

## 🔧 Configuration

### Backend Configuration (\`application.properties\`)
\`\`\`properties
# Server Configuration
server.port=8080

# Database Configuration
spring.datasource.url=jdbc:h2:mem:librarydb
spring.jpa.hibernate.ddl-auto=create-drop

# CORS Configuration
spring.web.cors.allowed-origins=http://localhost:3000
spring.web.cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS

# Logging
logging.level.com.library=DEBUG
\`\`\`

### Frontend Configuration
Create \`.env\` file in frontend directory:
\`\`\`
REACT_APP_API_URL=http://localhost:8080/api
\`\`\`

## 🧪 Testing Strategy

### Test-Driven Development (TDD)
This project follows Test-Driven Development principles:
1. **Red**: Write failing tests first
2. **Green**: Write minimal code to make tests pass
3. **Refactor**: Improve code while keeping tests green

### Testing Framework Stack
- **JUnit 5**: Core testing framework
- **Mockito**: Mocking framework for unit tests
- **Spring Boot Test**: Integration testing support
- **TestContainers**: Database integration testing
- **AssertJ**: Fluent assertion library
- **JaCoCo**: Code coverage reporting

### Test Categories

#### Unit Tests
- **Service Layer Tests**: Business logic validation
- **Controller Tests**: API endpoint testing with MockMvc
- **Repository Tests**: Data access layer testing
- **Entity Tests**: Domain model validation

#### Integration Tests
- **API Integration Tests**: End-to-end API testing
- **Database Integration Tests**: Real database operations
- **Component Integration Tests**: Multi-layer testing

#### Test Data Builders
- **Builder Pattern**: Consistent test data creation
- **Test Data Factories**: Reusable test objects
- **Faker Integration**: Random test data generation

### Running Tests

#### All Tests
\`\`\`bash
mvn test
\`\`\`

#### Unit Tests Only
\`\`\`bash
mvn test -Dtest="*Test"
\`\`\`

#### Integration Tests Only
\`\`\`bash
mvn test -Dtest="*IntegrationTest"
\`\`\`

#### Specific Test Class
\`\`\`bash
mvn test -Dtest="BookServiceTest"
\`\`\`

#### Specific Test Method
\`\`\`bash
mvn test -Dtest="BookServiceTest#shouldReturnAllBooksWhenBooksExist"
\`\`\`

#### With Code Coverage
\`\`\`bash
mvn clean test jacoco:report
\`\`\`

#### Integration Tests Profile
\`\`\`bash
mvn verify -Pintegration-tests
\`\`\`

### Code Coverage

#### Generate Coverage Report
\`\`\`bash
mvn clean test jacoco:report
\`\`\`

#### View Coverage Report
Open `target/site/jacoco/index.html` in your browser

#### Coverage Requirements
- **Minimum Line Coverage**: 80%
- **Package Coverage**: 80%
- **Class Coverage**: 90%

#### Coverage Exclusions
- Configuration classes
- Entity classes (getters/setters)
- Main application class
- Exception classes

### Test Configuration

#### Test Profiles
- **test**: Default test profile with H2 database
- **integration-test**: Profile for integration tests
- **test-mysql**: Profile for MySQL integration tests

#### Test Properties
\`\`\`properties
# src/test/resources/application-test.properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.hibernate.ddl-auto=create-drop
logging.level.com.library=WARN
springdoc.swagger-ui.enabled=false
\`\`\`

### Test Examples

#### Service Layer Test Example
\`\`\`java
@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    
    @InjectMocks
    private BookService bookService;
    
    @Test
    void shouldReturnBookWhenBookExists() {
        // Given
        Book testBook = BookTestDataBuilder.aBook().build();
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        
        // When
        Optional<Book> result = bookService.getBookById(1L);
        
        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testBook);
    }
}
\`\`\`

#### Controller Test Example
\`\`\`java
@WebMvcTest(BookController.class)
class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private BookService bookService;
    
    @Test
    void shouldReturnAllBooks() throws Exception {
        // Given
        List<Book> books = Arrays.asList(BookTestDataBuilder.aBook().build());
        when(bookService.getAllBooks()).thenReturn(books);
        
        // When & Then
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}
\`\`\`

#### Integration Test Example
\`\`\`java
@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class BookIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void shouldCreateAndRetrieveBook() throws Exception {
        // Create book via API
        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJson))
                .andExpect(status().isCreated());
        
        // Retrieve book via API
        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Test Book")));
    }
}
\`\`\`

### Test Data Management

#### Test Data Builders
\`\`\`java
public class BookTestDataBuilder {
    public static BookTestDataBuilder aBook() {
        return new BookTestDataBuilder();
    }
    
    public BookTestDataBuilder withTitle(String title) {
        this.title = title;
        return this;
    }
    
    public Book build() {
        return new Book(/* parameters */);
    }
}
\`\`\`

#### Usage Example
\`\`\`java
Book testBook = BookTestDataBuilder.aBook()
    .withTitle("Test Book")
    .withAuthor("Test Author")
    .withAvailableCopies(5)
    .build();
\`\`\`

### Continuous Integration

#### GitHub Actions Workflow
\`\`\`yaml
name: Tests
on: [push, pull_request]
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-java@v3
        with:
          java-version: '17'
      - run: mvn clean test
      - run: mvn jacoco:report
      - uses: codecov/codecov-action@v3
\`\`\`

### Best Practices

#### Test Naming
- Use descriptive test method names
- Follow pattern: `should[ExpectedBehavior]When[StateUnderTest]`
- Use `@DisplayName` for readable test descriptions

#### Test Structure
- **Arrange**: Set up test data and mocks
- **Act**: Execute the method under test
- **Assert**: Verify the expected behavior

#### Test Independence
- Each test should be independent
- Use `@BeforeEach` for test setup
- Clean up resources in `@AfterEach`

#### Mock Usage
- Mock external dependencies only
- Verify important interactions
- Use argument captors for complex verifications

#### Assertion Guidelines
- Use AssertJ for fluent assertions
- Test one concept per test method
- Provide meaningful assertion messages

### Performance Testing

#### Load Testing with JMeter
\`\`\`bash
# Install JMeter
wget https://jmeter.apache.org/download_jmeter.cgi

# Run load tests
jmeter -n -t load-test.jmx -l results.jtl
\`\`\`

#### Benchmark Testing
\`\`\`java
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class BookServiceBenchmark {
    @Benchmark
    public void testBookSearch() {
        bookService.searchBooks("keyword");
    }
}
\`\`\`

## 🧪 Testing

### Backend Tests
\`\`\`bash
cd backend
mvn test
\`\`\`

### Frontend Tests
\`\`\`bash
cd frontend
npm test
\`\`\`

## 📦 Production Deployment

### Backend Deployment
\`\`\`bash
# Build JAR file
mvn clean package

# Run production JAR
java -jar target/library-management-system-0.0.1-SNAPSHOT.jar
\`\`\`

### Frontend Deployment
\`\`\`bash
# Build for production
npm run build

# Serve static files (using serve)
npx serve -s build -l 3000
\`\`\`

### Docker Deployment
\`\`\`dockerfile
# Backend Dockerfile
FROM openjdk:17-jdk-slim
COPY target/library-management-system-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
\`\`\`

\`\`\`dockerfile
# Frontend Dockerfile
FROM node:16-alpine
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build
EXPOSE 3000
CMD ["npx", "serve", "-s", "build", "-l", "3000"]
\`\`\`

### Docker Compose
\`\`\`yaml
version: '3.8'
services:
  backend:
    build: ./backend
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=production
  
  frontend:
    build: ./frontend
    ports:
      - "3000:3000"
    depends_on:
      - backend
    environment:
      - REACT_APP_API_URL=http://localhost:8080/api
\`\`\`

## 🔒 Security Considerations

### Production Security Checklist
- [ ] Change default database credentials
- [ ] Enable HTTPS/SSL certificates
- [ ] Implement authentication and authorization
- [ ] Add input validation and sanitization
- [ ] Configure proper CORS policies
- [ ] Enable security headers
- [ ] Set up monitoring and logging
- [ ] Regular security updates

### Recommended Security Enhancements
\`\`\`java
// Add Spring Security dependency
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
\`\`\`

## 📈 Performance Optimization

### Backend Optimization
- Database indexing on frequently queried fields
- Connection pooling configuration
- Caching with Redis for frequently accessed data
- API response pagination
- Database query optimization

### Frontend Optimization
- Code splitting and lazy loading
- Image optimization and compression
- Bundle size optimization
- Caching strategies
- Performance monitoring

## 🐛 Troubleshooting

### Common Issues

#### Backend Not Starting
\`\`\`bash
# Check Java version
java -version

# Check port availability
netstat -an | grep 8080

# Check application logs
tail -f logs/application.log
\`\`\`

#### Frontend Connection Issues
\`\`\`bash
# Check backend health
curl http://localhost:8080/api/health

# Check CORS configuration
# Verify REACT_APP_API_URL in .env file
\`\`\`

#### Database Issues
\`\`\`bash
# Access H2 Console
# URL: http://localhost:8080/h2-console
# JDBC URL: jdbc:h2:mem:librarydb
# Username: sa
# Password: (empty)
\`\`\`

## 📚 Sample Data

The application includes sample data initialization:
- **10 Books** across various genres
- **8 Members** with different statuses
- **8 Borrowing Transactions** including overdue items
- **5 Notifications** for different scenarios
- **1 Fine** for overdue book

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (\`git checkout -b feature/amazing-feature\`)
3. Commit your changes (\`git commit -m 'Add amazing feature'\`)
4. Push to the branch (\`git push origin feature/amazing-feature\`)
5. Open a Pull Request

### Development Guidelines
- Follow Java coding standards (Google Java Style Guide)
- Use TypeScript for all React components
- Write unit tests for new features
- Update documentation for API changes
- Follow conventional commit messages

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Authors

- **Your Name** - *Initial work* - [YourGitHub](https://github.com/yourusername)

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- React team for the powerful frontend library
- shadcn/ui for beautiful UI components
- H2 Database for development convenience
- All contributors and testers

## 📞 Support

For support and questions:
- **Email**: support@librarymanagement.com
- **Issues**: [GitHub Issues](https://github.com/your-username/library-management-system/issues)
- **Documentation**: [Wiki](https://github.com/your-username/library-management-system/wiki)

## 🗺️ Roadmap

### Version 2.0 (Planned)
- [ ] User authentication and role-based access
- [ ] Email notification system
- [ ] Advanced reporting and analytics
- [ ] Mobile application
- [ ] Barcode scanning integration
- [ ] Multi-library support
- [ ] Advanced search with filters
- [ ] Book reservation system
- [ ] Integration with external book APIs
- [ ] Automated fine calculation and payment

### Version 1.1 (Next Release)
- [ ] MySQL database support
- [ ] Docker containerization
- [ ] Unit and integration tests
- [ ] API documentation with Swagger
- [ ] Performance monitoring
- [ ] Backup and restore functionality

---

**Built with ❤️ using Spring Boot and React.js**
