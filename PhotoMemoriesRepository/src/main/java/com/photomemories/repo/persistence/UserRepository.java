package com.photomemories.repo.persistence;

import com.photomemories.domain.persistence.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Transactional(rollbackOn = {SQLException.class, RuntimeException.class})
    @Modifying
    @Query(value = "update User set FirstName = ?1, LastName = ?2, Email = ?3, PhoneNumber = ?4 where UserId = ?5 ")
    int updateUser(String firstName, String lastName, String email, String phoneNumber, Integer userId);

    @Transactional(rollbackOn = {SQLException.class, RuntimeException.class})
    @Modifying
    @Query("delete from User u where u.UserId = ?1")
    int deleteByUserId(Integer UserId);

    @Query("select (count(u) > 0) from User u where u.Email = ?1")
    boolean existsByEmail(String Email);

    @Query("select u from User u where u.Email = ?1")
    User findByEmail(String Email);

    @Query("select u from User u where u.UserId = ?1")
    Optional<User> findByUserId(Integer UserId);

    @Query("select (count(u) > 0) from User u where u.UserHashPassword = ?1 and u.Email = ?2")
    boolean existsByUserHashPasswordAndEmail(String UserHashPassword, String Email);

    @Query("select (count(u) > 0) from User u where u.PhoneNumber = ?1 and u.Email = ?2")
    boolean existsByPhoneNumberAndEmail(String PhoneNumber, String Email);

    @Query("select (count(u) > 0) from User u where u.UserId = ?1")
    boolean existsByUserId(Integer UserId);
}
