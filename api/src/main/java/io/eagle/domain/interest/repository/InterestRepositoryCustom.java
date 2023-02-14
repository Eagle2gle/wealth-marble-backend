package io.eagle.domain.interest.repository;

import io.eagle.entity.Interest;
import io.eagle.entity.User;
import io.eagle.entity.Vacation;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InterestRepositoryCustom {

    List<Interest> findInterestByUser(User user, Pageable pageable);
    Boolean existsByUser(User user);
    Interest findByUserAndVacation(Long userId, Long vacationId);
    List<Interest> findAllByVacation(Long vacationId);

}
