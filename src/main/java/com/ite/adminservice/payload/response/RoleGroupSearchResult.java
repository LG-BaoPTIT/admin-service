package com.ite.adminservice.payload.response;

import com.ite.adminservice.payload.dto.RolegroupDTOresponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class RoleGroupSearchResult {
    private long totalRecords;
    private int currentPageSize;
    private List<RolegroupDTOresponse> roles;
}
