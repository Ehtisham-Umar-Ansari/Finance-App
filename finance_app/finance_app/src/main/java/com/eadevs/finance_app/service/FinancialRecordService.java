package com.eadevs.finance_app.service;

import com.eadevs.finance_app.dto.FinancialRecordRequestDTO;
import com.eadevs.finance_app.dto.FinancialRecordResponseDTO;
import com.eadevs.finance_app.dto.PageResponseDTO;
import com.eadevs.finance_app.dto.SummaryDTO;
import com.eadevs.finance_app.model.FinancialRecord;
import com.eadevs.finance_app.model.RecordType;
import com.eadevs.finance_app.model.Role;
import com.eadevs.finance_app.model.User;
import com.eadevs.finance_app.repository.FinancialRecordRepository;
import com.eadevs.finance_app.repository.UserRepository;
import com.eadevs.finance_app.security.util.SecurityUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FinancialRecordService {

    private final FinancialRecordRepository recordRepository;
    private final UserRepository userRepository;

    public FinancialRecordService(FinancialRecordRepository recordRepository, UserRepository userRepository) {
        this.recordRepository = recordRepository;
        this.userRepository = userRepository;
    }

    public FinancialRecordResponseDTO createRecord(FinancialRecordRequestDTO requestDTO) {
        User user = SecurityUtil.getCurrentUser(userRepository);
        FinancialRecord record = toEntity(requestDTO, user);
        recordRepository.save(record);
        return toResponseDTO(record);
    }

    public PageResponseDTO<FinancialRecordResponseDTO> getAllRecords(int page, int size) {

        User user = SecurityUtil.getCurrentUser(userRepository);

        Pageable pageable = PageRequest.of(page, size);

        Page<FinancialRecord> recordPage;

        if (Role.ADMIN.equals(user.getRole()) || Role.ANALYST.equals(user.getRole())) {
            recordPage = recordRepository.findAll(pageable);
        } else {
            recordPage = recordRepository.findByCreatedBy(user, pageable);
        }

        List<FinancialRecordResponseDTO> content = recordPage.getContent()
                .stream()
                .map(this::toResponseDTO)
                .toList();

        return new PageResponseDTO<>(
                content,
                recordPage.getNumber(),
                recordPage.getSize(),
                recordPage.getTotalElements(),
                recordPage.getTotalPages()
        );
    }


    public FinancialRecordResponseDTO updateRecord(Long id, FinancialRecordRequestDTO requestDTO) {
        FinancialRecord record = recordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Record not found"));

        if (requestDTO.getAmount() != null) {
            record.setAmount(requestDTO.getAmount());
        }
        if (requestDTO.getCategory() != null) {
            record.setCategory(requestDTO.getCategory().toLowerCase());
        }
        if (requestDTO.getDate() != null) {
            record.setDate(requestDTO.getDate());
        }
        if (requestDTO.getType() != null) {
            record.setType(requestDTO.getType());
        }
        if (requestDTO.getDescription() != null) {
            record.setDescription(requestDTO.getDescription());
        }

        FinancialRecord updatedRecord = recordRepository.save(record);
        return toResponseDTO(updatedRecord);
    }

    public void deleteRecord(Long id) {
        if (!recordRepository.existsById(id)) {
            throw new RuntimeException("Record not found with id: " + id);
        }
        recordRepository.deleteById(id);
    }

    public PageResponseDTO<FinancialRecordResponseDTO> filterByType(
            String type, int page, int size) {

        User user = SecurityUtil.getCurrentUser(userRepository);

        RecordType recordType = RecordType.valueOf(type.toUpperCase());

        Pageable pageable = PageRequest.of(page, size);

        Page<FinancialRecord> recordPage;

        if (Role.ADMIN.equals(user.getRole()) || Role.ANALYST.equals(user.getRole())) {
            recordPage = recordRepository.findByType(recordType, pageable);
        } else {
            recordPage = recordRepository.findByTypeAndCreatedBy(recordType, user, pageable);
        }

        List<FinancialRecordResponseDTO> content = recordPage.getContent()
                .stream()
                .map(this::toResponseDTO)
                .toList();

        return new PageResponseDTO<>(
                content,
                recordPage.getNumber(),
                recordPage.getSize(),
                recordPage.getTotalElements(),
                recordPage.getTotalPages()
        );
    }

    public PageResponseDTO<FinancialRecordResponseDTO> filterByCategory(
            String category, int page, int size) {

        User user = SecurityUtil.getCurrentUser(userRepository);

        Pageable pageable = PageRequest.of(page, size);

        Page<FinancialRecord> recordPage;

        if (Role.ADMIN.equals(user.getRole()) || Role.ANALYST.equals(user.getRole())) {
            recordPage = recordRepository.findByCategory(category, pageable);
        } else {
            recordPage = recordRepository.findByCategoryAndCreatedBy(category, user, pageable);
        }

        List<FinancialRecordResponseDTO> content = recordPage.getContent()
                .stream()
                .map(this::toResponseDTO)
                .toList();

        return new PageResponseDTO<>(
                content,
                recordPage.getNumber(),
                recordPage.getSize(),
                recordPage.getTotalElements(),
                recordPage.getTotalPages()
        );
    }

    public PageResponseDTO<FinancialRecordResponseDTO> filterByDateRange(
            LocalDate start, LocalDate end, int page, int size) {

        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start date must be before end date");
        }

        User user = SecurityUtil.getCurrentUser(userRepository);

        Pageable pageable = PageRequest.of(page, size);

        Page<FinancialRecord> recordPage;

        if (Role.ADMIN.equals(user.getRole()) || Role.ANALYST.equals(user.getRole())) {
            recordPage = recordRepository.findByDateBetween(start, end, pageable);
        } else {
            recordPage = recordRepository.findByDateBetweenAndCreatedBy(start, end, user, pageable);
        }

        List<FinancialRecordResponseDTO> content = recordPage.getContent()
                .stream()
                .map(this::toResponseDTO)
                .toList();

        return new PageResponseDTO<>(
                content,
                recordPage.getNumber(),
                recordPage.getSize(),
                recordPage.getTotalElements(),
                recordPage.getTotalPages()
        );
    }

    public SummaryDTO getSummary() {

        Double income = recordRepository.getTotalIncome();
        Double expense = recordRepository.getTotalExpense();

        return new SummaryDTO(income, expense, income-expense);
    }

    public Map<String, Double> getCategorySummary() {

        List<Object[]> results = recordRepository.getCategoryWiseTotals();

        Map<String, Double> categoryMap = new HashMap<>();

        for (Object[] row : results) {
            String category = (String) row[0];
            Double total = (Double) row[1];
            categoryMap.put(category, total);
        }

        return categoryMap;
    }

    public List<FinancialRecordResponseDTO> getRecentActivities(int limit) {
        if (limit <= 0 || limit > 100) {
            throw new IllegalArgumentException("Limit must be between 1 and 100");
        }

        User user = SecurityUtil.getCurrentUser(userRepository);
        Pageable pageable = PageRequest.of(0, limit);

        List<FinancialRecord> records;
        if (Role.ADMIN.equals(user.getRole()) || Role.ANALYST.equals(user.getRole())) {
            records = recordRepository.findAllByOrderByDateDesc(pageable);
        } else {
            records = recordRepository.findByCreatedByOrderByDateDesc(user, pageable);
        }

        return records.stream()
                .map(this::toResponseDTO)
                .toList();
    }

//    public FinancialRecordResponseDTO getRecordById(Long id) {
//    }

    public List<SummaryDTO> getMonthlyTrends() {

        List<Object[]> results = recordRepository.getMonthlyTrends();

        List<SummaryDTO> summary = new ArrayList<>();

        for (Object[] row : results) {
            String period = (String) row[0];
            Double income = (Double) row[1];
            Double expense = (Double) row[2];
            Double net = (Double) row[3];
            summary.add(new SummaryDTO(period, income, expense, net));
        }

        return summary;
    }

    public List<SummaryDTO> getWeeklyTrends() {
        List<Object[]> results = recordRepository.getWeeklyTrends();

        List<SummaryDTO> summary = new ArrayList<>();

        for (Object[] row : results) {
            String period = (String) row[0];
            Double income = (Double) row[1];
            Double expense = (Double) row[2];
            Double net = (Double) row[3];
            summary.add(new SummaryDTO(period, income, expense, net));
        }

        return summary;
    }

    private FinancialRecordResponseDTO toResponseDTO(FinancialRecord record) {
        FinancialRecordResponseDTO responseDTO = new FinancialRecordResponseDTO();
        responseDTO.setId(record.getId());
        responseDTO.setAmount(record.getAmount());
        responseDTO.setType(record.getType());
        responseDTO.setCategory(record.getCategory());
        responseDTO.setDate(record.getDate());
        responseDTO.setDescription(record.getDescription());
        responseDTO.setCreatedById(record.getCreatedBy() != null ? record.getCreatedBy().getId() : null);
        return responseDTO;
    }

    private FinancialRecord toEntity(FinancialRecordRequestDTO dto, User user) {
        FinancialRecord record = new FinancialRecord();
        record.setAmount(dto.getAmount());
        record.setType(dto.getType());
        record.setCategory(dto.getCategory() != null ? dto.getCategory().toLowerCase() : null);
        record.setDate(dto.getDate());
        record.setDescription(dto.getDescription());
        record.setCreatedBy(user);
        return record;
    }
}
