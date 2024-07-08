package com.ite.adminservice.payload.dto;

import com.ite.adminservice.entities.Permission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolegroupDTOresponse {
    private String roleGroupId;
    private String name;
    private List<Permission> permissionIds;
    private String description;
    private Instant createdAt;
    private Instant lastModifiedAt;
}
