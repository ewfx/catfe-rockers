package org.rockers.paymentgen.controller;

import lombok.RequiredArgsConstructor;
import org.rockers.paymentgen.service.OpenAIService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/testcases")
@RequiredArgsConstructor
public class TestGenController {
    private final OpenAIService openAIService;

    @GetMapping("/generate")
    public List<String> generateTestCases(
            @RequestParam String paymentType,
            @RequestParam int count,
            @RequestParam(required = false) String requirements) throws IOException {
        return openAIService.generateTestCases(paymentType, count, requirements);
    }
}