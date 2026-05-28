package com.omnisupport.authservice.dto;

import com.omnisupport.authservice.enums.Role;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String id;
    private String username;
    private String password;
    private Role role;
}
