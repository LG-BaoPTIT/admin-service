package com.ite.adminservice.repositories;


import com.ite.adminservice.entities.Permission;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRepository extends MongoRepository<Permission,String> {
    Permission findPermissionByPermissionId(String permissionId);

}
