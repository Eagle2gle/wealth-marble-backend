package io.eagle.domain.picture.repository;

import java.util.List;

public interface PictureRepositoryCustom {
    List<String> findUrlsByCahootsId(Long cahootsId);
}
