package com.omnisupport.userservice.exc;

import lombok.*;
import org.springframework.http.HttpStatus;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GenericErrorResponse extends RuntimeException {
    private String message;
    private HttpStatus httpStatus;

    public GenericErrorResponse(String message, HttpStatus httpStatus) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
