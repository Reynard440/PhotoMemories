package com.photomemories.repo.persistence;

import com.photomemories.domain.persistence.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Integer> {

    //TODO: replace boolean with int for the delete photo method!!!
    @Transactional
    @Modifying
    @Query("delete from Photo p where p.PhotoId = ?1")
    boolean deleteByPhotoId(Integer PhotoId);

    @Query("select (count(p) > 0) from Photo p where p.PhotoId = ?1")
    boolean existsByPhotoId(Integer PhotoId);
}
