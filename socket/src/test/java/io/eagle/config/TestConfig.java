package io.eagle.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.eagle.domain.PriceInfo.repository.PriceInfoRepositoryImpl;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@TestConfiguration
public class TestConfig {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }

    @Bean
    public PriceInfoRepositoryImpl querydslOrderRepository() {
        return new PriceInfoRepositoryImpl(jpaQueryFactory());
    }

}

