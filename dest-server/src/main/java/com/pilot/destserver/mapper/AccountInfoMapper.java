package com.pilot.destserver.mapper;

import com.pilot.destserver.dto.AccountDTO;
import com.pilot.destserver.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccountInfoMapper {
    Account mapToAccount(AccountDTO accountDTO);
}
