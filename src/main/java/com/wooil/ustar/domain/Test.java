package com.wooil.ustar.domain;

import com.wooil.ustar.dto.TestDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Table(name = "test")
@Getter
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "test_id")
    private Long testId;

    @Column(name = "test_value")
    private final String testValue;

    @Builder
    public Test(String testValue) {
        this.testValue = testValue;
    }

    public TestDto convertToDto(){
        return TestDto.builder().testId(this.testId).testValue(this.testValue).build();
    }
}
