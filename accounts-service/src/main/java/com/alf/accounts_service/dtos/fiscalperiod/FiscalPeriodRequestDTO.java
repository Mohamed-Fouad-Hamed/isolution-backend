package com.alf.accounts_service.dtos.fiscalperiod;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FiscalPeriodRequestDTO {

    private String name;

    private LocalDateTime startDate;


    private LocalDateTime endDate;


    private Integer fiscalYearId;
}
