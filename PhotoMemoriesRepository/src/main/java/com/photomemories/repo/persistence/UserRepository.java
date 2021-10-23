package com.photomemories.repo.persistence;

import com.photomemories.domain.persistence.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("select (count(u) > 0) from User u where u.UserId = ?1")
    boolean existsByUserId(Integer UserId);
}
