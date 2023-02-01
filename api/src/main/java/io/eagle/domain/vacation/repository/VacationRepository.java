package io.eagle.domain.vacation.repository;


import io.eagle.entity.Vacation;
import io.eagle.entity.type.VacationStatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface VacationRepository extends JpaRepository<Vacation, Long>, VacationCustom {
    Optional<Vacation> findByTitle(String title);
    List<Vacation> findByStatus(VacationStatusType type);

    Optional<Vacation> findByIdAndStatus(Long id, VacationStatusType status);

}
