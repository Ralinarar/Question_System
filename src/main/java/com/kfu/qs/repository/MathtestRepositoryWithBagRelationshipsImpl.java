package com.kfu.qs.repository;

import com.kfu.qs.domain.Mathtest;
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
public class MathtestRepositoryWithBagRelationshipsImpl implements MathtestRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Mathtest> fetchBagRelationships(Optional<Mathtest> mathtest) {
        return mathtest.map(this::fetchAssigneds);
    }

    @Override
    public Page<Mathtest> fetchBagRelationships(Page<Mathtest> mathtests) {
        return new PageImpl<>(fetchBagRelationships(mathtests.getContent()), mathtests.getPageable(), mathtests.getTotalElements());
    }

    @Override
    public List<Mathtest> fetchBagRelationships(List<Mathtest> mathtests) {
        return Optional.of(mathtests).map(this::fetchAssigneds).orElse(Collections.emptyList());
    }

    Mathtest fetchAssigneds(Mathtest result) {
        return entityManager
            .createQuery(
                "select mathtest from Mathtest mathtest left join fetch mathtest.assigneds where mathtest is :mathtest",
                Mathtest.class
            )
            .setParameter("mathtest", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Mathtest> fetchAssigneds(List<Mathtest> mathtests) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, mathtests.size()).forEach(index -> order.put(mathtests.get(index).getId(), index));
        List<Mathtest> result = entityManager
            .createQuery(
                "select distinct mathtest from Mathtest mathtest left join fetch mathtest.assigneds where mathtest in :mathtests",
                Mathtest.class
            )
            .setParameter("mathtests", mathtests)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
