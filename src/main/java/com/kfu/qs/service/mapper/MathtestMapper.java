package com.kfu.qs.service.mapper;

import com.kfu.qs.domain.Mathtest;
import com.kfu.qs.domain.User;
import com.kfu.qs.service.dto.MathtestDTO;
import com.kfu.qs.service.dto.UserDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Mathtest} and its DTO {@link MathtestDTO}.
 */
@Mapper(componentModel = "spring")
public interface MathtestMapper extends EntityMapper<MathtestDTO, Mathtest> {
    @Mapping(target = "assigneds", source = "assigneds", qualifiedByName = "userLoginSet")
    MathtestDTO toDto(Mathtest s);

    @Mapping(target = "removeAssigned", ignore = true)
    Mathtest toEntity(MathtestDTO mathtestDTO);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("userLoginSet")
    default Set<UserDTO> toDtoUserLoginSet(Set<User> user) {
        return user.stream().map(this::toDtoUserLogin).collect(Collectors.toSet());
    }
}
