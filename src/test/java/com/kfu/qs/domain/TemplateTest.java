package com.kfu.qs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.kfu.qs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TemplateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Template.class);
        Template template1 = new Template();
        template1.setId(1L);
        Template template2 = new Template();
        template2.setId(template1.getId());
        assertThat(template1).isEqualTo(template2);
        template2.setId(2L);
        assertThat(template1).isNotEqualTo(template2);
        template1.setId(null);
        assertThat(template1).isNotEqualTo(template2);
    }
}
