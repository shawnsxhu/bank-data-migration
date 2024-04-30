package com.pilot.srcserver.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;


@Data
@Builder
@Entity
@Table(name = "role")
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column
    private String name;
    @ManyToMany(mappedBy = "roles")
    Set<User> users;
}
