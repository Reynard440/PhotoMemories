package com.photomemories.domain.persistence;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "User")
public class User implements Serializable {
    private static final long serialVersionUID = -5512705369209852028L;

    @Id
    @Column(name = "USER_ID")
    private Long UserId;

    @Column(name = "USER_F_Name")
    private String FirstName;

    @Column(name = "USER_L_Name")
    private String LastName;

    @Column(name = "USER_JOIN_DATE")
    private LocalDate date;

    @Column(name = "USER_Email")
    private String Email;

    @Column(name = "USER_CELL_NR")
    private String PhoneNumber;

    @OneToMany(targetEntity = Photo.class, fetch = FetchType.LAZY, mappedBy = "PhotoId"/*, orphanRemoval = true, cascade = CascadeType.ALL*/)
    @JsonManagedReference
    private Set<Photo> Photos;

    public User() {
    }

    public User(String firstName, String lastName, LocalDate date, String email, String phoneNumber, Set<Photo> photos) {
        this.FirstName = firstName;
        this.LastName = lastName;
        this.date = date;
        this.Email = email;
        this.PhoneNumber = phoneNumber;
        this.Photos = photos;
    }

    public User(Long userId, String firstName, String lastName, LocalDate date, String email, String phoneNumber, Set<Photo> photos) {
        this.UserId = userId;
        this.FirstName = firstName;
        this.LastName = lastName;
        this.date = date;
        this.Email = email;
        this.PhoneNumber = phoneNumber;
        this.Photos = photos;
    }

    public Long getUserId() {
        return UserId;
    }

    public void setUserId(Long userId) {
        UserId = userId;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public Set<Photo> getPhotos() {
        return Photos;
    }

    public void setPhotos(Set<Photo> photos) {
        Photos = photos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(UserId, user.UserId) && Objects.equals(FirstName, user.FirstName) && Objects.equals(LastName, user.LastName) && Objects.equals(date, user.date) && Objects.equals(Email, user.Email) && Objects.equals(PhoneNumber, user.PhoneNumber) && Objects.equals(Photos, user.Photos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(UserId, FirstName, LastName, date, Email, PhoneNumber, Photos);
    }
}
