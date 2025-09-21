/**
 * TeLlevo Admin - LLM Context Memory System
 * 
 * This module provides comprehensive LLM memory functionality using both README.md files
 * as the primary knowledge base for intelligent code assistance, troubleshooting,
 * and development guidance.
 * 
 * @description AI-optimized context management for TeLlevo admin system
 * @version 1.0.0
 * @author TeLlevo Development Team
 */

class TelLevoLLMContextMemory {
  constructor() {
    this.initializeContext();
  }

  /**
   * Initialize comprehensive context from both README files
   */
  initializeContext() {
    this.contextMemory = {
      // Core System Information
      projectInfo: {
        name: "TeLlevo - Administración Frontend",
        type: "Eco-friendly carpooling administration platform",
        primaryObjective: "Reduce CO₂ footprint through intelligent ride sharing",
        architecture: "JPA Repository Architecture (Full PostgreSQL Migration)",
        domain: {
          production: "https://admin.tellevoapp.com",
          development: "http://localhost:5174",
          api: "https://admin.tellevoapp.com/api"
        }
      },

      // Technical Stack Memory
      techStack: {
        frontend: {
          framework: "Vue.js 3 + Composition API",
          buildTool: "Vite",
          styling: "Tailwind CSS + DaisyUI",
          packageManager: "Bun",
          charts: "Chart.js + vue-chartjs",
          icons: "Lucide Vue Next",
          http: "Axios",
          authentication: "JWT + localStorage"
        },
        backend: {
          framework: "Spring Boot 3.5.3",
          java: "Java 24",
          security: "Spring Security + JWT",
          database: {
            primary: "PostgreSQL (192.168.100.14:17432/tellevoappdb)",
            auth: "H2 (in-memory for development)",
            credentials: { username: "postgres", password: "tellevo$" }
          },
          buildTool: "Maven"
        }
      },

      // Architecture Patterns Memory
      architecturePatterns: {
        database: {
          type: "Hybrid Database Architecture",
          h2Usage: "Authentication, sessions, UI state (95% of operations)",
          postgresqlUsage: "Business data - empresas CRUD (5% of operations)",
          benefits: "40% faster boot time + external data persistence",
          connectionPattern: "JPA Repository with Spring Data"
        },
        frontend: {
          formValidation: "Reactive forms with real-time validation",
          logoPreview: "Live SVG validation and preview",
          routing: "Vue Router with authentication guards",
          stateManagement: "Composition API + localStorage"
        },
        security: {
          authentication: "JWT Bearer tokens with role-based authorization",
          authorization: "@PreAuthorize('hasRole('ADMIN')') on empresa operations",
          storage: "localStorage for persistence",
          tokenExpiry: "86400000ms (24 hours)",
          defaultCredentials: { username: "admin@tellevoapp.cl", password: "admin123" },
          roles: "ADMIN role required for empresa CRUD operations"
        }
      },

      // API Endpoints Memory
      apiEndpoints: {
        authentication: {
          login: { method: "POST", path: "/api/login", desc: "JWT authentication" },
          health: { method: "GET", path: "/api/health", desc: "System health check" },
          test: { method: "GET", path: "/api/test", desc: "Auth service test endpoint" }
        },
        empresas: {
          create: { method: "POST", path: "/api/empresas", desc: "Create new company" },
          list: { method: "GET", path: "/api/empresas", desc: "List all companies" },
          get: { method: "GET", path: "/api/empresas/{id}", desc: "Get company by ID" },
          getByDomain: { method: "GET", path: "/api/empresas/dominio/{dominio}", desc: "Get company by domain" },
          update: { method: "PUT", path: "/api/empresas/{id}", desc: "Update company" },
          delete: { method: "DELETE", path: "/api/empresas/{id}", desc: "Delete company" },
          health: { method: "GET", path: "/api/empresas/health", desc: "Empresa service health check" }
        }
      },

      // Component Architecture Memory
      componentArchitecture: {
        flow: [
          "EmpresaListView.vue → Display companies table + CRUD actions",
          "EmpresaView.vue → Container for create/edit, handles routing + localStorage", 
          "EmpresaForm.vue → Reusable form with validation + logo preview",
          "EmpresaController.java → REST API endpoints",
          "EmpresaService.java → Business service with JPA Repository",
          "EmpresaRepository.java → Spring Data JPA interface",
          "PostgreSQL → External database connection"
        ],
        keyFeatures: [
          "Real-time logo preview with SVG validation",
          "Cross-origin image loading with error boundaries",
          "Form validation: Client-side + server-side",
          "Responsive design with mobile-first approach"
        ]
      },

      // Troubleshooting Memory
      troubleshootingPatterns: {
        frontendBuildIssues: {
          symptoms: ["vite:css errors", "PostCSS config failures", "DaisyUI import errors"],
          solutions: [
            "Force PostCSS transformer in vite.config.js",
            "Use ESM format in postcss.config.js",
            "Avoid DaisyUI internal imports in tailwind.config.js"
          ]
        },
        backendConnectivity: {
          symptoms: ["PostgreSQL connection refused", "CORS errors", "401/403 on login"],
          solutions: [
            "Verify postgres.url in application.properties",
            "Check CORS allowed-origins configuration",
            "Ensure JwtAuthenticationFilter skips /api/login endpoint"
          ]
        },
        productionDeployment: {
          symptoms: ["API routing to localhost", "403 on login", "CORS preflight failures"],
          solutions: [
            "Use relative API paths in .env.production",
            "Update JwtAuthenticationFilter to skip permitAll endpoints",
            "Configure nginx CORS headers for OPTIONS requests"
          ]
        }
      },

      // Performance Memory
      performanceMetrics: {
        expected: {
          healthCheck: "50-100ms",
          empresaPost: "150-300ms",
          empresaGet: "100-200ms",
          logoPreview: "200-500ms first load"
        },
        resources: {
          ram: "~200MB (H2 + PostgreSQL connections)",
          cpu: "~10-15% startup, <5% steady state",
          startup: "< 4 seconds"
        }
      },

      // Development Memory
      developmentPatterns: {
        quickStart: [
          "bun install",
          "bun dev (frontend on :5174)",
          "mvn spring-boot:run (backend on :8080)",
          "Default login: admin@tellevoapp.cl / admin123"
        ],
        commonCommands: {
          frontend: ["bun dev", "bun run build", "bun run preview"],
          backend: ["mvn spring-boot:run", "mvn clean compile", "mvn clean package"]
        }
      }
    };
  }

  /**
   * Get context for specific area of the system
   * @param {string} area - The area to get context for
   * @returns {Object} Relevant context information
   */
  getContext(area) {
    const contextMap = {
      'architecture': this.contextMemory.architecturePatterns,
      'api': this.contextMemory.apiEndpoints,
      'tech-stack': this.contextMemory.techStack,
      'troubleshooting': this.contextMemory.troubleshootingPatterns,
      'components': this.contextMemory.componentArchitecture,
      'performance': this.contextMemory.performanceMetrics,
      'development': this.contextMemory.developmentPatterns,
      'project': this.contextMemory.projectInfo
    };

    return contextMap[area] || this.contextMemory;
  }

  /**
   * Search context memory for specific patterns
   * @param {string} query - Search query
   * @returns {Array} Matching context entries
   */
  searchContext(query) {
    const results = [];
    const searchTerms = query.toLowerCase().split(' ');

    const searchObject = (obj, path = []) => {
      for (const [key, value] of Object.entries(obj)) {
        const currentPath = [...path, key];
        
        if (typeof value === 'string') {
          const matchScore = searchTerms.reduce((score, term) => {
            return score + (value.toLowerCase().includes(term) ? 1 : 0);
          }, 0);
          
          if (matchScore > 0) {
            results.push({
              path: currentPath.join('.'),
              content: value,
              score: matchScore,
              context: 'string'
            });
          }
        } else if (Array.isArray(value)) {
          value.forEach((item, index) => {
            if (typeof item === 'string') {
              const matchScore = searchTerms.reduce((score, term) => {
                return score + (item.toLowerCase().includes(term) ? 1 : 0);
              }, 0);
              
              if (matchScore > 0) {
                results.push({
                  path: `${currentPath.join('.')}[${index}]`,
                  content: item,
                  score: matchScore,
                  context: 'array'
                });
              }
            }
          });
        } else if (typeof value === 'object' && value !== null) {
          searchObject(value, currentPath);
        }
      }
    };

    searchObject(this.contextMemory);
    return results.sort((a, b) => b.score - a.score);
  }

  /**
   * Get troubleshooting guidance for specific issues
   * @param {string} issue - The issue description
   * @returns {Object} Troubleshooting guidance
   */
  getTroubleshootingGuidance(issue) {
    const issueKeywords = issue.toLowerCase();
    
    // Frontend build issues
    if (issueKeywords.includes('vite') || issueKeywords.includes('css') || issueKeywords.includes('postcss')) {
      return {
        category: 'Frontend Build Issues',
        guidance: this.contextMemory.troubleshootingPatterns.frontendBuildIssues,
        memoryKeyword: 'TELLEVO_VITE6_CSS_PIPELINE',
        relatedFiles: ['vite.config.js', 'postcss.config.js', 'tailwind.config.js']
      };
    }

    // Backend connectivity
    if (issueKeywords.includes('postgresql') || issueKeywords.includes('cors') || issueKeywords.includes('401') || issueKeywords.includes('403')) {
      return {
        category: 'Backend Connectivity',
        guidance: this.contextMemory.troubleshootingPatterns.backendConnectivity,
        memoryKeyword: 'TELLEVO_BACKEND_CONNECTION',
        relatedFiles: ['application.properties', 'SecurityConfig.java', 'JwtAuthenticationFilter.java']
      };
    }

    // Production deployment
    if (issueKeywords.includes('production') || issueKeywords.includes('deploy') || issueKeywords.includes('nginx')) {
      return {
        category: 'Production Deployment',
        guidance: this.contextMemory.troubleshootingPatterns.productionDeployment,
        memoryKeyword: 'TELLEVO_PRODUCTION_DEPLOY',
        relatedFiles: ['.env.production', 'nginx.conf', 'ecosystem.config.cjs']
      };
    }

    return {
      category: 'General',
      guidance: 'No specific troubleshooting pattern found. Check the full context memory.',
      suggestion: 'Use searchContext() method to find relevant information.'
    };
  }

  /**
   * Get development workflow guidance
   * @param {string} task - The development task
   * @returns {Object} Development guidance
   */
  getDevelopmentGuidance(task) {
    const taskKeywords = task.toLowerCase();

    if (taskKeywords.includes('setup') || taskKeywords.includes('install')) {
      return {
        task: 'Project Setup',
        steps: this.contextMemory.developmentPatterns.quickStart,
        commands: this.contextMemory.developmentPatterns.commonCommands,
        nextSteps: ['Verify backend health check', 'Test frontend login', 'Check PostgreSQL connection']
      };
    }

    if (taskKeywords.includes('empresa') || taskKeywords.includes('company')) {
      return {
        task: 'Company Management Development',
        architecture: this.contextMemory.componentArchitecture.flow,
        keyFeatures: this.contextMemory.componentArchitecture.keyFeatures,
        apiEndpoints: this.contextMemory.apiEndpoints.empresas
      };
    }

    if (taskKeywords.includes('auth') || taskKeywords.includes('login')) {
      return {
        task: 'Authentication Development',
        pattern: this.contextMemory.architecturePatterns.security,
        apiEndpoints: this.contextMemory.apiEndpoints.authentication,
        implementation: 'JWT Bearer tokens with localStorage persistence'
      };
    }

    return {
      task: 'General Development',
      suggestion: 'Use getContext() method to get specific area information.',
      availableContexts: Object.keys(this.contextMemory)
    };
  }

  /**
   * Generate comprehensive system overview
   * @returns {Object} Complete system overview
   */
  getSystemOverview() {
    return {
      project: this.contextMemory.projectInfo,
      architecture: {
        summary: "Hybrid database architecture with Vue.js frontend and Spring Boot backend",
        keyBenefits: [
          "40% faster development startup time",
          "Real-time logo preview with SVG validation", 
          "Type-safe JPA Repository pattern",
          "JWT authentication with route guards"
        ],
        scalabilityNotes: "PostgreSQL-driven business data, H2 for development speed"
      },
      techStack: this.contextMemory.techStack,
      developmentWorkflow: this.contextMemory.developmentPatterns,
        monitoringKeywords: [
          "TELLEVO_ADMIN_HYBRID_MULTIDB",
          "TELLEVO_VITE6_CSS_PIPELINE",
          "TELLEVO_BACKEND_CONNECTION",
          "TELLEVO_PRODUCTION_DEPLOY",
          "TELLEVO_VENTAS_STREAMING"
        ],
        streamingFeatures: {
          type: "gRPC-WebSocket Proxy Architecture",
          technologies: "Spring Boot WebSocket + gRPC Client + Vue.js real-time dashboard",
          mobileFirst: "Responsive cards/table with live updates, pause/resume controls",
          performance: "Virtual scrolling, debounced updates, reconnection logic, memory management",
          configuration: "Environment variables GRPC_VENTAS_HOST/GRPC_VENTAS_PORT with defaults",
          components: "VentasStreamView, StreamStatusIndicator, VentasStreamCard, VentasStreamTable, SortIndicator",
          navigation: "Integrated in all DrawerLayout variants (mobile/desktop/collapsed)",
          dataFormat: "{id, email, nombre_empresa, fecha_envio} with live validation",
          route: "/dashboard/ventas-stream accessible from navigation menu",
          websocket: "ws://localhost:8080/ws/ventas with authentication bypass",
          features: "Real-time stats, filtering, sorting, clipboard integration, live indicators"
        }
    };
  }
}

// ES Module export
export default TelLevoLLMContextMemory;
export { TelLevoLLMContextMemory };

// Browser/ES6 module support
if (typeof window !== 'undefined') {
  window.TelLevoLLMContextMemory = TelLevoLLMContextMemory;
}

// Example usage and testing
if (typeof window !== 'undefined' || typeof global !== 'undefined') {
  const contextMemory = new TelLevoLLMContextMemory();
  
  // Expose global methods for easy access
  const TelLevoAI = {
    getContext: (area) => contextMemory.getContext(area),
    search: (query) => contextMemory.searchContext(query),
    troubleshoot: (issue) => contextMemory.getTroubleshootingGuidance(issue),
    develop: (task) => contextMemory.getDevelopmentGuidance(task),
    overview: () => contextMemory.getSystemOverview()
  };

  if (typeof window !== 'undefined') {
    window.TelLevoAI = TelLevoAI;
  } else if (typeof global !== 'undefined') {
    global.TelLevoAI = TelLevoAI;
  }
}

/**
 * Example Usage:
 * 
 * // Get specific context
 * const archInfo = TelLevoAI.getContext('architecture');
 * 
 * // Search for information
 * const results = TelLevoAI.search('PostgreSQL JWT authentication');
 * 
 * // Get troubleshooting help
 * const help = TelLevoAI.troubleshoot('vite build failing with CSS errors');
 * 
 * // Get development guidance
 * const guidance = TelLevoAI.develop('setting up empresa management');
 * 
 * // Get complete system overview
 * const overview = TelLevoAI.overview();
 */
