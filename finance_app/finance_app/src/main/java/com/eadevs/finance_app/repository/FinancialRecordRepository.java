package com.eadevs.finance_app.repository;

import com.eadevs.finance_app.model.FinancialRecord;
import com.eadevs.finance_app.model.RecordType;
import com.eadevs.finance_app.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface FinancialRecordRepository extends JpaRepository<FinancialRecord, Long> {

    Page<FinancialRecord> findByType(RecordType type, Pageable pageable);

    Page<FinancialRecord> findByCategory(String category, Pageable pageable);

    Page<FinancialRecord> findByDateBetween(LocalDate start, LocalDate end, Pageable pageable);

    @Query("SELECT COALESCE(SUM(f.amount), 0) FROM FinancialRecord f WHERE f.type = 'INCOME'")
    Double getTotalIncome();

    @Query("SELECT COALESCE(SUM(f.amount), 0) FROM FinancialRecord f WHERE f.type = 'EXPENSE'")
    Double getTotalExpense();

    @Query("SELECT f.category, SUM(f.amount) FROM FinancialRecord f GROUP BY f.category")
    List<Object[]> getCategoryWiseTotals();

    List<FinancialRecord> findAllByOrderByDateDesc(Pageable pageable);

    @Query("SELECT FUNCTION('DATE_FORMAT', f.date, '%Y-%m') AS month, " +
            "SUM(CASE WHEN type = 'INCOME' THEN amount ELSE 0 END) AS total_income, " +
            "SUM(CASE WHEN type = 'EXPENSE' THEN amount ELSE 0 END) AS total_expense, " +
            "SUM(CASE WHEN type = 'INCOME' THEN amount ELSE -amount END) AS net_balance " +
            "FROM FinancialRecord f GROUP BY FUNCTION('DATE_FORMAT', f.date, '%Y-%m') " +
            "ORDER BY FUNCTION('DATE_FORMAT', f.date, '%Y-%m')")
    List<Object[]> getMonthlyTrends();

    @Query("SELECT FUNCTION('DATE_FORMAT', f.date, '%Y-%u') AS week, " +
            "SUM(CASE WHEN type = 'INCOME' THEN amount ELSE 0 END) AS total_income, " +
            "SUM(CASE WHEN type = 'EXPENSE' THEN amount ELSE 0 END) AS total_expense, " +
            "SUM(CASE WHEN type = 'INCOME' THEN amount ELSE -amount END) AS net_balance " +
            "FROM FinancialRecord f GROUP BY FUNCTION('DATE_FORMAT', f.date, '%Y-%u') " +
            "ORDER BY FUNCTION('DATE_FORMAT', f.date, '%Y-%u')")
    List<Object[]> getWeeklyTrends();

    Page<FinancialRecord> findByCreatedBy(User user, Pageable pageable);

    Page<FinancialRecord> findByTypeAndCreatedBy(RecordType type, User user, Pageable pageable);

    Page<FinancialRecord> findByCategoryAndCreatedBy(String category, User user, Pageable pageable);

    Page<FinancialRecord> findByDateBetweenAndCreatedBy(LocalDate start, LocalDate end, User user, Pageable pageable);

    List<FinancialRecord> findByCreatedByOrderByDateDesc(User user, Pageable pageable);
}
