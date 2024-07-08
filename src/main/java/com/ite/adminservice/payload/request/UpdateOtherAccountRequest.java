package com.ite.adminservice.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOtherAccountRequest {
    private String adminId;
    private String name;
    private String roleGroupId;

}
