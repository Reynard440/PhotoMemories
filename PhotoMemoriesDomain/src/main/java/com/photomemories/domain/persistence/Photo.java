package com.photomemories.domain.persistence;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "Photo", schema = "PhotoMemoriesDB")
public class Photo implements Serializable {
    private static final long serialVersionUID = 2034551615183602888L;

    //TODO considering to make use of Integer instead of Long
    @Id
    @Column(name = "PHOTO_ID")
    private Integer PhotoId;

    @Column(name = "PH_NAME")
    private String PhotoName;

    @Column(name = "PH_SIZE")
    private Double PhotoSize;

    @Column(name = "PH_UPLOAD_DATE")
    private LocalDate UploadDate;

    @Column(name = "PH_MODIFIED_DATE")
    private LocalDate DateModified;

    @Column(name = "PH_LINK")
    private String PhotoLink;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    @JsonBackReference
    private User UserId;

    @Column(name = "PH_LOCATION")
    private String PhotoLocation;

    @Column(name = "PH_FORMAT")
    private String PhotoFormat;

    @Column(name = "PH_PIXELS")
    private String PhotoPixels;

    @OneToMany(targetEntity = Shared.class, mappedBy = "PhotoId")
    @JsonManagedReference
    private Set<Shared> shares;

    public Photo() {
    }

    public Photo(String photoName, Double photoSize, LocalDate uploadDate, LocalDate dateModified, String photoLink, User userId, String photoLocation, String photoFormat, String photoPixels, Set<Shared> shares) {
        this.PhotoName = photoName;
        this.PhotoSize = photoSize;
        this.UploadDate = uploadDate;
        this.DateModified = dateModified;
        this.PhotoLink = photoLink;
        this.UserId = userId;
        this.PhotoLocation = photoLocation;
        this.PhotoFormat = photoFormat;
        this.PhotoPixels = photoPixels;
        this.shares = shares;
    }

    public Photo(Integer photoId, String photoName, Double photoSize, LocalDate uploadDate, LocalDate dateModified, String photoLink, User userId, String photoLocation, String photoFormat, String photoPixels, Set<Shared> shares) {
        this.PhotoId = photoId;
        this.PhotoName = photoName;
        this.PhotoSize = photoSize;
        this.UploadDate = uploadDate;
        this.DateModified = dateModified;
        this.PhotoLink = photoLink;
        this.UserId = userId;
        this.PhotoLocation = photoLocation;
        this.PhotoFormat = photoFormat;
        this.PhotoPixels = photoPixels;
        this.shares = shares;
    }

    public Integer getPhotoId() {
        return PhotoId;
    }

    public void setPhotoId(Integer photoId) {
        PhotoId = photoId;
    }

    public String getPhotoName() {
        return PhotoName;
    }

    public void setPhotoName(String photoName) {
        PhotoName = photoName;
    }

    public Double getPhotoSize() {
        return PhotoSize;
    }

    public void setPhotoSize(Double photoSize) {
        PhotoSize = photoSize;
    }

    public LocalDate getUploadDate() {
        return UploadDate;
    }

    public void setUploadDate(LocalDate uploadDate) {
        UploadDate = uploadDate;
    }

    public LocalDate getDateModified() {
        return DateModified;
    }

    public void setDateModified(LocalDate dateModified) {
        DateModified = dateModified;
    }

    public String getPhotoLink() {
        return PhotoLink;
    }

    public void setPhotoLink(String photoLink) {
        PhotoLink = photoLink;
    }

    public User getUserId() {
        return UserId;
    }

    public void setUserId(User userId) {
        UserId = userId;
    }

    public String getPhotoLocation() {
        return PhotoLocation;
    }

    public void setPhotoLocation(String photoLocation) {
        PhotoLocation = photoLocation;
    }

    public String getPhotoFormat() {
        return PhotoFormat;
    }

    public void setPhotoFormat(String photoFormat) {
        PhotoFormat = photoFormat;
    }

    public String getPhotoPixels() {
        return PhotoPixels;
    }

    public void setPhotoPixels(String photoPixels) {
        PhotoPixels = photoPixels;
    }

    public Set<Shared> getShares() {
        return shares;
    }

    public void setShares(Set<Shared> shares) {
        this.shares = shares;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Photo photo = (Photo) o;
        return Objects.equals(PhotoId, photo.PhotoId) && Objects.equals(PhotoName, photo.PhotoName) && Objects.equals(PhotoSize, photo.PhotoSize) && Objects.equals(UploadDate, photo.UploadDate) && Objects.equals(DateModified, photo.DateModified) && Objects.equals(PhotoLink, photo.PhotoLink) && Objects.equals(UserId, photo.UserId) && Objects.equals(PhotoLocation, photo.PhotoLocation) && Objects.equals(PhotoFormat, photo.PhotoFormat) && Objects.equals(PhotoPixels, photo.PhotoPixels) && Objects.equals(shares, photo.shares);
    }

    @Override
    public int hashCode() {
        return Objects.hash(PhotoId, PhotoName, PhotoSize, UploadDate, DateModified, PhotoLink, UserId, PhotoLocation, PhotoFormat, PhotoPixels, shares);
    }
}
