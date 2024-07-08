package com.ite.adminservice.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "role_group")
public class RoleGroup {
    @Id
    private String roleGroupId;
    private String name;
    //@DBRef
    private List<String> permissionIds;
    private String description;
    private Instant createdAt;
    private Instant lastModifiedAt;

}
