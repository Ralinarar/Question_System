package com.kfu.qs.service.mapper;

import com.kfu.qs.domain.Template;
import com.kfu.qs.domain.User;
import com.kfu.qs.service.dto.TemplateDTO;
import com.kfu.qs.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Template} and its DTO {@link TemplateDTO}.
 */
@Mapper(componentModel = "spring")
public interface TemplateMapper extends EntityMapper<TemplateDTO, Template> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    TemplateDTO toDto(Template s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
