package io.eagle.domain.interest.controller;

import io.eagle.common.ApiResponse;
import io.eagle.domain.interest.service.InterestService;
import io.eagle.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "/api/interests")
@RequiredArgsConstructor
public class InterestController {

    private final InterestService interestService;

    @GetMapping("/me/interest")
    public ApiResponse getAllUserInterests(@AuthenticationPrincipal User user) {
        return ApiResponse.createSuccess(interestService.getAllInterest(user));
    }
}
