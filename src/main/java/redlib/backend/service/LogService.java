package redlib.backend.service;

import org.apache.poi.ss.usermodel.Workbook;
import redlib.backend.dto.query.LogQueryDTO;
import redlib.backend.model.MonthlyProfit;
import redlib.backend.model.Page;
import redlib.backend.vo.LogVO;

import java.util.Date;
import java.util.List;
import java.util.Map;


public interface LogService {
    Page<LogVO> list(LogQueryDTO queryDTO);
    Workbook export(LogQueryDTO queryDTO);
    List<MonthlyProfit> calculateMonthlyProfitForYear(Integer year);
    Integer yearProfit(Integer year);
    List<Map<String, Object>> calculateDailyProfitBetweenDates(Date startDate, Date endDate);
    Double calculateTotalProfitBetweenDates(Date startDate, Date endDate);
    List<Map<String, Object>> calculateProfitByBarberNameAndDateRange(String barberName,Date startDate, Date endDate);
    Double calculateAllProfitByBarberNameAndDateRange(String barberName, Date startDate, Date endDate);
}
