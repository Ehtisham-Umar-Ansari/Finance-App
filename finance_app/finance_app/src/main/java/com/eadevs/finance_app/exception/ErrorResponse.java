package com.eadevs.finance_app.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class ErrorResponse {

    private String error;
    private Map<String, String> details;
}