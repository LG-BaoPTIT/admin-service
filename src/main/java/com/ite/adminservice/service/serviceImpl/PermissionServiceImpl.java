package com.ite.adminservice.service.serviceImpl;


import com.ite.adminservice.constants.MessageConstant;
import com.ite.adminservice.entities.Permission;
import com.ite.adminservice.payload.dto.PermissionDTO;
import com.ite.adminservice.repositories.PermissionRepository;
import com.ite.adminservice.service.PermissionService;
import com.ite.adminservice.utils.ResponseUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public Permission getPermissionById(String permissionId) {
        return permissionRepository.findPermissionByPermissionId(permissionId);
    }

    @Override
    public ResponseEntity<?> getAllPermissions() {
        try {
            List<Permission> permissions = permissionRepository.findAll();
            List<PermissionDTO> result = permissions.stream().map(permission -> modelMapper.map(permission, PermissionDTO.class)).collect(Collectors.toList());
            return ResponseEntity.ok(result);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
