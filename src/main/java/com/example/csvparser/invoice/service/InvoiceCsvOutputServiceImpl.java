package com.example.csvparser.invoice.service;

import com.example.csvparser.invoice.service.model.InvoiceCsvBean;
import com.example.csvparser.utils.InvoiceCsvReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Slf4j
public class InvoiceCsvOutputServiceImpl implements InvoiceCsvOutputService {

    public static final String CSV_FILE_EXTENSION = ".csv";

    @Override
    public void downloadInvoicesCsvOutput(HttpServletResponse httpServletResponse, MultipartFile file) {
        try (ZipOutputStream zos = new ZipOutputStream(httpServletResponse.getOutputStream())) {
            Set<String> buyers = InvoiceCsvReader.getDistinctBuyers(file);
            for (String buyer : buyers) {
                try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                    CsvToBean<InvoiceCsvBean> csvToBean = new CsvToBeanBuilder(reader)
                            .withType(InvoiceCsvBean.class)
                            .withIgnoreLeadingWhiteSpace(true)
                            .withFilter(b -> b[0].equals(buyer))
                            .build();

                    String filename = buyer + CSV_FILE_EXTENSION;
                    ZipEntry entry = new ZipEntry(filename);
                    zos.putNextEntry(entry);

                    CSVWriter writer = new CSVWriter(new OutputStreamWriter(zos),
                            CSVWriter.DEFAULT_SEPARATOR,
                            CSVWriter.NO_QUOTE_CHARACTER,
                            CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                            CSVWriter.RFC4180_LINE_END);
                    StatefulBeanToCsv<InvoiceCsvBean> beanToCsv = new StatefulBeanToCsvBuilder<InvoiceCsvBean>(writer)
                            .build();
                    beanToCsv.write(csvToBean.stream());
                    writer.flush();
                    zos.closeEntry();
                } catch (Exception ex) {
                    log.error("Unable to read the input CSV file. Error message: " + ex.getMessage());
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
