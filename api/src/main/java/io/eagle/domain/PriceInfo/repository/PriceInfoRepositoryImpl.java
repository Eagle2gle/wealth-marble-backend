package io.eagle.domain.PriceInfo.repository;

import com.querydsl.jpa.JPQLQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PriceInfoRepositoryImpl implements PriceInfoRepositoryCustom {

    private final JPQLQueryFactory jpqlQueryFactory;

}
