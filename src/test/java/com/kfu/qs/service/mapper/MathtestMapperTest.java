package com.kfu.qs.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MathtestMapperTest {

    private MathtestMapper mathtestMapper;

    @BeforeEach
    public void setUp() {
        mathtestMapper = new MathtestMapperImpl();
    }
}
