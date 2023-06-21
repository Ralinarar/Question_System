package com.kfu.qs.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.kfu.qs.domain.Predicate} entity. This class is used
 * in {@link com.kfu.qs.web.rest.PredicateResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /predicates?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PredicateCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter rdfValue;

    private LongFilter authorId;

    private LongFilter templateId;

    private Boolean distinct;

    public PredicateCriteria() {}

    public PredicateCriteria(PredicateCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.rdfValue = other.rdfValue == null ? null : other.rdfValue.copy();
        this.authorId = other.authorId == null ? null : other.authorId.copy();
        this.templateId = other.templateId == null ? null : other.templateId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PredicateCriteria copy() {
        return new PredicateCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getRdfValue() {
        return rdfValue;
    }

    public StringFilter rdfValue() {
        if (rdfValue == null) {
            rdfValue = new StringFilter();
        }
        return rdfValue;
    }

    public void setRdfValue(StringFilter rdfValue) {
        this.rdfValue = rdfValue;
    }

    public LongFilter getAuthorId() {
        return authorId;
    }

    public LongFilter authorId() {
        if (authorId == null) {
            authorId = new LongFilter();
        }
        return authorId;
    }

    public void setAuthorId(LongFilter authorId) {
        this.authorId = authorId;
    }

    public LongFilter getTemplateId() {
        return templateId;
    }

    public LongFilter templateId() {
        if (templateId == null) {
            templateId = new LongFilter();
        }
        return templateId;
    }

    public void setTemplateId(LongFilter templateId) {
        this.templateId = templateId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PredicateCriteria that = (PredicateCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(rdfValue, that.rdfValue) &&
            Objects.equals(authorId, that.authorId) &&
            Objects.equals(templateId, that.templateId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rdfValue, authorId, templateId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PredicateCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (rdfValue != null ? "rdfValue=" + rdfValue + ", " : "") +
            (authorId != null ? "authorId=" + authorId + ", " : "") +
            (templateId != null ? "templateId=" + templateId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
