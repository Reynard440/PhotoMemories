package com.photomemories.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.photomemories.domain.persistence.Photo;
import com.photomemories.domain.persistence.Shared;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@ApiModel(value = "PhotoDto", description = "A DTO that represents all the photos.")
public class PhotoDto implements Serializable {
    private static final long serialVersionUID = 2762647550806268600L;

    private Integer PhotoId;
    private String PhotoName;
    private Double PhotoSize;
    private LocalDate UploadDate;
    private LocalDate DateModified;
    private String PhotoLink;
    private String PhotoLocation;
    private String PhotoFormat;
    private String PhotoCapturedBy;

    @ApiModelProperty(position = 1,
            value = "Photo id",
            name = "Photo ID",
            notes = "Unique to each photo.",
            dataType = "java.lang.Integer",
            example = "1")
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

    public PhotoDto() {
    }

    public PhotoDto(Integer photoId) {
        PhotoId = photoId;
    }

    public PhotoDto(Integer photoId, String photoName, Double photoSize, LocalDate uploadDate, LocalDate dateModified, String photoLink, String photoLocation, String photoFormat, String photoCapturedBy) {
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

    public PhotoDto(Photo photo){
        this.PhotoId = photo.getPhotoId();
        this.PhotoName = photo.getPhotoName();
        this.PhotoSize = photo.getPhotoSize();
        this.UploadDate = photo.getUploadDate();
        this.DateModified = photo.getDateModified();
        this.PhotoLink = photo.getPhotoLink();
        this.PhotoLocation = photo.getPhotoLocation();
        this.PhotoFormat = photo.getPhotoFormat();
        this.PhotoCapturedBy = photo.getPhotoCapturedBy();
    }

    @JsonIgnore
    public Photo buildPhoto() {
        return new Photo(this.getPhotoId(), this.getPhotoName(), this.getPhotoSize(), this.getUploadDate(),
                this.getDateModified(), this.getPhotoLink(), this.getPhotoLocation(), this.getPhotoFormat(), this.getPhotoCapturedBy());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhotoDto photoDto = (PhotoDto) o;
        return Objects.equals(PhotoId, photoDto.PhotoId) && Objects.equals(PhotoName, photoDto.PhotoName) && Objects.equals(PhotoSize, photoDto.PhotoSize) && Objects.equals(UploadDate, photoDto.UploadDate) && Objects.equals(DateModified, photoDto.DateModified) && Objects.equals(PhotoLink, photoDto.PhotoLink) && Objects.equals(PhotoLocation, photoDto.PhotoLocation) && Objects.equals(PhotoFormat, photoDto.PhotoFormat) && Objects.equals(PhotoCapturedBy, photoDto.PhotoCapturedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(PhotoId, PhotoName, PhotoSize, UploadDate, DateModified, PhotoLink, PhotoLocation, PhotoFormat, PhotoCapturedBy);
    }

    @Override
    public String toString() {
        return "PhotoDto{" +
                "PhotoId=" + PhotoId +
                ", PhotoName='" + PhotoName + '\'' +
                ", PhotoSize=" + PhotoSize +
                ", UploadDate=" + UploadDate +
                ", DateModified=" + DateModified +
                ", PhotoLink='" + PhotoLink + '\'' +
                ", PhotoLocation='" + PhotoLocation + '\'' +
                ", PhotoFormat='" + PhotoFormat + '\'' +
                ", PhotoCapturedBy='" + PhotoCapturedBy + '\'' +
                '}';
    }
}
