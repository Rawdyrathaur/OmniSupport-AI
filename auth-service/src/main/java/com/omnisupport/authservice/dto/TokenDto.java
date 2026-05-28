package com.omnisupport.authservice.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto {
    private String token;
}
