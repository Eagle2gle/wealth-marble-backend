package io.eagle.domain.ContestParticipation.controller;


import io.eagle.common.ApiResponse;
import io.eagle.domain.ContestParticipation.service.ContestParticipationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cahoots")
@RequiredArgsConstructor
public class ContestParticipationController {
    private final ContestParticipationService contestParticipationService;

    @PostMapping("/{cahootsId}")
    public ApiResponse participateCahoots (@PathVariable("cahootsId") Long cahootsId, @RequestBody Map<String,Integer> param){
        contestParticipationService.participate(cahootsId, param.get("stocks"));
        return ApiResponse.createSuccessWithNoContent();
    }

    @GetMapping(value = "/{cahootsId}", params = "info=history")
    public ApiResponse getCahootsHistoryInfo(@PathVariable("cahootsId") Long cahootsId){
        return ApiResponse.createSuccess(contestParticipationService.getHistory(cahootsId));
    }
}
