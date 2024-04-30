package com.pilot.destserver.repository;

import com.pilot.destserver.entity.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountCrudRepo extends CrudRepository<Account, Long> {
}
