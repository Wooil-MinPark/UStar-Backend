package com.wooil.ustar.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TestsDto {
    private List<TestDto> tests;

    public TestsDto(List<TestDto> tests) {
        this.tests = tests;
    }
}
