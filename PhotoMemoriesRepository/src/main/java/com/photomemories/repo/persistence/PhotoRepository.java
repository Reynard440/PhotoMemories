package com.photomemories.repo.persistence;

import com.photomemories.domain.persistence.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Integer> {
    @Query("select (count(p) > 0) from Photo p where p.PhotoId = ?1")
    boolean existsByPhotoId(Integer PhotoId);
}
