package cl.tellevo.admin.repository;

import cl.tellevo.admin.entity.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    /**
     * Find empresa by dominio
     * @param dominio the dominio to search for
     * @return Optional containing the empresa if found
     */
    Optional<Empresa> findByDominio(String dominio);

    /**
     * Check if an empresa exists with the given dominio
     * @param dominio the dominio to check
     * @return true if exists, false otherwise
     */
    boolean existsByDominio(String dominio);

    /**
     * Find empresa by nombre
     * @param nombre the nombre to search for
     * @return Optional containing the empresa if found
     */
    Optional<Empresa> findByNombre(String nombre);
}
