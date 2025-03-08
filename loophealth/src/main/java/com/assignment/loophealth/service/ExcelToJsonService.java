package com.assignment.loophealth.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class ExcelToJsonService {

    public Map<String, List<Map<String, Object>>> convertExcelToJson(MultipartFile file) throws IOException {
        Map<String, List<Map<String, Object>>> sheetDataMap = new LinkedHashMap<>();

        try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {
            for (Sheet sheet : workbook) {
                List<Map<String, Object>> dataList = new ArrayList<>();
                Iterator<Row> rowIterator = sheet.iterator();
                List<String> headers = new ArrayList<>();

                if (rowIterator.hasNext()) {
                    Row headerRow = rowIterator.next();
                    for (Cell cell : headerRow) {
                        headers.add(getCellValueAsString(cell));
                    }
                }

                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    Map<String, Object> rowData = new LinkedHashMap<>();
                    for (int i = 0; i < headers.size(); i++) {
                        Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        rowData.put(headers.get(i), getCellValue(cell));
                    }
                    dataList.add(rowData);
                }

                // 🔹 Add data of this sheet to the map
                sheetDataMap.put(sheet.getSheetName(), dataList);
            }
        }
        return sheetDataMap;
    }

    private Object getCellValue(Cell cell) {
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> cell.getNumericCellValue();
            case BOOLEAN -> cell.getBooleanCellValue();
            case FORMULA -> cell.getCellFormula();
            case BLANK, ERROR -> "";
            default -> throw new IllegalStateException("Unexpected cell type: " + cell.getCellType());
        };
    }

    private String getCellValueAsString(Cell cell) {
        if (cell.getCellType() == CellType.STRING) return cell.getStringCellValue();
        if (cell.getCellType() == CellType.NUMERIC) return String.valueOf(cell.getNumericCellValue());
        return "";
    }
}
