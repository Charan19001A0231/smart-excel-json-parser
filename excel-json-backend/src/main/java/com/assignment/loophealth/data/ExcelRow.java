package com.assignment.loophealth.data;

import lombok.Data;

import java.util.Map;

@Data
public class ExcelRow {
    private Map<String, String> data;
}
