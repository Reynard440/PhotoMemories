package com.photomemories.domain.persistence;

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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PHOTO_ID", nullable = false)
    private Integer PhotoId;

    @Column(name = "PH_NAME", nullable = false)
    private String PhotoName;

    @Column(name = "PH_SIZE", nullable = true)
    private Double PhotoSize;

    @Column(name = "PH_UPLOAD_DATE", nullable = false)
    private LocalDate UploadDate;

    @Column(name = "PH_MODIFIED_DATE", nullable = false)
    private LocalDate DateModified;

    @Column(name = "PH_LINK", nullable = false)
    private String PhotoLink;

    @Column(name = "PH_LOCATION", nullable = true)
    private String PhotoLocation;

    @Column(name = "PH_FORMAT", nullable = false)
    private String PhotoFormat;

    @Column(name = "PH_CAPTUREDBY", nullable = false)
    private String PhotoCapturedBy;

    @OneToMany(targetEntity = Shared.class, mappedBy = "PhotoId", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Shared> shares;

    public Photo() {
    }

    public Photo(String photoName, Double photoSize, LocalDate uploadDate, LocalDate dateModified, String photoLink, String photoLocation, String photoFormat, String photoCapturedBy, Set<Shared> shares) {
        this.PhotoName = photoName;
        this.PhotoSize = photoSize;
        this.UploadDate = uploadDate;
        this.DateModified = dateModified;
        this.PhotoLink = photoLink;
        this.PhotoLocation = photoLocation;
        this.PhotoFormat = photoFormat;
        this.shares = shares;
        this.PhotoCapturedBy =  photoCapturedBy;
    }

    public Photo(Integer photoId, String photoName, Double photoSize, LocalDate uploadDate, LocalDate dateModified, String photoLink, String photoLocation, String photoFormat, String photoCapturedBy, Set<Shared> shares) {
        this.PhotoId = photoId;
        this.PhotoName = photoName;
        this.PhotoSize = photoSize;
        this.UploadDate = uploadDate;
        this.DateModified = dateModified;
        this.PhotoLink = photoLink;
        this.PhotoLocation = photoLocation;
        this.PhotoFormat = photoFormat;
        this.shares = shares;
        this.PhotoCapturedBy =  photoCapturedBy;
    }

    public Photo(Integer photoId, String photoName, Double photoSize, LocalDate uploadDate, LocalDate dateModified, String photoLink, String photoLocation, String photoFormat, String photoCapturedBy) {
        this.PhotoId = photoId;
        this.PhotoName = photoName;
        this.PhotoSize = photoSize;
        this.UploadDate = uploadDate;
        this.DateModified = dateModified;
        this.PhotoLink = photoLink;
        this.PhotoLocation = photoLocation;
        this.PhotoFormat = photoFormat;
        this.PhotoCapturedBy = photoCapturedBy;
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

    public String getPhotoCapturedBy() {
        return PhotoCapturedBy;
    }

    public void setPhotoCapturedBy(String photoCapturedBy) {
        PhotoCapturedBy = photoCapturedBy;
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
        return Objects.equals(PhotoId, photo.PhotoId) && Objects.equals(PhotoName, photo.PhotoName) && Objects.equals(PhotoSize, photo.PhotoSize) && Objects.equals(UploadDate, photo.UploadDate) && Objects.equals(DateModified, photo.DateModified) && Objects.equals(PhotoLink, photo.PhotoLink) && Objects.equals(PhotoLocation, photo.PhotoLocation) && Objects.equals(PhotoFormat, photo.PhotoFormat) && Objects.equals(PhotoCapturedBy, photo.PhotoCapturedBy) && Objects.equals(shares, photo.shares);
    }

    @Override
    public int hashCode() {
        return Objects.hash(PhotoId, PhotoName, PhotoSize, UploadDate, DateModified, PhotoLink, PhotoLocation, PhotoFormat, PhotoCapturedBy, shares);
    }
}
