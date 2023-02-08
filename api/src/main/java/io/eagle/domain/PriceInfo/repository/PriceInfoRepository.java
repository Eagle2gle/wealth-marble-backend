package io.eagle.domain.PriceInfo.repository;

import io.eagle.entity.PriceInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceInfoRepository extends JpaRepository<PriceInfo, Long>, PriceInfoRepositoryCustom {
}
