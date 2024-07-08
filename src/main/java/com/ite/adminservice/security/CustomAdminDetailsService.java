package com.ite.adminservice.security;


import com.ite.adminservice.entities.Admin;
import com.ite.adminservice.repositories.AdminRepository;
import com.ite.adminservice.service.PermissionService;
import com.ite.adminservice.service.RoleGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CustomAdminDetailsService implements UserDetailsService{

    @Autowired
    private AdminRepository adminRepository;


    @Autowired
    private RoleGroupService roleGroupService;

    @Autowired
    private PermissionService permissionService;

    private Admin adminDetail;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        this.adminDetail = adminRepository.findAdminByEmail(email);
        if(Objects.isNull(adminDetail)) {
            throw new UsernameNotFoundException("admin not found");
        }
        List<GrantedAuthority> authorities = getListAuthorities(adminDetail.getRoleGroupId());
        return CustomAdminDetails.mapAdminToUserDetail(adminDetail,authorities);


    }

    private List<GrantedAuthority> getListAuthorities(String roleGroupId){

        return roleGroupService.getRoleGroupById(roleGroupId).getPermissionIds().stream()
                .map(permissionId -> new SimpleGrantedAuthority(permissionService.getPermissionById(permissionId).getName().name()))
                .collect(Collectors.toList());
    }


    public Admin getAdminDetail(){
        // userDetail.setPassword(null);
        return adminDetail;
    }
}
