package com.photomemories.repo.persistence;

import com.photomemories.domain.persistence.Photo;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = {RepositoryTestConfiguration.class})
public class PhotoRepositoryTest {

    @Autowired
    PhotoRepository photoRepository;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Transactional
    @Test
    @DisplayName("Should update the photo metadata.")
    public void shouldUpdatePhoto() {
        Photo photo = photoRepository.findByPhotoId(1);
        assertNotNull(photo);
        int result = photoRepository.updatePhoto("Reynard", "Sasolburg", "Lourenz Engels", 1);
        assertEquals(result, 1);
    }

    @Transactional
    @Cascade(CascadeType.ALL)
    @Test(expected =  DataIntegrityViolationException.class)
    @DisplayName("Should delete the photo by id and link.")
    public void shouldDeleteByPhotoIdAndPhotoLink() {
        Photo photo = photoRepository.findByPhotoId(1);
        assertNotNull(photo);
        int value = photoRepository.deleteByPhotoIdAndPhotoLink(1, "ReynardEngels.jpeg");
        assertEquals(value, 1);
    }

    @Test
    @DisplayName("Should find photo by id.")
    public void shouldFindByPhotoId() {
        Photo photo = photoRepository.findByPhotoId(1);
        assertNotNull(photo);
        assertEquals("Vaalpark", photo.getPhotoLocation());
    }

    @Test
    @DisplayName("Should verify photo exists by id and link.")
    public void shouldExistsByPhotoIdAndPhotoLink() {
        boolean value = photoRepository.existsByPhotoIdAndPhotoLink(1, "ReynardEngels.jpeg");
        assertEquals(true, value);
    }
}