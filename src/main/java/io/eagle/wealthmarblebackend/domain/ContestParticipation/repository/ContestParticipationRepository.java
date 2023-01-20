package io.eagle.wealthmarblebackend.domain.ContestParticipation.repository;

import io.eagle.wealthmarblebackend.domain.ContestParticipation.entity.ContestParticipation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContestParticipationRepository extends JpaRepository<ContestParticipation, Long>, ContestParticipationRepositoryCustom {

}
