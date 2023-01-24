package io.eagle.wealthmarblebackend.domain.vacation.controller;

import io.eagle.wealthmarblebackend.domain.vacation.dto.BreifCahootListDto;
import io.eagle.wealthmarblebackend.domain.vacation.dto.CreateCahootsDto;
import io.eagle.wealthmarblebackend.domain.vacation.dto.DetailCahootsDto;
import io.eagle.wealthmarblebackend.domain.vacation.entity.type.VacationStatusType;
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
    public BreifCahootListDto getBreifCahootsInfo(Integer offset){
        VacationStatusType[] types = new VacationStatusType[]{VacationStatusType.CAHOOTS_ONGOING};
        return cahootsService.getBreifList(types, offset);
    }

    @GetMapping(params = "status=ended")
    public BreifCahootListDto getEndedBreifCahootsInfo(){
        VacationStatusType[] types = new VacationStatusType[]{VacationStatusType.CAHOOTS_CLOSE, VacationStatusType.CAHOOTS_OPEN};
        return cahootsService.getBreifList(types, 0);
    }
}
