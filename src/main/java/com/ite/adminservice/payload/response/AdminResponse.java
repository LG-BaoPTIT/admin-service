package com.ite.adminservice.payload.response;

import com.ite.adminservice.entities.EStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminResponse {
    private String adminId;

    private String name;

    private String email;

    private String phone;

    private EStatus status;
}
