package com.pilot.srcserver.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Date;

@Data
@Builder
public class AccountDTO {
    private String accountNumber;
    private String accountType;
    private String currencyType;
    private BigDecimal balance;
    private String accountStatus;
    private Date openDate;
    private Date closedDate;
}
