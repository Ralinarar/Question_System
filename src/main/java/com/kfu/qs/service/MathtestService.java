package com.kfu.qs.service;

import com.kfu.qs.domain.Mathtest;
import com.kfu.qs.repository.MathtestRepository;
import com.kfu.qs.service.dto.MathtestDTO;
import com.kfu.qs.service.mapper.MathtestMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Mathtest}.
 */
@Service
@Transactional
public class MathtestService {

    private final Logger log = LoggerFactory.getLogger(MathtestService.class);

    private final MathtestRepository mathtestRepository;

    private final MathtestMapper mathtestMapper;

    public MathtestService(MathtestRepository mathtestRepository, MathtestMapper mathtestMapper) {
        this.mathtestRepository = mathtestRepository;
        this.mathtestMapper = mathtestMapper;
    }

    /**
     * Save a mathtest.
     *
     * @param mathtestDTO the entity to save.
     * @return the persisted entity.
     */
    public MathtestDTO save(MathtestDTO mathtestDTO) {
        log.debug("Request to save Mathtest : {}", mathtestDTO);
        Mathtest mathtest = mathtestMapper.toEntity(mathtestDTO);
        mathtest = mathtestRepository.save(mathtest);
        return mathtestMapper.toDto(mathtest);
    }

    /**
     * Update a mathtest.
     *
     * @param mathtestDTO the entity to save.
     * @return the persisted entity.
     */
    public MathtestDTO update(MathtestDTO mathtestDTO) {
        log.debug("Request to update Mathtest : {}", mathtestDTO);
        Mathtest mathtest = mathtestMapper.toEntity(mathtestDTO);
        mathtest = mathtestRepository.save(mathtest);
        return mathtestMapper.toDto(mathtest);
    }

    /**
     * Partially update a mathtest.
     *
     * @param mathtestDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MathtestDTO> partialUpdate(MathtestDTO mathtestDTO) {
        log.debug("Request to partially update Mathtest : {}", mathtestDTO);

        return mathtestRepository
            .findById(mathtestDTO.getId())
            .map(existingMathtest -> {
                mathtestMapper.partialUpdate(existingMathtest, mathtestDTO);

                return existingMathtest;
            })
            .map(mathtestRepository::save)
            .map(mathtestMapper::toDto);
    }

    /**
     * Get all the mathtests.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MathtestDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Mathtests");
        return mathtestRepository.findAll(pageable).map(mathtestMapper::toDto);
    }

    /**
     * Get all the mathtests with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<MathtestDTO> findAllWithEagerRelationships(Pageable pageable) {
        return mathtestRepository.findAllWithEagerRelationships(pageable).map(mathtestMapper::toDto);
    }

    /**
     * Get one mathtest by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MathtestDTO> findOne(Long id) {
        log.debug("Request to get Mathtest : {}", id);
        return mathtestRepository.findOneWithEagerRelationships(id).map(mathtestMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<MathtestDTO> findByIdAndUserIdInAssigned(Long id, Long userId) {
        log.debug("Request to get Mathtest : {} {}", id, userId);
        return mathtestRepository.findOneWithEagerRelationships(id).map(mathtestMapper::toDto);
    }

    /**
     * Delete the mathtest by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Mathtest : {}", id);
        mathtestRepository.deleteById(id);
    }
}
