package io.eagle.wealthmarblebackend.domain.interest.repository;

import io.eagle.wealthmarblebackend.domain.interest.entity.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterestRepository extends JpaRepository<Interest, Long>, InterestRepositoryCustom {

}
