package io.eagle.domain.vacation.service;

import io.eagle.domain.interest.repository.InterestRepository;
import io.eagle.domain.picture.S3;
import io.eagle.domain.vacation.dto.request.CreateCahootsDto;
import io.eagle.domain.vacation.dto.response.*;
import io.eagle.entity.Picture;
import io.eagle.domain.picture.repository.PictureRepository;
import io.eagle.domain.vacation.dto.*;
import io.eagle.entity.Vacation;
import io.eagle.domain.vacation.repository.VacationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CahootsService {

    private final VacationRepository vacationRepository;
    private final PictureRepository pictureRepository;

    private final S3 s3;

    public void create(CreateCahootsDto createCahootsDto) {
        createCahootsDto.validateCahootsPeriod();
        // TODO : 요청 사용자의 정보 추가
        Vacation newVacation = createCahootsDto.buildVacation();
        System.out.println(newVacation);
        if(!createCahootsDto.isImagesEmpty()) {
            List<Picture> pictureList = s3.getUrlsFromS3(createCahootsDto.getImages(), "VACATION");
            newVacation.setPictureList(pictureList);
        }
        vacationRepository.save(newVacation);
    }

    public DetailCahootsDto getDetail(Long cahootsId) {
        DetailCahootsDto detailCahootsDto = vacationRepository.getVacationDetail(cahootsId).checkNull();
        detailCahootsDto.setImages(getImageUrls(cahootsId));
        return detailCahootsDto;
    }

    public BreifCahootsListDto getBreifList(InfoConditionDto infoConditionDto){
        List<BreifCahootsDto> breifCahootsList = vacationRepository.getVacationsBreif(infoConditionDto);
        // TODO : 사용자 정보 추가
        List<Long> myInterestVacation = vacationRepository.findVacationIdByUserInterested(1L);
        breifCahootsList.forEach(breifCahootsDto -> {
            breifCahootsDto.setImages(getImageUrls(breifCahootsDto.getId()));
            breifCahootsDto.setIsInterest(myInterestVacation.contains(breifCahootsDto.getId()));
        });
        return BreifCahootsListDto.builder().result(breifCahootsList).build();
    }

    public BreifV2CahootsListDto getBreifV2List(InfoConditionDto infoConditionDto){
        List<BreifV2CahootsDto> breifCahootsList = vacationRepository.getVacationsBreifV2(infoConditionDto);
        breifCahootsList.forEach(breifCahootsDto -> {breifCahootsDto.setImages(getImageUrls(breifCahootsDto.getId()));});
        return BreifV2CahootsListDto.builder().result(breifCahootsList).build();
    }

    public LatestCahootsListDto getLatestsList(){
        List<LatestCahootsDto> latestCahootsDtoList = vacationRepository.findLatestVacations();
        latestCahootsDtoList.forEach(latestCahootsDto -> {latestCahootsDto.setImages(getImageUrls(latestCahootsDto.getId()));});
        return LatestCahootsListDto.builder().result(latestCahootsDtoList).build();
    }

    public List<String> getImageUrls(Long id){
        return pictureRepository.findUrlsByCahootsId(id);
    }
}
