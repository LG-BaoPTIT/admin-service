package com.ite.adminservice.service.serviceImpl;


import com.ite.adminservice.constants.MessageConstant;
import com.ite.adminservice.entities.Admin;
import com.ite.adminservice.entities.Permission;
import com.ite.adminservice.entities.RoleGroup;
import com.ite.adminservice.event.eventProducer.RoleGroupEventPublisher;
import com.ite.adminservice.payload.dto.PermissionDTO;
import com.ite.adminservice.payload.dto.RoleGroupDTO;
import com.ite.adminservice.payload.dto.RolegroupDTOresponse;
import com.ite.adminservice.payload.dto.SearchRolegroupDTO;
import com.ite.adminservice.payload.request.AddRoleGroupRequest;
import com.ite.adminservice.payload.request.DeleteDTORequest;
import com.ite.adminservice.payload.request.RolegroupDTOrequest;
import com.ite.adminservice.payload.request.UpdateRoleDTO;
import com.ite.adminservice.payload.response.RoleGroupDetailResponse;
import com.ite.adminservice.payload.response.RoleGroupSearchResult;
import com.ite.adminservice.repositories.AdminRepository;
import com.ite.adminservice.repositories.PermissionRepository;
import com.ite.adminservice.repositories.RoleGroupRepository;
import com.ite.adminservice.service.RoleGroupService;
import com.ite.adminservice.utils.ResponseUtil;
//import com.netflix.discovery.converters.Auto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoleGroupServiceImpl implements RoleGroupService {
    @Autowired
    private RoleGroupRepository roleGroupRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private RoleGroupEventPublisher roleGroupEventPublisher;

    @Override
    public ResponseEntity<?> getListRoleGroup() {
        try{
            List<RoleGroup> roleGroups = roleGroupRepository.findAll();
            List<RoleGroupDTO> result = roleGroups.stream().map(roleGroup -> modelMapper.map(roleGroup,RoleGroupDTO.class)).collect(Collectors.toList());
            return ResponseEntity.ok(result);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> addRoleGroup(AddRoleGroupRequest addRoleGroupRequest) {
        try{
            if (isAnyFieldNullOrEmpty(addRoleGroupRequest)) {
                return ResponseUtil.getResponseEntity("01", MessageConstant.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
            if(roleGroupRepository.existsByName(addRoleGroupRequest.getName())){
                return ResponseUtil.getResponseEntity("03", MessageConstant.INVALID_DATA,HttpStatus.BAD_REQUEST);
            }

            if(!checkListPermissionId(addRoleGroupRequest.getPermissionIds())){
                return ResponseUtil.getResponseEntity("04", MessageConstant.INVALID_DATA,HttpStatus.BAD_REQUEST);

            }

            RoleGroup roleGroup = modelMapper.map(addRoleGroupRequest,RoleGroup.class);
            roleGroup.setCreatedAt(Instant.now());
            roleGroupRepository.save(roleGroup);
            roleGroupEventPublisher.createRoleGroup(modelMapper.map(roleGroup,RoleGroupDTO.class));
            return ResponseUtil.getResponseEntity("00", MessageConstant.ADD_ROLE_GROUP_SUCCESS,HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @Override
    public RoleGroup getRoleGroupById(String roleGroupId) {
        return roleGroupRepository.findRoleGroupByRoleGroupId(roleGroupId);
    }

    @Override
    public ResponseEntity<?> getRoleGroupDetail(String roleGroupId) {
        RoleGroup roleGroup = roleGroupRepository.findRoleGroupByRoleGroupId(roleGroupId);
        if(!Objects.isNull(roleGroup)){
            List<PermissionDTO> permissionsDTO = roleGroup.getPermissionIds().stream().map(s -> modelMapper.map(permissionRepository.findPermissionByPermissionId(s), PermissionDTO.class)).collect(Collectors.toList());
            RoleGroupDetailResponse roleGroupDetailResponse = modelMapper.map(roleGroup,RoleGroupDetailResponse.class);
            roleGroupDetailResponse.setPermissions(permissionsDTO);
            return ResponseEntity.ok(roleGroupDetailResponse);
        }else {
            return ResponseUtil.getResponseEntity("Không tìm thấy nhóm quyền",HttpStatus.BAD_REQUEST);
        }
    }
//11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111
    @Override
    public ResponseEntity<?> searchrolegroup(SearchRolegroupDTO searchRolegroupDTO, HttpServletRequest request) {
        try{
            Pageable pageable = (Pageable) PageRequest.of(searchRolegroupDTO.getPage(), searchRolegroupDTO.getSize(), Sort.by("createdAt").ascending());
            Page<RoleGroup> roleGroupPage;
            if((searchRolegroupDTO.getRoleGroupId() != null && searchRolegroupDTO.getRoleGroupId().isEmpty())||
                    (searchRolegroupDTO.getName() != null && searchRolegroupDTO.getName().isEmpty())||
                    (searchRolegroupDTO.getCreatedAt() != null )) {
                roleGroupPage = roleGroupRepository.findByRoleGroupIdOrNameOrCreatedAt(searchRolegroupDTO.getRoleGroupId(), searchRolegroupDTO.getName(), searchRolegroupDTO.getCreatedAt(),pageable);
            }else {
                roleGroupPage=roleGroupRepository.findAll(pageable);
            }
            List<RoleGroup> roleGroups = roleGroupPage.getContent();
            List<RolegroupDTOresponse> roleGroupDTOList = roleGroups.stream()
                    .map(roleGroup -> {
                        RolegroupDTOresponse rolegroupDTOresponse = modelMapper.map(roleGroup, RolegroupDTOresponse.class);
                        List<Permission> permissions = getRoleGroupById(rolegroupDTOresponse.getRoleGroupId())
                                .getPermissionIds().stream()
                                .map(permissionId -> permissionRepository.findPermissionByPermissionId(permissionId))
                                .collect(Collectors.toList());
                        rolegroupDTOresponse.setPermissionIds(permissions);

                        return rolegroupDTOresponse;
                    })
                    .collect(Collectors.toList());
            long totalRecords = roleGroupPage.getTotalElements();
            int currentPageSize = roleGroupDTOList.size();
            RoleGroupSearchResult roleGroupSearchResult = new RoleGroupSearchResult(totalRecords,currentPageSize, roleGroupDTOList);
            return ResponseEntity.ok(roleGroupSearchResult);
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }



    }

    @Override
    public ResponseEntity<?> updateRoleGroup( UpdateRoleDTO updatedRoleGroup) {
        try{
        Optional<RoleGroup> roleGroupOptional= roleGroupRepository.findById(updatedRoleGroup.getRoleGroupId());
        if (roleGroupOptional.isPresent()){
            if(!checkListPermissionId(updatedRoleGroup.getPermission())){
                return ResponseUtil.getResponseEntity("07", MessageConstant.ROLE_GROUP_PERMISSION_EXISTED,HttpStatus.BAD_REQUEST);}
            if(roleGroupRepository.existsByName(updatedRoleGroup.getRolename())){
                return ResponseUtil.getResponseEntity("03", MessageConstant.ROLE_GROUP_NAME_EXISTED,HttpStatus.BAD_REQUEST);
            }
            RoleGroup roleGroup=roleGroupOptional.get();
            roleGroup.setName(updatedRoleGroup.getRolename());
            roleGroup.setPermissionIds(updatedRoleGroup.getPermission());
            roleGroup.setDescription(updatedRoleGroup.getDescription());
            roleGroup.setLastModifiedAt(Instant.now());
            roleGroupRepository.save(roleGroup);
            roleGroupEventPublisher.updateRoleGroupEvent(modelMapper.map(roleGroup, RoleGroupDTO.class));
            return ResponseUtil.getResponseEntity("00",MessageConstant.UPADATE_ROLE_SUCCESS,HttpStatus.OK);
        }else {
                return ResponseUtil.getResponseEntity("08",MessageConstant.ROLE_DOES_NOT_EXIST, HttpStatus.BAD_REQUEST);
            }}catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<?> deleteRole(DeleteDTORequest deleteDTORequest) {
        try{
        Optional<RoleGroup> roleGroupOptional = roleGroupRepository.findById(deleteDTORequest.getRoleGroupId());
        if (roleGroupOptional.isPresent()) {
            if (!adminRepository.existsByRoleGroupId(deleteDTORequest.getRoleGroupId())) {
                roleGroupRepository.deleteById(deleteDTORequest.getRoleGroupId());
                roleGroupEventPublisher.deleteRoleGroupEvent(deleteDTORequest.getRoleGroupId());
                return ResponseUtil.getResponseEntity(MessageConstant.DELETE_ROLE_SUCCESS, HttpStatus.OK);
            } else {
                return ResponseUtil.getResponseEntity(MessageConstant.ROLE_GROUP_EXIST_IN_ADMIN, HttpStatus.BAD_REQUEST);
            }
        } else {
            return ResponseUtil.getResponseEntity(MessageConstant.ROLE_DOES_NOT_EXIST, HttpStatus.BAD_REQUEST);
        }}catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private boolean isValidRoleGroup(RoleGroupDTO roleGroupDTO) {
        return isNullOrEmpty(roleGroupDTO.getRoleGroupId().trim()) &&
                isNullOrEmpty(roleGroupDTO.getName().trim()) &&
                roleGroupDTO.getPermissionIds() != null && !roleGroupDTO.getPermissionIds().isEmpty() &&
                isNullOrEmpty(roleGroupDTO.getDescription().trim());
    }

    private boolean isNullOrEmpty(String str) {
        return str != null && !str.isEmpty();
    }

    private boolean isAnyFieldNullOrEmpty(AddRoleGroupRequest addRoleGroupRequest) {
        return  addRoleGroupRequest.getName() == null || addRoleGroupRequest.getName().trim().isEmpty() ||
                addRoleGroupRequest.getPermissionIds() == null || addRoleGroupRequest.getPermissionIds().isEmpty() ||
                addRoleGroupRequest.getDescription() == null || addRoleGroupRequest.getDescription().trim().isEmpty();
    }
    private boolean checkListPermissionId(List<String> permissionIds) {
        List<Permission> permissions = permissionRepository.findAll();

        List<String> validPermissionIds = permissions.stream().map(Permission::getPermissionId).collect(Collectors.toList());
        for(String id : permissionIds){
            if(!validPermissionIds.contains(id)){
                return false;
            }
        }
        return true;
    }

}
