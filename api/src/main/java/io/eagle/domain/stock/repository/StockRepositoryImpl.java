package io.eagle.domain.stock.repository;

import io.eagle.domain.stock.dto.StockInfoDto;
import io.eagle.domain.stock.dto.response.StockMineDto;
import io.eagle.domain.stock.vo.StockMineVO;
import lombok.RequiredArgsConstructor;
import org.qlrm.mapper.JpaResultMapper;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class StockRepositoryImpl implements StockRepositoryCustom {

    private final EntityManager entityManager;

    @Override
    public List<StockInfoDto> getTotalStockValueByUser(Long userId) {
        String sql = "SELECT " +
            "(SELECT t.price FROM transaction t WHERE t.vacation_id = v.id ORDER BY created_at DESC LIMIT 1) as price, " +
            "SUM(s.amount) as amount " +
            "FROM stock s LEFT JOIN vacation v ON s.vacation_id = v.id " +
            "WHERE s.user_id = ? GROUP BY s.vacation_id";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, userId);

        JpaResultMapper jpaResultMapper = new JpaResultMapper();
        return new ArrayList<>(jpaResultMapper.list(query, StockInfoDto.class));
    }

    @Override
    public List<StockMineDto> getMineStockByUser(Long userId) {
        String sql = "SELECT " +
            "v.title as title, " +
            "(SELECT t.price FROM transaction t WHERE t.vacation_id = v.id ORDER BY created_at DESC LIMIT 1) as currentPrice, " +
            "SUM(s.price) as totalPrice, " +
            "SUM(s.amount) as totalAmount " +
            "FROM stock s LEFT JOIN vacation v ON s.vacation_id = v.id " +
            "WHERE s.user_id = ? GROUP BY s.vacation_id";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, userId);

        JpaResultMapper jpaResultMapper = new JpaResultMapper();
        return jpaResultMapper.list(query, StockMineVO.class).stream().map(vo -> {
            Double pricePerStock = (vo.getTotalPrice() / (double) vo.getTotalAmount());
            Double profitRate = Double.valueOf(String.format("%.2f", (vo.getCurrentPrice() * 100 / pricePerStock) - 100));
            return StockMineDto
                .builder()
                .title(vo.getTitle())
                .pricePerStock(pricePerStock)
                .currentPrice(vo.getCurrentPrice())
                .profitRate(profitRate)
                .totalAmount(vo.getTotalAmount())
                .build();
        }).collect(Collectors.toList());
    }
}
