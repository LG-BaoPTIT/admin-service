package com.ite.adminservice.repositories;

import com.ite.adminservice.entities.RoleGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoleGroupRepository extends MongoRepository<RoleGroup,String> {
    boolean existsByRoleGroupId(String roleGroupId);
    boolean existsByName(String name);
    RoleGroup findRoleGroupByRoleGroupId(String roleGroupId);
    RoleGroup findRoleGroupByName(String name);
    List<RoleGroup> findAll();
    Optional<RoleGroup> findByName(String name);
    Page<RoleGroup> findByRoleGroupIdOrNameOrCreatedAt(String roleGroupId, String name, Instant createdAt, Pageable pageable);

    Page<RoleGroup> findAll(Pageable pageable);
}
