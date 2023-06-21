package com.kfu.qs.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.kfu.qs.domain.Question} entity. This class is used
 * in {@link com.kfu.qs.web.rest.QuestionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /questions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuestionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter plain;

    private StringFilter answer;

    private LongFilter testId;

    private Boolean distinct;

    public QuestionCriteria() {}

    public QuestionCriteria(QuestionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.plain = other.plain == null ? null : other.plain.copy();
        this.answer = other.answer == null ? null : other.answer.copy();
        this.testId = other.testId == null ? null : other.testId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public QuestionCriteria copy() {
        return new QuestionCriteria(this);
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

    public StringFilter getPlain() {
        return plain;
    }

    public StringFilter plain() {
        if (plain == null) {
            plain = new StringFilter();
        }
        return plain;
    }

    public void setPlain(StringFilter plain) {
        this.plain = plain;
    }

    public StringFilter getAnswer() {
        return answer;
    }

    public StringFilter answer() {
        if (answer == null) {
            answer = new StringFilter();
        }
        return answer;
    }

    public void setAnswer(StringFilter answer) {
        this.answer = answer;
    }

    public LongFilter getTestId() {
        return testId;
    }

    public LongFilter testId() {
        if (testId == null) {
            testId = new LongFilter();
        }
        return testId;
    }

    public void setTestId(LongFilter testId) {
        this.testId = testId;
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
        final QuestionCriteria that = (QuestionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(plain, that.plain) &&
            Objects.equals(answer, that.answer) &&
            Objects.equals(testId, that.testId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, plain, answer, testId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (plain != null ? "plain=" + plain + ", " : "") +
            (answer != null ? "answer=" + answer + ", " : "") +
            (testId != null ? "testId=" + testId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
