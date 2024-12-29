package redlib.backend.dao;

import redlib.backend.dto.query.LogQueryDTO;
import redlib.backend.model.Log;
import org.apache.ibatis.annotations.Param;
import redlib.backend.model.MonthlyProfit;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface LogMapper {
    Log findLogById(Integer logId); // 通过ID查找日志
    int deleteLogById(Integer logId); // 通过ID删除日志
    int insertSelective(Log record);

    Log findLastFinancialChange(); //用于一键撤销

    Integer count(@Param("queryDTO") LogQueryDTO queryDTO);

    List<Log> list(
            @Param("queryDTO") LogQueryDTO queryDTO,
            @Param("offset") Integer offset,
            @Param("limit") Integer limit);
    List<MonthlyProfit> calculateMonthlyProfitByYear(@Param("year") Integer year);

    Integer yearProfit(@Param("year") Integer year);
    List<Map<String, Object>> calculateDailyProfitBetweenDates(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);

    Double calculateTotalProfitBetweenDates(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);

    Double calculateAllProfitByBarberNameAndDateRange(
            @Param("barberName") String barberName,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);
    List<Map<String, Object>> calculateProfitByBarberNameAndDateRange(
            @Param("barberName") String barberName,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);
}



