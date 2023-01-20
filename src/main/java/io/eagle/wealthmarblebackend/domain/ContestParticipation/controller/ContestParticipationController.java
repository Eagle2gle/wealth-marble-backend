package io.eagle.wealthmarblebackend.domain.ContestParticipation.controller;


import io.eagle.wealthmarblebackend.domain.ContestParticipation.dto.HistoryCahootsDto;
import io.eagle.wealthmarblebackend.domain.ContestParticipation.dto.HistoryCahootsDtoList;
import io.eagle.wealthmarblebackend.domain.ContestParticipation.service.ContestParticipationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cahoots")
@RequiredArgsConstructor
public class ContestParticipationController {
    private final ContestParticipationService contestParticipationService;

    @PostMapping("/{cahootsId}")
    public ResponseEntity participateCahoots (@PathVariable("cahootsId") Long cahootsId, @RequestBody Map<String,Integer> param){
        contestParticipationService.participate(cahootsId, param.get("stocks"));
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{cahootsId}", params = "info=history")
    public HistoryCahootsDtoList getCahootsHistoryInfo(@PathVariable("cahootsId") Long cahootsId){
        List<HistoryCahootsDto> result = contestParticipationService.getHistory(cahootsId);
        return new HistoryCahootsDtoList(result);
    }
}
