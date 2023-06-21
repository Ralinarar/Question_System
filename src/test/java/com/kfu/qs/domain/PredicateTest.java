package com.kfu.qs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.kfu.qs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PredicateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Predicate.class);
        Predicate predicate1 = new Predicate();
        predicate1.setId(1L);
        Predicate predicate2 = new Predicate();
        predicate2.setId(predicate1.getId());
        assertThat(predicate1).isEqualTo(predicate2);
        predicate2.setId(2L);
        assertThat(predicate1).isNotEqualTo(predicate2);
        predicate1.setId(null);
        assertThat(predicate1).isNotEqualTo(predicate2);
    }
}
