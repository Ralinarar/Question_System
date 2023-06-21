package com.kfu.qs.service;

import com.kfu.qs.domain.*; // for static metamodels
import com.kfu.qs.domain.Mathtest;
import com.kfu.qs.repository.MathtestRepository;
import com.kfu.qs.service.criteria.MathtestCriteria;
import com.kfu.qs.service.dto.MathtestDTO;
import com.kfu.qs.service.mapper.MathtestMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Mathtest} entities in the database.
 * The main input is a {@link MathtestCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MathtestDTO} or a {@link Page} of {@link MathtestDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MathtestQueryService extends QueryService<Mathtest> {

    private final Logger log = LoggerFactory.getLogger(MathtestQueryService.class);

    private final MathtestRepository mathtestRepository;

    private final MathtestMapper mathtestMapper;

    public MathtestQueryService(MathtestRepository mathtestRepository, MathtestMapper mathtestMapper) {
        this.mathtestRepository = mathtestRepository;
        this.mathtestMapper = mathtestMapper;
    }

    /**
     * Return a {@link List} of {@link MathtestDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MathtestDTO> findByCriteria(MathtestCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Mathtest> specification = createSpecification(criteria);
        return mathtestMapper.toDto(mathtestRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MathtestDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MathtestDTO> findByCriteria(MathtestCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Mathtest> specification = createSpecification(criteria);
        return mathtestRepository.findAll(specification, page).map(mathtestMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MathtestCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Mathtest> specification = createSpecification(criteria);
        return mathtestRepository.count(specification);
    }

    /**
     * Function to convert {@link MathtestCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Mathtest> createSpecification(MathtestCriteria criteria) {
        Specification<Mathtest> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Mathtest_.id));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), Mathtest_.amount));
            }
            if (criteria.getKeys() != null) {
                specification = specification.and(buildStringSpecification(criteria.getKeys(), Mathtest_.keys));
            }
            if (criteria.getTreshold() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTreshold(), Mathtest_.treshold));
            }
            if (criteria.getAssignedId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getAssignedId(), root -> root.join(Mathtest_.assigneds, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }
}
