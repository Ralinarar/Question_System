package com.kfu.qs.web.rest;

import com.kfu.qs.domain.User;
import com.kfu.qs.repository.MathtestRepository;
import com.kfu.qs.service.MathtestQueryService;
import com.kfu.qs.service.MathtestService;
import com.kfu.qs.service.QuestionGeneratorService;
import com.kfu.qs.service.UserService;
import com.kfu.qs.service.criteria.MathtestCriteria;
import com.kfu.qs.service.dto.MathtestDTO;
import com.kfu.qs.service.dto.QuestionTestDTO;
import com.kfu.qs.web.rest.errors.BadRequestAlertException;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import com.opencsv.CSVReader;
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

import javax.persistence.EntityNotFoundException;

/**
 * REST controller for managing {@link com.kfu.qs.domain.Mathtest}.
 */
@RestController
@RequestMapping("/api")
public class MathtestResource {

    private final Logger log = LoggerFactory.getLogger(MathtestResource.class);

    private static final String ENTITY_NAME = "mathtest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MathtestService mathtestService;

    private final MathtestRepository mathtestRepository;

    private final MathtestQueryService mathtestQueryService;

    private final QuestionGeneratorService questionGeneratorService;

    private final UserService userService;

    public MathtestResource(
        MathtestService mathtestService,
        MathtestRepository mathtestRepository,
        MathtestQueryService mathtestQueryService,
        QuestionGeneratorService questionGeneratorService, UserService userService) {
        this.mathtestService = mathtestService;
        this.mathtestRepository = mathtestRepository;
        this.mathtestQueryService = mathtestQueryService;
        this.questionGeneratorService = questionGeneratorService;
        this.userService = userService;
    }

    /**
     * {@code POST  /mathtests} : Create a new mathtest.
     *
     * @param mathtestDTO the mathtestDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mathtestDTO, or with status {@code 400 (Bad Request)} if the mathtest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/mathtests")
    public ResponseEntity<MathtestDTO> createMathtest(@RequestBody MathtestDTO mathtestDTO) throws URISyntaxException {
        log.debug("REST request to save Mathtest : {}", mathtestDTO);
        if (mathtestDTO.getId() != null) {
            throw new BadRequestAlertException("A new mathtest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MathtestDTO result = mathtestService.save(mathtestDTO);
        return ResponseEntity
            .created(new URI("/api/mathtests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /mathtests/:id} : Updates an existing mathtest.
     *
     * @param id          the id of the mathtestDTO to save.
     * @param mathtestDTO the mathtestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mathtestDTO,
     * or with status {@code 400 (Bad Request)} if the mathtestDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mathtestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/mathtests/{id}")
    public ResponseEntity<MathtestDTO> updateMathtest(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MathtestDTO mathtestDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Mathtest : {}, {}", id, mathtestDTO);
        if (mathtestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mathtestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mathtestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MathtestDTO result = mathtestService.update(mathtestDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mathtestDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /mathtests/:id} : Partial updates given fields of an existing mathtest, field will ignore if it is null
     *
     * @param id          the id of the mathtestDTO to save.
     * @param mathtestDTO the mathtestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mathtestDTO,
     * or with status {@code 400 (Bad Request)} if the mathtestDTO is not valid,
     * or with status {@code 404 (Not Found)} if the mathtestDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the mathtestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/mathtests/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public ResponseEntity<MathtestDTO> partialUpdateMathtest(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MathtestDTO mathtestDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Mathtest partially : {}, {}", id, mathtestDTO);
        if (mathtestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mathtestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mathtestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MathtestDTO> result = mathtestService.partialUpdate(mathtestDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mathtestDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /mathtests} : get all the mathtests.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mathtests in body.
     */
    @GetMapping("/mathtests")
    public ResponseEntity<List<MathtestDTO>> getAllMathtests(
        MathtestCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Mathtests by criteria: {}", criteria);
        Page<MathtestDTO> page = mathtestQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/mathtests/questions/{id}")
    public ResponseEntity<List<QuestionTestDTO>> getAllQuestions(@PathVariable(value = "id", required = false) final Long id) {
        User user = userService.getCurrentUser();
        MathtestDTO mathtestDTO = mathtestService.findByIdAndUserIdInAssigned(id, user.getId())
            .orElseThrow(EntityNotFoundException::new);
        List<QuestionTestDTO> questionTestDTOS = questionGeneratorService.generateTest(mathtestDTO);
        return ResponseEntity.ok().body(questionTestDTOS);
    }


    @GetMapping("/mathtests/quiz/{id}")
    public ResponseEntity<List<QuestionTestDTO>> getAllQuiz(
        @PathVariable(value = "id", required = false) final Long id
    ) throws Exception {
        List<QuestionTestDTO> body = new ArrayList<>();
        MathtestDTO mathtestDTO = mathtestService.findOne(id).get();
        if (mathtestDTO.getKeys().toLowerCase().contains("уравнен")) {
            List<QuestionTestDTO> questionTestDTOS = MathtestResourceUtils.extracted();
            body = MathtestResourceUtils.shuffleAndReduceList(questionTestDTOS, mathtestDTO.getAmount());
        }

        return ResponseEntity.ok().body(body);
    }


    /**
     * {@code GET  /mathtests/count} : count all the mathtests.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/mathtests/count")
    public ResponseEntity<Long> countMathtests(MathtestCriteria criteria) {
        log.debug("REST request to count Mathtests by criteria: {}", criteria);
        return ResponseEntity.ok().body(mathtestQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /mathtests/:id} : get the "id" mathtest.
     *
     * @param id the id of the mathtestDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mathtestDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/mathtests/{id}")
    public ResponseEntity<MathtestDTO> getMathtest(@PathVariable Long id) {
        log.debug("REST request to get Mathtest : {}", id);
        Optional<MathtestDTO> mathtestDTO = mathtestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mathtestDTO);
    }

    /**
     * {@code DELETE  /mathtests/:id} : delete the "id" mathtest.
     *
     * @param id the id of the mathtestDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/mathtests/{id}")
    public ResponseEntity<Void> deleteMathtest(@PathVariable Long id) {
        log.debug("REST request to delete Mathtest : {}", id);
        mathtestService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
