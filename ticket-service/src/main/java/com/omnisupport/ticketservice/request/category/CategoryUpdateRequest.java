package com.omnisupport.ticketservice.request.category;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryUpdateRequest {
    @NotBlank(message = "Category id is required")
    private String id;
    private String name;
    private String description;
}
