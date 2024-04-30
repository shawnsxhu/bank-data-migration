package com.pilot.destserver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {
    private String accountNumber;
    private String accountType;
    private String currencyType;
    private BigDecimal balance;
    private String accountStatus;
    private Date openDate;
    private Date closedDate;
}
