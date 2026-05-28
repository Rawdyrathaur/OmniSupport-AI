package com.omnisupport.authservice.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {
    private String id;
    private String username;
    private String email;
}
