package io.eagle.domain.vacation.controller;

import io.eagle.auth.AuthDetails;
import io.eagle.common.ApiResponse;
import io.eagle.domain.vacation.dto.*;
import io.eagle.domain.vacation.dto.request.CreateCahootsDto;
import io.eagle.domain.vacation.dto.response.ImminentInfoDto;
import io.eagle.entity.type.VacationStatusType;
import io.eagle.domain.vacation.service.CahootsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CahootsController {
    private final CahootsService cahootsService;

    @PostMapping("/auth/cahoots")
    public ApiResponse createCahoots (@Valid CreateCahootsDto createCahootsDto, @AuthenticationPrincipal AuthDetails authDetails){
        cahootsService.create(createCahootsDto, authDetails.getUser());
        return ApiResponse.createSuccessWithNoContent();
    }

    @GetMapping(value = "/cahoots/{cahootsId}", params = "info=detail")
    public ApiResponse getCahootsDetailInfo(@PathVariable("cahootsId") Long cahootsId){
        return ApiResponse.createSuccess(cahootsService.getDetail(cahootsId));
    }

    @GetMapping(value="/cahoots", params = "status=ongoing")
    public ApiResponse getBreifCahootsInfo(@Valid InfoConditionDto infoConditionDto){
        infoConditionDto.setTypes(new VacationStatusType[]{VacationStatusType.CAHOOTS_ONGOING});
        return ApiResponse.createSuccess(cahootsService.getBreifList(infoConditionDto));
    }

    @GetMapping(value="/cahoots", params = "status=ended")
    public ApiResponse getEndedBreifCahootsInfo(){
        VacationStatusType[] types = new VacationStatusType[]{VacationStatusType.CAHOOTS_CLOSE, VacationStatusType.CAHOOTS_OPEN};
        InfoConditionDto infoConditionDto = InfoConditionDto.builder().types(types).page(0).build();
        return ApiResponse.createSuccess(cahootsService.getBreifList(infoConditionDto));
    }

    @GetMapping(value="/cahoots",params = "status=ending-soon")
    public ApiResponse getEndedSoonBreifCahootsInfo(){
        VacationStatusType[] types = new VacationStatusType[]{VacationStatusType.CAHOOTS_ONGOING};
        InfoConditionDto infoConditionDto = InfoConditionDto.builder().types(types).page(0).build();
        return ApiResponse.createSuccess(cahootsService.getBreifV2List(infoConditionDto));
    }

    @GetMapping(value= "/cahoots/mini", params = "status=ending-soon")
    public ApiResponse getMostImminentEndedSoon(){
        return ApiResponse.createSuccess(cahootsService.getMostImminentCahoots());
    }

    @GetMapping("/cahoots/recent")
    public ApiResponse getlatestCahootsInfo(){
        return ApiResponse.createSuccess(cahootsService.getLatestsList());
    }
}
