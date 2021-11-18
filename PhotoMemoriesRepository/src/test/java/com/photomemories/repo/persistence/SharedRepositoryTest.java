package com.photomemories.repo.persistence;

import com.photomemories.domain.persistence.Shared;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = {RepositoryTestConfiguration.class})
public class SharedRepositoryTest {

    @Autowired
    SharedRepository sharedRepository;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Transactional
    @Cascade(CascadeType.ALL)
    @Test
    @DisplayName("Should delete the shared record by sharedWith id and photo id.")
    public void shouldDeleteBySharedWithAndPhotoId_PhotoId() {
        int value = sharedRepository.deleteBySharedWithAndPhotoId_PhotoId(1, 1);
        assertEquals(1, value);
    }

    @Test
    @DisplayName("Should find shared record by sharedWith id and photo id.")
    public void shouldFindBySharedWithAndPhotoId() {
        Shared shared = sharedRepository.findBySharedWithAndPhotoId(1, 1);
        assertNotNull(shared);
        assertEquals(LocalDate.parse("2021-10-30"), shared.getSharedDate());
    }
}