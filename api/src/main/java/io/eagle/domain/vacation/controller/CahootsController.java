package io.eagle.domain.vacation.controller;

import io.eagle.common.ApiResponse;
import io.eagle.domain.vacation.dto.*;
import io.eagle.domain.vacation.dto.request.CreateCahootsDto;
import io.eagle.entity.type.VacationStatusType;
import io.eagle.domain.vacation.service.CahootsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/cahoots")
@RequiredArgsConstructor
public class CahootsController {
    private final CahootsService cahootsService;

    @PostMapping
    public ApiResponse createCahoots (@Valid CreateCahootsDto createCahootsDto){
        cahootsService.create(createCahootsDto);
        return ApiResponse.createSuccessWithNoContent();
    }

    @GetMapping(value = "/{cahootsId}", params = "info=detail")
    public ApiResponse getCahootsDetailInfo(@PathVariable("cahootsId") Long cahootsId){
        return ApiResponse.createSuccess(cahootsService.getDetail(cahootsId));
    }

    @GetMapping(params = "status=ongoing")
    public ApiResponse getBreifCahootsInfo(@Valid InfoConditionDto infoConditionDto){
        infoConditionDto.setTypes(new VacationStatusType[]{VacationStatusType.CAHOOTS_ONGOING});
        return ApiResponse.createSuccess(cahootsService.getBreifList(infoConditionDto));
    }

    @GetMapping(params = "status=ended")
    public ApiResponse getEndedBreifCahootsInfo(){
        VacationStatusType[] types = new VacationStatusType[]{VacationStatusType.CAHOOTS_CLOSE, VacationStatusType.CAHOOTS_OPEN};
        InfoConditionDto infoConditionDto = InfoConditionDto.builder().types(types).page(0).build();
        return ApiResponse.createSuccess(cahootsService.getBreifList(infoConditionDto));
    }

    @GetMapping(params = "status=ending-soon")
    public ApiResponse getEndedSoonBreifCahootsInfo(){
        VacationStatusType[] types = new VacationStatusType[]{VacationStatusType.CAHOOTS_ONGOING};
        InfoConditionDto infoConditionDto = InfoConditionDto.builder().types(types).page(0).build();
        return ApiResponse.createSuccess(cahootsService.getBreifV2List(infoConditionDto));
    }

    @GetMapping("/recent")
    public ApiResponse getlatestCahootsInfo(){
        return ApiResponse.createSuccess(cahootsService.getLatestsList());
    }
}
