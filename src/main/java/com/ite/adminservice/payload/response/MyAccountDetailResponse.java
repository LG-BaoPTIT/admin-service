package com.ite.adminservice.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyAccountDetailResponse {
    private String adminId;

    private String name;

    private String email;

    private String phone;

    private Instant registrationDate;

    private String roleGroupName;
}
