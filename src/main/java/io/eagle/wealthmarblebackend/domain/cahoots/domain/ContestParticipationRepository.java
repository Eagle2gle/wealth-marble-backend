package io.eagle.wealthmarblebackend.domain.cahoots.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContestParticipationRepository extends JpaRepository<ContestParticipation, Long> {
}
