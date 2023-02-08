package io.eagle.domain.interest.controller;

import io.eagle.common.ApiResponse;
import io.eagle.domain.interest.dto.InterestDto;
import io.eagle.domain.interest.service.InterestService;
import io.eagle.entity.Interest;
import io.eagle.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class InterestController {

    private final InterestService interestService;

    @GetMapping("/interests")
    public ApiResponse getInterest() {
        return ApiResponse.createSuccess("hello interest");
    }

    @GetMapping("/interests/me/interest")
    public ApiResponse getAllUserInterests(@AuthenticationPrincipal User user) {
        return ApiResponse.createSuccess(interestService.getAllInterest(user));
    }

    @PostMapping("/interests")
    public ApiResponse createInterest(@RequestBody InterestDto interestDto) {
        Interest interest = interestService.createInterest(interestDto);
        return interest != null ? ApiResponse.createSuccess(interest) : ApiResponse.createError("해당 유저나 휴가 정보가 존재하지 않습니다.");
    }

    @DeleteMapping("/interests")
    public ApiResponse deleteInterest(@RequestBody InterestDto interestDto) {
        Boolean isSuccessDelete = interestService.deleteInterest(interestDto);
        return isSuccessDelete ? ApiResponse.createSuccessWithNoContent() : ApiResponse.createError("삭제에 실패하였습니다.");
    }
}
