package com.example.spring_6_mvc.services;

import com.example.spring_6_mvc.model.BeerCSVRecord;

import java.io.File;
import java.util.List;

public interface BeerCSVParser {
    List<BeerCSVRecord> parseCsv(File csvFile);
}
