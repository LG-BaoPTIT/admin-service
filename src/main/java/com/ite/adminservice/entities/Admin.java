package com.ite.adminservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "admins")
public class Admin {
    @Id
    private String adminId;

    private String name;

    @Indexed(unique = true)
    private String email;

    @JsonIgnore
    private String password;

    @Indexed(unique = true)
    private String phone;

    private Date dateOfBirth;

    //private LocalDateTime registrationDate;
    private Instant registrationDate;

    private EStatus status;

    private String secret;

    private String roleGroupId;

    private int failedLogin;

    private boolean isVerified;


}
