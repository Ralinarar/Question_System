package com.kfu.qs.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.kfu.qs.domain.Mathtest} entity. This class is used
 * in {@link com.kfu.qs.web.rest.MathtestResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /mathtests?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MathtestCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter amount;

    private StringFilter keys;

    private IntegerFilter treshold;

    private LongFilter assignedId;

    private Boolean distinct;

    public MathtestCriteria() {}

    public MathtestCriteria(MathtestCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.amount = other.amount == null ? null : other.amount.copy();
        this.keys = other.keys == null ? null : other.keys.copy();
        this.treshold = other.treshold == null ? null : other.treshold.copy();
        this.assignedId = other.assignedId == null ? null : other.assignedId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public MathtestCriteria copy() {
        return new MathtestCriteria(this);
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

    public IntegerFilter getAmount() {
        return amount;
    }

    public IntegerFilter amount() {
        if (amount == null) {
            amount = new IntegerFilter();
        }
        return amount;
    }

    public void setAmount(IntegerFilter amount) {
        this.amount = amount;
    }

    public StringFilter getKeys() {
        return keys;
    }

    public StringFilter keys() {
        if (keys == null) {
            keys = new StringFilter();
        }
        return keys;
    }

    public void setKeys(StringFilter keys) {
        this.keys = keys;
    }

    public IntegerFilter getTreshold() {
        return treshold;
    }

    public IntegerFilter treshold() {
        if (treshold == null) {
            treshold = new IntegerFilter();
        }
        return treshold;
    }

    public void setTreshold(IntegerFilter treshold) {
        this.treshold = treshold;
    }

    public LongFilter getAssignedId() {
        return assignedId;
    }

    public LongFilter assignedId() {
        if (assignedId == null) {
            assignedId = new LongFilter();
        }
        return assignedId;
    }

    public void setAssignedId(LongFilter assignedId) {
        this.assignedId = assignedId;
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
        final MathtestCriteria that = (MathtestCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(keys, that.keys) &&
            Objects.equals(treshold, that.treshold) &&
            Objects.equals(assignedId, that.assignedId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, keys, treshold, assignedId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MathtestCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (amount != null ? "amount=" + amount + ", " : "") +
            (keys != null ? "keys=" + keys + ", " : "") +
            (treshold != null ? "treshold=" + treshold + ", " : "") +
            (assignedId != null ? "assignedId=" + assignedId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
