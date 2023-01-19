package io.eagle.wealthmarblebackend.domain.cahoots.domain.ContestParticipation;

import java.util.List;

public interface ContestParticipationRepositoryCustom {
    List<Integer> getCurrentContestNum(Long cahootsId);

}
