package com.ite.adminservice.payload.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminSearchResult {
    private long totalRecords;
    private int currentPageSize;
    private List<AdminResponse> admins;
}
