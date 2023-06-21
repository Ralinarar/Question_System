package com.kfu.qs.web.rest;

import com.kfu.qs.repository.PredicateRepository;
import com.kfu.qs.service.PredicateQueryService;
import com.kfu.qs.service.PredicateService;
import com.kfu.qs.service.criteria.PredicateCriteria;
import com.kfu.qs.service.dto.PredicateDTO;
import com.kfu.qs.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.kfu.qs.domain.Predicate}.
 */
@RestController
@RequestMapping("/api")
public class PredicateResource {

    private final Logger log = LoggerFactory.getLogger(PredicateResource.class);

    private static final String ENTITY_NAME = "predicate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PredicateService predicateService;

    private final PredicateRepository predicateRepository;

    private final PredicateQueryService predicateQueryService;

    public PredicateResource(
        PredicateService predicateService,
        PredicateRepository predicateRepository,
        PredicateQueryService predicateQueryService
    ) {
        this.predicateService = predicateService;
        this.predicateRepository = predicateRepository;
        this.predicateQueryService = predicateQueryService;
    }

    /**
     * {@code POST  /predicates} : Create a new predicate.
     *
     * @param predicateDTO the predicateDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new predicateDTO, or with status {@code 400 (Bad Request)} if the predicate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/predicates")
    public ResponseEntity<PredicateDTO> createPredicate(@Valid @RequestBody PredicateDTO predicateDTO) throws URISyntaxException {
        log.debug("REST request to save Predicate : {}", predicateDTO);
        if (predicateDTO.getId() != null) {
            throw new BadRequestAlertException("A new predicate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PredicateDTO result = predicateService.save(predicateDTO);
        return ResponseEntity
            .created(new URI("/api/predicates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /predicates/:id} : Updates an existing predicate.
     *
     * @param id the id of the predicateDTO to save.
     * @param predicateDTO the predicateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated predicateDTO,
     * or with status {@code 400 (Bad Request)} if the predicateDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the predicateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/predicates/{id}")
    public ResponseEntity<PredicateDTO> updatePredicate(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PredicateDTO predicateDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Predicate : {}, {}", id, predicateDTO);
        if (predicateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, predicateDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!predicateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PredicateDTO result = predicateService.update(predicateDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, predicateDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /predicates/:id} : Partial updates given fields of an existing predicate, field will ignore if it is null
     *
     * @param id the id of the predicateDTO to save.
     * @param predicateDTO the predicateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated predicateDTO,
     * or with status {@code 400 (Bad Request)} if the predicateDTO is not valid,
     * or with status {@code 404 (Not Found)} if the predicateDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the predicateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/predicates/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PredicateDTO> partialUpdatePredicate(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PredicateDTO predicateDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Predicate partially : {}, {}", id, predicateDTO);
        if (predicateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, predicateDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!predicateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PredicateDTO> result = predicateService.partialUpdate(predicateDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, predicateDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /predicates} : get all the predicates.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of predicates in body.
     */
    @GetMapping("/predicates")
    public ResponseEntity<List<PredicateDTO>> getAllPredicates(
        PredicateCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Predicates by criteria: {}", criteria);
        Page<PredicateDTO> page = predicateQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /predicates/count} : count all the predicates.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/predicates/count")
    public ResponseEntity<Long> countPredicates(PredicateCriteria criteria) {
        log.debug("REST request to count Predicates by criteria: {}", criteria);
        return ResponseEntity.ok().body(predicateQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /predicates/:id} : get the "id" predicate.
     *
     * @param id the id of the predicateDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the predicateDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/predicates/{id}")
    public ResponseEntity<PredicateDTO> getPredicate(@PathVariable Long id) {
        log.debug("REST request to get Predicate : {}", id);
        Optional<PredicateDTO> predicateDTO = predicateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(predicateDTO);
    }

    /**
     * {@code DELETE  /predicates/:id} : delete the "id" predicate.
     *
     * @param id the id of the predicateDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/predicates/{id}")
    public ResponseEntity<Void> deletePredicate(@PathVariable Long id) {
        log.debug("REST request to delete Predicate : {}", id);
        predicateService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
