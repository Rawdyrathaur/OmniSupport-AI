package com.omnisupport.authservice.exc;

import lombok.*;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ValidationException extends RuntimeException {
    private Map<String, String> validationErrors;
}