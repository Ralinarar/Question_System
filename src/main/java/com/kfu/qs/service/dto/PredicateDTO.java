package com.kfu.qs.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.kfu.qs.domain.Predicate} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PredicateDTO implements Serializable {

    private Long id;

    private String rdfValue;

    private UserDTO author;

    private Set<TemplateDTO> templates = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRdfValue() {
        return rdfValue;
    }

    public void setRdfValue(String rdfValue) {
        this.rdfValue = rdfValue;
    }

    public UserDTO getAuthor() {
        return author;
    }

    public void setAuthor(UserDTO author) {
        this.author = author;
    }

    public Set<TemplateDTO> getTemplates() {
        return templates;
    }

    public void setTemplates(Set<TemplateDTO> templates) {
        this.templates = templates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PredicateDTO)) {
            return false;
        }

        PredicateDTO predicateDTO = (PredicateDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, predicateDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PredicateDTO{" +
            "id=" + getId() +
            ", rdfValue='" + getRdfValue() + "'" +
            ", author=" + getAuthor() +
            ", templates=" + getTemplates() +
            "}";
    }
}
