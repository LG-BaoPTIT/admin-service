package com.ite.adminservice.service;


import com.ite.adminservice.entities.Permission;
import com.ite.adminservice.entities.RoleGroup;
import com.ite.adminservice.payload.dto.RoleGroupDTO;
import com.ite.adminservice.payload.dto.SearchRolegroupDTO;
import com.ite.adminservice.payload.request.*;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.List;

public interface RoleGroupService {
    RoleGroup getRoleGroupById(String roleGroupId);

    ResponseEntity<?> getRoleGroupDetail(String roleGroupId);

    ResponseEntity<?> getListRoleGroup();

    ResponseEntity<?> addRoleGroup(AddRoleGroupRequest request);

    ResponseEntity<?> searchrolegroup(SearchRolegroupDTO searchRolegroupDTO, HttpServletRequest request);

    ResponseEntity<?> updateRoleGroup(UpdateRoleDTO updateRoleDTO);

    ResponseEntity<?> deleteRole(DeleteDTORequest deleteDTORequest);

    
}
