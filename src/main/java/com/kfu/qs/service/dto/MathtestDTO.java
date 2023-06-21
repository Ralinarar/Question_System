package com.kfu.qs.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.kfu.qs.domain.Mathtest} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MathtestDTO implements Serializable {

    private Long id;

    private Integer amount;

    private String keys;

    private Integer treshold;

    private Set<UserDTO> assigneds = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getKeys() {
        return keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }

    public Integer getTreshold() {
        return treshold;
    }

    public void setTreshold(Integer treshold) {
        this.treshold = treshold;
    }

    public Set<UserDTO> getAssigneds() {
        return assigneds;
    }

    public void setAssigneds(Set<UserDTO> assigneds) {
        this.assigneds = assigneds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MathtestDTO)) {
            return false;
        }

        MathtestDTO mathtestDTO = (MathtestDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, mathtestDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MathtestDTO{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", keys='" + getKeys() + "'" +
            ", treshold=" + getTreshold() +
            ", assigneds=" + getAssigneds() +
            "}";
    }
}
