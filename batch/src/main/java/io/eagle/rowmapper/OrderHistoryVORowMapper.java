package io.eagle.rowmapper;

import io.eagle.domain.order.vo.OrderHistoryVO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderHistoryVORowMapper implements RowMapper<OrderHistoryVO> {
    @Override
    public OrderHistoryVO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return OrderHistoryVO.builder()
            .id(rs.getLong("id"))
            .price(rs.getInt("price"))
            .amount(rs.getInt("amount"))
            .build();
    }
}
