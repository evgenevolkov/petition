package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Petition;
import com.mycompany.myapp.repository.PetitionRepository;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing Petition.
 */
@RestController
@RequestMapping("/api")
public class PetitionResource {

    private final Logger log = LoggerFactory.getLogger(PetitionResource.class);
        
    @Inject
    private PetitionRepository petitionRepository;
    
    /**
     * POST  /petitions -> Create a new petition.
     */
    @RequestMapping(value = "/petitions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Petition> createPetition(@Valid @RequestBody Petition petition) throws URISyntaxException {
        log.debug("REST request to save Petition : {}", petition);
        if (petition.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("petition", "idexists", "A new petition cannot already have an ID")).body(null);
        }
        Petition result = petitionRepository.save(petition);
        return ResponseEntity.created(new URI("/api/petitions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("petition", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /petitions -> Updates an existing petition.
     */
    @RequestMapping(value = "/petitions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Petition> updatePetition(@Valid @RequestBody Petition petition) throws URISyntaxException {
        log.debug("REST request to update Petition : {}", petition);
        if (petition.getId() == null) {
            return createPetition(petition);
        }
        Petition result = petitionRepository.save(petition);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("petition", petition.getId().toString()))
            .body(result);
    }

    /**
     * GET  /petitions -> get all the petitions.
     */
    @RequestMapping(value = "/petitions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Petition> getAllPetitions(@RequestParam(required = false) String filter) {
        if ("subscription-is-null".equals(filter)) {
            log.debug("REST request to get all Petitions where subscription is null");
            return StreamSupport
                .stream(petitionRepository.findAll().spliterator(), false)
                .filter(petition -> petition.getSubscription() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all Petitions");
        return petitionRepository.findAll();
            }

    /**
     * GET  /petitions/:id -> get the "id" petition.
     */
    @RequestMapping(value = "/petitions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Petition> getPetition(@PathVariable Long id) {
        log.debug("REST request to get Petition : {}", id);
        Petition petition = petitionRepository.findOne(id);
        return Optional.ofNullable(petition)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /petitions/:id -> delete the "id" petition.
     */
    @RequestMapping(value = "/petitions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePetition(@PathVariable Long id) {
        log.debug("REST request to delete Petition : {}", id);
        petitionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("petition", id.toString())).build();
    }
}
