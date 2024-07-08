package com.ite.adminservice.event.messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApproveAccountNotification{
    private String email;
    private String name;
    private String qrCode;
}
