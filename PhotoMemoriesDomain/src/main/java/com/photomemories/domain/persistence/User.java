package com.photomemories.domain.persistence;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "User", schema = "PhotoMemoriesDB")
public class User implements Serializable {
    private static final long serialVersionUID = -5512705369209852028L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Integer UserId;

    @Column(name = "USER_F_Name")
    private String FirstName;

    @Column(name = "USER_L_Name")
    private String LastName;

    @Column(name = "USER_JOIN_DATE")
    private LocalDate date;

    @Column(name = "USER_HASH_PASSWORD")
    private String UserHashPassword;

    @Column(name = "USER_Email")
    private String Email;

    @Column(name = "USER_CELL_NR")
    private String PhoneNumber;

    @OneToMany(targetEntity = Photo.class, fetch = FetchType.LAZY, mappedBy = "UserId"/*, orphanRemoval = true, cascade = CascadeType.ALL*/)
    @JsonManagedReference
    private Set<Photo> Photos;

    @OneToMany(targetEntity = Shared.class, fetch = FetchType.LAZY, mappedBy = "UserId"/*, orphanRemoval = true, cascade = CascadeType.ALL*/)
    @JsonManagedReference
    private Set<Shared> Shares;

    public User() {
    }

    public User(String firstName, String lastName, LocalDate date, String userHashPassword, String email, String phoneNumber, Set<Photo> photos, Set<Shared> shares) {
        this.FirstName = firstName;
        this.LastName = lastName;
        this.date = date;
        this.UserHashPassword = userHashPassword;
        this.Email = email;
        this.PhoneNumber = phoneNumber;
        this.Photos = photos;
        this.Shares = shares;
    }

    public User(Integer userId, String firstName, String lastName, LocalDate date, String userHashPassword, String email, String phoneNumber, Set<Photo> photos, Set<Shared> shares) {
        this.UserId = userId;
        this.FirstName = firstName;
        this.LastName = lastName;
        this.date = date;
        this.UserHashPassword = userHashPassword;
        this.Email = email;
        this.PhoneNumber = phoneNumber;
        this.Photos = photos;
        this.Shares = shares;
    }

    public User(String firstName, String lastName, LocalDate date, String userHashPassword, String email, String phoneNumber) {
        this.FirstName = firstName;
        this.LastName = lastName;
        this.date = date;
        this.UserHashPassword = userHashPassword;
        this.Email = email;
        this.PhoneNumber = phoneNumber;
    }

    public Integer getUserId() {
        return UserId;
    }

    public void setUserId(Integer userId) {
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

    public String getUserHashPassword() {
        return UserHashPassword;
    }

    public void setUserHashPassword(String userHashPassword) {
        UserHashPassword = userHashPassword;
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

    public Set<Shared> getShares() {
        return Shares;
    }

    public void setShares(Set<Shared> shares) {
        Shares = shares;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(UserId, user.UserId) && Objects.equals(FirstName, user.FirstName) && Objects.equals(LastName, user.LastName) && Objects.equals(date, user.date) && Objects.equals(UserHashPassword, user.UserHashPassword) && Objects.equals(Email, user.Email) && Objects.equals(PhoneNumber, user.PhoneNumber) && Objects.equals(Photos, user.Photos) && Objects.equals(Shares, user.Shares);
    }

    @Override
    public int hashCode() {
        return Objects.hash(UserId, FirstName, LastName, date, UserHashPassword, Email, PhoneNumber, Photos, Shares);
    }
}
