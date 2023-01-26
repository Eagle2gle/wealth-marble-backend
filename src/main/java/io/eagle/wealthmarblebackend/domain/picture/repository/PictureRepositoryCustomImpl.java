package io.eagle.wealthmarblebackend.domain.picture.repository;

import com.querydsl.jpa.JPQLQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static io.eagle.wealthmarblebackend.domain.picture.entity.QPicture.picture;

@RequiredArgsConstructor
public class PictureRepositoryCustomImpl implements PictureRepositoryCustom{
    private final JPQLQueryFactory queryFactory;


    @Override
    public List<String> findUrlsByCahootsId(Long cahootsId){
        return queryFactory
                .select(picture.url)
                .from(picture)
                .where(picture.vacation.id.eq(cahootsId))
                .fetch();

    }
}
