package cl.tellevo.admin.controller;

import cl.tellevo.admin.dto.EmpresaRequest;
import cl.tellevo.admin.dto.EmpresaResponse;
import cl.tellevo.admin.service.EmpresaService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/empresas")
@CrossOrigin(origins = "http://localhost:5173")
public class EmpresaController {

    private static final Logger logger = LoggerFactory.getLogger(EmpresaController.class);

    @Autowired
    private EmpresaService empresaService;

    /**
     * Create a new empresa
     * @param request empresa data
     * @return ResponseEntity with created empresa data
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> crearEmpresa(@Valid @RequestBody EmpresaRequest request) {
        try {
            logger.info("POST /api/empresas - Creating empresa with dominio: {}", request.getDominio());

            EmpresaResponse response = empresaService.crearEmpresa(request);

            logger.info("Empresa created successfully with ID: {}", response.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            logger.warn("Business rule violation: {}", e.getMessage());

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

        } catch (Exception e) {
            logger.error("Unexpected error while creating empresa: {}", e.getMessage(), e);

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error interno del servidor al crear la empresa");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Get all empresas
     * @return ResponseEntity with list of empresas
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> obtenerTodasEmpresas() {
        try {
            logger.info("GET /api/empresas - Retrieving all empresas");

            List<EmpresaResponse> empresas = empresaService.obtenerTodasEmpresas();

            logger.info("Retrieved {} empresas", empresas.size());
            return ResponseEntity.ok(empresas);

        } catch (Exception e) {
            logger.error("Unexpected error while retrieving empresas: {}", e.getMessage(), e);

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error interno del servidor al obtener las empresas");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Get empresa by ID
     * @param id empresa ID
     * @return ResponseEntity with empresa data
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> obtenerEmpresaPorId(@PathVariable Long id) {
        try {
            logger.info("GET /api/empresas/{} - Retrieving empresa", id);

            EmpresaResponse response = empresaService.obtenerEmpresaPorId(id);

            logger.info("Empresa retrieved successfully with ID: {}", id);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            logger.warn("Empresa with ID {} not found", id);

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);

        } catch (Exception e) {
            logger.error("Unexpected error while retrieving empresa with ID {}: {}", id, e.getMessage(), e);

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error interno del servidor al obtener la empresa");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Get empresa by dominio
     * @param dominio empresa dominio
     * @return ResponseEntity with empresa data
     */
    @GetMapping("/dominio/{dominio}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> obtenerEmpresaPorDominio(@PathVariable String dominio) {
        try {
            logger.info("GET /api/empresas/dominio/{} - Retrieving empresa", dominio);

            EmpresaResponse response = empresaService.obtenerEmpresaPorDominio(dominio);

            logger.info("Empresa retrieved successfully with dominio: {}", dominio);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            logger.warn("Empresa with dominio {} not found", dominio);

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);

        } catch (Exception e) {
            logger.error("Unexpected error while retrieving empresa with dominio {}: {}", dominio, e.getMessage(), e);

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error interno del servidor al obtener la empresa");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Update empresa by ID
     * @param id empresa ID
     * @param request updated empresa data
     * @return ResponseEntity with updated empresa data
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> actualizarEmpresa(@PathVariable Long id, @Valid @RequestBody EmpresaRequest request) {
        try {
            logger.info("PUT /api/empresas/{} - Updating empresa", id);

            EmpresaResponse response = empresaService.actualizarEmpresa(id, request);

            logger.info("Empresa updated successfully with ID: {}", id);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            logger.warn("Business rule violation while updating empresa {}: {}", id, e.getMessage());

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

        } catch (Exception e) {
            logger.error("Unexpected error while updating empresa with ID {}: {}", id, e.getMessage(), e);

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error interno del servidor al actualizar la empresa");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Delete empresa by ID
     * @param id empresa ID to delete
     * @return ResponseEntity with success message
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> eliminarEmpresa(@PathVariable Long id) {
        try {
            logger.info("DELETE /api/empresas/{} - Deleting empresa", id);

            EmpresaResponse response = empresaService.eliminarEmpresa(id);

            logger.info("Empresa deleted successfully with ID: {}", id);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            logger.warn("Empresa with ID {} not found for deletion", id);

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);

        } catch (Exception e) {
            logger.error("Unexpected error while deleting empresa with ID {}: {}", id, e.getMessage(), e);

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error interno del servidor al eliminar la empresa");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Health check for empresa service
     * @return ResponseEntity with status
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("service", "Empresa Controller");
        response.put("status", "UP");
        response.put("message", "Empresa service is running");
        return ResponseEntity.ok(response);
    }
}
