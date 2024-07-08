package com.ite.adminservice.payload.request;

import com.ite.adminservice.entities.Permission;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
public class RolegroupDTOrequest {
    @Id
    private String roleGroupId;
    private String name;
    //@DBRef
    private List<Permission> permissionIds;
    private String description;
    private Instant createdAt;
    private Instant lastModifiedAt;

}
