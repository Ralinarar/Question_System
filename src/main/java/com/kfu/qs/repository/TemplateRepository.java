package com.kfu.qs.repository;

import com.kfu.qs.domain.Template;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Template entity.
 */
@Repository
public interface TemplateRepository extends JpaRepository<Template, Long>, JpaSpecificationExecutor<Template> {
    @Query("select template from Template template where template.user.login = ?#{principal.username}")
    List<Template> findByUserIsCurrentUser();

    default Optional<Template> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Template> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Template> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct template from Template template left join fetch template.user",
        countQuery = "select count(distinct template) from Template template"
    )
    Page<Template> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct template from Template template left join fetch template.user")
    List<Template> findAllWithToOneRelationships();

    @Query("select template from Template template left join fetch template.user where template.id =:id")
    Optional<Template> findOneWithToOneRelationships(@Param("id") Long id);
}
