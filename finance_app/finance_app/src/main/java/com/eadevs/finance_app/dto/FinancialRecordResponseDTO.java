package com.eadevs.finance_app.dto;

import com.eadevs.finance_app.model.RecordType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class FinancialRecordResponseDTO {

    private Long id;

    private Double amount;

    private RecordType type;

    private String category;

    private LocalDate date;

    private String description;

    private Long createdById;
}
