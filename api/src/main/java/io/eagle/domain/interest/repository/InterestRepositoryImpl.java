package io.eagle.domain.interest.repository;

import com.querydsl.jpa.JPQLQueryFactory;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class InterestRepositoryImpl implements InterestRepositoryCustom {

    private final JPQLQueryFactory jpqlQueryFactory;


}
