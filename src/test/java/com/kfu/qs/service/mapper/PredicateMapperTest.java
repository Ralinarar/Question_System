package com.kfu.qs.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PredicateMapperTest {

    private PredicateMapper predicateMapper;

    @BeforeEach
    public void setUp() {
        predicateMapper = new PredicateMapperImpl();
    }
}
