package io.eagle.domain.stock.repository;

import io.eagle.entity.Stock;
import io.eagle.entity.User;
import io.eagle.entity.Vacation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long>, StockRepositoryCustom {

    Optional<Stock> findByUserAndVacation(User user, Vacation vacation);

}
