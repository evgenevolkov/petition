package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Petition;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Petition entity.
 */
public interface PetitionRepository extends JpaRepository<Petition,Long> {

    @Query("select petition from Petition petition where petition.user.login = ?#{principal.username}")
    List<Petition> findByUserIsCurrentUser();

}
