package com.kfu.qs.service;

import com.kfu.qs.domain.Predicate;
import com.kfu.qs.repository.PredicateRepository;
import com.kfu.qs.service.dto.PredicateDTO;
import com.kfu.qs.service.mapper.PredicateMapper;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Predicate}.
 */
@Service
@Transactional
public class PredicateService {

    private final Logger log = LoggerFactory.getLogger(PredicateService.class);

    private final PredicateRepository predicateRepository;

    private final PredicateMapper predicateMapper;

    public PredicateService(PredicateRepository predicateRepository, PredicateMapper predicateMapper) {
        this.predicateRepository = predicateRepository;
        this.predicateMapper = predicateMapper;
    }

    /**
     * Save a predicate.
     *
     * @param predicateDTO the entity to save.
     * @return the persisted entity.
     */
    public PredicateDTO save(PredicateDTO predicateDTO) {
        log.debug("Request to save Predicate : {}", predicateDTO);
        Predicate predicate = predicateMapper.toEntity(predicateDTO);
        predicate = predicateRepository.save(predicate);
        return predicateMapper.toDto(predicate);
    }

    /**
     * Update a predicate.
     *
     * @param predicateDTO the entity to save.
     * @return the persisted entity.
     */
    public PredicateDTO update(PredicateDTO predicateDTO) {
        log.debug("Request to update Predicate : {}", predicateDTO);
        Predicate predicate = predicateMapper.toEntity(predicateDTO);
        predicate = predicateRepository.save(predicate);
        return predicateMapper.toDto(predicate);
    }

    /**
     * Partially update a predicate.
     *
     * @param predicateDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PredicateDTO> partialUpdate(PredicateDTO predicateDTO) {
        log.debug("Request to partially update Predicate : {}", predicateDTO);

        return predicateRepository
            .findById(predicateDTO.getId())
            .map(existingPredicate -> {
                predicateMapper.partialUpdate(existingPredicate, predicateDTO);

                return existingPredicate;
            })
            .map(predicateRepository::save)
            .map(predicateMapper::toDto);
    }

    /**
     * Get all the predicates.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PredicateDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Predicates");
        return predicateRepository.findAll(pageable).map(predicateMapper::toDto);
    }

    public Set<PredicateDTO> getByPredicate(Set<String> rdf) {
        log.debug("Request to get all Predicates");
        return predicateRepository.findAll().stream().map(predicateMapper::toDto).collect(Collectors.toSet());
    }

    /**
     * Get all the predicates with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PredicateDTO> findAllWithEagerRelationships(Pageable pageable) {
        return predicateRepository.findAllWithEagerRelationships(pageable).map(predicateMapper::toDto);
    }

    /**
     * Get one predicate by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PredicateDTO> findOne(Long id) {
        log.debug("Request to get Predicate : {}", id);
        return predicateRepository.findOneWithEagerRelationships(id).map(predicateMapper::toDto);
    }

    /**
     * Delete the predicate by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Predicate : {}", id);
        predicateRepository.deleteById(id);
    }
}
