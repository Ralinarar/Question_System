package com.kfu.qs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.kfu.qs.IntegrationTest;
import com.kfu.qs.domain.Predicate;
import com.kfu.qs.domain.Template;
import com.kfu.qs.domain.User;
import com.kfu.qs.repository.PredicateRepository;
import com.kfu.qs.service.PredicateService;
import com.kfu.qs.service.criteria.PredicateCriteria;
import com.kfu.qs.service.dto.PredicateDTO;
import com.kfu.qs.service.mapper.PredicateMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PredicateResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PredicateResourceIT {

    private static final String DEFAULT_RDF_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_RDF_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/predicates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PredicateRepository predicateRepository;

    @Mock
    private PredicateRepository predicateRepositoryMock;

    @Autowired
    private PredicateMapper predicateMapper;

    @Mock
    private PredicateService predicateServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPredicateMockMvc;

    private Predicate predicate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Predicate createEntity(EntityManager em) {
        Predicate predicate = new Predicate().rdfValue(DEFAULT_RDF_VALUE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        predicate.setAuthor(user);
        return predicate;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Predicate createUpdatedEntity(EntityManager em) {
        Predicate predicate = new Predicate().rdfValue(UPDATED_RDF_VALUE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        predicate.setAuthor(user);
        return predicate;
    }

    @BeforeEach
    public void initTest() {
        predicate = createEntity(em);
    }

    @Test
    @Transactional
    void createPredicate() throws Exception {
        int databaseSizeBeforeCreate = predicateRepository.findAll().size();
        // Create the Predicate
        PredicateDTO predicateDTO = predicateMapper.toDto(predicate);
        restPredicateMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(predicateDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Predicate in the database
        List<Predicate> predicateList = predicateRepository.findAll();
        assertThat(predicateList).hasSize(databaseSizeBeforeCreate + 1);
        Predicate testPredicate = predicateList.get(predicateList.size() - 1);
        assertThat(testPredicate.getRdfValue()).isEqualTo(DEFAULT_RDF_VALUE);
    }

    @Test
    @Transactional
    void createPredicateWithExistingId() throws Exception {
        // Create the Predicate with an existing ID
        predicate.setId(1L);
        PredicateDTO predicateDTO = predicateMapper.toDto(predicate);

        int databaseSizeBeforeCreate = predicateRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPredicateMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(predicateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Predicate in the database
        List<Predicate> predicateList = predicateRepository.findAll();
        assertThat(predicateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPredicates() throws Exception {
        // Initialize the database
        predicateRepository.saveAndFlush(predicate);

        // Get all the predicateList
        restPredicateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(predicate.getId().intValue())))
            .andExpect(jsonPath("$.[*].rdfValue").value(hasItem(DEFAULT_RDF_VALUE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPredicatesWithEagerRelationshipsIsEnabled() throws Exception {
        when(predicateServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPredicateMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(predicateServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPredicatesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(predicateServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPredicateMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(predicateRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPredicate() throws Exception {
        // Initialize the database
        predicateRepository.saveAndFlush(predicate);

        // Get the predicate
        restPredicateMockMvc
            .perform(get(ENTITY_API_URL_ID, predicate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(predicate.getId().intValue()))
            .andExpect(jsonPath("$.rdfValue").value(DEFAULT_RDF_VALUE));
    }

    @Test
    @Transactional
    void getPredicatesByIdFiltering() throws Exception {
        // Initialize the database
        predicateRepository.saveAndFlush(predicate);

        Long id = predicate.getId();

        defaultPredicateShouldBeFound("id.equals=" + id);
        defaultPredicateShouldNotBeFound("id.notEquals=" + id);

        defaultPredicateShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPredicateShouldNotBeFound("id.greaterThan=" + id);

        defaultPredicateShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPredicateShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPredicatesByRdfValueIsEqualToSomething() throws Exception {
        // Initialize the database
        predicateRepository.saveAndFlush(predicate);

        // Get all the predicateList where rdfValue equals to DEFAULT_RDF_VALUE
        defaultPredicateShouldBeFound("rdfValue.equals=" + DEFAULT_RDF_VALUE);

        // Get all the predicateList where rdfValue equals to UPDATED_RDF_VALUE
        defaultPredicateShouldNotBeFound("rdfValue.equals=" + UPDATED_RDF_VALUE);
    }

    @Test
    @Transactional
    void getAllPredicatesByRdfValueIsInShouldWork() throws Exception {
        // Initialize the database
        predicateRepository.saveAndFlush(predicate);

        // Get all the predicateList where rdfValue in DEFAULT_RDF_VALUE or UPDATED_RDF_VALUE
        defaultPredicateShouldBeFound("rdfValue.in=" + DEFAULT_RDF_VALUE + "," + UPDATED_RDF_VALUE);

        // Get all the predicateList where rdfValue equals to UPDATED_RDF_VALUE
        defaultPredicateShouldNotBeFound("rdfValue.in=" + UPDATED_RDF_VALUE);
    }

    @Test
    @Transactional
    void getAllPredicatesByRdfValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        predicateRepository.saveAndFlush(predicate);

        // Get all the predicateList where rdfValue is not null
        defaultPredicateShouldBeFound("rdfValue.specified=true");

        // Get all the predicateList where rdfValue is null
        defaultPredicateShouldNotBeFound("rdfValue.specified=false");
    }

    @Test
    @Transactional
    void getAllPredicatesByRdfValueContainsSomething() throws Exception {
        // Initialize the database
        predicateRepository.saveAndFlush(predicate);

        // Get all the predicateList where rdfValue contains DEFAULT_RDF_VALUE
        defaultPredicateShouldBeFound("rdfValue.contains=" + DEFAULT_RDF_VALUE);

        // Get all the predicateList where rdfValue contains UPDATED_RDF_VALUE
        defaultPredicateShouldNotBeFound("rdfValue.contains=" + UPDATED_RDF_VALUE);
    }

    @Test
    @Transactional
    void getAllPredicatesByRdfValueNotContainsSomething() throws Exception {
        // Initialize the database
        predicateRepository.saveAndFlush(predicate);

        // Get all the predicateList where rdfValue does not contain DEFAULT_RDF_VALUE
        defaultPredicateShouldNotBeFound("rdfValue.doesNotContain=" + DEFAULT_RDF_VALUE);

        // Get all the predicateList where rdfValue does not contain UPDATED_RDF_VALUE
        defaultPredicateShouldBeFound("rdfValue.doesNotContain=" + UPDATED_RDF_VALUE);
    }

    @Test
    @Transactional
    void getAllPredicatesByAuthorIsEqualToSomething() throws Exception {
        User author;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            predicateRepository.saveAndFlush(predicate);
            author = UserResourceIT.createEntity(em);
        } else {
            author = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(author);
        em.flush();
        predicate.setAuthor(author);
        predicateRepository.saveAndFlush(predicate);
        Long authorId = author.getId();

        // Get all the predicateList where author equals to authorId
        defaultPredicateShouldBeFound("authorId.equals=" + authorId);

        // Get all the predicateList where author equals to (authorId + 1)
        defaultPredicateShouldNotBeFound("authorId.equals=" + (authorId + 1));
    }

    @Test
    @Transactional
    void getAllPredicatesByTemplateIsEqualToSomething() throws Exception {
        Template template;
        if (TestUtil.findAll(em, Template.class).isEmpty()) {
            predicateRepository.saveAndFlush(predicate);
            template = TemplateResourceIT.createEntity(em);
        } else {
            template = TestUtil.findAll(em, Template.class).get(0);
        }
        em.persist(template);
        em.flush();
        predicate.addTemplate(template);
        predicateRepository.saveAndFlush(predicate);
        Long templateId = template.getId();

        // Get all the predicateList where template equals to templateId
        defaultPredicateShouldBeFound("templateId.equals=" + templateId);

        // Get all the predicateList where template equals to (templateId + 1)
        defaultPredicateShouldNotBeFound("templateId.equals=" + (templateId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPredicateShouldBeFound(String filter) throws Exception {
        restPredicateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(predicate.getId().intValue())))
            .andExpect(jsonPath("$.[*].rdfValue").value(hasItem(DEFAULT_RDF_VALUE)));

        // Check, that the count call also returns 1
        restPredicateMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPredicateShouldNotBeFound(String filter) throws Exception {
        restPredicateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPredicateMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPredicate() throws Exception {
        // Get the predicate
        restPredicateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPredicate() throws Exception {
        // Initialize the database
        predicateRepository.saveAndFlush(predicate);

        int databaseSizeBeforeUpdate = predicateRepository.findAll().size();

        // Update the predicate
        Predicate updatedPredicate = predicateRepository.findById(predicate.getId()).get();
        // Disconnect from session so that the updates on updatedPredicate are not directly saved in db
        em.detach(updatedPredicate);
        updatedPredicate.rdfValue(UPDATED_RDF_VALUE);
        PredicateDTO predicateDTO = predicateMapper.toDto(updatedPredicate);

        restPredicateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, predicateDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(predicateDTO))
            )
            .andExpect(status().isOk());

        // Validate the Predicate in the database
        List<Predicate> predicateList = predicateRepository.findAll();
        assertThat(predicateList).hasSize(databaseSizeBeforeUpdate);
        Predicate testPredicate = predicateList.get(predicateList.size() - 1);
        assertThat(testPredicate.getRdfValue()).isEqualTo(UPDATED_RDF_VALUE);
    }

    @Test
    @Transactional
    void putNonExistingPredicate() throws Exception {
        int databaseSizeBeforeUpdate = predicateRepository.findAll().size();
        predicate.setId(count.incrementAndGet());

        // Create the Predicate
        PredicateDTO predicateDTO = predicateMapper.toDto(predicate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPredicateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, predicateDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(predicateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Predicate in the database
        List<Predicate> predicateList = predicateRepository.findAll();
        assertThat(predicateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPredicate() throws Exception {
        int databaseSizeBeforeUpdate = predicateRepository.findAll().size();
        predicate.setId(count.incrementAndGet());

        // Create the Predicate
        PredicateDTO predicateDTO = predicateMapper.toDto(predicate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPredicateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(predicateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Predicate in the database
        List<Predicate> predicateList = predicateRepository.findAll();
        assertThat(predicateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPredicate() throws Exception {
        int databaseSizeBeforeUpdate = predicateRepository.findAll().size();
        predicate.setId(count.incrementAndGet());

        // Create the Predicate
        PredicateDTO predicateDTO = predicateMapper.toDto(predicate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPredicateMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(predicateDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Predicate in the database
        List<Predicate> predicateList = predicateRepository.findAll();
        assertThat(predicateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePredicateWithPatch() throws Exception {
        // Initialize the database
        predicateRepository.saveAndFlush(predicate);

        int databaseSizeBeforeUpdate = predicateRepository.findAll().size();

        // Update the predicate using partial update
        Predicate partialUpdatedPredicate = new Predicate();
        partialUpdatedPredicate.setId(predicate.getId());

        restPredicateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPredicate.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPredicate))
            )
            .andExpect(status().isOk());

        // Validate the Predicate in the database
        List<Predicate> predicateList = predicateRepository.findAll();
        assertThat(predicateList).hasSize(databaseSizeBeforeUpdate);
        Predicate testPredicate = predicateList.get(predicateList.size() - 1);
        assertThat(testPredicate.getRdfValue()).isEqualTo(DEFAULT_RDF_VALUE);
    }

    @Test
    @Transactional
    void fullUpdatePredicateWithPatch() throws Exception {
        // Initialize the database
        predicateRepository.saveAndFlush(predicate);

        int databaseSizeBeforeUpdate = predicateRepository.findAll().size();

        // Update the predicate using partial update
        Predicate partialUpdatedPredicate = new Predicate();
        partialUpdatedPredicate.setId(predicate.getId());

        partialUpdatedPredicate.rdfValue(UPDATED_RDF_VALUE);

        restPredicateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPredicate.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPredicate))
            )
            .andExpect(status().isOk());

        // Validate the Predicate in the database
        List<Predicate> predicateList = predicateRepository.findAll();
        assertThat(predicateList).hasSize(databaseSizeBeforeUpdate);
        Predicate testPredicate = predicateList.get(predicateList.size() - 1);
        assertThat(testPredicate.getRdfValue()).isEqualTo(UPDATED_RDF_VALUE);
    }

    @Test
    @Transactional
    void patchNonExistingPredicate() throws Exception {
        int databaseSizeBeforeUpdate = predicateRepository.findAll().size();
        predicate.setId(count.incrementAndGet());

        // Create the Predicate
        PredicateDTO predicateDTO = predicateMapper.toDto(predicate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPredicateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, predicateDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(predicateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Predicate in the database
        List<Predicate> predicateList = predicateRepository.findAll();
        assertThat(predicateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPredicate() throws Exception {
        int databaseSizeBeforeUpdate = predicateRepository.findAll().size();
        predicate.setId(count.incrementAndGet());

        // Create the Predicate
        PredicateDTO predicateDTO = predicateMapper.toDto(predicate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPredicateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(predicateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Predicate in the database
        List<Predicate> predicateList = predicateRepository.findAll();
        assertThat(predicateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPredicate() throws Exception {
        int databaseSizeBeforeUpdate = predicateRepository.findAll().size();
        predicate.setId(count.incrementAndGet());

        // Create the Predicate
        PredicateDTO predicateDTO = predicateMapper.toDto(predicate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPredicateMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(predicateDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Predicate in the database
        List<Predicate> predicateList = predicateRepository.findAll();
        assertThat(predicateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePredicate() throws Exception {
        // Initialize the database
        predicateRepository.saveAndFlush(predicate);

        int databaseSizeBeforeDelete = predicateRepository.findAll().size();

        // Delete the predicate
        restPredicateMockMvc
            .perform(delete(ENTITY_API_URL_ID, predicate.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Predicate> predicateList = predicateRepository.findAll();
        assertThat(predicateList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
