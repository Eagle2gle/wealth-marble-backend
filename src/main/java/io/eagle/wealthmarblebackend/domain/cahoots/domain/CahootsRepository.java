package io.eagle.wealthmarblebackend.domain.cahoots.domain;

import io.eagle.wealthmarblebackend.domain.cahoots.domain.type.VacationStatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CahootsRepository extends JpaRepository<Cahoots, Long> {
    Optional<Cahoots> findByTitle(String title);
    List<Cahoots> findByStatus(VacationStatusType type);
}
