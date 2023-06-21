package com.kfu.qs.repository;

import com.kfu.qs.domain.Mathtest;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface MathtestRepositoryWithBagRelationships {
    Optional<Mathtest> fetchBagRelationships(Optional<Mathtest> mathtest);

    List<Mathtest> fetchBagRelationships(List<Mathtest> mathtests);

    Page<Mathtest> fetchBagRelationships(Page<Mathtest> mathtests);
}
