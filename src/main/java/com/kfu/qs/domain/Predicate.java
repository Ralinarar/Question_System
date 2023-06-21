package com.kfu.qs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Predicate.
 */
@Entity
@Table(name = "predicate")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Predicate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "rdf_value")
    private String rdfValue;

    @ManyToOne(optional = false)
    @NotNull
    private User author;

    @ManyToMany
    @JoinTable(
        name = "rel_predicate__template",
        joinColumns = @JoinColumn(name = "predicate_id"),
        inverseJoinColumns = @JoinColumn(name = "template_id")
    )
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private Set<Template> templates = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Predicate id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRdfValue() {
        return this.rdfValue;
    }

    public Predicate rdfValue(String rdfValue) {
        this.setRdfValue(rdfValue);
        return this;
    }

    public void setRdfValue(String rdfValue) {
        this.rdfValue = rdfValue;
    }

    public User getAuthor() {
        return this.author;
    }

    public void setAuthor(User user) {
        this.author = user;
    }

    public Predicate author(User user) {
        this.setAuthor(user);
        return this;
    }

    public Set<Template> getTemplates() {
        return this.templates;
    }

    public void setTemplates(Set<Template> templates) {
        this.templates = templates;
    }

    public Predicate templates(Set<Template> templates) {
        this.setTemplates(templates);
        return this;
    }

    public Predicate addTemplate(Template template) {
        this.templates.add(template);
        return this;
    }

    public Predicate removeTemplate(Template template) {
        this.templates.remove(template);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Predicate)) {
            return false;
        }
        return id != null && id.equals(((Predicate) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Predicate{" +
            "id=" + getId() +
            ", rdfValue='" + getRdfValue() + "'" +
            "}";
    }
}
