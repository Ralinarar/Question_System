package com.kfu.qs.service;

import com.kfu.qs.domain.Template;
import com.kfu.qs.repository.TemplateRepository;
import com.kfu.qs.service.dto.TemplateDTO;
import com.kfu.qs.service.mapper.TemplateMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Template}.
 */
@Service
@Transactional
public class TemplateService {

    private final Logger log = LoggerFactory.getLogger(TemplateService.class);

    private final TemplateRepository templateRepository;

    private final TemplateMapper templateMapper;

    public TemplateService(TemplateRepository templateRepository, TemplateMapper templateMapper) {
        this.templateRepository = templateRepository;
        this.templateMapper = templateMapper;
    }

    /**
     * Save a template.
     *
     * @param templateDTO the entity to save.
     * @return the persisted entity.
     */
    public TemplateDTO save(TemplateDTO templateDTO) {
        log.debug("Request to save Template : {}", templateDTO);
        Template template = templateMapper.toEntity(templateDTO);
        template = templateRepository.save(template);
        return templateMapper.toDto(template);
    }

    /**
     * Update a template.
     *
     * @param templateDTO the entity to save.
     * @return the persisted entity.
     */
    public TemplateDTO update(TemplateDTO templateDTO) {
        log.debug("Request to update Template : {}", templateDTO);
        Template template = templateMapper.toEntity(templateDTO);
        template = templateRepository.save(template);
        return templateMapper.toDto(template);
    }

    /**
     * Partially update a template.
     *
     * @param templateDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TemplateDTO> partialUpdate(TemplateDTO templateDTO) {
        log.debug("Request to partially update Template : {}", templateDTO);

        return templateRepository
            .findById(templateDTO.getId())
            .map(existingTemplate -> {
                templateMapper.partialUpdate(existingTemplate, templateDTO);

                return existingTemplate;
            })
            .map(templateRepository::save)
            .map(templateMapper::toDto);
    }

    /**
     * Get all the templates.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TemplateDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Templates");
        return templateRepository.findAll(pageable).map(templateMapper::toDto);
    }

    /**
     * Get all the templates with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<TemplateDTO> findAllWithEagerRelationships(Pageable pageable) {
        return templateRepository.findAllWithEagerRelationships(pageable).map(templateMapper::toDto);
    }

    /**
     * Get one template by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TemplateDTO> findOne(Long id) {
        log.debug("Request to get Template : {}", id);
        return templateRepository.findOneWithEagerRelationships(id).map(templateMapper::toDto);
    }

    /**
     * Delete the template by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Template : {}", id);
        templateRepository.deleteById(id);
    }
}
