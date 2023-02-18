package io.eagle.rowmapper;

import io.eagle.entity.Vacation;
import io.eagle.entity.embeded.Period;
import io.eagle.entity.embeded.Plan;
import io.eagle.entity.embeded.Stock;
import io.eagle.entity.embeded.Theme;
import io.eagle.entity.type.ThemeBuildingType;
import io.eagle.entity.type.ThemeLocationType;
import io.eagle.entity.type.VacationStatusType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VacationMapper implements RowMapper<Vacation> {

    @Override
    public Vacation mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Vacation.builder()
            .id(rs.getLong("id"))
            .expectedRateOfReturn(rs.getInt("expected_rate_of_return"))
            .location(rs.getString("location"))
            .stock(Stock.builder()
                .price(rs.getLong("price"))
                .num(rs.getInt("num"))
                .build())
            .title(rs.getString("title"))
            .country(rs.getString("country"))
            .description(rs.getString("description"))
            .theme(Theme.builder()
                .building(ThemeBuildingType.valueOf(rs.getString("theme_building")))
                .location(ThemeLocationType.valueOf(rs.getString("theme_location")))
                .build())
            .stockPeriod(Period.builder()
                .start(rs.getDate("start").toLocalDate())
                .end(rs.getDate("end").toLocalDate())
                .build())
            .status(VacationStatusType.valueOf(rs.getString("status")))
            .shortDescription(rs.getString("short_description"))
            .plan(Plan.builder()
                .expectedTotalCost(rs.getLong("expected_total_cost"))
                .expectedMonth(rs.getInt("expected_month"))
                .build())
            .build();
    }
}
