package io.eagle.domain.ContestParticipation.controller;


import io.eagle.auth.AuthDetails;
import io.eagle.common.ApiResponse;
import io.eagle.domain.ContestParticipation.service.ContestParticipationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ContestParticipationController {
    private final ContestParticipationService contestParticipationService;

    @PostMapping("/auth/cahoots/{cahootsId}")
    public ApiResponse participateCahoots (@PathVariable("cahootsId") Long cahootsId, @RequestBody Map<String,Integer> param, @AuthenticationPrincipal AuthDetails authDetails){
        contestParticipationService.participate(cahootsId, param.get("stocks"), authDetails.getUser());
        return ApiResponse.createSuccessWithNoContent();
    }

    @GetMapping(value = "/cahoots/{cahootsId}", params = "info=history")
    public ApiResponse getCahootsHistoryInfo(@PathVariable("cahootsId") Long cahootsId){
        return ApiResponse.createSuccess(contestParticipationService.getHistory(cahootsId));
    }

    @GetMapping("/auth/contestParticipation/me")
    public ApiResponse getMyContestParticipation(
        @AuthenticationPrincipal AuthDetails authDetails
        ) {
        return ApiResponse.createSuccess(contestParticipationService.getMyContestParticipation(authDetails.getUser()));
    }
}
