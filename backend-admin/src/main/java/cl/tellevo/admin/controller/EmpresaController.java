package cl.tellevo.admin.controller;

import cl.tellevo.admin.dto.EmpresaRequest;
import cl.tellevo.admin.dto.EmpresaResponse;
import cl.tellevo.admin.dto.EmpresaUploadRequest;
import cl.tellevo.admin.service.EmpresaService;
import cl.tellevo.admin.service.FileStorageService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/empresas")
public class EmpresaController {

    private static final Logger logger = LoggerFactory.getLogger(EmpresaController.class);

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private FileStorageService fileStorageService;



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
     * Create empresa with file upload
     * @param nombre empresa name
     * @param dominio empresa domain
     * @param logoFile logo file (required)
     * @return ResponseEntity with created empresa data
     */
    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> crearEmpresaConArchivo(
            @RequestParam("nombre") String nombre,
            @RequestParam("dominio") String dominio,
            @RequestParam("logoFile") MultipartFile logoFile) {

        try {
            logger.info("POST /api/empresas/upload - Creating empresa with file upload: {}", dominio);

            EmpresaUploadRequest request = new EmpresaUploadRequest(nombre, dominio, logoFile);

            EmpresaResponse response = empresaService.crearEmpresaConArchivo(request);

            logger.info("Empresa created with file upload successfully with ID: {}", response.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            logger.warn("Business rule violation: {}", e.getMessage());

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

        } catch (Exception e) {
            logger.error("Unexpected error while creating empresa with file upload: {}", e.getMessage(), e);

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error interno del servidor al crear la empresa");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Update empresa with file upload
     * @param id empresa ID
     * @param nombre empresa name
     * @param dominio empresa domain
     * @param logoFile logo file (required)
     * @return ResponseEntity with updated empresa data
     */
    @PutMapping("/{id}/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> actualizarEmpresaConArchivo(
            @PathVariable Long id,
            @RequestParam("nombre") String nombre,
            @RequestParam("dominio") String dominio,
            @RequestParam("logoFile") MultipartFile logoFile) {

        try {
            logger.info("PUT /api/empresas/{}/upload - Updating empresa with file upload", id);

            EmpresaUploadRequest request = new EmpresaUploadRequest(nombre, dominio, logoFile);

            EmpresaResponse response = empresaService.actualizarEmpresaConArchivo(id, request);

            logger.info("Empresa updated with file upload successfully with ID: {}", id);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            logger.warn("Business rule violation while updating empresa {}: {}", id, e.getMessage());

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

        } catch (Exception e) {
            logger.error("Unexpected error while updating empresa with file upload {}: {}", id, e.getMessage(), e);

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error interno del servidor al actualizar la empresa");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Upload logo for existing empresa
     * @param id empresa ID
     * @param logoFile logo file
     * @return ResponseEntity with file upload response
     */
    @PostMapping("/{id}/logo")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> subirLogoEmpresa(
            @PathVariable Long id,
            @RequestParam("logoFile") MultipartFile logoFile) {

        try {
            logger.info("POST /api/empresas/{}/logo - Uploading logo for empresa", id);

            if (logoFile == null || logoFile.isEmpty()) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", "Debe proporcionar un archivo de imagen");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }

            String fileUrl = empresaService.storeEmpresaLogo(id, logoFile);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Logo subido exitosamente");
            response.put("logoUrl", fileUrl);

            logger.info("Logo uploaded successfully for empresa ID: {}", id);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            logger.warn("Business rule violation while uploading logo for empresa {}: {}", id, e.getMessage());

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

        } catch (Exception e) {
            logger.error("Unexpected error while uploading logo for empresa {}: {}", id, e.getMessage(), e);

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error interno del servidor al subir el logo");

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
