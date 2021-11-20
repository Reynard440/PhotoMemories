package com.photomemories.repo.persistence;

import com.photomemories.domain.persistence.User;
import com.photomemories.repo.config.RepositoryTestConfiguration;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = {RepositoryTestConfiguration.class})
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Transactional
    @Test
    @DisplayName("Should update a user's information.")
    public void shouldUpdateUser() {
        Optional<User> user = userRepository.findByUserId(1);
        assertNotNull(user);
        int result = userRepository.updateUser("Kevin", "Hamilton", "kevinhamilton@gmail.com", "0826516374", 1);
        assertEquals(result, 1);
    }

    @Transactional
    @Cascade(CascadeType.ALL)
    @Test(expected =  DataIntegrityViolationException.class)
    @DisplayName("Should delete a user by id.")
    public void shouldDeleteByUserId() {
        Optional<User> user = userRepository.findByUserId(1);
        assertNotNull(user);
        int value = userRepository.deleteByUserId(1);
        assertEquals(value, 1);
    }

    @Test
    @DisplayName("Should verify a user exists by email.")
    public void shouldExistsByEmail() {
        boolean value = userRepository.existsByEmail("reynardengels@gmail.com");
        assertTrue(value);
    }

    @Test
    @DisplayName("Should find a user by email.")
    public void shouldFindByEmail() {
        User user = userRepository.findByEmail("reynardengels@gmail.com");
        assertNotNull(user);
        assertEquals("Reynard", user.getFirstName());
        assertEquals("Engels", user.getLastName());
    }

    @Test
    @DisplayName("Should find a user by id.")
    public void shouldFindByUserId() {
        Optional<User> user = userRepository.findByUserId(1);
        assertNotNull(user);
    }

    @Test
    @DisplayName("Should verify a user exists with password and email.")
    public void shouldExistsByUserHashPasswordAndEmail() {
        User user = userRepository.findByEmail("reynardengels@gmail.com");
        assertNotNull(user);
        boolean value = userRepository.existsByUserHashPasswordAndEmail("King6", "reynardengels@gmail.com");
        assertTrue(value);
    }

    @Test
    @DisplayName("Should verify a user exists with phone number and email.")
    public void shouldExistsByPhoneNumberAndEmail() {
        User user = userRepository.findByEmail("reynardengels@gmail.com");
        assertNotNull(user);
        boolean value = userRepository.existsByPhoneNumberAndEmail("0723949955", "reynardengels@gmail.com");
        assertTrue(value);
    }

    @Test
    @DisplayName("Should verify a user exists with id.")
    public void shouldExistsByUserId() {
        User user = userRepository.findByEmail("reynardengels@gmail.com");
        assertNotNull(user);
        boolean value = userRepository.existsById(1);
        assertTrue(value);
    }
}