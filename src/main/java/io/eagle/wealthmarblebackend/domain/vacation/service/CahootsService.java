package io.eagle.wealthmarblebackend.domain.vacation.service;

import io.eagle.wealthmarblebackend.domain.picture.S3;
import io.eagle.wealthmarblebackend.domain.picture.entity.Picture;
import io.eagle.wealthmarblebackend.domain.ContestParticipation.repository.ContestParticipationRepository;
import io.eagle.wealthmarblebackend.domain.vacation.dto.*;
import io.eagle.wealthmarblebackend.domain.vacation.entity.Vacation;
import io.eagle.wealthmarblebackend.domain.vacation.entity.type.VacationStatusType;
import io.eagle.wealthmarblebackend.domain.vacation.repository.VacationRepository;
import io.eagle.wealthmarblebackend.exception.ApiException;
import io.eagle.wealthmarblebackend.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CahootsService {

    private final VacationRepository vacationRepository;
    private final ContestParticipationRepository contestParticipationRepository;
    private final S3 s3;

    public void create(CreateCahootsDto createCahootsDto) {
        createCahootsDto.validateCahootsPeriod();
        // TODO : 요청 사용자의 정보 추가
        Vacation newVacation = Vacation.builder().createCahootsDto(createCahootsDto).build();
        if(!createCahootsDto.isImagesEmpty()) {
            List<Picture> pictureList = s3.getUrlsFromS3(createCahootsDto.getImages(), "VACATION");
            newVacation.setPictureList(pictureList);
        }
        vacationRepository.save(newVacation);
    }

    public DetailCahootsDto getDetail(Long cahootsId) {
        Vacation vacation = vacationRepository.findById(cahootsId).orElseThrow(()-> new ApiException(ErrorCode.VACATION_NOT_FOUND));
        Integer currentTotalStock = contestParticipationRepository.getCurrentContestNum(cahootsId).orElse(0);
        Integer competitionRate =  currentTotalStock * 100 / vacation.getStock().getNum();
        return DetailCahootsDto.toDto(vacation, competitionRate);
    }

    public BreifCahootsListDto getBreifList(InfoConditionDto infoConditionDto){
        List<BreifCahootsDto> breifCahootsList = vacationRepository.getVacationsBreif(infoConditionDto);
        return BreifCahootsListDto.builder().result(breifCahootsList).build();
    }

    public BreifV2CahootsListDto getBreifV2List(InfoConditionDto infoConditionDto){
        List<BreifV2CahootsDto> breifCahootsList = vacationRepository.getVacationsBreifV2(infoConditionDto);
        return BreifV2CahootsListDto.builder().result(breifCahootsList).build();
    }
}
