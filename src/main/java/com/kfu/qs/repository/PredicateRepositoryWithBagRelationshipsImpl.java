package com.kfu.qs.repository;

import com.kfu.qs.domain.Predicate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class PredicateRepositoryWithBagRelationshipsImpl implements PredicateRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Predicate> fetchBagRelationships(Optional<Predicate> predicate) {
        return predicate.map(this::fetchTemplates);
    }

    @Override
    public Page<Predicate> fetchBagRelationships(Page<Predicate> predicates) {
        return new PageImpl<>(fetchBagRelationships(predicates.getContent()), predicates.getPageable(), predicates.getTotalElements());
    }

    @Override
    public List<Predicate> fetchBagRelationships(List<Predicate> predicates) {
        return Optional.of(predicates).map(this::fetchTemplates).orElse(Collections.emptyList());
    }

    Predicate fetchTemplates(Predicate result) {
        return entityManager
            .createQuery(
                "select predicate from Predicate predicate left join fetch predicate.templates where predicate is :predicate",
                Predicate.class
            )
            .setParameter("predicate", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Predicate> fetchTemplates(List<Predicate> predicates) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, predicates.size()).forEach(index -> order.put(predicates.get(index).getId(), index));
        List<Predicate> result = entityManager
            .createQuery(
                "select distinct predicate from Predicate predicate left join fetch predicate.templates where predicate in :predicates",
                Predicate.class
            )
            .setParameter("predicates", predicates)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
