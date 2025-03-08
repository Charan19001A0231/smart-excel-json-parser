package com.assignment.loophealth.controller;

import com.assignment.loophealth.service.ExcelToJsonService;
import com.assignment.loophealth.service.JsonToExcelService;
import com.fasterxml.jackson.core.type.TypeReference;
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

    public ExcelJsonParserController(ExcelToJsonService excelToJsonService, JsonToExcelService jsonToExcelService) {
        this.excelToJsonService = excelToJsonService;
        this.jsonToExcelService = jsonToExcelService;
        this.objectMapper = new ObjectMapper();
    }

    // ✅ Convert Excel to JSON (No changes)
    @PostMapping(value = "/excel-to-json", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> excelToJson(@RequestParam("file") MultipartFile file) {
        try {
            Map<String, List<Map<String, Object>>> jsonData = excelToJsonService.convertExcelToJson(file);
            return ResponseEntity.ok(jsonData);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error processing file: " + e.getMessage());
        }
    }

    // ✅ Convert JSON to Excel (Simplified: Only JSON body)
    @PostMapping(value = "/json-to-excel", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> jsonToExcel(@RequestBody Map<String, List<Map<String, Object>>> jsonData) {
        try {
            byte[] excelBytes = jsonToExcelService.convertJsonToExcel(jsonData);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "converted.xlsx");


            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelBytes);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
