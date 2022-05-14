package boxgym.helper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelFileHelper {
    public static XSSFCellStyle excelStyle(XSSFWorkbook workbook, String fontName, boolean bold, BorderStyle border, byte[] rgb) {
        XSSFFont font = workbook.createFont();
        font.setFontName(fontName);
        font.setBold(bold);
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setBorderTop(border);
        style.setBorderBottom(border);
        style.setBorderLeft(border);
        style.setBorderRight(border);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        style.setFillForegroundColor(new XSSFColor(rgb, new DefaultIndexedColorMap()));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    public static XSSFCell createStyledCell(XSSFRow row, int cellIndex, String cellValue, XSSFCellStyle style) {
        XSSFCell cell = row.createCell(cellIndex);
        cell.setCellValue(cellValue);
        cell.setCellStyle(style);
        return cell;
    }

    public static XSSFCell createStyledCell(XSSFRow row, int cellIndex, Integer cellValue, XSSFCellStyle style) {
        XSSFCell cell = row.createCell(cellIndex);
        cell.setCellValue(cellValue);
        cell.setCellStyle(style);
        return cell;
    }
    
    public static XSSFCell createStyledCell(XSSFRow row, int cellIndex, Double cellValue, XSSFCellStyle style) {
        XSSFCell cell = row.createCell(cellIndex);
        cell.setCellValue(cellValue);
        cell.setCellStyle(style);
        return cell;
    }
    
    public static XSSFCell createStyledDateCell(XSSFRow row, int cellIndex, String cellValue, DateTimeFormatter dateFormat, XSSFCellStyle style) {
        LocalDate parsed = LocalDate.parse(cellValue, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        XSSFCell cell = row.createCell(cellIndex);
        cell.setCellValue(parsed.format(dateFormat));
        cell.setCellStyle(style);
        return cell;
    }
    
    public static XSSFCell createStyledDateTimeCell(XSSFRow row, int cellIndex, String cellValue, DateTimeFormatter dateTimeFormat, XSSFCellStyle style) {
        String subStr = cellValue.substring(0, cellValue.length() - 2); // Remover o ".0" do final da hora
        LocalDateTime parsed = LocalDateTime.parse(subStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        XSSFCell cell = row.createCell(cellIndex);
        cell.setCellValue(parsed.format(dateTimeFormat));
        cell.setCellStyle(style);
        return cell;
    }
}
