package com.ite.adminservice.payload.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleGroupDTO {
    private String roleGroupId;
    private String name;
    private List<String> permissionIds;
    private String description;
    private Instant createdAt;
    private Instant lastModifiedAt;
}
