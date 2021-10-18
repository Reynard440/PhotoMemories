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

    //TODO considering to make use of Integer instead of Long
    @Id
    @Column(name = "SHARED_ID")
    private Long SharedId;

    @Column(name = "SH_SHARED_DATE")
    private Long SharedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    @JsonBackReference
    private User UserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PHOTO_ID")
    @JsonBackReference
    private Photo PhotoId;

    public Shared() {
    }

    public Shared(Long sharedBy, User userId, Photo photoId) {
        this.SharedBy = sharedBy;
        this.UserId = userId;
        this.PhotoId = photoId;
    }

    public Shared(Long sharedId, Long sharedBy, User userId, Photo photoId) {
        this.SharedId = sharedId;
        this.SharedBy = sharedBy;
        this.UserId = userId;
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
        return Objects.equals(SharedId, shared.SharedId) && Objects.equals(SharedBy, shared.SharedBy) && Objects.equals(UserId, shared.UserId) && Objects.equals(PhotoId, shared.PhotoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(SharedId, SharedBy, UserId, PhotoId);
    }
}
