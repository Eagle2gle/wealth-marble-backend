package io.eagle.wealthmarblebackend.domain.ContestParticipation.repository;

import io.eagle.wealthmarblebackend.domain.ContestParticipation.entity.ContestParticipation;
import java.util.List;
import java.util.Optional;

public interface ContestParticipationRepositoryCustom {
    Optional<Integer> getCurrentContestNum(Long cahootsId);
    List<ContestParticipation> findAllByCahootsId(Long cahootsId);
}
