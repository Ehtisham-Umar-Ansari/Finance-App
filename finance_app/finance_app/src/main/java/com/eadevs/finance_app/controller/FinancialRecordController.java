package com.eadevs.finance_app.controller;

import com.eadevs.finance_app.dto.FinancialRecordRequestDTO;
import com.eadevs.finance_app.dto.FinancialRecordResponseDTO;
import com.eadevs.finance_app.dto.PageResponseDTO;
import com.eadevs.finance_app.dto.SummaryDTO;
import com.eadevs.finance_app.service.FinancialRecordService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/records")
public class FinancialRecordController {

    private final FinancialRecordService service;

    public FinancialRecordController(FinancialRecordService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST','EMPLOYEE')")
    public FinancialRecordResponseDTO create(@RequestBody @Valid FinancialRecordRequestDTO requestDTO) {
        return service.createRecord(requestDTO);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST','EMPLOYEE')")
    public PageResponseDTO<FinancialRecordResponseDTO> getAllRecords(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return service.getAllRecords(page, size);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public FinancialRecordResponseDTO update(
            @PathVariable Long id,
            @RequestBody @Valid FinancialRecordRequestDTO requestDTO) {
        return service.updateRecord(id, requestDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        service.deleteRecord(id);
    }

    @GetMapping("/filter/type")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST','EMPLOYEE')")
    public PageResponseDTO<FinancialRecordResponseDTO> filterByType(
            @RequestParam String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return service.filterByType(type, page, size);
    }

    @GetMapping("/filter/category")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST','EMPLOYEE')")
    public PageResponseDTO<FinancialRecordResponseDTO> filterByCategory(@RequestParam String category,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size) {
        return service.filterByCategory(category, page, size);
    }

    @GetMapping("/filter/date")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST','EMPLOYEE')")
    public PageResponseDTO<FinancialRecordResponseDTO> filterByDate(
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return service.filterByDateRange(
                LocalDate.parse(start),
                LocalDate.parse(end),
                page,
                size
        );
    }

    @GetMapping("/summary")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
    public SummaryDTO getSummary() {
        return service.getSummary();
    }

    @GetMapping("/summary/category")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
    public Map<String, Double> getCategorySummary() {
        return service.getCategorySummary();
    }

    @GetMapping("/recent")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST','EMPLOYEE')")
    public List<FinancialRecordResponseDTO> getRecent(
            @RequestParam(defaultValue = "5") int limit) {

        return service.getRecentActivities(limit);
    }

    @GetMapping("/summary/monthly")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
    public List<SummaryDTO> getMonthlyTrends() {
        return service.getMonthlyTrends();
    }

    @GetMapping("/summary/weekly")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
    public List<SummaryDTO> getWeeklyTrends() {
        return service.getWeeklyTrends();
    }

//    @GetMapping("/{id}")
    //    @PreAuthorize("hasAnyRole('ADMIN','ANALYST','VIEWER')")
//    public FinancialRecordResponseDTO getById(@PathVariable Long id) {
//        return service.getRecordById(id);
//    }
}