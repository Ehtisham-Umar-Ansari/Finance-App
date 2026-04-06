package com.eadevs.finance_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SummaryDTO {

    private String period;
    private Double totalIncome;
    private Double totalExpense;
    private Double netBalance;

    public SummaryDTO(Double totalIncome, Double totalExpense, Double netBalance) {
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
        this.netBalance = netBalance;
    }
}
