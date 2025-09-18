package cl.tellevo.admin.service;

import cl.tellevo.admin.dto.EmpresaRequest;
import cl.tellevo.admin.dto.EmpresaResponse;
import cl.tellevo.admin.entity.Empresa;
import cl.tellevo.admin.repository.EmpresaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmpresaService {

    private static final Logger logger = LoggerFactory.getLogger(EmpresaService.class);

    @Autowired
    private EmpresaRepository empresaRepository;

    /**
     * Create a new empresa
     * @param request the empresa request data
     * @return EmpresaResponse with created empresa data
     */
    @Transactional
    public EmpresaResponse crearEmpresa(EmpresaRequest request) {
        try {
            logger.info("Attempting to create empresa with dominio: {}", request.getDominio());

            // Check if dominio already exists
            if (empresaRepository.existsByDominio(request.getDominio())) {
                logger.warn("Empresa with dominio {} already exists", request.getDominio());
                throw new IllegalArgumentException("Ya existe una empresa con este dominio");
            }

            // Create new empresa
            Empresa empresa = new Empresa();
            empresa.setNombre(request.getNombre());
            empresa.setCodigoPais("CL"); // Always set to CL
            empresa.setDominio(request.getDominio());
            empresa.setLogoUrl(request.getLogoUrl());

            // Save empresa
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

        } catch (DataIntegrityViolationException e) {
            logger.error("Data integrity violation while creating empresa: {}", e.getMessage());
            throw new IllegalArgumentException("Error de integridad de datos. Posiblemente el dominio ya existe.");
        } catch (Exception e) {
            logger.error("Unexpected error while creating empresa: {}", e.getMessage(), e);
            throw new RuntimeException("Error inesperado al crear la empresa");
        }
    }

    /**
     * Get all empresas
     * @return List of EmpresaResponse
     */
    @Transactional(readOnly = true)
    public List<EmpresaResponse> obtenerTodasEmpresas() {
        try {
            logger.debug("Retrieving all empresas");

            List<Empresa> empresas = empresaRepository.findAll();
            logger.info("Found {} empresas", empresas.size());

            return empresas.stream()
                .map(empresa -> new EmpresaResponse(
                    empresa.getId(),
                    empresa.getNombre(),
                    empresa.getCodigoPais(),
                    empresa.getDominio(),
                    empresa.getLogoUrl()
                ))
                .collect(Collectors.toList());

        } catch (Exception e) {
            logger.error("Error retrieving all empresas: {}", e.getMessage(), e);
            throw new RuntimeException("Error al obtener las empresas");
        }
    }

    /**
     * Get empresa by ID
     * @param id the empresa ID
     * @return EmpresaResponse if found
     */
    @Transactional(readOnly = true)
    public EmpresaResponse obtenerEmpresaPorId(Long id) {
        try {
            logger.debug("Retrieving empresa with ID: {}", id);

            Optional<Empresa> empresaOpt = empresaRepository.findById(id);

            if (empresaOpt.isPresent()) {
                Empresa empresa = empresaOpt.get();
                logger.info("Empresa found with ID: {}", id);

                return new EmpresaResponse(
                    empresa.getId(),
                    empresa.getNombre(),
                    empresa.getCodigoPais(),
                    empresa.getDominio(),
                    empresa.getLogoUrl()
                );
            } else {
                logger.warn("Empresa with ID {} not found", id);
                throw new IllegalArgumentException("Empresa no encontrada");
            }

        } catch (IllegalArgumentException e) {
            throw e; // Re-throw business exceptions
        } catch (Exception e) {
            logger.error("Error retrieving empresa with ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error al obtener la empresa");
        }
    }

    /**
     * Get empresa by dominio
     * @param dominio the empresa dominio
     * @return EmpresaResponse if found
     */
    @Transactional(readOnly = true)
    public EmpresaResponse obtenerEmpresaPorDominio(String dominio) {
        try {
            logger.debug("Retrieving empresa with dominio: {}", dominio);

            Optional<Empresa> empresaOpt = empresaRepository.findByDominio(dominio);

            if (empresaOpt.isPresent()) {
                Empresa empresa = empresaOpt.get();
                logger.info("Empresa found with dominio: {}", dominio);

                return new EmpresaResponse(
                    empresa.getId(),
                    empresa.getNombre(),
                    empresa.getCodigoPais(),
                    empresa.getDominio(),
                    empresa.getLogoUrl()
                );
            } else {
                logger.warn("Empresa with dominio {} not found", dominio);
                throw new IllegalArgumentException("Empresa no encontrada");
            }

        } catch (IllegalArgumentException e) {
            throw e; // Re-throw business exceptions
        } catch (Exception e) {
            logger.error("Error retrieving empresa with dominio {}: {}", dominio, e.getMessage(), e);
            throw new RuntimeException("Error al obtener la empresa");
        }
    }

    /**
     * Update empresa
     * @param id the empresa ID
     * @param request the updated empresa data
     * @return EmpresaResponse with updated empresa data
     */
    @Transactional
    public EmpresaResponse actualizarEmpresa(Long id, EmpresaRequest request) {
        try {
            logger.info("Attempting to update empresa with ID: {}", id);

            Optional<Empresa> empresaOpt = empresaRepository.findById(id);

            if (empresaOpt.isPresent()) {
                Empresa empresa = empresaOpt.get();

                // Check if new dominio conflicts with existing empresas (excluding current one)
                if (!empresa.getDominio().equals(request.getDominio()) &&
                    empresaRepository.existsByDominio(request.getDominio())) {
                    logger.warn("Empresa with dominio {} already exists", request.getDominio());
                    throw new IllegalArgumentException("Ya existe una empresa con este dominio");
                }

                // Update fields
                empresa.setNombre(request.getNombre());
                empresa.setDominio(request.getDominio());
                empresa.setLogoUrl(request.getLogoUrl());

                // Save updated empresa
                Empresa updatedEmpresa = empresaRepository.save(empresa);
                logger.info("Empresa updated successfully with ID: {}", id);

                return new EmpresaResponse(
                    updatedEmpresa.getId(),
                    updatedEmpresa.getNombre(),
                    updatedEmpresa.getCodigoPais(),
                    updatedEmpresa.getDominio(),
                    updatedEmpresa.getLogoUrl(),
                    "Empresa actualizada exitosamente"
                );
            } else {
                logger.warn("Empresa with ID {} not found for update", id);
                throw new IllegalArgumentException("Empresa no encontrada");
            }

        } catch (DataIntegrityViolationException e) {
            logger.error("Data integrity violation while updating empresa: {}", e.getMessage());
            throw new IllegalArgumentException("Error de integridad de datos");
        } catch (IllegalArgumentException e) {
            throw e; // Re-throw business exceptions
        } catch (Exception e) {
            logger.error("Unexpected error while updating empresa with ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error inesperado al actualizar la empresa");
        }
    }

    /**
     * Delete empresa
     * @param id the empresa ID to delete
     * @return EmpresaResponse with success message
     */
    @Transactional
    public EmpresaResponse eliminarEmpresa(Long id) {
        try {
            logger.info("Attempting to delete empresa with ID: {}", id);

            if (empresaRepository.existsById(id)) {
                empresaRepository.deleteById(id);
                logger.info("Empresa deleted successfully with ID: {}", id);

                return new EmpresaResponse("Empresa eliminada exitosamente");
            } else {
                logger.warn("Empresa with ID {} not found for deletion", id);
                throw new IllegalArgumentException("Empresa no encontrada");
            }

        } catch (Exception e) {
            logger.error("Error deleting empresa with ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error al eliminar la empresa");
        }
    }
}
