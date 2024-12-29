package redlib.backend.service.impl;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import redlib.backend.dao.LogMapper;
import redlib.backend.dto.query.LogQueryDTO;
import redlib.backend.model.Log;
import redlib.backend.model.MonthlyProfit;
import redlib.backend.model.Page;
import redlib.backend.service.LogService;
import redlib.backend.utils.FormatUtils;
import redlib.backend.utils.PageUtils;
import redlib.backend.utils.XlsUtils;
import redlib.backend.vo.LogVO;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class LogServicelmpl implements LogService {
    @Autowired
    private LogMapper logMapper;
    @Override
    public Page<LogVO> list(LogQueryDTO queryDTO) {
        Assert.notNull(queryDTO, "查询参数不能为空");
        FormatUtils.trimFieldToNull(queryDTO);
        queryDTO.setOrderBy(FormatUtils.formatOrderBy(queryDTO.getOrderBy()));

        Integer total = logMapper.count(queryDTO);
        PageUtils pageUtils = new PageUtils(queryDTO.getCurrent(), queryDTO.getPageSize(), total);
        if (pageUtils.isDataEmpty()) {
            return pageUtils.getNullPage();
        }

        List<Log> list = logMapper.list(queryDTO, pageUtils.getOffset(), pageUtils.getLimit());
        List<LogVO> voList = list.stream()
                .map(item -> {
                    LogVO vo = new LogVO();
                    BeanUtils.copyProperties(item, vo);
                    return vo;
                })
                .collect(Collectors.toList());
        return new Page<>(pageUtils.getCurrent(), pageUtils.getPageSize(), pageUtils.getTotal(), voList);
    }
    @Override
    public Workbook export(LogQueryDTO queryDTO) {
        queryDTO.setPageSize(100);
        Map<String, String> map = new LinkedHashMap<>();
        map.put("logId", "日志ID");
        map.put("memberId", "会员ID");
        map.put("changeFlag", "操作");
        map.put("serviceProjectId", "服务项目ID");
        map.put("projectName", "服务项目名称");
        map.put("changeAmount", "变动金额");
        map.put("remark", "备注");
        map.put("changeDate", "操作时间");

        final AtomicBoolean finalPage = new AtomicBoolean(false);
        Workbook workbook = XlsUtils.exportToExcel(page -> {
            if (finalPage.get()) {
                return null;
            }
            queryDTO.setCurrent(page);
            List<LogVO> list = list(queryDTO).getList();
            if (list.size() != 100) {
                finalPage.set(true);
            }
            return list;
        }, map);

        return workbook;
    }
    @Override
    public List<MonthlyProfit> calculateMonthlyProfitForYear(Integer year) {
        Assert.notNull(year, "年份不能为空");
        return logMapper.calculateMonthlyProfitByYear(year);
    }
    @Override
    public Integer yearProfit(Integer year){
        Assert.notNull(year, "年份不能为空");
        return logMapper.yearProfit(year);
    }
    @Override
    public List<Map<String, Object>> calculateDailyProfitBetweenDates(Date startDate, Date endDate) {
        Assert.notNull(startDate, "开始日期不能为空");
        Assert.notNull(endDate, "结束日期不能为空");
        return logMapper.calculateDailyProfitBetweenDates(startDate, endDate);
    }
    @Override
    public Double calculateTotalProfitBetweenDates(Date startDate, Date endDate) {
        Assert.notNull(startDate, "开始日期不能为空");
        Assert.notNull(endDate, "结束日期不能为空");
        return logMapper.calculateTotalProfitBetweenDates(startDate, endDate);
    }
    @Override
    public List<Map<String, Object>> calculateProfitByBarberNameAndDateRange(String barberName,Date startDate, Date endDate) {
        Assert.notNull(barberName, "理发师姓名不能为空");
        Assert.notNull(startDate, "开始日期不能为空");
        Assert.notNull(endDate, "结束日期不能为空");
        return logMapper.calculateProfitByBarberNameAndDateRange(barberName, startDate, endDate);
    }
    @Override
    public Double calculateAllProfitByBarberNameAndDateRange(String barberName, Date startDate, Date endDate) {
        Assert.notNull(barberName, "理发师姓名不能为空");
        Assert.notNull(startDate, "开始日期不能为空");
        Assert.notNull(endDate, "结束日期不能为空");
        return logMapper.calculateAllProfitByBarberNameAndDateRange(barberName, startDate, endDate);
    }
}
