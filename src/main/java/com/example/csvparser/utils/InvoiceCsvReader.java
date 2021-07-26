package com.example.csvparser.utils;

import com.example.csvparser.invoice.service.model.InvoiceCsvBean;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class InvoiceCsvReader {
    public static Set<String> getDistinctBuyers(MultipartFile file) {
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CsvToBean<InvoiceCsvBean> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(InvoiceCsvBean.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            Set<String> buyers = csvToBean.stream().map(b -> b.getBuyer()).distinct().collect(Collectors.toSet());
            return buyers;
        } catch (Exception ex) {
            log.error("Unable to read the input CSV file. Error message: " + ex.getMessage());
        }
        return Collections.EMPTY_SET;
    }
}
