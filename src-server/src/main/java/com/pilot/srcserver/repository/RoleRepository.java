package com.pilot.srcserver.repository;

import com.pilot.srcserver.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
