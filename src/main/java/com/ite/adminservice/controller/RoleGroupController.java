package com.ite.adminservice.controller;

import com.ite.adminservice.constants.MessageConstant;
import com.ite.adminservice.entities.Permission;
import com.ite.adminservice.payload.dto.RoleGroupDTO;
import com.ite.adminservice.payload.dto.SearchRolegroupDTO;
import com.ite.adminservice.payload.request.*;
import com.ite.adminservice.service.RoleGroupService;
import com.ite.adminservice.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/admin/roleGroup")
public class RoleGroupController {
    @Autowired
    private RoleGroupService roleGroupService;
    @GetMapping("/listRoleGroup")
    @PreAuthorize("hasAuthority('MANAGE_PERMISSION')")
    public ResponseEntity<?> getListRoleGroup(){
        try{
            return roleGroupService.getListRoleGroup();
        }catch (Exception e){
            return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/detail")
    @PreAuthorize("hasAuthority('MANAGE_PERMISSION')")
    public ResponseEntity<?> getRoleGroupDetail(@RequestParam("roleGroupId") String roleGroupId){
        try{
            return roleGroupService.getRoleGroupDetail(roleGroupId);
        }catch (Exception e){
            return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/add")
    @PreAuthorize("hasAuthority('MANAGE_PERMISSION')")
    public ResponseEntity<?> addRoleGroup(@RequestBody AddRoleGroupRequest addRoleGroupRequest) {
        return roleGroupService.addRoleGroup(addRoleGroupRequest);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('MANAGE_PERMISSION')")
    public ResponseEntity<?> searchrolegroup(
            @RequestBody SearchRolegroupDTO searchRolegroupDTO,
            HttpServletRequest request) throws Exception {
        return  roleGroupService.searchrolegroup(searchRolegroupDTO,request);
    }
    @PostMapping("/updaterole")
    @PreAuthorize("hasAuthority('MANAGE_PERMISSION')")
    public ResponseEntity<?> updateRoleGroup( @RequestBody UpdateRoleDTO updatedRoleGroup) {
        return roleGroupService.updateRoleGroup(updatedRoleGroup);
    }

    @PostMapping("/deleteRole")
    @PreAuthorize("hasAuthority('MANAGE_PERMISSION')")
    public ResponseEntity<?> deleteRole(@RequestBody DeleteDTORequest deleteDTORequest){
        return roleGroupService.deleteRole(deleteDTORequest);
    }


}
