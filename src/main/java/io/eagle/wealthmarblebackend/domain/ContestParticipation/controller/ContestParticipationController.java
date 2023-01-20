package io.eagle.wealthmarblebackend.domain.ContestParticipation.controller;


import io.eagle.wealthmarblebackend.domain.ContestParticipation.service.ContestParticipationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contest-participation")
@RequiredArgsConstructor
public class ContestParticipationController {
    private final ContestParticipationService contestParticipationService;

    @PostMapping
    public ResponseEntity participateCahoots (@PathVariable("cahootsId") Long cahootsId, @RequestBody Integer stocks){
        contestParticipationService.participate(cahootsId, stocks);
        return ResponseEntity.ok().build();
    }
}
