package io.eagle.domain.interest.controller;

import io.eagle.common.ApiResponse;
import io.eagle.domain.interest.dto.InterestDto;
import io.eagle.domain.interest.service.InterestService;
import io.eagle.entity.Interest;
import io.eagle.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "/api/interests")
@RequiredArgsConstructor
public class InterestController {

    private final InterestService interestService;

    @GetMapping("/me/interest")
    public ApiResponse getAllUserInterests(@AuthenticationPrincipal User user) {
        return ApiResponse.createSuccess(interestService.getAllInterest(user));
    }

    @PostMapping("/")
    public ApiResponse createInterest(@RequestBody InterestDto interestDto) {
        Interest interest = interestService.createInterest(interestDto);
        return interest != null ? ApiResponse.createSuccess(interest) : ApiResponse.createError("해당 유저나 휴가 정보가 존재하지 않습니다.");
    }
}
