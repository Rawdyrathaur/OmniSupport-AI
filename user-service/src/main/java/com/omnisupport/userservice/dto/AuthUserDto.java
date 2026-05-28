package com.omnisupport.userservice.dto;

import com.omnisupport.userservice.enums.Role;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthUserDto {
    private String id;
    private String username;
    private String password;
    private Role role;
}