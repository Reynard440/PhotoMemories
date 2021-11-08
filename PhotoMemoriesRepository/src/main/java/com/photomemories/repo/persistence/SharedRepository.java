package com.photomemories.repo.persistence;

import com.photomemories.domain.persistence.Shared;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SharedRepository extends JpaRepository<Shared, Integer> {
    @Query("select s from Shared s where s.SharedId = ?1 and s.UserId.UserId = ?2")
    Shared findBySharedIdAndUserId_UserId(Integer SharedId, Integer UserId);

    @Query("select s from Shared s where s.SharedId = ?1")
    Shared findBySharedId(Integer sharedId);
}
