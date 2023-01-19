package io.eagle.wealthmarblebackend.domain.cahoots.controller;

import io.eagle.wealthmarblebackend.domain.cahoots.dto.CreateCahootsDto;
import io.eagle.wealthmarblebackend.domain.cahoots.dto.DetailCahootsDto;
import io.eagle.wealthmarblebackend.domain.cahoots.dto.HistoryCahootsDto;
import io.eagle.wealthmarblebackend.domain.cahoots.service.CahootsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

//    @GetMapping(value = "/{cahootsId}", params = "info=history")
//    public HistoryCahootsDto getCahootsHistoryInfo(@PathVariable("cahootsId") Long cahootsId){
////        return cahootsService.getHistory(cahootsId);
//    }
}
