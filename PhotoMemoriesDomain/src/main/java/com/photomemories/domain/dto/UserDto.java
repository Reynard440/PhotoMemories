package com.photomemories.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.photomemories.domain.persistence.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@ApiModel(value = "UserDto", description = "A DTO that represents all the users.")
public class UserDto implements Serializable {
    private static final long serialVersionUID = 7185715504020990477L;

    private Integer UserId;

    private String FirstName;

    private String LastName;

    private LocalDate date;

    private String UserHashPassword;

    private String Email;

    private String PhoneNumber;

    @ApiModelProperty(position = 1,
            value = "User id",
            name = "User ID",
            notes = "Unique to each user.",
            dataType = "java.lang.Integer",
            example = "1")
    public Integer getUserId() {
        return UserId;
    }

    public void setUserId(Integer userId) {
        UserId = userId;
    }

    @ApiModelProperty(position = 2,
            value = "User first name",
            name = "First Name",
            notes = "Might find other users with the same names.",
            dataType = "java.lang.String",
            example = "Reynard")
    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    @ApiModelProperty(position = 3,
            value = "User last name",
            name = "Last Name",
            notes = "Might find other users with the same last names.",
            dataType = "java.lang.String",
            example = "Engels")
    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    @ApiModelProperty(position = 4,
            value = "Date when user became a user.",
            name = "User join Date",
            notes = "This field will automatically stamp the exact moment the user joined.",
            dataType = "java.time.LocalDate",
            example = "2012-01-01")
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @ApiModelProperty(position = 5,
            value = "User password",
            name = "Password",
            notes = "This field will be changed upon registration.",
            dataType = "java.lang.String",
            example = "King6")
    public String getUserHashPassword() {
        return UserHashPassword;
    }

    public void setUserHashPassword(String userHashPassword) {
        UserHashPassword = userHashPassword;
    }

    @ApiModelProperty(position = 6,
            value = "User email",
            name = "Email",
            notes = "This is required to be unique to each user.",
            dataType = "java.lang.String",
            example = "reynardnegels@gmail.com")
    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    @ApiModelProperty(position = 7,
            value = "User contact phone number",
            name = "Contact phone number",
            notes = "This is also unique to each user.",
            dataType = "java.lang.String",
            example = "0723949955")
    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public UserDto() {
    }

    public UserDto(Integer userId) {
        this.UserId = userId;
    }

    public UserDto(String firstName, String lastName, LocalDate date, String userHashPassword, String email, String phoneNumber) {
        this.FirstName = firstName;
        this.LastName = lastName;
        this.date = date;
        this.UserHashPassword = userHashPassword;
        this.Email = email;
        this.PhoneNumber = phoneNumber;
    }

    public UserDto(Integer userId, String firstName, String lastName, LocalDate date, String userHashPassword, String email, String phoneNumber) {
        this.UserId = userId;
        this.FirstName = firstName;
        this.LastName = lastName;
        this.date = date;
        this.UserHashPassword = userHashPassword;
        this.Email = email;
        this.PhoneNumber = phoneNumber;
    }

    public UserDto(User user){
        this.UserId = user.getUserId();
        this.FirstName = user.getFirstName();
        this.LastName = user.getLastName();
        this.date = user.getDate();
        this.UserHashPassword = user.getUserHashPassword();
        this.Email = user.getEmail();
        this.PhoneNumber = user.getPhoneNumber();
    }

    @JsonIgnore
    public User buildUser() {
        return new User(this.getFirstName(), this.getLastName(), this.getDate(), this.getUserHashPassword(), this.getEmail(),
                this.getPhoneNumber());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return Objects.equals(UserId, userDto.UserId) && Objects.equals(FirstName, userDto.FirstName) && Objects.equals(LastName, userDto.LastName) && Objects.equals(date, userDto.date) && Objects.equals(UserHashPassword, userDto.UserHashPassword) && Objects.equals(Email, userDto.Email) && Objects.equals(PhoneNumber, userDto.PhoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(UserId, FirstName, LastName, date, UserHashPassword, Email, PhoneNumber);
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "UserId=" + UserId +
                ", FirstName='" + FirstName + '\'' +
                ", LastName='" + LastName + '\'' +
                ", date=" + date +
                ", UserHashPassword='" + UserHashPassword + '\'' +
                ", Email='" + Email + '\'' +
                ", PhoneNumber='" + PhoneNumber + '\'' +
                '}';
    }
}
