package io.eagle.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.eagle.domain.order.repository.OrderRepository;
import io.eagle.domain.order.service.OrderService;
import io.eagle.repository.UserRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

//@TestConfiguration
//public class TestConfig {
//
//    @PersistenceContext
//    private EntityManager entityManager;
//
//    @Bean
//    public JPAQueryFactory jpaQueryFactory() {
//        return new JPAQueryFactory(entityManager);
//    }
//
//}

