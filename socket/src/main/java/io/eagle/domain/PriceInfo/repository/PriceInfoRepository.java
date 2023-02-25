package io.eagle.domain.PriceInfo.repository;

import io.eagle.entity.PriceInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceInfoRepository extends JpaRepository<PriceInfo, Long> {

    @Query(value = "SELECT * FROM price_info p WHERE p.vacation_id = :id ORDER BY p.created_at LIMIT 1", nativeQuery = true)
    PriceInfo findByVacationOrderByCreatedAt(
        @Param("id") Long id
    );

}
