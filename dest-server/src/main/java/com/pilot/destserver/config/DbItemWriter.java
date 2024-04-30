package com.pilot.destserver.config;

import com.pilot.destserver.entity.Account;
import com.pilot.destserver.repository.AccountCrudRepo;
import lombok.NonNull;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DbItemWriter implements ItemWriter<Account> {

    @Autowired
    private AccountCrudRepo accountRepository;

    @Override
    public void write(@NonNull Chunk<? extends Account> chunk) throws Exception {
        accountRepository.saveAll(chunk);
    }
}