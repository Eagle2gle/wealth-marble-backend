package io.eagle.domain.interest.service;

import io.eagle.domain.interest.dto.InterestDto;
import io.eagle.domain.interest.dto.InterestInfoDto;
import io.eagle.domain.interest.repository.InterestRepository;
import io.eagle.entity.type.VacationStatusType;
import io.eagle.repository.UserRepository;
import io.eagle.domain.vacation.repository.VacationRepository;
import io.eagle.entity.Interest;
import io.eagle.entity.User;
import io.eagle.entity.Vacation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InterestService {

    private final InterestRepository interestRepository;
    private final UserRepository userRepository;
    private final VacationRepository vacationRepository;

    public List<InterestInfoDto> getAllInterest(String type, User user, Pageable pageable) {
        return interestRepository.findInterestByUser(user, pageable).stream().map(interest -> {
            VacationStatusType vacationType = interest.getVacation().getStatus();
            if (type.equals("market") && vacationType.equals(VacationStatusType.MARKET_ONGOING)) {
                return new InterestInfoDto(interest.getVacation());
            } else if (type.equals("cahoot") && !vacationType.equals(VacationStatusType.MARKET_ONGOING)) {
                return new InterestInfoDto(interest.getVacation());
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public Boolean isUserInterest(User user) {
        return interestRepository.existsByUser(user);
    }

    public Interest createInterest(InterestDto interestDto) {
        User user = userRepository.getReferenceById(interestDto.getUserId());
        Vacation vacation = vacationRepository.getReferenceById(interestDto.getVacationId());
        Interest interest = interestRepository.findByUserAndVacation(interestDto.getUserId(), interestDto.getVacationId());
        if (interest != null) {
            return null;
        }
        return interestRepository.save(new Interest(user, vacation));
    }

    public Boolean deleteInterest(InterestDto interestDto) {
        Interest interest = interestRepository.findByUserAndVacation(interestDto.getUserId(), interestDto.getVacationId());
        if (interest != null) {
            interestRepository.delete(interest);
            return true;
        }
        return false;
    }
}
