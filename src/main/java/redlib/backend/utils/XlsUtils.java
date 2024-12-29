package redlib.backend.utils;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.beans.PropertyDescriptor;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @Description: Excel文件导入工具类
 * @Author: 李洪文
 * @Date: Created on 2019/1/11
 */
public class XlsUtils {
    public static final String EXCEL_TYPE_XLS = "xls";
    public static final String EXCEL_TYPE_XLSX = "xlsx";


    public static <T> Workbook exportToExcel(Function<Integer, List<T>> handler, Map<String, String> fieldMap) {
        try {
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFPalette palette = wb.getCustomPalette();
            palette.setColorAtIndex(IndexedColors.GREY_25_PERCENT.getIndex(), (byte) (0xE0), (byte) (0xE0), (byte) (0xE0));
            // 创建Excel的工作sheet,对应到一个excel文档的tab
            HSSFSheet sheet = wb.createSheet("data");
            for (int i = 0; i < fieldMap.size(); i++) {
                sheet.setColumnWidth(i, 3500);
            }

            // 创建字体样式
            HSSFFont fontHeader = wb.createFont();
            fontHeader.setFontName("黑体");
            fontHeader.setBold(true);
            fontHeader.setFontHeightInPoints((short) 11);

            // 创建Header单元格样式

            HSSFCellStyle hdStyle = wb.createCellStyle();
            hdStyle.setAlignment(HorizontalAlignment.CENTER);
            hdStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            hdStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            hdStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            hdStyle.setBorderBottom(BorderStyle.THIN);
            hdStyle.setBorderLeft(BorderStyle.THIN);
            hdStyle.setBorderRight(BorderStyle.THIN);
            hdStyle.setBorderTop(BorderStyle.THIN);
            hdStyle.setFont(fontHeader);

            //创建一般单元格格式
            HSSFCellStyle cellStyle = wb.createCellStyle();
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);
            cellStyle.setBorderTop(BorderStyle.THIN);
            HSSFFont fontCell = wb.createFont();
            fontCell.setFontName("宋体");
            fontCell.setBold(true);
            fontCell.setFontHeightInPoints((short) 9);
            cellStyle.setFont(fontCell);// 设置字体

            // 创建Excel的sheet的一行
            HSSFRow row = sheet.createRow(0);
            row.setHeight((short) 500);// 设定行的高度
            // 创建一个Excel的单元格
            int cellIndex = 0;
            for (Map.Entry<String, String> entry : fieldMap.entrySet()) {
                HSSFCell cell = row.createCell(cellIndex++);
                cell.setCellStyle(hdStyle);
                cell.setCellValue(entry.getValue());
            }

            int page = 1;
            int rowNumber = 1;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            while (true) {
                List<T> list = handler.apply(page++);
                if (CollectionUtils.isEmpty(list)) {
                    break;
                }

                for (int i = 0; i < list.size(); i++) {
                    T object = list.get(i);
                    row = sheet.createRow(rowNumber);
                    row.setHeight((short) 400);
                    cellIndex = 0;
                    for (Map.Entry<String, String> entry : fieldMap.entrySet()) {
                        HSSFCell cell = row.createCell(cellIndex++);
                        cell.setCellStyle(cellStyle);
                        cell.setCellType(CellType.STRING);
                        Method getter = new PropertyDescriptor(entry.getKey(), object.getClass()).getReadMethod();
                        Object o = getter.invoke(object);
                        if (o != null) {
                            if (o instanceof Date) {
                                cell.setCellValue(sdf.format(o));
                            } else {
                                cell.setCellValue(o.toString());
                            }
                        }
                    }
                    rowNumber++;
                }

            }
            return wb;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    public static <T> void importFromExcel(InputStream inputStream, String fileName, Consumer<T> handler, Map<String, String> fieldMap, Class<T> cls) throws Exception {
        Workbook workbook = null;
        String extension = "";

        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex >= 0) {
            extension = fileName.substring(dotIndex + 1);
        }

        if ("xls".equals(extension)) {
            workbook = new HSSFWorkbook(inputStream);
        } else if ("xlsx".equalsIgnoreCase(extension)) {
            workbook = new XSSFWorkbook(inputStream);
        } else {
            throw new RuntimeException("文件格式不正确，无法读取。");
        }

        DataFormatter dataFormatter = new DataFormatter();
        FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();

        //取得excel文件的第一个工作表
        Sheet sheet = workbook.getSheetAt(0);
        int firstRowNumber = sheet.getFirstRowNum();
        int lastRowNumber = sheet.getLastRowNum();
        Assert.isTrue(firstRowNumber >= 0 && lastRowNumber >= 0, "Excel文件内无数据可以读。");
        Row row = sheet.getRow(firstRowNumber);
        Map<Integer, String> fieldCellIndexMap = new HashMap<>();
        for (int i = row.getFirstCellNum(); i <= row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell == null) {
                continue;
            }

            String name = fieldMap.get(cell.toString());
            if (name != null) {
                fieldCellIndexMap.put(i, name);
            }
        }

        for (int i = firstRowNumber + 1; i <= lastRowNumber; i++) {
            row = sheet.getRow(i);
            if (row == null) {
                continue;
            }

            T object = cls.newInstance();
            for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
                Cell cell = row.getCell(j);
                if (cell == null) {
                    continue;
                }
                String fieldName = fieldCellIndexMap.get(j);
                if (fieldName == null) {
                    continue;
                }


                Method setter = new PropertyDescriptor(fieldName, object.getClass()).getWriteMethod();
                Class<?>[] paramTypes = setter.getParameterTypes();
                if (paramTypes.length != 1) {
                    continue;
                }

                String cellValue = dataFormatter.formatCellValue(cell, formulaEvaluator);
                if (paramTypes[0] == String.class) {
                    setter.invoke(object, cellValue);
                } else if (paramTypes[0] == Integer.class) {
                    setter.invoke(object, Integer.parseInt(cellValue));
                } else if (paramTypes[0] == Date.class) {
                    setter.invoke(object, cellValue);
                }
            }

            handler.accept(object);
        }
    }
}
