# TeLlevo Admin System - Agent Guidelines

## Build/Lint/Test Commands

### Frontend (Vue.js + Vite)
- **Development server**: `pnpm dev`
- **Production build**: `pnpm run build`
- **Type checking**: `pnpm run typecheck`
- **Clean build**: `pnpm run clean`
- **Preview build**: `pnpm run preview`

### Backend (Spring Boot + Maven)
- **Run application**: `mvn spring-boot:run` (from backend-admin/)
- **Build only**: `mvn clean compile` (from backend-admin/)
- **Package JAR**: `mvn clean package` (from backend-admin/)
- **Test**: `mvn test` (from backend-admin/)

### Single Test Execution
- **Frontend**: No specific single test command configured
- **Backend**: `mvn test -Dtest=TestClassName` (from backend-admin/)

## Code Style Guidelines

### Java (Spring Boot)
- **Imports**: Organize by type (standard library, Spring framework, project packages)
- **Naming**: camelCase for variables/methods, PascalCase for classes
- **Logging**: Use SLF4J LoggerFactory with class-level loggers
- **Error Handling**: Try-catch blocks with specific exception types, proper HTTP status codes
- **Annotations**: Use Spring annotations (@Service, @Controller, @Autowired, etc.)
- **DTOs**: Simple POJOs with validation annotations (@NotNull, @Size, etc.)
- **Security**: JWT tokens stored in localStorage, proper CORS configuration

### Vue.js (Composition API)
- **Script setup**: Use `<script setup>` syntax
- **Reactivity**: Use `ref()` and `reactive()` appropriately
- **Props/Emits**: Define with `defineProps()` and `defineEmits()`
- **Naming**: PascalCase for components, camelCase for variables
- **Imports**: Group by Vue composables, then services, then components
- **Styling**: Tailwind CSS classes with DaisyUI components
- **Forms**: Reactive validation with immediate feedback

### General
- **No comments**: Avoid adding comments unless explicitly requested
- **Error handling**: Comprehensive try-catch with user-friendly messages
- **Validation**: Client-side and server-side validation
- **Security**: Never log sensitive data, proper input sanitization

## Copilot Instructions Integration

### LLM Memory Context
- **Primary knowledge sources**: Root README.md and backend-admin/README.md
- **Memory tags**: Use ARCHITECTURE_TYPE, FRONTEND_STACK, BACKEND_STACK, KEY_FEATURES
- **Context retrieval**: Reference README sections for business requirements and technical details

### Workflow Rules
- **Onboarding**: Use byterover-check-handbook-existence → byterover-create-handbook → byterover-store-modules
- **Planning**: byterover-retrieve-active-plans → byterover-save-implementation-plan → byterover-retrieve-knowledge
- **Implementation**: byterover-update-plan-progress → byterover-store-knowledge
- **Context**: byterover-reflect-context and byterover-assess-context throughout

### Required Context Links
- **Frontend-Backend**: Authentication flow, CORS configuration, JWT handling
- **Development**: Build/run instructions, testing procedures, troubleshooting
- **Business**: CO₂ emission reduction metrics, analytics requirements

## Project Architecture Notes

### Database
- **Hybrid setup**: H2 for authentication, PostgreSQL for business data
- **JPA Pattern**: Spring Data JPA with PostgreSQL for type-safe operations
- **Connection**: PostgreSQL at 192.168.100.14:17432/tellevoappdb

### Real-time Features
- **Logo Preview**: Live SVG validation and loading
- **Form Validation**: Immediate feedback with visual indicators
- **WebSocket**: Configured for real-time updates (future implementation)

### Security
- **JWT Authentication**: Stateless tokens with localStorage persistence
- **CORS**: Configured for both development and production domains
- **Route Guards**: Vue Router protection for authenticated routes

## Common Patterns

### API Integration
```javascript
// Axios interceptors for JWT tokens
apiClient.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) config.headers.Authorization = `Bearer ${token}`
  return config
})
```

### Reactive Form Validation
```javascript
const validateField = (field) => {
  // Real-time validation with immediate feedback
  // Update errors reactive object
  // Visual indicators for success/error states
}
```

### JPA Repository Usage
```java
@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    // Type-safe database operations
    Optional<Empresa> findByDominio(String dominio);
}
```

## Troubleshooting References

- **Database connection**: Check PostgreSQL at 192.168.100.14:17432
- **CORS issues**: Verify vite.config.js proxy and application.properties CORS settings
- **JWT errors**: Check token expiration and localStorage persistence
- **Build failures**: Ensure Node >=18, Java 24, and proper Maven installation
