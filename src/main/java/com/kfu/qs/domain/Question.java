package com.kfu.qs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A Question.
 */
@Entity
@Table(name = "question")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "plain")
    private String plain;

    @Column(name = "answer")
    private String answer;

    @ManyToOne
    @JsonIgnoreProperties(value = { "assigneds" }, allowSetters = true)
    private Mathtest test;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Question id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlain() {
        return this.plain;
    }

    public Question plain(String plain) {
        this.setPlain(plain);
        return this;
    }

    public void setPlain(String plain) {
        this.plain = plain;
    }

    public String getAnswer() {
        return this.answer;
    }

    public Question answer(String answer) {
        this.setAnswer(answer);
        return this;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Mathtest getTest() {
        return this.test;
    }

    public void setTest(Mathtest mathtest) {
        this.test = mathtest;
    }

    public Question test(Mathtest mathtest) {
        this.setTest(mathtest);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Question)) {
            return false;
        }
        return id != null && id.equals(((Question) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Question{" +
            "id=" + getId() +
            ", plain='" + getPlain() + "'" +
            ", answer='" + getAnswer() + "'" +
            "}";
    }
}
