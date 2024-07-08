package com.ite.adminservice.payload.dto;

//import com.example.ite.Entity.Invoice;

import com.ite.adminservice.entities.RoleGroup;
import com.ite.adminservice.payload.request.RolegroupDTOrequest;
import com.ite.adminservice.payload.request.RolegroupDTOrequest2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class Mapper {

    public static RolegroupDTOrequest2 torolegroupsDTOresponse(RoleGroup roleGroup){
        return RolegroupDTOrequest2.builder()
                .roleGroupId(roleGroup.getRoleGroupId())
                .name(roleGroup.getName())
                .permissionIds(roleGroup.getPermissionIds())
                .description(roleGroup.getDescription())
                .createdAt(roleGroup.getCreatedAt())
                .lastModifiedAt(roleGroup.getLastModifiedAt())
                .build();
    }
}
