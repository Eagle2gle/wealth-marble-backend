package io.eagle.wealthmarblebackend.domain.cahoots.domain.ContestParticipation;

import java.util.List;
import java.util.Optional;

public interface ContestParticipationRepositoryCustom {
    Optional<Integer> getCurrentContestNum(Long cahootsId);

}
