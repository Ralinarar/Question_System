package com.kfu.qs.repository;

import com.kfu.qs.domain.Predicate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface PredicateRepositoryWithBagRelationships {
    Optional<Predicate> fetchBagRelationships(Optional<Predicate> predicate);

    List<Predicate> fetchBagRelationships(List<Predicate> predicates);

    Page<Predicate> fetchBagRelationships(Page<Predicate> predicates);
}
