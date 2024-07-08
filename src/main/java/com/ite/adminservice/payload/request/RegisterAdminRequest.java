package com.ite.adminservice.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterAdminRequest {
    private String name;

    private String email;

    private String password;

    private String rePassword;

    private String phone;
}
