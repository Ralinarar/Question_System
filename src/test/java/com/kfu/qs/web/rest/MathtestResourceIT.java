package com.kfu.qs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.kfu.qs.IntegrationTest;
import com.kfu.qs.domain.Mathtest;
import com.kfu.qs.domain.User;
import com.kfu.qs.repository.MathtestRepository;
import com.kfu.qs.service.MathtestService;
import com.kfu.qs.service.criteria.MathtestCriteria;
import com.kfu.qs.service.dto.MathtestDTO;
import com.kfu.qs.service.mapper.MathtestMapper;
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
 * Integration tests for the {@link MathtestResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MathtestResourceIT {

    private static final Integer DEFAULT_AMOUNT = 1;
    private static final Integer UPDATED_AMOUNT = 2;
    private static final Integer SMALLER_AMOUNT = 1 - 1;

    private static final String DEFAULT_KEYS = "AAAAAAAAAA";
    private static final String UPDATED_KEYS = "BBBBBBBBBB";

    private static final Integer DEFAULT_TRESHOLD = 1;
    private static final Integer UPDATED_TRESHOLD = 2;
    private static final Integer SMALLER_TRESHOLD = 1 - 1;

    private static final String ENTITY_API_URL = "/api/mathtests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MathtestRepository mathtestRepository;

    @Mock
    private MathtestRepository mathtestRepositoryMock;

    @Autowired
    private MathtestMapper mathtestMapper;

    @Mock
    private MathtestService mathtestServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMathtestMockMvc;

    private Mathtest mathtest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mathtest createEntity(EntityManager em) {
        Mathtest mathtest = new Mathtest().amount(DEFAULT_AMOUNT).keys(DEFAULT_KEYS).treshold(DEFAULT_TRESHOLD);
        return mathtest;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mathtest createUpdatedEntity(EntityManager em) {
        Mathtest mathtest = new Mathtest().amount(UPDATED_AMOUNT).keys(UPDATED_KEYS).treshold(UPDATED_TRESHOLD);
        return mathtest;
    }

    @BeforeEach
    public void initTest() {
        mathtest = createEntity(em);
    }

    @Test
    @Transactional
    void createMathtest() throws Exception {
        int databaseSizeBeforeCreate = mathtestRepository.findAll().size();
        // Create the Mathtest
        MathtestDTO mathtestDTO = mathtestMapper.toDto(mathtest);
        restMathtestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mathtestDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Mathtest in the database
        List<Mathtest> mathtestList = mathtestRepository.findAll();
        assertThat(mathtestList).hasSize(databaseSizeBeforeCreate + 1);
        Mathtest testMathtest = mathtestList.get(mathtestList.size() - 1);
        assertThat(testMathtest.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testMathtest.getKeys()).isEqualTo(DEFAULT_KEYS);
        assertThat(testMathtest.getTreshold()).isEqualTo(DEFAULT_TRESHOLD);
    }

    @Test
    @Transactional
    void createMathtestWithExistingId() throws Exception {
        // Create the Mathtest with an existing ID
        mathtest.setId(1L);
        MathtestDTO mathtestDTO = mathtestMapper.toDto(mathtest);

        int databaseSizeBeforeCreate = mathtestRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMathtestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mathtestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mathtest in the database
        List<Mathtest> mathtestList = mathtestRepository.findAll();
        assertThat(mathtestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMathtests() throws Exception {
        // Initialize the database
        mathtestRepository.saveAndFlush(mathtest);

        // Get all the mathtestList
        restMathtestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mathtest.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.[*].keys").value(hasItem(DEFAULT_KEYS)))
            .andExpect(jsonPath("$.[*].treshold").value(hasItem(DEFAULT_TRESHOLD)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMathtestsWithEagerRelationshipsIsEnabled() throws Exception {
        when(mathtestServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMathtestMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(mathtestServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMathtestsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(mathtestServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMathtestMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(mathtestRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMathtest() throws Exception {
        // Initialize the database
        mathtestRepository.saveAndFlush(mathtest);

        // Get the mathtest
        restMathtestMockMvc
            .perform(get(ENTITY_API_URL_ID, mathtest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mathtest.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT))
            .andExpect(jsonPath("$.keys").value(DEFAULT_KEYS))
            .andExpect(jsonPath("$.treshold").value(DEFAULT_TRESHOLD));
    }

    @Test
    @Transactional
    void getMathtestsByIdFiltering() throws Exception {
        // Initialize the database
        mathtestRepository.saveAndFlush(mathtest);

        Long id = mathtest.getId();

        defaultMathtestShouldBeFound("id.equals=" + id);
        defaultMathtestShouldNotBeFound("id.notEquals=" + id);

        defaultMathtestShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMathtestShouldNotBeFound("id.greaterThan=" + id);

        defaultMathtestShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMathtestShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMathtestsByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        mathtestRepository.saveAndFlush(mathtest);

        // Get all the mathtestList where amount equals to DEFAULT_AMOUNT
        defaultMathtestShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the mathtestList where amount equals to UPDATED_AMOUNT
        defaultMathtestShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllMathtestsByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        mathtestRepository.saveAndFlush(mathtest);

        // Get all the mathtestList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultMathtestShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the mathtestList where amount equals to UPDATED_AMOUNT
        defaultMathtestShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllMathtestsByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        mathtestRepository.saveAndFlush(mathtest);

        // Get all the mathtestList where amount is not null
        defaultMathtestShouldBeFound("amount.specified=true");

        // Get all the mathtestList where amount is null
        defaultMathtestShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    void getAllMathtestsByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mathtestRepository.saveAndFlush(mathtest);

        // Get all the mathtestList where amount is greater than or equal to DEFAULT_AMOUNT
        defaultMathtestShouldBeFound("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the mathtestList where amount is greater than or equal to UPDATED_AMOUNT
        defaultMathtestShouldNotBeFound("amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllMathtestsByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mathtestRepository.saveAndFlush(mathtest);

        // Get all the mathtestList where amount is less than or equal to DEFAULT_AMOUNT
        defaultMathtestShouldBeFound("amount.lessThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the mathtestList where amount is less than or equal to SMALLER_AMOUNT
        defaultMathtestShouldNotBeFound("amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllMathtestsByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        mathtestRepository.saveAndFlush(mathtest);

        // Get all the mathtestList where amount is less than DEFAULT_AMOUNT
        defaultMathtestShouldNotBeFound("amount.lessThan=" + DEFAULT_AMOUNT);

        // Get all the mathtestList where amount is less than UPDATED_AMOUNT
        defaultMathtestShouldBeFound("amount.lessThan=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllMathtestsByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        mathtestRepository.saveAndFlush(mathtest);

        // Get all the mathtestList where amount is greater than DEFAULT_AMOUNT
        defaultMathtestShouldNotBeFound("amount.greaterThan=" + DEFAULT_AMOUNT);

        // Get all the mathtestList where amount is greater than SMALLER_AMOUNT
        defaultMathtestShouldBeFound("amount.greaterThan=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllMathtestsByKeysIsEqualToSomething() throws Exception {
        // Initialize the database
        mathtestRepository.saveAndFlush(mathtest);

        // Get all the mathtestList where keys equals to DEFAULT_KEYS
        defaultMathtestShouldBeFound("keys.equals=" + DEFAULT_KEYS);

        // Get all the mathtestList where keys equals to UPDATED_KEYS
        defaultMathtestShouldNotBeFound("keys.equals=" + UPDATED_KEYS);
    }

    @Test
    @Transactional
    void getAllMathtestsByKeysIsInShouldWork() throws Exception {
        // Initialize the database
        mathtestRepository.saveAndFlush(mathtest);

        // Get all the mathtestList where keys in DEFAULT_KEYS or UPDATED_KEYS
        defaultMathtestShouldBeFound("keys.in=" + DEFAULT_KEYS + "," + UPDATED_KEYS);

        // Get all the mathtestList where keys equals to UPDATED_KEYS
        defaultMathtestShouldNotBeFound("keys.in=" + UPDATED_KEYS);
    }

    @Test
    @Transactional
    void getAllMathtestsByKeysIsNullOrNotNull() throws Exception {
        // Initialize the database
        mathtestRepository.saveAndFlush(mathtest);

        // Get all the mathtestList where keys is not null
        defaultMathtestShouldBeFound("keys.specified=true");

        // Get all the mathtestList where keys is null
        defaultMathtestShouldNotBeFound("keys.specified=false");
    }

    @Test
    @Transactional
    void getAllMathtestsByKeysContainsSomething() throws Exception {
        // Initialize the database
        mathtestRepository.saveAndFlush(mathtest);

        // Get all the mathtestList where keys contains DEFAULT_KEYS
        defaultMathtestShouldBeFound("keys.contains=" + DEFAULT_KEYS);

        // Get all the mathtestList where keys contains UPDATED_KEYS
        defaultMathtestShouldNotBeFound("keys.contains=" + UPDATED_KEYS);
    }

    @Test
    @Transactional
    void getAllMathtestsByKeysNotContainsSomething() throws Exception {
        // Initialize the database
        mathtestRepository.saveAndFlush(mathtest);

        // Get all the mathtestList where keys does not contain DEFAULT_KEYS
        defaultMathtestShouldNotBeFound("keys.doesNotContain=" + DEFAULT_KEYS);

        // Get all the mathtestList where keys does not contain UPDATED_KEYS
        defaultMathtestShouldBeFound("keys.doesNotContain=" + UPDATED_KEYS);
    }

    @Test
    @Transactional
    void getAllMathtestsByTresholdIsEqualToSomething() throws Exception {
        // Initialize the database
        mathtestRepository.saveAndFlush(mathtest);

        // Get all the mathtestList where treshold equals to DEFAULT_TRESHOLD
        defaultMathtestShouldBeFound("treshold.equals=" + DEFAULT_TRESHOLD);

        // Get all the mathtestList where treshold equals to UPDATED_TRESHOLD
        defaultMathtestShouldNotBeFound("treshold.equals=" + UPDATED_TRESHOLD);
    }

    @Test
    @Transactional
    void getAllMathtestsByTresholdIsInShouldWork() throws Exception {
        // Initialize the database
        mathtestRepository.saveAndFlush(mathtest);

        // Get all the mathtestList where treshold in DEFAULT_TRESHOLD or UPDATED_TRESHOLD
        defaultMathtestShouldBeFound("treshold.in=" + DEFAULT_TRESHOLD + "," + UPDATED_TRESHOLD);

        // Get all the mathtestList where treshold equals to UPDATED_TRESHOLD
        defaultMathtestShouldNotBeFound("treshold.in=" + UPDATED_TRESHOLD);
    }

    @Test
    @Transactional
    void getAllMathtestsByTresholdIsNullOrNotNull() throws Exception {
        // Initialize the database
        mathtestRepository.saveAndFlush(mathtest);

        // Get all the mathtestList where treshold is not null
        defaultMathtestShouldBeFound("treshold.specified=true");

        // Get all the mathtestList where treshold is null
        defaultMathtestShouldNotBeFound("treshold.specified=false");
    }

    @Test
    @Transactional
    void getAllMathtestsByTresholdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mathtestRepository.saveAndFlush(mathtest);

        // Get all the mathtestList where treshold is greater than or equal to DEFAULT_TRESHOLD
        defaultMathtestShouldBeFound("treshold.greaterThanOrEqual=" + DEFAULT_TRESHOLD);

        // Get all the mathtestList where treshold is greater than or equal to UPDATED_TRESHOLD
        defaultMathtestShouldNotBeFound("treshold.greaterThanOrEqual=" + UPDATED_TRESHOLD);
    }

    @Test
    @Transactional
    void getAllMathtestsByTresholdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mathtestRepository.saveAndFlush(mathtest);

        // Get all the mathtestList where treshold is less than or equal to DEFAULT_TRESHOLD
        defaultMathtestShouldBeFound("treshold.lessThanOrEqual=" + DEFAULT_TRESHOLD);

        // Get all the mathtestList where treshold is less than or equal to SMALLER_TRESHOLD
        defaultMathtestShouldNotBeFound("treshold.lessThanOrEqual=" + SMALLER_TRESHOLD);
    }

    @Test
    @Transactional
    void getAllMathtestsByTresholdIsLessThanSomething() throws Exception {
        // Initialize the database
        mathtestRepository.saveAndFlush(mathtest);

        // Get all the mathtestList where treshold is less than DEFAULT_TRESHOLD
        defaultMathtestShouldNotBeFound("treshold.lessThan=" + DEFAULT_TRESHOLD);

        // Get all the mathtestList where treshold is less than UPDATED_TRESHOLD
        defaultMathtestShouldBeFound("treshold.lessThan=" + UPDATED_TRESHOLD);
    }

    @Test
    @Transactional
    void getAllMathtestsByTresholdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        mathtestRepository.saveAndFlush(mathtest);

        // Get all the mathtestList where treshold is greater than DEFAULT_TRESHOLD
        defaultMathtestShouldNotBeFound("treshold.greaterThan=" + DEFAULT_TRESHOLD);

        // Get all the mathtestList where treshold is greater than SMALLER_TRESHOLD
        defaultMathtestShouldBeFound("treshold.greaterThan=" + SMALLER_TRESHOLD);
    }

    @Test
    @Transactional
    void getAllMathtestsByAssignedIsEqualToSomething() throws Exception {
        User assigned;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            mathtestRepository.saveAndFlush(mathtest);
            assigned = UserResourceIT.createEntity(em);
        } else {
            assigned = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(assigned);
        em.flush();
        mathtest.addAssigned(assigned);
        mathtestRepository.saveAndFlush(mathtest);
        Long assignedId = assigned.getId();

        // Get all the mathtestList where assigned equals to assignedId
        defaultMathtestShouldBeFound("assignedId.equals=" + assignedId);

        // Get all the mathtestList where assigned equals to (assignedId + 1)
        defaultMathtestShouldNotBeFound("assignedId.equals=" + (assignedId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMathtestShouldBeFound(String filter) throws Exception {
        restMathtestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mathtest.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.[*].keys").value(hasItem(DEFAULT_KEYS)))
            .andExpect(jsonPath("$.[*].treshold").value(hasItem(DEFAULT_TRESHOLD)));

        // Check, that the count call also returns 1
        restMathtestMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMathtestShouldNotBeFound(String filter) throws Exception {
        restMathtestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMathtestMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMathtest() throws Exception {
        // Get the mathtest
        restMathtestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMathtest() throws Exception {
        // Initialize the database
        mathtestRepository.saveAndFlush(mathtest);

        int databaseSizeBeforeUpdate = mathtestRepository.findAll().size();

        // Update the mathtest
        Mathtest updatedMathtest = mathtestRepository.findById(mathtest.getId()).get();
        // Disconnect from session so that the updates on updatedMathtest are not directly saved in db
        em.detach(updatedMathtest);
        updatedMathtest.amount(UPDATED_AMOUNT).keys(UPDATED_KEYS).treshold(UPDATED_TRESHOLD);
        MathtestDTO mathtestDTO = mathtestMapper.toDto(updatedMathtest);

        restMathtestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mathtestDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mathtestDTO))
            )
            .andExpect(status().isOk());

        // Validate the Mathtest in the database
        List<Mathtest> mathtestList = mathtestRepository.findAll();
        assertThat(mathtestList).hasSize(databaseSizeBeforeUpdate);
        Mathtest testMathtest = mathtestList.get(mathtestList.size() - 1);
        assertThat(testMathtest.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testMathtest.getKeys()).isEqualTo(UPDATED_KEYS);
        assertThat(testMathtest.getTreshold()).isEqualTo(UPDATED_TRESHOLD);
    }

    @Test
    @Transactional
    void putNonExistingMathtest() throws Exception {
        int databaseSizeBeforeUpdate = mathtestRepository.findAll().size();
        mathtest.setId(count.incrementAndGet());

        // Create the Mathtest
        MathtestDTO mathtestDTO = mathtestMapper.toDto(mathtest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMathtestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mathtestDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mathtestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mathtest in the database
        List<Mathtest> mathtestList = mathtestRepository.findAll();
        assertThat(mathtestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMathtest() throws Exception {
        int databaseSizeBeforeUpdate = mathtestRepository.findAll().size();
        mathtest.setId(count.incrementAndGet());

        // Create the Mathtest
        MathtestDTO mathtestDTO = mathtestMapper.toDto(mathtest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMathtestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mathtestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mathtest in the database
        List<Mathtest> mathtestList = mathtestRepository.findAll();
        assertThat(mathtestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMathtest() throws Exception {
        int databaseSizeBeforeUpdate = mathtestRepository.findAll().size();
        mathtest.setId(count.incrementAndGet());

        // Create the Mathtest
        MathtestDTO mathtestDTO = mathtestMapper.toDto(mathtest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMathtestMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mathtestDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mathtest in the database
        List<Mathtest> mathtestList = mathtestRepository.findAll();
        assertThat(mathtestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMathtestWithPatch() throws Exception {
        // Initialize the database
        mathtestRepository.saveAndFlush(mathtest);

        int databaseSizeBeforeUpdate = mathtestRepository.findAll().size();

        // Update the mathtest using partial update
        Mathtest partialUpdatedMathtest = new Mathtest();
        partialUpdatedMathtest.setId(mathtest.getId());

        partialUpdatedMathtest.amount(UPDATED_AMOUNT).keys(UPDATED_KEYS);

        restMathtestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMathtest.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMathtest))
            )
            .andExpect(status().isOk());

        // Validate the Mathtest in the database
        List<Mathtest> mathtestList = mathtestRepository.findAll();
        assertThat(mathtestList).hasSize(databaseSizeBeforeUpdate);
        Mathtest testMathtest = mathtestList.get(mathtestList.size() - 1);
        assertThat(testMathtest.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testMathtest.getKeys()).isEqualTo(UPDATED_KEYS);
        assertThat(testMathtest.getTreshold()).isEqualTo(DEFAULT_TRESHOLD);
    }

    @Test
    @Transactional
    void fullUpdateMathtestWithPatch() throws Exception {
        // Initialize the database
        mathtestRepository.saveAndFlush(mathtest);

        int databaseSizeBeforeUpdate = mathtestRepository.findAll().size();

        // Update the mathtest using partial update
        Mathtest partialUpdatedMathtest = new Mathtest();
        partialUpdatedMathtest.setId(mathtest.getId());

        partialUpdatedMathtest.amount(UPDATED_AMOUNT).keys(UPDATED_KEYS).treshold(UPDATED_TRESHOLD);

        restMathtestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMathtest.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMathtest))
            )
            .andExpect(status().isOk());

        // Validate the Mathtest in the database
        List<Mathtest> mathtestList = mathtestRepository.findAll();
        assertThat(mathtestList).hasSize(databaseSizeBeforeUpdate);
        Mathtest testMathtest = mathtestList.get(mathtestList.size() - 1);
        assertThat(testMathtest.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testMathtest.getKeys()).isEqualTo(UPDATED_KEYS);
        assertThat(testMathtest.getTreshold()).isEqualTo(UPDATED_TRESHOLD);
    }

    @Test
    @Transactional
    void patchNonExistingMathtest() throws Exception {
        int databaseSizeBeforeUpdate = mathtestRepository.findAll().size();
        mathtest.setId(count.incrementAndGet());

        // Create the Mathtest
        MathtestDTO mathtestDTO = mathtestMapper.toDto(mathtest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMathtestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mathtestDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mathtestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mathtest in the database
        List<Mathtest> mathtestList = mathtestRepository.findAll();
        assertThat(mathtestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMathtest() throws Exception {
        int databaseSizeBeforeUpdate = mathtestRepository.findAll().size();
        mathtest.setId(count.incrementAndGet());

        // Create the Mathtest
        MathtestDTO mathtestDTO = mathtestMapper.toDto(mathtest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMathtestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mathtestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mathtest in the database
        List<Mathtest> mathtestList = mathtestRepository.findAll();
        assertThat(mathtestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMathtest() throws Exception {
        int databaseSizeBeforeUpdate = mathtestRepository.findAll().size();
        mathtest.setId(count.incrementAndGet());

        // Create the Mathtest
        MathtestDTO mathtestDTO = mathtestMapper.toDto(mathtest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMathtestMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mathtestDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mathtest in the database
        List<Mathtest> mathtestList = mathtestRepository.findAll();
        assertThat(mathtestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMathtest() throws Exception {
        // Initialize the database
        mathtestRepository.saveAndFlush(mathtest);

        int databaseSizeBeforeDelete = mathtestRepository.findAll().size();

        // Delete the mathtest
        restMathtestMockMvc
            .perform(delete(ENTITY_API_URL_ID, mathtest.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Mathtest> mathtestList = mathtestRepository.findAll();
        assertThat(mathtestList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
