package com.photomemories.repo.persistence;

import com.photomemories.domain.persistence.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Integer> {
    @Query("select p from Photo p left join p.shares shares where p.PhotoId = ?1 and shares.PhotoId.PhotoId = ?2 and shares.UserId.UserId = ?3 and shares.UserId.Email = ?4")
    List<Photo> findByPhotoIdAndShares_PhotoId_PhotoIdAndShares_UserId_UserIdAndShares_UserId_Email(Integer PhotoId, Integer PhotoId1, Integer UserId, String Email);

    @Query("select p from Photo p left join p.shares shares where p.PhotoId = ?1 and shares.UserId.Email = ?2")
    List<Photo> findByPhotoIdAndShares_UserId_Email(Integer PhotoId, String Email);

    @Query("select p from Photo p where p.PhotoName = ?1 and p.PhotoFormat = ?2")
    Photo findByPhotoNameAndPhotoFormat(String PhotoName, String PhotoFormat);

    @Query("select p from Photo p where p.PhotoId = ?1")
    Photo findByPhotoId(Integer PhotoId);

    //TODO: replace boolean with int for the delete photo method!!!
    @Transactional
    @Modifying
    @Query("delete from Photo p where p.PhotoId = ?1")
    boolean deleteByPhotoId(Integer PhotoId);

    @Query("select (count(p) > 0) from Photo p where p.PhotoId = ?1")
    boolean existsByPhotoId(Integer PhotoId);
}
