package com.pilot.srcserver.repository;

import com.pilot.srcserver.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepoJPA extends JpaRepository<Account, Long> {
}
