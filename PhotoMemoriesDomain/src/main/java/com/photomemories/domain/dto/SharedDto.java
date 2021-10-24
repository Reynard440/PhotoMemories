package com.photomemories.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.photomemories.domain.persistence.Shared;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@ApiModel(value = "SharedDto", description = "A DTO that represents all the shared records.")
public class SharedDto implements Serializable {
    private static final long serialVersionUID = -2727455203345739826L;

    private Integer SharedId;
    private LocalDate SharedDate;
    private Integer SharedWith;
    private Boolean SharedHasAccess;
    private Integer UserId;
    private Integer PhotoId;

    @ApiModelProperty(position = 1,
            value = "User id",
            name = "User ID",
            notes = "Unique to each user.",
            dataType = "java.lang.Integer",
            example = "1")
    public Integer getSharedId() {
        return SharedId;
    }

    public void setSharedId(Integer sharedId) {
        SharedId = sharedId;
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

    public Integer getUserId() {
        return UserId;
    }

    public void setUserId(Integer userId) {
        UserId = userId;
    }

    public Integer getPhotoId() {
        return PhotoId;
    }

    public void setPhotoId(Integer photoId) {
        PhotoId = photoId;
    }

    public SharedDto() {
    }

    public SharedDto(Integer sharedId) {
        this.SharedId = sharedId;
    }

    public SharedDto(Integer sharedId, LocalDate sharedDate, Integer sharedWith, Boolean sharedHasAccess, Integer userId, Integer photoId) {
        this.SharedId = sharedId;
        this.SharedDate = sharedDate;
        this.SharedWith = sharedWith;
        this.SharedHasAccess = sharedHasAccess;
        this.UserId = userId;
        this.PhotoId = photoId;
    }

    public SharedDto(Shared shared){
        this.SharedId = shared.getSharedId();
        this.SharedDate = shared.getSharedDate();
        this.SharedWith = shared.getSharedWith();
        this.SharedHasAccess = shared.getSharedHasAccess();
        this.UserId = shared.getUserId().getUserId();
        this.PhotoId = shared.getPhotoId().getPhotoId();
    }

    @JsonIgnore
    public Shared buildShared() {
        return new Shared(this.getSharedId(), this.getSharedDate(), this.getSharedId(), this.getSharedHasAccess(), this.getUserId(), this.getPhotoId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SharedDto sharedDto = (SharedDto) o;
        return Objects.equals(SharedId, sharedDto.SharedId) && Objects.equals(SharedDate, sharedDto.SharedDate) && Objects.equals(SharedWith, sharedDto.SharedWith) && Objects.equals(SharedHasAccess, sharedDto.SharedHasAccess) && Objects.equals(UserId, sharedDto.UserId) && Objects.equals(PhotoId, sharedDto.PhotoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(SharedId, SharedDate, SharedWith, SharedHasAccess, UserId, PhotoId);
    }

    @Override
    public String toString() {
        return "SharedDto{" +
                "SharedId=" + SharedId +
                ", SharedDate=" + SharedDate +
                ", SharedWith=" + SharedWith +
                ", SharedHasAccess=" + SharedHasAccess +
                ", UserId=" + UserId +
                ", PhotoId=" + PhotoId +
                '}';
    }
}
