package com.assignment.loophealth;

import com.assignment.loophealth.controller.ExcelJsonParserController;
import com.assignment.loophealth.service.ExcelToJsonService;
import com.assignment.loophealth.service.JsonToExcelService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

@ExtendWith(MockitoExtension.class)
class ExcelJsonParserControllerTest {

    @Mock
    private ExcelToJsonService excelToJsonService;

    @Mock
    private JsonToExcelService jsonToExcelService;

    @InjectMocks
    private ExcelJsonParserController controller;

    @Test
    void testExcelToJson_Success() throws IOException {
        MockMultipartFile mockFile = new MockMultipartFile(
                "file", "test.xlsx", MediaType.MULTIPART_FORM_DATA_VALUE, new byte[]{1, 2, 3});

        Map<String, List<Map<String, Object>>> mockJson = Collections.singletonMap("Sheet1", List.of(Map.of("key", "value")));

        when(excelToJsonService.convertExcelToJson(mockFile)).thenReturn(mockJson);

        ResponseEntity<?> response = controller.excelToJson(mockFile);

        assertEquals(OK, response.getStatusCode());
        assertEquals(mockJson, response.getBody());
    }

    @Test
    void testExcelToJson_Failure() throws IOException {
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.xlsx", MediaType.MULTIPART_FORM_DATA_VALUE, new byte[]{});

        when(excelToJsonService.convertExcelToJson(mockFile)).thenThrow(new IOException("Invalid file"));

        ResponseEntity<?> response = controller.excelToJson(mockFile);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Error processing file: Invalid file", response.getBody());
    }

    @Test
    void testJsonToExcel_Success() throws IOException {
        Map<String, List<Map<String, Object>>> mockJson = Collections.singletonMap("Sheet1", List.of(Map.of("key", "value")));
        byte[] mockExcelBytes = new byte[]{1, 2, 3};

        when(jsonToExcelService.convertJsonToExcel(mockJson)).thenReturn(mockExcelBytes);

        ResponseEntity<byte[]> response = controller.jsonToExcel(mockJson);

        assertEquals(OK, response.getStatusCode());
        assertArrayEquals(mockExcelBytes, response.getBody());
    }

    @Test
    void testJsonToExcel_Failure() throws IOException {
        Map<String, List<Map<String, Object>>> mockJson = Collections.emptyMap();

        when(jsonToExcelService.convertJsonToExcel(mockJson)).thenThrow(new IOException("Conversion failed"));

        ResponseEntity<byte[]> response = controller.jsonToExcel(mockJson);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }
}
