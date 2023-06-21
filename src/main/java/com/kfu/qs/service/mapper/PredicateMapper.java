package com.kfu.qs.service.mapper;

import com.kfu.qs.domain.Predicate;
import com.kfu.qs.domain.Template;
import com.kfu.qs.domain.User;
import com.kfu.qs.service.dto.PredicateDTO;
import com.kfu.qs.service.dto.TemplateDTO;
import com.kfu.qs.service.dto.UserDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Predicate} and its DTO {@link PredicateDTO}.
 */
@Mapper(componentModel = "spring")
public interface PredicateMapper extends EntityMapper<PredicateDTO, Predicate> {
    @Mapping(target = "author", source = "author", qualifiedByName = "userLogin")
    @Mapping(target = "templates", source = "templates", qualifiedByName = "templateMockSet")
    PredicateDTO toDto(Predicate s);

    @Mapping(target = "removeTemplate", ignore = true)
    Predicate toEntity(PredicateDTO predicateDTO);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("templateMock")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "mock", source = "mock")
    TemplateDTO toDtoTemplateMock(Template template);

    @Named("templateMockSet")
    default Set<TemplateDTO> toDtoTemplateMockSet(Set<Template> template) {
        return template.stream().map(this::toDtoTemplateMock).collect(Collectors.toSet());
    }
}
