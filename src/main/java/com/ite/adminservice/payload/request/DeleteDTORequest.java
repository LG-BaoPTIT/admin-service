package com.ite.adminservice.payload.request;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
public class DeleteDTORequest {
    private String roleGroupId;
}
