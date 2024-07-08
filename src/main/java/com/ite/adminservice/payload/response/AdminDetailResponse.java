package com.ite.adminservice.payload.response;

import com.ite.adminservice.entities.EStatus;
import com.ite.adminservice.entities.Permission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDetailResponse {

    private String adminId;

    private String name;

    private String email;

    private String phone;

    private Instant registrationDate;

    private EStatus status;

    private String roleGroupName;

    private List<Permission> permissions;

}
