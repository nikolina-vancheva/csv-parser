package com.example.csvparser.invoice.controller;


import com.example.csvparser.invoice.service.InvoiceCsvOutputService;
import com.example.csvparser.invoice.service.InvoiceXmlOutputService;
import com.example.csvparser.utils.OutputFormatEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@RestController
@RequestMapping("/invoices")
@Slf4j
public class InvoiceController {

    @Autowired
    private InvoiceCsvOutputService invoiceCsvOutputService;
    @Autowired
    private InvoiceXmlOutputService invoiceXmlOutputService;

    @PostMapping(path = "/csv/upload")
    public void produceInvoicesOutput(HttpServletResponse httpServletResponse, @RequestParam("file") MultipartFile file,
                                      @RequestParam String outputFormat) {
        if (file.isEmpty()) {
            log.error("The uploaded file is empty.");
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (!isOutputFormatValid(outputFormat)) {
            log.error("Invalid output format.");
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (OutputFormatEnum.CSV.outputFormat.equals(outputFormat)) {
            httpServletResponse.setHeader("Content-Disposition", "attachment;filename=invoicesCsvOutput.zip");
            invoiceCsvOutputService.downloadInvoicesCsvOutput(httpServletResponse, file);
        } else if (OutputFormatEnum.XML.outputFormat.equals(outputFormat)) {
            httpServletResponse.setHeader("Content-Disposition", "attachment;filename=invoicesXmlOutput.zip");
            invoiceXmlOutputService.downloadInvoicesXmlOutput(httpServletResponse, file);
        }

        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
    }

    private boolean isOutputFormatValid(String outputFormat) {
        return Arrays.stream(OutputFormatEnum.values()).anyMatch(e -> e.outputFormat.equals(outputFormat));
    }
}
