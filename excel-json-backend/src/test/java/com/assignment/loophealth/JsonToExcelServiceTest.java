package com.assignment.loophealth;

import com.assignment.loophealth.service.JsonToExcelService;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JsonToExcelServiceTest {

    private final JsonToExcelService service = new JsonToExcelService();

    @Test
    void testConvertJsonToExcel_Success() throws IOException {
        Map<String, List<Map<String, Object>>> jsonData = Collections.singletonMap("Sheet1", List.of(Map.of("Name", "Alice", "Age", 25)));

        byte[] excelBytes = service.convertJsonToExcel(jsonData);

        assertNotNull(excelBytes);
        assertTrue(excelBytes.length > 0);

        Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(excelBytes));
        assertEquals(1, workbook.getNumberOfSheets());
        assertEquals("Sheet1", workbook.getSheetAt(0).getSheetName());
    }
}
