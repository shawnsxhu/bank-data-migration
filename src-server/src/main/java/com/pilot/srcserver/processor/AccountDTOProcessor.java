package com.pilot.srcserver.processor;

import com.pilot.srcserver.dto.AccountDTO;
import com.pilot.srcserver.entity.Account;
import com.pilot.srcserver.mapper.AccountInfoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
public class AccountDTOProcessor implements ItemProcessor<Account, AccountDTO> {
    @Autowired
    private AccountInfoMapper accountInfoMapper;

    AtomicInteger count = new AtomicInteger(0);

    @Override
    public AccountDTO process(Account account) throws Exception {
        int i = count.addAndGet(1);
        return accountInfoMapper.mapToDTO(account);
    }
}
