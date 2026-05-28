package com.omnisupport.ticketservice.repository;

import com.omnisupport.ticketservice.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {
}
