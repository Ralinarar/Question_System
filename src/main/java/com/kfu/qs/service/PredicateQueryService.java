package com.kfu.qs.service;

import com.kfu.qs.domain.*; // for static metamodels
import com.kfu.qs.domain.Predicate;
import com.kfu.qs.repository.PredicateRepository;
import com.kfu.qs.service.criteria.PredicateCriteria;
import com.kfu.qs.service.dto.PredicateDTO;
import com.kfu.qs.service.mapper.PredicateMapper;
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
 * Service for executing complex queries for {@link Predicate} entities in the database.
 * The main input is a {@link PredicateCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PredicateDTO} or a {@link Page} of {@link PredicateDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PredicateQueryService extends QueryService<Predicate> {

    private final Logger log = LoggerFactory.getLogger(PredicateQueryService.class);

    private final PredicateRepository predicateRepository;

    private final PredicateMapper predicateMapper;

    public PredicateQueryService(PredicateRepository predicateRepository, PredicateMapper predicateMapper) {
        this.predicateRepository = predicateRepository;
        this.predicateMapper = predicateMapper;
    }

    /**
     * Return a {@link List} of {@link PredicateDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PredicateDTO> findByCriteria(PredicateCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Predicate> specification = createSpecification(criteria);
        return predicateMapper.toDto(predicateRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PredicateDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PredicateDTO> findByCriteria(PredicateCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Predicate> specification = createSpecification(criteria);
        return predicateRepository.findAll(specification, page).map(predicateMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PredicateCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Predicate> specification = createSpecification(criteria);
        return predicateRepository.count(specification);
    }

    /**
     * Function to convert {@link PredicateCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Predicate> createSpecification(PredicateCriteria criteria) {
        Specification<Predicate> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Predicate_.id));
            }
            if (criteria.getRdfValue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRdfValue(), Predicate_.rdfValue));
            }
            if (criteria.getAuthorId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getAuthorId(), root -> root.join(Predicate_.author, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getTemplateId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTemplateId(),
                            root -> root.join(Predicate_.templates, JoinType.LEFT).get(Template_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
