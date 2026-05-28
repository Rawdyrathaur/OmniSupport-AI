package com.omnisupport.ticketservice.model;

import com.omnisupport.ticketservice.enums.Role;
import lombok.*;

import javax.persistence.*;

@Entity(name = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {
    private String name;
    private String email;
    private String password;
    
    @Enumerated(EnumType.STRING)
    private Role role;
    
    private boolean isActive = true;
}
