package com.shyiko.coursework.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Base64;

@Entity
@Table(name = "user_profiles")
@Getter
@Setter
@NoArgsConstructor
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, length = 60)
    private String username;

    @Column(name = "fname", nullable = false, length = 60)
    private String fname;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "profile_photo", columnDefinition = "bytea")
    private byte[] profilePhoto;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "bio", length = 512)
    private String bio;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "role", nullable = false)
    private String role;
    // constructors, getters and setters

    public String getEncoded() {
        if (profilePhoto != null && profilePhoto.length > 0) return Base64.getEncoder().encodeToString(profilePhoto);
        return "";
    }

    public UserProfile(String username, String fname, String password, byte[] profilePhoto, LocalDate birthday, String bio, String gender, Role role) {
        this.username = username;
        this.fname = fname;
        this.password = password;
        this.profilePhoto = profilePhoto;
        this.birthday = birthday;
        this.bio = bio;
        this.gender = gender;
        this.role = role.name();
    }
}