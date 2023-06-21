package com.kfu.qs.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TemplateMapperTest {

    private TemplateMapper templateMapper;

    @BeforeEach
    public void setUp() {
        templateMapper = new TemplateMapperImpl();
    }
}
