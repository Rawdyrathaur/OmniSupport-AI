package com.omnisupport.ticketservice.model;

import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity(name = "categories")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category extends BaseEntity {
    private String name;
    private String description;
    private String imageId;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Job> jobs;
}