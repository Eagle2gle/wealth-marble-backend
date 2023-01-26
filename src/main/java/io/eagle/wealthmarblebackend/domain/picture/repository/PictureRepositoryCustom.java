package io.eagle.wealthmarblebackend.domain.picture.repository;

import io.eagle.wealthmarblebackend.domain.picture.entity.Picture;

import java.util.List;

public interface PictureRepositoryCustom {
    List<String> findUrlsByCahootsId(Long cahootsId);
}
