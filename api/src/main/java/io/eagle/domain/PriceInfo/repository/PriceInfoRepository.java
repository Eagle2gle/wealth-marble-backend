package io.eagle.domain.PriceInfo.repository;

import io.eagle.entity.PriceInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PriceInfoRepository extends JpaRepository<PriceInfo, Long>, PriceInfoRepositoryCustom {

    @Query(value = "SELECT * FROM price_info p WHERE p.vacationId = :vacationId ORDER BY p.id LIMIT 1", nativeQuery = true)
    PriceInfo findOneByVacationId(@Param("vacationId") Long vacationId);

}
