package com.pilot.destserver.processor;

import com.pilot.destserver.dto.AccountDTO;
import com.pilot.destserver.dto.LatestRate;
import com.pilot.destserver.entity.Account;
import com.pilot.destserver.mapper.AccountInfoMapper;
import com.pilot.destserver.service.ForeignExchangeRestClient;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
public class AccountProcessor implements ItemProcessor<AccountDTO, Account> {

    private AccountInfoMapper accountInfoMapper;
    private ForeignExchangeRestClient foreignExchangeRestClient;
    private final String BASE = "EUR";

    private final String TO = "USD";
    private AtomicInteger count;
    private LatestRate latestRate;

    @Autowired
    public AccountProcessor(AccountInfoMapper accountInfoMapper, ForeignExchangeRestClient foreignExchangeRestClient) {
        this.accountInfoMapper = accountInfoMapper;
        this.foreignExchangeRestClient = foreignExchangeRestClient;
        this.count = new AtomicInteger(0);
        this.latestRate = foreignExchangeRestClient.latestRate();
    }

    @Override
    public Account process(AccountDTO accountDTO) throws Exception {
        int i = count.addAndGet(1);
        Account account = accountInfoMapper.mapToAccount(accountDTO);
        String accountBase = account.getCurrencyType();
        BigDecimal base = account.getBalance();
        if (!accountBase.equals(BASE)) {
            base = account.getBalance().divide(latestRate.getRates().get(accountBase), RoundingMode.DOWN);
        }
        account.setBalance(base.multiply(latestRate.getRates().get(TO)));
        account.setAccountType(BASE);
        return account;
    }
}
