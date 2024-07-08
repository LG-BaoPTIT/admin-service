package com.ite.adminservice.payload.request;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
public class UpdateRoleDTO {
    private String roleGroupId;
    private String rolename;
    private List<String> permission;
    private String description;
}
