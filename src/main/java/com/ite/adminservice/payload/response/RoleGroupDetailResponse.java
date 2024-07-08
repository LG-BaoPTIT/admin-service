package com.ite.adminservice.payload.response;

import com.ite.adminservice.payload.dto.PermissionDTO;
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
public class RoleGroupDetailResponse {
    private String roleGroupId;
    private String name;
    private String description;
    private Instant createdAt;
    private Instant lastModifiedAt;
    private List<PermissionDTO> permissions;
}
