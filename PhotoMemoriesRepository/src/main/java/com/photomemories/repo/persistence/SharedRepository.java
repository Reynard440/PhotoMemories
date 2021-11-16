package com.photomemories.repo.persistence;

import com.photomemories.domain.persistence.Shared;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.SQLException;

@Repository
public interface SharedRepository extends JpaRepository<Shared, Integer> {
    @Transactional(rollbackOn = {SQLException.class, RuntimeException.class, Exception.class})
    @Modifying
    @Query("delete from Shared s where s.SharedWith = ?1 and s.PhotoId.PhotoId = ?2")
    int deleteBySharedWithAndPhotoId_PhotoId(Integer sharedWith, Integer photoId);

    @Query("select (count(s) > 0) from Shared s where s.SharedWith = ?1 and s.UserId.UserId = ?2 and s.PhotoId.PhotoId = ?3")
    boolean existsBySharedWithAndUserId_UserIdAndPhotoId_PhotoId(Integer sharedWith, Integer userId, Integer photoId);

    @Query("select (count(s) > 0) from Shared s where s.SharedWith = ?1 and s.PhotoId.PhotoId = ?2")
    boolean existsBySharedWithAndPhotoId_PhotoId(Integer sharedWith, Integer photoId);

    @Query("select s from Shared s where s.SharedWith = ?1 and s.PhotoId.PhotoId = ?2")
    Shared findBySharedWithAndPhotoId(Integer sharedWith, Integer photoId);

    @Query("select s from Shared s where s.SharedId = ?1")
    Shared findBySharedId(Integer sharedId);
}
