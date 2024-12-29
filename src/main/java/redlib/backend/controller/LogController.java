package redlib.backend.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import redlib.backend.annotation.BackendModule;
import redlib.backend.annotation.Privilege;
import redlib.backend.dto.query.LogQueryDTO;
import redlib.backend.model.MonthlyProfit;
import redlib.backend.model.Page;
import redlib.backend.service.LogService;
import redlib.backend.vo.LogVO;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/log")
@BackendModule({"page:页面"})
public class LogController {
    @Autowired
    private LogService logService;

    @PostMapping("listLog")
    @Privilege("page")
    public Page<LogVO> listLog(@RequestBody LogQueryDTO queryDTO) {
        return logService.list(queryDTO);
    }

    @PostMapping("exportLog")
    @Privilege("page")
    public void exportLog(@RequestBody LogQueryDTO queryDTO, HttpServletResponse response) throws Exception {
        Workbook workbook = logService.export(queryDTO);
        response.setContentType("application/vnd.ms-excel");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd$HHmmss");
        response.addHeader("Content-Disposition", "attachment;filename=file" + sdf.format(new Date()) + ".xls");
        OutputStream os = response.getOutputStream();
        workbook.write(os);
        os.close();
        workbook.close();
    }

    @GetMapping("/calculateMonthlyProfitForYear")
    public List<MonthlyProfit> calculateMonthlyProfitForYear(@RequestParam("year") Integer year) {
        return logService.calculateMonthlyProfitForYear(year);
    }

    @GetMapping("/yearProfit")
    public Integer yearProfit(@RequestParam("year") Integer year) {
        return logService.yearProfit(year);
    }

    /**
     * 根据起止日期查询每天的利润
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 每天的利润列表
     */
    @GetMapping("/calculateDailyProfitBetweenDates")
    public ResponseEntity<List<Map<String, Object>>> calculateDailyProfitBetweenDates(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        List<Map<String, Object>> dailyProfits = logService.calculateDailyProfitBetweenDates(startDate, endDate);
        return ResponseEntity.ok(dailyProfits);
    }

    @GetMapping("/calculateTotalProfitBetweenDates")
    public ResponseEntity<Double> calculateTotalProfitBetweenDates(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        Double totalProfit = logService.calculateTotalProfitBetweenDates(startDate, endDate);
        return ResponseEntity.ok(totalProfit);
    }
    @GetMapping("/calculateDailyBarberProfit")
    public ResponseEntity<List<Map<String, Object>>> calculateDailyBarberProfit(
            @RequestParam("barberName") String barberName,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        List<Map<String, Object>> dailyProfits = logService.calculateProfitByBarberNameAndDateRange(barberName, startDate, endDate);
        return ResponseEntity.ok(dailyProfits);
    }
    /**
     * 根据理发师名字和起止日期查询利润
     * @param barberName 理发师姓名
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 根据理发师名字和日期范围计算的利润
     */
    @GetMapping("/calculateProfitByBarberNameAndDateRange")
    public ResponseEntity<Double> calculateProfitByBarberNameAndDateRange(
            @RequestParam("barberName") String barberName,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        Double profit = logService.calculateAllProfitByBarberNameAndDateRange(barberName, startDate, endDate);
        return ResponseEntity.ok(profit);
    }
}
