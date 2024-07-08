package com.ite.adminservice.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddRoleGroupRequest {

    private String name;
    private List<String> permissionIds;
    private String description;
}
