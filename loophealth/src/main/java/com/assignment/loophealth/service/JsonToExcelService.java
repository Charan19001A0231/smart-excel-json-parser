package com.assignment.loophealth.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.*;

@Service
public class JsonToExcelService {

    public byte[] convertJsonToExcel(Map<String, List<Map<String, Object>>> jsonData) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            for (String sheetName : jsonData.keySet()) {
                List<Map<String, Object>> data = jsonData.get(sheetName);
                Sheet sheet = workbook.createSheet(sheetName);
                if (!data.isEmpty()) {
                    createSheetWithData(sheet, data);
                }
            }
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    private void createSheetWithData(Sheet sheet, List<Map<String, Object>> data) {
        List<String> headers = new ArrayList<>(data.get(0).keySet());
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.size(); i++) {
            headerRow.createCell(i).setCellValue(headers.get(i));
        }
        for (int rowIndex = 1; rowIndex <= data.size(); rowIndex++) {
            Row row = sheet.createRow(rowIndex);
            Map<String, Object> rowData = data.get(rowIndex - 1);
            for (int colIndex = 0; colIndex < headers.size(); colIndex++) {
                Object value = rowData.get(headers.get(colIndex));
                if (value != null) row.createCell(colIndex).setCellValue(value.toString());
            }
        }
    }
}