# AGENTS.md - TeLlevo Admin System

## Build/Lint/Test Commands

### Frontend (Vue.js + Vite)
- `pnpm dev`: Start development server
- `pnpm build`: Build for production
- `pnpm build:clean`: Clean and build
- `pnpm typecheck`: Run TypeScript checks
- `pnpm preview`: Preview production build
- `pnpm clean`: Clean dist and cache

### Backend (Spring Boot + Maven)
- `mvn clean compile`: Clean and compile
- `mvn test`: Run all tests
- `mvn test -Dtest=TestClassName`: Run single test
- `mvn spring-boot:run`: Start application
- `mvn package`: Build JAR

## Code Style Guidelines

### Java (Backend)
- Use Spring Boot conventions: @RestController, @Autowired, @Valid
- Imports: Standard order, no wildcards
- Naming: camelCase for methods/variables, PascalCase for classes
- Error Handling: Try-catch with logging (SLF4J), return ResponseEntity with error maps
- Formatting: 4 spaces indentation, 120 char line limit

### Vue.js (Frontend)
- Composition API: Use <script setup>, ref() for reactivity
- Imports: ES6 modules, relative paths for local files
- Naming: camelCase for variables/functions, PascalCase for components
- Styling: Tailwind CSS classes, scoped styles
- Error Handling: Try-catch in async functions, user-friendly messages

## Memory Rules
See .github/copilot-instructions.md for LLM context, business rules, and Byterover tool workflows. Always reference README.md for system architecture and use Byterover tools for knowledge management.