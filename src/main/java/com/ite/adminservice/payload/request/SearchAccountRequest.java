package com.ite.adminservice.payload.request;

import com.ite.adminservice.entities.EStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchAccountRequest {
    boolean allStatus;
    EStatus status;
    String keyWord;
    int page;
    int size;

}
