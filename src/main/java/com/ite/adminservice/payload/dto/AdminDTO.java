package com.ite.adminservice.payload.dto;

import com.ite.adminservice.entities.EStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminDTO {

    private String adminId;

    private String name;

    private String email;

    private String password;

    private String phone;

    private Instant registrationDate;

    private EStatus status;

    private String secret;

    private String roleGroupId;

    private int failedLogin;

    private boolean isVerified;

    private String action;
}
