package com.pilot.srcserver.mapper;

import com.pilot.srcserver.dto.AccountDTO;
import com.pilot.srcserver.entity.Account;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccountInfoMapper {
    AccountDTO mapToDTO(Account accountEntity);
}
