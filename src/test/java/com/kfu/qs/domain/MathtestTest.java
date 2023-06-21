package com.kfu.qs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.kfu.qs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MathtestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Mathtest.class);
        Mathtest mathtest1 = new Mathtest();
        mathtest1.setId(1L);
        Mathtest mathtest2 = new Mathtest();
        mathtest2.setId(mathtest1.getId());
        assertThat(mathtest1).isEqualTo(mathtest2);
        mathtest2.setId(2L);
        assertThat(mathtest1).isNotEqualTo(mathtest2);
        mathtest1.setId(null);
        assertThat(mathtest1).isNotEqualTo(mathtest2);
    }
}
