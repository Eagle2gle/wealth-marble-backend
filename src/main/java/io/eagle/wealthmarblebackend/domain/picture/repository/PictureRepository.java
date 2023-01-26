package io.eagle.wealthmarblebackend.domain.picture.repository;

import io.eagle.wealthmarblebackend.domain.picture.entity.Picture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PictureRepository extends JpaRepository<Picture, Long>, PictureRepositoryCustom {
}
