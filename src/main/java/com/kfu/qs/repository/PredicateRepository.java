package com.kfu.qs.repository;

import com.kfu.qs.domain.Predicate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Predicate entity.
 *
 * When extending this class, extend PredicateRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface PredicateRepository
    extends PredicateRepositoryWithBagRelationships, JpaRepository<Predicate, Long>, JpaSpecificationExecutor<Predicate> {
    @Query("select predicate from Predicate predicate where predicate.author.login = ?#{principal.username}")
    List<Predicate> findByAuthorIsCurrentUser();

    default Optional<Predicate> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<Predicate> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<Predicate> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select distinct predicate from Predicate predicate left join fetch predicate.author",
        countQuery = "select count(distinct predicate) from Predicate predicate"
    )
    Page<Predicate> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct predicate from Predicate predicate left join fetch predicate.author")
    List<Predicate> findAllWithToOneRelationships();

    @Query("select predicate from Predicate predicate left join fetch predicate.author where predicate.id =:id")
    Optional<Predicate> findOneWithToOneRelationships(@Param("id") Long id);
}
