package com.ite.adminservice.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ite.adminservice.entities.Admin;
import com.ite.adminservice.entities.EStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
public class CustomAdminDetails implements UserDetails {

    private String adminId;
    private String name;
    private String email;
    @JsonIgnore
    private String password;
    private String phone;
    private EStatus status;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomAdminDetails() {
    }


    public static CustomAdminDetails mapAdminToUserDetail(Admin admin, List<GrantedAuthority> listAuthorities) {
        return new CustomAdminDetails(
                admin.getAdminId(),
                admin.getName(),
                admin.getEmail(),
                admin.getPassword(),
                admin.getPhone(),
                admin.getStatus(),
                listAuthorities
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }
    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.status.equals(EStatus.LOCK);
        //return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.status.equals(EStatus.ACTIVE);
        //return true;
    }


}
