package io.eagle.domain.interest.repository;

import io.eagle.entity.Interest;
import io.eagle.entity.User;

import java.util.List;

public interface InterestRepositoryCustom {

    List<Interest> findAllByUser(User user);
    Boolean existsByUser(User user);
    Interest findByUserAndVacation(Long userId, Long vacationId);

}
