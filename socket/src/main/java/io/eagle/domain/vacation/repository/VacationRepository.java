package io.eagle.domain.vacation.repository;


import io.eagle.entity.Vacation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface VacationRepository extends JpaRepository<Vacation, Long> {

}
