package com.photomemories.domain.persistence;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "Shared", schema = "local_photo_memories_db")
public class Shared implements Serializable {
    private static final long serialVersionUID = -807419981572411616L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SHARED_ID", nullable = false)
    private Integer SharedId;

    @Column(name = "SH_SHARED_DATE", nullable = false)
    private LocalDate SharedDate;

    @Column(name = "SH_SHARED_WITH", nullable = false)
    private Integer SharedWith;

    @Column(name = "SH_HAS_ACCESS", nullable = false)
    private Boolean SharedHasAccess;

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

    public Shared(LocalDate sharedDate, Integer sharedWith, Boolean sharedHasAccess, User userId, Photo photoId) {
        this.SharedDate = sharedDate;
        this.SharedWith = sharedWith;
        this.SharedHasAccess = sharedHasAccess;
        this.UserId = userId;
        this.PhotoId = photoId;
    }

    public Shared(Integer sharedId, LocalDate sharedDate, Integer sharedWith, Boolean sharedHasAccess, User userId, Photo photoId) {
        this.SharedId = sharedId;
        this.SharedDate = sharedDate;
        this.SharedWith = sharedWith;
        this.SharedHasAccess = sharedHasAccess;
        this.UserId = userId;
        this.PhotoId = photoId;
    }

    public Shared(Integer sharedId, LocalDate sharedDate, Integer sharedWith, Boolean sharedHasAccess) {
        SharedId = sharedId;
        SharedDate = sharedDate;
        SharedWith = sharedWith;
        SharedHasAccess = sharedHasAccess;
    }

    public Shared(Integer sharedId, LocalDate sharedDate, Integer sharedWith, Boolean sharedHasAccess, Integer userId, Integer photoId) {
        this.SharedId = sharedId;
        this.SharedDate = sharedDate;
        this.SharedWith = sharedWith;
        this.SharedHasAccess = sharedHasAccess;
        this.UserId = new User(userId);
        this.PhotoId = new Photo(photoId);
    }

    public Integer getSharedId() {
        return SharedId;
    }

    public void setSharedId(Integer sharedId) {
        SharedId = sharedId;
    }

    public LocalDate getSharedBy() {
        return SharedDate;
    }

    public void setSharedBy(LocalDate sharedBy) {
        SharedDate = sharedBy;
    }

    public Photo getPhotoId() {
        return PhotoId;
    }

    public void setPhotoId(Photo photoId) {
        PhotoId = photoId;
    }

    public LocalDate getSharedDate() {
        return SharedDate;
    }

    public void setSharedDate(LocalDate sharedDate) {
        SharedDate = sharedDate;
    }

    public Integer getSharedWith() {
        return SharedWith;
    }

    public void setSharedWith(Integer sharedWith) {
        SharedWith = sharedWith;
    }

    public Boolean getSharedHasAccess() {
        return SharedHasAccess;
    }

    public void setSharedHasAccess(Boolean sharedHasAccess) {
        SharedHasAccess = sharedHasAccess;
    }

    public User getUserId() {
        return UserId;
    }

    public void setUserId(User userId) {
        UserId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shared shared = (Shared) o;
        return Objects.equals(SharedId, shared.SharedId) && Objects.equals(SharedDate, shared.SharedDate) && Objects.equals(SharedWith, shared.SharedWith) && Objects.equals(SharedHasAccess, shared.SharedHasAccess) && Objects.equals(UserId, shared.UserId) && Objects.equals(PhotoId, shared.PhotoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(SharedId, SharedDate, SharedWith, SharedHasAccess, UserId, PhotoId);
    }
}
