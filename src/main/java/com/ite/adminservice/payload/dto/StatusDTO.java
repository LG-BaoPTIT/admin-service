package com.ite.adminservice.payload.dto;

import com.ite.adminservice.entities.EStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatusDTO {
    private String statusId;

    private EStatus statusName;

    private String description;
}
