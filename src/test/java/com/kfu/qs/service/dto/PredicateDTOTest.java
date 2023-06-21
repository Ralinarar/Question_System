package com.kfu.qs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.kfu.qs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PredicateDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PredicateDTO.class);
        PredicateDTO predicateDTO1 = new PredicateDTO();
        predicateDTO1.setId(1L);
        PredicateDTO predicateDTO2 = new PredicateDTO();
        assertThat(predicateDTO1).isNotEqualTo(predicateDTO2);
        predicateDTO2.setId(predicateDTO1.getId());
        assertThat(predicateDTO1).isEqualTo(predicateDTO2);
        predicateDTO2.setId(2L);
        assertThat(predicateDTO1).isNotEqualTo(predicateDTO2);
        predicateDTO1.setId(null);
        assertThat(predicateDTO1).isNotEqualTo(predicateDTO2);
    }
}
