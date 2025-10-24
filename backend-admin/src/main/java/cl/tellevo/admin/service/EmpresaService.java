package cl.tellevo.admin.service;

import cl.tellevo.admin.dto.EmpresaRequest;
import cl.tellevo.admin.dto.EmpresaResponse;
import cl.tellevo.admin.dto.EmpresaUploadRequest;
import cl.tellevo.admin.entity.Empresa;
import cl.tellevo.admin.repository.EmpresaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmpresaService {

    private static final Logger logger = LoggerFactory.getLogger(EmpresaService.class);

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Value("${app.images.url.path}")
    private String imagesBaseUrl;

    /**
     * Create a new empresa
     * @param request the empresa request data
     * @return EmpresaResponse with created empresa data
     */
    public EmpresaResponse crearEmpresa(EmpresaRequest request) {
        logger.info("Attempting to create empresa with dominio: {}", request.getDominio());

        // Check if dominio already exists
        if (empresaRepository.existsByDominio(request.getDominio().trim())) {
            logger.warn("Empresa with dominio {} already exists", request.getDominio());
            throw new IllegalArgumentException("Ya existe una empresa con este dominio");
        }

        try {
            Empresa empresa = new Empresa(
                request.getNombre().trim(),
                request.getDominio().trim(),
                request.getLogoUrl().trim()
            );

            Empresa savedEmpresa = empresaRepository.save(empresa);
            logger.info("Empresa created successfully with ID: {}", savedEmpresa.getId());

            return new EmpresaResponse(
                savedEmpresa.getId(),
                savedEmpresa.getNombre(),
                savedEmpresa.getCodigoPais(),
                savedEmpresa.getDominio(),
                savedEmpresa.getLogoUrl(),
                "Empresa creada exitosamente"
            );
        } catch (Exception e) {
            logger.error("Database error while creating empresa: {}", e.getMessage(), e);

            if (e.getMessage().contains("duplicate key value") || e.getMessage().contains("constraint")) {
                throw new IllegalArgumentException("Ya existe una empresa con este dominio");
            }

            throw new RuntimeException("Error al crear la empresa", e);
        }
    }

    /**
     * Get all empresas
     * @return List of EmpresaResponse
     */
    public List<EmpresaResponse> obtenerTodasEmpresas() {
        logger.debug("Retrieving all empresas");

        List<Empresa> empresas = empresaRepository.findAll();
        List<EmpresaResponse> response = empresas.stream()
            .map(empresa -> new EmpresaResponse(
                empresa.getId(),
                empresa.getNombre(),
                empresa.getCodigoPais(),
                empresa.getDominio(),
                empresa.getLogoUrl()
            ))
            .collect(Collectors.toList());

        logger.info("Found {} empresas", response.size());
        return response;
    }

    /**
     * Get empresa by ID
     * @param id the empresa ID
     * @return EmpresaResponse if found
     */
    public EmpresaResponse obtenerEmpresaPorId(Long id) {
        logger.debug("Retrieving empresa with ID: {}", id);

        Empresa empresa = empresaRepository.findById(id)
            .orElseThrow(() -> {
                logger.warn("Empresa with ID {} not found", id);
                return new IllegalArgumentException("Empresa no encontrada");
            });

        logger.info("Empresa found with ID: {}", id);
        return new EmpresaResponse(
            empresa.getId(),
            empresa.getNombre(),
            empresa.getCodigoPais(),
            empresa.getDominio(),
            empresa.getLogoUrl()
        );
    }

    /**
     * Get empresa by dominio
     * @param dominio the empresa dominio
     * @return EmpresaResponse if found
     */
    public EmpresaResponse obtenerEmpresaPorDominio(String dominio) {
        logger.debug("Retrieving empresa with dominio: {}", dominio);

        Empresa empresa = empresaRepository.findByDominio(dominio.trim())
            .orElseThrow(() -> {
                logger.warn("Empresa with dominio {} not found", dominio);
                return new IllegalArgumentException("Empresa no encontrada");
            });

        logger.info("Empresa found with dominio: {}", dominio);
        return new EmpresaResponse(
            empresa.getId(),
            empresa.getNombre(),
            empresa.getCodigoPais(),
            empresa.getDominio(),
            empresa.getLogoUrl()
        );
    }

    /**
     * Update empresa
     * @param id the empresa ID
     * @param request the updated empresa data
     * @return EmpresaResponse with updated empresa data
     */
    public EmpresaResponse actualizarEmpresa(Long id, EmpresaRequest request) {
        logger.info("Attempting to update empresa with ID: {}", id);

        Empresa existingEmpresa = empresaRepository.findById(id)
            .orElseThrow(() -> {
                logger.warn("Empresa with ID {} not found for update", id);
                return new IllegalArgumentException("Empresa no encontrada");
            });

        // Check if new dominio conflicts with existing empresas (excluding current one)
        String newDominio = request.getDominio().trim();
        if (!existingEmpresa.getDominio().equals(newDominio) &&
            empresaRepository.existsByDominio(newDominio)) {
            logger.warn("Empresa with dominio {} already exists", newDominio);
            throw new IllegalArgumentException("Ya existe una empresa con este dominio");
        }

        try {
            existingEmpresa.setNombre(request.getNombre().trim());
            existingEmpresa.setDominio(newDominio);
            existingEmpresa.setLogoUrl(request.getLogoUrl().trim());

            Empresa updatedEmpresa = empresaRepository.save(existingEmpresa);
            logger.info("Empresa updated successfully with ID: {}", id);

            return new EmpresaResponse(
                updatedEmpresa.getId(),
                updatedEmpresa.getNombre(),
                updatedEmpresa.getCodigoPais(),
                updatedEmpresa.getDominio(),
                updatedEmpresa.getLogoUrl(),
                "Empresa actualizada exitosamente"
            );
        } catch (Exception e) {
            logger.error("Database error while updating empresa with ID {}: {}", id, e.getMessage(), e);

            if (e.getMessage().contains("duplicate key value") || e.getMessage().contains("constraint")) {
                throw new IllegalArgumentException("Ya existe una empresa con este dominio");
            }

            throw new RuntimeException("Error al actualizar la empresa", e);
        }
    }

    /**
     * Delete empresa
     * @param id the empresa ID to delete
     * @return EmpresaResponse with success message
     */
    public EmpresaResponse eliminarEmpresa(Long id) {
        logger.info("Attempting to delete empresa with ID: {}", id);

        Empresa empresa = empresaRepository.findById(id)
            .orElseThrow(() -> {
                logger.warn("Empresa with ID {} not found for deletion", id);
                return new IllegalArgumentException("Empresa no encontrada");
            });

        try {
            // Delete associated files first
            fileStorageService.deleteEmpresaFiles(id);

            empresaRepository.deleteById(id);
            logger.info("Empresa deleted successfully with ID: {}", id);
            return new EmpresaResponse("Empresa eliminada exitosamente");
        } catch (Exception e) {
            logger.error("Database error while deleting empresa with ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error al eliminar la empresa", e);
        }
    }

    /**
     * Create empresa with file upload support
     * @param request the empresa upload request data
     * @return EmpresaResponse with created empresa data
     */
    public EmpresaResponse crearEmpresaConArchivo(EmpresaUploadRequest request) {
        logger.info("Attempting to create empresa with file upload: {}", request.getDominio());

        // Check if dominio already exists
        if (empresaRepository.existsByDominio(request.getDominio().trim())) {
            logger.warn("Empresa with dominio {} already exists", request.getDominio());
            throw new IllegalArgumentException("Ya existe una empresa con este dominio");
        }

        try {
            String logoUrl = processLogo(request);
            Empresa empresa = new Empresa(
                request.getNombre().trim(),
                request.getDominio().trim(),
                logoUrl
            );

            Empresa savedEmpresa = empresaRepository.save(empresa);
            logger.info("Empresa created successfully with ID: {}", savedEmpresa.getId());

            return new EmpresaResponse(
                savedEmpresa.getId(),
                savedEmpresa.getNombre(),
                savedEmpresa.getCodigoPais(),
                savedEmpresa.getDominio(),
                savedEmpresa.getLogoUrl(),
                "Empresa creada exitosamente"
            );
        } catch (Exception e) {
            logger.error("Database error while creating empresa: {}", e.getMessage(), e);
            throw new RuntimeException("Error al crear la empresa", e);
        }
    }

    /**
     * Update empresa with file upload support
     * @param id the empresa ID
     * @param request the updated empresa data
     * @return EmpresaResponse with updated empresa data
     */
    public EmpresaResponse actualizarEmpresaConArchivo(Long id, EmpresaUploadRequest request) {
        logger.info("Attempting to update empresa with ID: {} and file upload", id);

        Empresa existingEmpresa = empresaRepository.findById(id)
            .orElseThrow(() -> {
                logger.warn("Empresa with ID {} not found for update", id);
                return new IllegalArgumentException("Empresa no encontrada");
            });

        // Check if new dominio conflicts with existing empresas (excluding current one)
        String newDominio = request.getDominio().trim();
        if (!existingEmpresa.getDominio().equals(newDominio) &&
            empresaRepository.existsByDominio(newDominio)) {
            logger.warn("Empresa with dominio {} already exists", newDominio);
            throw new IllegalArgumentException("Ya existe una empresa con este dominio");
        }

        try {
            String logoUrl = processLogoForUpdate(request, existingEmpresa);
            existingEmpresa.setNombre(request.getNombre().trim());
            existingEmpresa.setDominio(newDominio);
            existingEmpresa.setLogoUrl(logoUrl);

            Empresa updatedEmpresa = empresaRepository.save(existingEmpresa);
            logger.info("Empresa updated successfully with ID: {}", id);

            return new EmpresaResponse(
                updatedEmpresa.getId(),
                updatedEmpresa.getNombre(),
                updatedEmpresa.getCodigoPais(),
                updatedEmpresa.getDominio(),
                updatedEmpresa.getLogoUrl(),
                "Empresa actualizada exitosamente"
            );
        } catch (Exception e) {
            logger.error("Database error while updating empresa with ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error al actualizar la empresa", e);
        }
    }

    /**
     * Process logo from upload request
     * @param request the upload request
     * @return the logo URL
     */
    private String processLogo(EmpresaUploadRequest request) {
        if (request.hasLogoFile()) {
            // Handle file upload - will be processed after empresa creation
            // Return placeholder URL for now, will be updated after file storage
            return "file://placeholder";
        } else if (request.hasLogoUrl()) {
            return request.getLogoUrl().trim();
        } else {
            throw new IllegalArgumentException("Debe proporcionar una URL de logo o un archivo de imagen");
        }
    }

    /**
     * Process logo for update operation
     * @param request the upload request
     * @param existingEmpresa the existing empresa
     * @return the logo URL
     */
    private String processLogoForUpdate(EmpresaUploadRequest request, Empresa existingEmpresa) {
        if (request.hasLogoFile()) {
            // Handle file upload - will be processed after empresa update
            String url = imagesBaseUrl+request.getLogoFile().getOriginalFilename();
            return url;
        } else if (request.hasLogoUrl()) {
            return request.getLogoUrl().trim();
        } else {
            // Keep existing logo URL if no new logo provided
            return existingEmpresa.getLogoUrl();
        }
    }

    /**
     * Store uploaded file and update empresa logo URL
     * @param empresaId the empresa ID
     * @param file the uploaded file
     * @return the file URL
     */
    public String storeEmpresaLogo(Long empresaId, MultipartFile file) {
        logger.info("Storing logo file for empresa ID: {}", empresaId);

        String relativePath = fileStorageService.storeFile(file, empresaId);
        String fileUrl = imagesBaseUrl + "/" + relativePath;

        // Update empresa with the file URL
        Empresa empresa = empresaRepository.findById(empresaId)
            .orElseThrow(() -> new IllegalArgumentException("Empresa no encontrada"));

        empresa.setLogoUrl(fileUrl);
        empresaRepository.save(empresa);

        logger.info("Logo stored and empresa updated for ID: {}", empresaId);
        return fileUrl;
    }
}
