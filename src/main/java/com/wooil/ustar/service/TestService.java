package com.wooil.ustar.service;

import com.wooil.ustar.domain.Test;
import com.wooil.ustar.dto.TestDto;
import com.wooil.ustar.dto.TestsDto;
import com.wooil.ustar.repository.TestRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
public class TestService {

    @Autowired
    private TestRepository testRepository;

    public TestsDto getAllTests() {
        List<Test> tests = testRepository.findAll();
        List<TestDto> testDtos = tests.stream().map(Test::convertToDto).toList();

        return TestsDto.builder()
                .tests(testDtos)
                .build();
    }

    public boolean createTest(String testValue) {
        Test test = Test.builder().testValue(testValue).build();
        Test savedTest = testRepository.save(test);

        return true;
    }

}
