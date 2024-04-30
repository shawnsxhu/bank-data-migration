package com.pilot.srcserver.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;

@Data
@Builder
@Entity
@Table(name = "account")
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqGen")
    @SequenceGenerator(name = "seqGen", sequenceName = "seq", initialValue = 1)
    @Column(name = "id")
    private Long accountId;
    @Column(name = "account_number")
    private String accountNumber;
    @Column(name = "account_type")
    private String accountType;
    @Column(name = "currency_type")
    private String currencyType;
    @Column
    private BigDecimal balance;
    @Column(name = "account_status")
    private String accountStatus;
    @Column(name = "open_date")
    private Date openDate;
    @Column(name = "closed_date")
    private Date closedDate;
    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;
}
