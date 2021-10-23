package com.photomemories.repo.persistence;

import com.photomemories.domain.persistence.Shared;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SharedRepository extends JpaRepository<Shared, Integer> {
}
