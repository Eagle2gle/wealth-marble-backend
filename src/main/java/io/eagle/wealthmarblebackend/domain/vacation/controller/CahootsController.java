package io.eagle.wealthmarblebackend.domain.vacation.controller;

import io.eagle.wealthmarblebackend.domain.vacation.dto.CreateCahootsDto;
import io.eagle.wealthmarblebackend.domain.vacation.dto.DetailCahootsDto;
import io.eagle.wealthmarblebackend.domain.vacation.service.CahootsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/cahoots")
@RequiredArgsConstructor
public class CahootsController {
    private final CahootsService cahootsService;

    @PostMapping
    public ResponseEntity createCahoots (@Valid CreateCahootsDto createCahootsDto){
        cahootsService.create(createCahootsDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{cahootsId}", params = "info=detail")
    public DetailCahootsDto getCahootsDetailInfo(@PathVariable("cahootsId") Long cahootsId){
        return cahootsService.getDetail(cahootsId);
    }

    @GetMapping(params = "status=ongoing")
    public BreifCahootsListDto getBreifCahootsInfo(){
        return cahootsService.getBreifList();
    }
}
