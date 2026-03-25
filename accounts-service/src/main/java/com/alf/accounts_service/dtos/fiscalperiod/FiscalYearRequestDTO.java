package com.alf.accounts_service.dtos.fiscalperiod;



import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FiscalYearRequestDTO {

    private String name;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    // 'closed' is typically managed by specific endpoints, not general create/update
    // private Boolean closed; // Default to false on entity creation

    private Long accountId;
}