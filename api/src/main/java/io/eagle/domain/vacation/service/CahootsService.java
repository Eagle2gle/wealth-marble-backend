package io.eagle.domain.vacation.service;

import io.eagle.domain.interest.repository.InterestRepository;
import io.eagle.domain.picture.S3;
import io.eagle.domain.vacation.dto.request.CreateCahootsDto;
import io.eagle.domain.vacation.dto.response.*;
import io.eagle.domain.vacation.vo.DetailCahootsVO;
import io.eagle.entity.Interest;
import io.eagle.entity.Picture;
import io.eagle.domain.picture.repository.PictureRepository;
import io.eagle.domain.vacation.dto.*;
import io.eagle.entity.User;
import io.eagle.entity.Vacation;
import io.eagle.domain.vacation.repository.VacationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

import static io.eagle.entity.type.MarketRankingType.CountryKey;
import static io.eagle.entity.type.VacationStatusType.MARKET_ONGOING;

@Service
@RequiredArgsConstructor
public class CahootsService {

    private final VacationRepository vacationRepository;
    private final PictureRepository pictureRepository;
    private final InterestRepository interestRepository;

    private final S3 s3;

    public void create(CreateCahootsDto createCahootsDto, User user) {
        createCahootsDto.validateCahootsPeriod();
        Vacation newVacation = createCahootsDto.buildVacation();
        newVacation.setUser(user);
        if(!createCahootsDto.isImagesEmpty()) {
            List<Picture> pictureList = s3.getUrlsFromS3(createCahootsDto.getImages(), "VACATION");
            newVacation.setPictureList(pictureList);
        }
        vacationRepository.save(newVacation);
    }

    public DetailCahootsDto getDetail(Long cahootsId, Long userId) {
        List<DetailCahootsVO> vacationDetailList = vacationRepository.findVacationDetail(cahootsId, userId);
        DetailCahootsDto detailCahootsDto = DetailCahootsDto.toDto(vacationDetailList);
        return detailCahootsDto;
    }

    public List<BreifCahootsDto> getBreifList(InfoConditionDto infoConditionDto){
        List<BreifCahootsDto> breifCahootsList = vacationRepository.getVacationsBreif(infoConditionDto);
        Long userId = infoConditionDto.getUserId();
        List<Long> myInterestVacation = (userId != null ? vacationRepository.findVacationIdByUserInterested(userId) : List.of());
        breifCahootsList.forEach(breifCahootsDto -> {
            breifCahootsDto.setImages(getImageUrls(breifCahootsDto.getId()));
            breifCahootsDto.setIsInterest(myInterestVacation.contains(breifCahootsDto.getId()));
        });
        return breifCahootsList;
    }

    public List<BreifV2CahootsDto> getBreifV2List(InfoConditionDto infoConditionDto){
        List<BreifV2CahootsDto> breifCahootsList = vacationRepository.getVacationsBreifV2(infoConditionDto);
        breifCahootsList.forEach(breifCahootsDto -> {breifCahootsDto.setImages(getImageUrls(breifCahootsDto.getId()));});
        return breifCahootsList;
    }

    public List<ImminentInfoDto> getMostImminentCahoots(){
        return vacationRepository.findByImminentEndVacation();
    }

    public List<LatestCahootsDto> getLatestsList(){
        List<LatestCahootsDto> latestCahootsDtoList = vacationRepository.findLatestVacations();
        latestCahootsDtoList.forEach(latestCahootsDto -> {latestCahootsDto.setImages(getImageUrls(latestCahootsDto.getId()));});
        return latestCahootsDtoList;
    }

    public List<String> getImageUrls(Long id){
        return pictureRepository.findUrlsByCahootsId(id);
    }


}
