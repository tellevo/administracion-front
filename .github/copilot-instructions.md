# TeLlevo Admin System – AI Agent Context & Memory Rules

## 🧠 LLM Memory Primer

The TeLlevo Admin System has two primary memory context sources for AI agents:

1. **Root README.md**: Contains high-level system architecture, business context, frontend details, and deployment configurations
2. **backend-admin/README.md**: Provides backend-specific implementation details, API documentation, and integration guides

Together, these files form a comprehensive knowledge base for the system.

### Production Environment
- Frontend: https://admin.tellevoapp.com
- Backend API: https://admin.tellevoapp.com/api
- Development: Uses Bun + Vite for optimized builds

### Memory Structure Tags

Use these tags when parsing the README.md:
- `**LLM Context Memory File**`: Identifies primary knowledge store
- `**ARCHITECTURE_TYPE**`: Core architectural pattern
- `**FRONTEND_STACK**`, `**BACKEND_STACK**`: Technology stacks
- `**KEY_FEATURES**`: Essential system capabilities
- `**TECH_IMPLEMENTATION_INSIGHT**`: Implementation details

### Code Reference Blocks

README.md uses structured code blocks for implementation details:
```
REASON: Purpose/rationale for implementation
IMPACT: Business/technical effects
TECH: Technical implementation details
VALIDATION: Validation rules/checks

CROSS_REFERENCE: Related file locations
RELATED_FILES: Specific file dependencies
RELATED_FUNCTIONS: Associated methods/functions
```

## 🎯 Business Context Retrieval

When analyzing business requirements, focus on:
1. **Sustainability Goals**: CO₂ emission reduction metrics
2. **Analytics Requirements**: Usage patterns and environmental impact
3. **Management Tools**: Route optimization and user management needs
4. **Environmental Impact**: Transparency and measurement approaches

## 🔍 Technical Context Access

### Frontend Context (Root README.md)
1. **Architecture**: Read the ARCHITECTURE_TYPE tag first
2. **Stack Details**: Parse FRONTEND_STACK tags (now includes Bun)
3. **Features**: Reference KEY_FEATURES for core capabilities
4. **Implementation**: Use TECH_IMPLEMENTATION_INSIGHT for frontend patterns
5. **Environment**: Check production domain and deployment info

### Backend Context (backend-admin/README.md)
1. **API Documentation**: Reference the API Endpoints section
2. **Authentication Flow**: See Default Credentials and JWT configuration
3. **Integration Guide**: Check the "Integration with Frontend" section
4. **Configuration**: Review application.properties for both environments
5. **Environments**: See environment-specific settings and CORS config
6. **Troubleshooting**: Consult the dedicated section for common issues

## 📋 Memory Persistence Rules

1. Always reference README.md when starting new tasks
2. Use code block structure for storing new implementation details
3. Maintain cross-references between related components
4. Update technical insights when architectural changes occur

## 🔄 Context Update Workflow

When updating system knowledge:
1. Parse existing README.md structure
2. Identify relevant context sections
3. Maintain consistent tagging format
4. Preserve cross-references and related file links

## 🚫 Context Boundaries

The README.md context explicitly covers:
- System architecture and stack choices
- Business objectives and requirements
- Technical implementation details
- Cross-component relationships
- Validation and error handling patterns

Do not assume documentation completeness for:
- Deployment configurations
- External system integrations
- Third-party service credentials
- Performance metrics

## 🔗 Required Context Links

### Frontend-Backend Integration Points
- Authentication flow between Vue.js and Spring Security
- CORS configuration alignment
- API endpoint mappings to frontend routes
- JWT token handling on both sides

### Development Workflow Links
- Build and run instructions for both parts
- Testing procedures (backend API tests, frontend integration)
- Troubleshooting common issues
- Configuration management

Remember: Together, these README files serve as the authoritative memory store for the TeLlevo Admin System. Always verify against both when making system-wide changes, particularly for features that span the frontend-backend boundary.
