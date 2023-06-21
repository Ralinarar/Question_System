package com.kfu.qs.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Mathtest.
 */
@Entity
@Table(name = "mathtest")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Mathtest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "keys")
    private String keys;

    @Column(name = "treshold")
    private Integer treshold;

    @ManyToMany
    @JoinTable(
        name = "rel_mathtest__assigned",
        joinColumns = @JoinColumn(name = "mathtest_id"),
        inverseJoinColumns = @JoinColumn(name = "assigned_id")
    )
    private Set<User> assigneds = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Mathtest id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAmount() {
        return this.amount;
    }

    public Mathtest amount(Integer amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getKeys() {
        return this.keys;
    }

    public Mathtest keys(String keys) {
        this.setKeys(keys);
        return this;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }

    public Integer getTreshold() {
        return this.treshold;
    }

    public Mathtest treshold(Integer treshold) {
        this.setTreshold(treshold);
        return this;
    }

    public void setTreshold(Integer treshold) {
        this.treshold = treshold;
    }

    public Set<User> getAssigneds() {
        return this.assigneds;
    }

    public void setAssigneds(Set<User> users) {
        this.assigneds = users;
    }

    public Mathtest assigneds(Set<User> users) {
        this.setAssigneds(users);
        return this;
    }

    public Mathtest addAssigned(User user) {
        this.assigneds.add(user);
        return this;
    }

    public Mathtest removeAssigned(User user) {
        this.assigneds.remove(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Mathtest)) {
            return false;
        }
        return id != null && id.equals(((Mathtest) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Mathtest{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", keys='" + getKeys() + "'" +
            ", treshold=" + getTreshold() +
            "}";
    }
}
