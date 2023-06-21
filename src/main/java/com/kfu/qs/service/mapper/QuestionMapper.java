package com.kfu.qs.service.mapper;

import com.kfu.qs.domain.Mathtest;
import com.kfu.qs.domain.Question;
import com.kfu.qs.service.dto.MathtestDTO;
import com.kfu.qs.service.dto.QuestionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Question} and its DTO {@link QuestionDTO}.
 */
@Mapper(componentModel = "spring")
public interface QuestionMapper extends EntityMapper<QuestionDTO, Question> {
    @Mapping(target = "test", source = "test", qualifiedByName = "mathtestId")
    QuestionDTO toDto(Question s);

    @Named("mathtestId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MathtestDTO toDtoMathtestId(Mathtest mathtest);
}
