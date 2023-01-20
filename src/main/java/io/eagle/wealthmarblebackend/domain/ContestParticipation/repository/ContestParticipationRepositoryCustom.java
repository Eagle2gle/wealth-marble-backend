package io.eagle.wealthmarblebackend.domain.ContestParticipation.repository;

import java.util.List;
import java.util.Optional;

public interface ContestParticipationRepositoryCustom {
    Optional<Integer> getCurrentContestNum(Long cahootsId);

}
