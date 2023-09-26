package com.unexus.minegocio.repository;

import com.unexus.minegocio.entity.Client;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    List<Client> findByIdentificationNumberContaining(String identificationNumber);

    @Query(
            value = "SELECT * FROM client WHERE LOWER(client.name) LIKE LOWER(CONCAT('%', :name, '%'))",
            nativeQuery = true
    )
    List<Client> findByNameContaining(@Param("name") String name);

    Optional<Client> findByIdentificationNumber(String identificationNumber);

}
