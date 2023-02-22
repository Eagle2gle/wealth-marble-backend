package io.eagle.domain.stock.repository;

import io.eagle.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long>, StockRepositoryCustom {

}
