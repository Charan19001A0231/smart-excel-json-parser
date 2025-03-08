package com.assignment.loophealth.controller;

import com.assignment.loophealth.service.ExcelToJsonService;
import com.assignment.loophealth.service.JsonToExcelService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ExcelJsonParserController {

    private final ExcelToJsonService excelToJsonService;
    private final JsonToExcelService jsonToExcelService;
    private final ObjectMapper objectMapper;

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    public ExcelJsonParserController(ExcelToJsonService excelToJsonService, JsonToExcelService jsonToExcelService) {
        this.excelToJsonService = excelToJsonService;
        this.jsonToExcelService = jsonToExcelService;
        this.objectMapper = new ObjectMapper();
    }

    // ✅ Convert Excel to JSON with File Type & Size Validation
    @PostMapping(value = "/excel-to-json", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> excelToJson(@RequestParam("file") MultipartFile file) {
        try {
            // 🔹 Validate File Type
            if (!isValidExcelFile(file)) {
                return ResponseEntity.badRequest().body("Invalid file type. Please upload an .xlsx file.");
            }

            // 🔹 Validate File Size
            if (file.getSize() > MAX_FILE_SIZE) {
                return ResponseEntity.badRequest().body("File size exceeds the 5MB limit.");
            }

            Map<String, List<Map<String, Object>>> jsonData = excelToJsonService.convertExcelToJson(file);
            return ResponseEntity.ok(jsonData);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error processing file: " + e.getMessage());
        }
    }

    @PostMapping(value = "/json-to-excel", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> jsonToExcel(@RequestBody Map<String, Object> jsonData) {
        try {
            if (!isValidJsonStructure(jsonData)) {
                return ResponseEntity.badRequest().body(null);
            }

            // ✅ Safe Type Casting
            @SuppressWarnings("unchecked")
            Map<String, List<Map<String, Object>>> parsedJsonData = (Map<String, List<Map<String, Object>>>) (Map<?, ?>) jsonData;

            byte[] excelBytes = jsonToExcelService.convertJsonToExcel(parsedJsonData);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "converted.xlsx");

            return ResponseEntity.ok().headers(headers).body(excelBytes);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


    // ✅ JSON Structure Validation
    private boolean isValidJsonStructure(Map<String, Object> jsonData) {
        if (jsonData == null || jsonData.isEmpty()) {
            return false;
        }
        for (Object value : jsonData.values()) {
            if (!(value instanceof List)) {
                return false;
            }
            List<?> dataList = (List<?>) value;
            for (Object entry : dataList) {
                if (!(entry instanceof Map)) {
                    return false;
                }
            }
        }
        return true;
    }

    // ✅ File Type Validation: Only Accepts .xlsx
    private boolean isValidExcelFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }
}
