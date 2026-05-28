package com.omnisupport.ticketservice.request.category;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryCreateRequest {
    @NotBlank(message = "Category name is required")
    private String name;
    private String description;
}
