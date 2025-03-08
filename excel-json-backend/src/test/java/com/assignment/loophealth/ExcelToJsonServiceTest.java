package com.assignment.loophealth;

import com.assignment.loophealth.service.ExcelToJsonService;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ExcelToJsonServiceTest {

    private final ExcelToJsonService service = new ExcelToJsonService();

    @Test
    void testConvertExcelToJson_Success() throws IOException {
        byte[] excelData = createMockExcelFile();
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", excelData);

        Map<String, List<Map<String, Object>>> jsonData = service.convertExcelToJson(mockFile);

        assertNotNull(jsonData);
        assertFalse(jsonData.isEmpty());
        assertTrue(jsonData.containsKey("Sheet1"));
    }

    private byte[] createMockExcelFile() throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.createSheet("Sheet1").createRow(0).createCell(0).setCellValue("Test");
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }
}
