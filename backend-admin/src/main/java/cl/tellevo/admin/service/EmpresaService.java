package cl.tellevo.admin.service;

import cl.tellevo.admin.dto.EmpresaRequest;
import cl.tellevo.admin.dto.EmpresaResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmpresaService {

    private static final Logger logger = LoggerFactory.getLogger(EmpresaService.class);

    @Value("${postgres.url}")
    private String postgresUrl;

    @Value("${postgres.username}")
    private String postgresUsername;

    @Value("${postgres.password}")
    private String postgresPassword;

    private Connection getPostgresConnection() throws SQLException {
        try {
            logger.debug("Connecting to PostgreSQL database: {}", postgresUrl);
            return DriverManager.getConnection(postgresUrl, postgresUsername, postgresPassword);
        } catch (SQLException e) {
            logger.error("Failed to connect to PostgreSQL database: {}", e.getMessage());
            throw new RuntimeException("Error connecting to PostgreSQL database", e);
        }
    }

    /**
     * Create enterprise table in PostgreSQL if it doesn't exist
     */
    private void crearTablaEmpresaSiNoExiste() {
        // Create table if not exists
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS empresa (
                id BIGSERIAL PRIMARY KEY,
                nombre VARCHAR(255) NOT NULL,
                codigo_pais VARCHAR(2) NOT NULL DEFAULT 'CL',
                dominio VARCHAR(255) NOT NULL UNIQUE,
                logo_url VARCHAR(500) NOT NULL
            );
            """;

        // Create index for better performance
        String createIndexSQL = """
            CREATE INDEX IF NOT EXISTS idx_empresa_dominio
            ON empresa(dominio);
            """;

        // Reset sequence to avoid conflicts (smart insertion approach)
        String resetSequenceSQL = """
            SELECT setval('empresa_id_seq', COALESCE((SELECT MAX(id) FROM empresa), 0) + 1, false);
            """;

        try (Connection conn = getPostgresConnection()) {

            // Execute table creation
            try (PreparedStatement stmt = conn.prepareStatement(createTableSQL)) {
                stmt.execute();
                logger.info("Empresa table created or already exists");
            }

            // Execute index creation
            try (PreparedStatement stmt = conn.prepareStatement(createIndexSQL)) {
                stmt.execute();
                logger.debug("Empresa index created or already exists");
            }

            // Reset sequence to ensure next ID is correct
            try (PreparedStatement stmt = conn.prepareStatement(resetSequenceSQL)) {
                stmt.execute();
                logger.debug("Empresa ID sequence reset successfully");
            }

        } catch (SQLException e) {
            logger.error("Error setting up empresa table: {}", e.getMessage());
            throw new RuntimeException("Error setting up empresa table", e);
        }
    }

    /**
     * Create a new empresa
     * @param request the empresa request data
     * @return EmpresaResponse with created empresa data
     */
    public EmpresaResponse crearEmpresa(EmpresaRequest request) {
        // Ensure table exists
        crearTablaEmpresaSiNoExiste();

        try (Connection conn = getPostgresConnection()) {
            logger.info("Attempting to create empresa with dominio: {}", request.getDominio());

            // Check if dominio already exists
            if (existeDominio(request.getDominio())) {
                logger.warn("Empresa with dominio {} already exists", request.getDominio());
                throw new IllegalArgumentException("Ya existe una empresa con este dominio");
            }

            String insertSQL = """
                INSERT INTO empresa (nombre, codigo_pais, dominio, logo_url)
                VALUES (?, ?, ?, ?)
                RETURNING id
                """;

            try (PreparedStatement stmt = conn.prepareStatement(insertSQL)) {
                stmt.setString(1, request.getNombre().trim());
                stmt.setString(2, "CL");
                stmt.setString(3, request.getDominio().trim());
                stmt.setString(4, request.getLogoUrl().trim());

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    long empresaId = rs.getLong("id");
                    logger.info("Empresa created successfully with ID: {}", empresaId);

                    return new EmpresaResponse(
                        empresaId,
                        request.getNombre(),
                        "CL",
                        request.getDominio(),
                        request.getLogoUrl(),
                        "Empresa creada exitosamente"
                    );
                } else {
                    throw new RuntimeException("Failed to create empresa");
                }
            }

        } catch (SQLException e) {
            logger.error("Database error while creating empresa: {}", e.getMessage(), e);

            if (e.getMessage().contains("duplicate key value")) {
                throw new IllegalArgumentException("Ya existe una empresa con este dominio");
            }

            throw new RuntimeException("Error al crear la empresa", e);
        }
    }

    private boolean existeDominio(String dominio) {
        String selectSQL = "SELECT COUNT(*) FROM empresa WHERE dominio = ?";

        try (Connection conn = getPostgresConnection();
             PreparedStatement stmt = conn.prepareStatement(selectSQL)) {

            stmt.setString(1, dominio);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;

        } catch (SQLException e) {
            logger.error("Error checking dominio existence: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Get all empresas
     * @return List of EmpresaResponse
     */
    public List<EmpresaResponse> obtenerTodasEmpresas() {
        // Ensure table exists
        crearTablaEmpresaSiNoExiste();

        List<EmpresaResponse> empresas = new ArrayList<>();
        String selectSQL = "SELECT id, nombre, codigo_pais, dominio, logo_url FROM empresa ORDER BY id";

        try (Connection conn = getPostgresConnection();
             PreparedStatement stmt = conn.prepareStatement(selectSQL);
             ResultSet rs = stmt.executeQuery()) {

            logger.debug("Retrieving all empresas");

            while (rs.next()) {
                empresas.add(new EmpresaResponse(
                    rs.getLong("id"),
                    rs.getString("nombre"),
                    rs.getString("codigo_pais"),
                    rs.getString("dominio"),
                    rs.getString("logo_url")
                ));
            }

            logger.info("Found {} empresas", empresas.size());
            return empresas;

        } catch (SQLException e) {
            logger.error("Error retrieving all empresas: {}", e.getMessage(), e);
            throw new RuntimeException("Error al obtener las empresas", e);
        }
    }

    /**
     * Get empresa by ID
     * @param id the empresa ID
     * @return EmpresaResponse if found
     */
    public EmpresaResponse obtenerEmpresaPorId(Long id) {
        // Ensure table exists
        crearTablaEmpresaSiNoExiste();

        try (Connection conn = getPostgresConnection()) {
            logger.debug("Retrieving empresa with ID: {}", id);

            String selectSQL = "SELECT id, nombre, codigo_pais, dominio, logo_url FROM empresa WHERE id = ?";

            try (PreparedStatement stmt = conn.prepareStatement(selectSQL)) {
                stmt.setLong(1, id);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    EmpresaResponse empresa = new EmpresaResponse(
                        rs.getLong("id"),
                        rs.getString("nombre"),
                        rs.getString("codigo_pais"),
                        rs.getString("dominio"),
                        rs.getString("logo_url")
                    );

                    logger.info("Empresa found with ID: {}", id);
                    return empresa;
                } else {
                    logger.warn("Empresa with ID {} not found", id);
                    throw new IllegalArgumentException("Empresa no encontrada");
                }
            }

        } catch (SQLException e) {
            logger.error("Error retrieving empresa with ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error al obtener la empresa", e);
        }
    }

    /**
     * Get empresa by dominio
     * @param dominio the empresa dominio
     * @return EmpresaResponse if found
     */
    public EmpresaResponse obtenerEmpresaPorDominio(String dominio) {
        // Ensure table exists
        crearTablaEmpresaSiNoExiste();

        try (Connection conn = getPostgresConnection()) {
            logger.debug("Retrieving empresa with dominio: {}", dominio);

            String selectSQL = "SELECT id, nombre, codigo_pais, dominio, logo_url FROM empresa WHERE dominio = ?";

            try (PreparedStatement stmt = conn.prepareStatement(selectSQL)) {
                stmt.setString(1, dominio);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    EmpresaResponse empresa = new EmpresaResponse(
                        rs.getLong("id"),
                        rs.getString("nombre"),
                        rs.getString("codigo_pais"),
                        rs.getString("dominio"),
                        rs.getString("logo_url")
                    );

                    logger.info("Empresa found with dominio: {}", dominio);
                    return empresa;
                } else {
                    logger.warn("Empresa with dominio {} not found", dominio);
                    throw new IllegalArgumentException("Empresa no encontrada");
                }
            }

        } catch (SQLException e) {
            logger.error("Error retrieving empresa with dominio {}: {}", dominio, e.getMessage(), e);
            throw new RuntimeException("Error al obtener la empresa", e);
        }
    }

    /**
     * Update empresa
     * @param id the empresa ID
     * @param request the updated empresa data
     * @return EmpresaResponse with updated empresa data
     */
    public EmpresaResponse actualizarEmpresa(Long id, EmpresaRequest request) {
        // Ensure table exists
        crearTablaEmpresaSiNoExiste();

        try (Connection conn = getPostgresConnection()) {
            logger.info("Attempting to update empresa with ID: {}", id);

            // Check if new dominio conflicts with existing empresas (excluding current one)
            if (existeDominio(request.getDominio())) {
                String checkSQL = "SELECT id FROM empresa WHERE dominio = ?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkSQL)) {
                    checkStmt.setString(1, request.getDominio());
                    ResultSet rs = checkStmt.executeQuery();

                    if (rs.next() && !id.equals(rs.getLong("id"))) {
                        logger.warn("Empresa with dominio {} already exists", request.getDominio());
                        throw new IllegalArgumentException("Ya existe una empresa con este dominio");
                    }
                }
            }

            String updateSQL = """
                UPDATE empresa
                SET nombre = ?, dominio = ?, logo_url = ?
                WHERE id = ?
                """;

            try (PreparedStatement stmt = conn.prepareStatement(updateSQL)) {
                stmt.setString(1, request.getNombre().trim());
                stmt.setString(2, request.getDominio().trim());
                stmt.setString(3, request.getLogoUrl().trim());
                stmt.setLong(4, id);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    logger.info("Empresa updated successfully with ID: {}", id);
                    return new EmpresaResponse(
                        id,
                        request.getNombre(),
                        "CL",
                        request.getDominio(),
                        request.getLogoUrl(),
                        "Empresa actualizada exitosamente"
                    );
                } else {
                    logger.warn("Empresa with ID {} not found for update", id);
                    throw new IllegalArgumentException("Empresa no encontrada");
                }
            }

        } catch (SQLException e) {
            logger.error("Database error while updating empresa with ID {}: {}", id, e.getMessage(), e);

            if (e.getMessage().contains("duplicate key value")) {
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
        // Ensure table exists
        crearTablaEmpresaSiNoExiste();

        try (Connection conn = getPostgresConnection()) {
            logger.info("Attempting to delete empresa with ID: {}", id);

            String deleteSQL = "DELETE FROM empresa WHERE id = ?";

            try (PreparedStatement stmt = conn.prepareStatement(deleteSQL)) {
                stmt.setLong(1, id);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    logger.info("Empresa deleted successfully with ID: {}", id);
                    return new EmpresaResponse("Empresa eliminada exitosamente");
                } else {
                    logger.warn("Empresa with ID {} not found for deletion", id);
                    throw new IllegalArgumentException("Empresa no encontrada");
                }
            }

        } catch (SQLException e) {
            logger.error("Database error while deleting empresa with ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error al eliminar la empresa", e);
        }
    }
}
