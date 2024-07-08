package com.ite.adminservice.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchRolegroupDTO {
    private String roleGroupId;
    private String name;
    private Instant createdAt;
    private int page;
    private int size;
}
