package io.eagle.domain.vacation.service;

import io.eagle.domain.interest.repository.InterestRepository;
import io.eagle.domain.picture.repository.PictureRepository;
import io.eagle.domain.transaction.repository.TransactionRepository;
import io.eagle.domain.vacation.dto.MarketDetailDto;
import io.eagle.domain.vacation.repository.VacationRepository;
import io.eagle.entity.Transaction;
import io.eagle.entity.Vacation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MarketService {

    private final VacationRepository vacationRepository;
    private final TransactionRepository transactionRepository;
    private final PictureRepository pictureRepository;
    private final InterestRepository interestRepository;

    public MarketDetailDto getOne(Long vacationId) {
        Vacation vacation = vacationRepository.findById(vacationId).orElse(null);
        if (vacation != null) {
            Transaction transaction = transactionRepository.findByVacation(vacationId);
            String picture = pictureRepository.findUrlsByCahootsId(vacationId).get(0);
            List<Long> userIds = interestRepository.findAllByVacation(vacationId)
                .stream()
                .map(interest -> interest.getUser().getId())
                .collect(Collectors.toList());
            return MarketDetailDto
                .builder()
                .title(vacation.getTitle())
                .location(vacation.getLocation())
                .shortDescription(vacation.getShortDescription())
                .expectedRateOfReturn(vacation.getExpectedRateOfReturn())
                .price(transaction.getPrice())
                .picture(picture)
                .userIds(userIds)
                .build();
        }
        return null;
    }

}
