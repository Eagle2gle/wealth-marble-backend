package io.eagle.domain.interest.repository;

import io.eagle.entity.Interest;
import io.eagle.entity.Vacation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterestRepository extends JpaRepository<Interest, Long>, InterestRepositoryCustom {

    List<Interest> findByVacation(Vacation referenceById);
}
