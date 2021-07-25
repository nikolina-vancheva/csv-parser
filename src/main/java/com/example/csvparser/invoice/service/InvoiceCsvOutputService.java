package com.example.csvparser.invoice.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface InvoiceCsvOutputService {
    void downloadInvoicesCsvOutput(HttpServletResponse httpServletResponse, MultipartFile file);
}
