package com.photomemories.repo.persistence;

import com.photomemories.domain.persistence.Shared;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SharedRepository extends JpaRepository<Shared, Integer> {
//    @Query("select s from Shared s where s.SharedId = ?1 and s.SharedWith = ?2 and s.PhotoId.PhotoId = ?3 and s.UserId.UserId = ?4")
//    Shared findBySharedVerifiedIds(Integer sharedId, Integer sharedWith, Integer photoId, Integer userId);

//    @Query("select s from Shared s where s.SharedWith = ?1 and s.UserId.UserId = ?2 and s.PhotoId.PhotoId = ?3")
//    Shared findBySharedVerifiedIds(Integer sharedWitdId, Integer UserId, Integer PhotoId);

    @Query("select s from Shared s where s.UserId.UserId = ?1")
    Shared findByUserId_UserId(Integer UserId);
}
