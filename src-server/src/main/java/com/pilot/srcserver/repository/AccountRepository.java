package com.pilot.srcserver.repository;

import com.pilot.srcserver.entity.Account;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.stream.Stream;

import static org.hibernate.jpa.HibernateHints.HINT_CACHEABLE;
import static org.hibernate.jpa.HibernateHints.HINT_FETCH_SIZE;
import static org.hibernate.jpa.QueryHints.HINT_READONLY;

public interface AccountRepository extends PagingAndSortingRepository<Account, Long> {
    @Query("SELECT DISTINCT a from Account a")
    @QueryHints(value = {
            @QueryHint(name = HINT_FETCH_SIZE, value = "" + Integer.MIN_VALUE),
            @QueryHint(name = HINT_CACHEABLE, value = "false"),
            @QueryHint(name = HINT_READONLY, value = "true"),
    })
    Stream<Account> streamAll();

    Page<Account> findAll(Pageable pageable);
}
