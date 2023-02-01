package io.eagle.domain.picture.repository;

import io.eagle.entity.Picture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PictureRepository extends JpaRepository<Picture, Long>, PictureRepositoryCustom {
}
