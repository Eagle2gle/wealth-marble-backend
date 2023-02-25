package io.eagle.domain.vacation.service;

import io.eagle.domain.interest.repository.InterestRepository;
import io.eagle.domain.picture.S3;
import io.eagle.domain.vacation.dto.request.CreateCahootsDto;
import io.eagle.domain.vacation.dto.response.*;
import io.eagle.entity.Interest;
import io.eagle.entity.Picture;
import io.eagle.domain.picture.repository.PictureRepository;
import io.eagle.domain.vacation.dto.*;
import io.eagle.entity.User;
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
        DetailCahootsDto detailCahootsDto = vacationRepository.getVacationDetail(cahootsId).checkNull();
        List<Interest> interests = interestRepository.findByVacation(vacationRepository.getReferenceById(cahootsId));
        detailCahootsDto.setInterestCount(interests.size());
        Boolean isInterest = (userId != null ? interests.stream().map(Interest::getUser).map(User::getId).anyMatch(id -> id.equals(userId)) : false);
        detailCahootsDto.setIsInterest(isInterest);
        detailCahootsDto.setImages(getImageUrls(cahootsId));
        return detailCahootsDto;
    }

    public List<BreifCahootsDto> getBreifList(InfoConditionDto infoConditionDto){
        List<BreifCahootsDto> breifCahootsList = vacationRepository.getVacationsBreif(infoConditionDto);
        Long userId = infoConditionDto.getUserId();
        List<Long> myInterestVacation = (userId != null ? vacationRepository.findVacationIdByUserInterested(infoConditionDto.getUserId()) : List.of());
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
