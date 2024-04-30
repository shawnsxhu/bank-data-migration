package com.pilot.srcserver.repository;

import com.pilot.srcserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepoJPA extends JpaRepository<User, Long> {
}
