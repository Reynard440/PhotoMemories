package com.photomemories.domain.persistence;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "Shared", schema = "PhotoMemoriesDB")
public class Shared implements Serializable {
    private static final long serialVersionUID = -807419981572411616L;

    @Id
    @Column(name = "SHARED_ID")
    private Long SharedId;

    @Column(name = "SH_SHARED_BY")
    private Long SharedBy;

    @Column(name = "SH_SHARED_WITH")
    private Long SharedWith;

    @Column(name = "SH_DATE_SHARED")
    private LocalDate DateShared;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PhotoId")
    @JsonBackReference
    private Photo PhotoId;

    public Shared() {
    }

    public Shared(Long sharedBy, Long sharedWith, LocalDate dateShared, Photo photoId) {
        this.SharedBy = sharedBy;
        this.SharedWith = sharedWith;
        this.DateShared = dateShared;
        this.PhotoId = photoId;
    }

    public Shared(Long sharedId, Long sharedBy, Long sharedWith, LocalDate dateShared, Photo photoId) {
        this.SharedId = sharedId;
        this.SharedBy = sharedBy;
        this.SharedWith = sharedWith;
        this.DateShared = dateShared;
        this.PhotoId = photoId;
    }

    public Long getSharedId() {
        return SharedId;
    }

    public void setSharedId(Long sharedId) {
        SharedId = sharedId;
    }

    public Long getSharedBy() {
        return SharedBy;
    }

    public void setSharedBy(Long sharedBy) {
        SharedBy = sharedBy;
    }

    public Long getSharedWith() {
        return SharedWith;
    }

    public void setSharedWith(Long sharedWith) {
        SharedWith = sharedWith;
    }

    public LocalDate getDateShared() {
        return DateShared;
    }

    public void setDateShared(LocalDate dateShared) {
        DateShared = dateShared;
    }

    public Photo getPhotoId() {
        return PhotoId;
    }

    public void setPhotoId(Photo photoId) {
        PhotoId = photoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shared shared = (Shared) o;
        return Objects.equals(SharedId, shared.SharedId) && Objects.equals(SharedBy, shared.SharedBy) && Objects.equals(SharedWith, shared.SharedWith) && Objects.equals(DateShared, shared.DateShared) && Objects.equals(PhotoId, shared.PhotoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(SharedId, SharedBy, SharedWith, DateShared, PhotoId);
    }
}
