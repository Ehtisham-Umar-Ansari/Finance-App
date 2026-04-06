package com.eadevs.finance_app.dto;

import com.eadevs.finance_app.model.RecordType;
import jakarta.validation.constraints.*;

import lombok.Data;

import java.time.LocalDate;

@Data
public class FinancialRecordRequestDTO {

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Double amount;

    @NotNull(message = "Record type is required")
    private RecordType type;

    @NotBlank(message = "Category is required")
    private String category;

    @NotNull(message = "Date is required")
    @PastOrPresent(message = "Date cannot be in the future")
    private LocalDate date;

    private String description;
}
