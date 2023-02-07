package io.eagle.domain.interest.repository;

import io.eagle.entity.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterestRepository extends JpaRepository<Interest, Long>, InterestRepositoryCustom {
}
