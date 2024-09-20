package com.wooil.ustar.controller;

import com.wooil.ustar.dto.TestsDto;
import com.wooil.ustar.service.TestService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/test")
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return new ResponseEntity<>("Hello!", HttpStatus.OK);
    }

    @GetMapping("/get")
    public TestsDto getTest() {
        return testService.getAllTests();
    }


}
