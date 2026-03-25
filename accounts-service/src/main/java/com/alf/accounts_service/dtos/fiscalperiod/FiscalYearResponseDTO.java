package com.alf.accounts_service.dtos.fiscalperiod;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FiscalYearResponseDTO {
    private Integer id;
    private String serialId;
    private String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss") // Ensure ISO format
    private LocalDateTime startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss") // Ensure ISO format
    private LocalDateTime endDate;

    private boolean closed;
    private Integer accountId;
    private String accountName;

}