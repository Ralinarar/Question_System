package com.kfu.qs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.kfu.qs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MathtestDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MathtestDTO.class);
        MathtestDTO mathtestDTO1 = new MathtestDTO();
        mathtestDTO1.setId(1L);
        MathtestDTO mathtestDTO2 = new MathtestDTO();
        assertThat(mathtestDTO1).isNotEqualTo(mathtestDTO2);
        mathtestDTO2.setId(mathtestDTO1.getId());
        assertThat(mathtestDTO1).isEqualTo(mathtestDTO2);
        mathtestDTO2.setId(2L);
        assertThat(mathtestDTO1).isNotEqualTo(mathtestDTO2);
        mathtestDTO1.setId(null);
        assertThat(mathtestDTO1).isNotEqualTo(mathtestDTO2);
    }
}
