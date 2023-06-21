package com.kfu.qs.service.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

/**
 * A DTO for the {@link com.kfu.qs.domain.Mathtest} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuestionTestDTO implements Serializable {
    private String plain;

    private String answer;

    public QuestionTestDTO(String plain, String answer) {
        this.plain = plain;
        this.answer = answer;
    }

    public String getPlain() {
        return plain;
    }

    public void setPlain(String plain) {
        this.plain = plain;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        QuestionTestDTO that = (QuestionTestDTO) o;

        return new EqualsBuilder().append(plain, that.plain).append(answer, that.answer).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(plain).append(answer).toHashCode();
    }

    @Override
    public String toString() {
        return "QuestionTestDTO{" +
            "question='" + plain + '\'' +
            ", answer=" + answer +
            '}';
    }
}
