package com.omnisupport.userservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.omnisupport.userservice.model.UserDetails;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    private String id;
    private String username;
    private String email;
    private UserDetails userDetails;
}
