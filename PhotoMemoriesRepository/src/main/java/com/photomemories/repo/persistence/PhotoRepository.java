package com.photomemories.repo.persistence;

import com.photomemories.domain.persistence.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Integer> {
    @Query("select distinct p from Photo p left join p.shares shares where shares.UserId.UserId = ?1 and p.PhotoId = shares.PhotoId.PhotoId")
    List<Photo> findByShares_UserId_UserId(Integer UserId);

    @Query("select p from Photo p left join p.shares shares where shares.UserId.Email = ?1")
    List<Photo> findByUserEmail(String Email);

    @Query("select p from Photo p where p.PhotoName = ?1 and p.PhotoFormat = ?2")
    Photo findByPhotoNameAndPhotoFormat(String PhotoName, String PhotoFormat);

    @Query("select p from Photo p where p.PhotoId = ?1")
    Photo findByPhotoId(Integer PhotoId);

    @Transactional(rollbackOn = {RuntimeException.class, Exception.class})
    @Modifying
    @Query(value = "update Photo set PhotoName = ?1, PhotoLocation = ?2, PhotoCapturedBy = ?3 where PhotoId = ?4 ")
    int updatePhoto(String pName, String pLocation, String pCapturedBy, Integer photoId);

    @Transactional(rollbackOn = {RuntimeException.class, Exception.class})
    @Modifying
    @Query("delete from Photo p where p.PhotoId = ?1 and p.PhotoLink = ?2")
    int deleteByPhotoIdAndPhotoLink(Integer PhotoId, String photoLink);

    @Query("select (count(p) > 0) from Photo p where p.PhotoId = ?1 and p.PhotoLink = ?2")
    boolean existsByPhotoIdAndPhotoLink(Integer PhotoId, String photoLink);
}
