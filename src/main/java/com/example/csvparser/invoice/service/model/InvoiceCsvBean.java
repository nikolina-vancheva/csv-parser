package com.example.csvparser.invoice.service.model;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceCsvBean {
    @CsvBindByName(column = "buyer")
    private String buyer;

    @CsvBindByName(column = "image_name")
    private String imageName;

    @CsvBindByName(column = "invoice_image")
    private String invoiceImage;

    @CsvBindByName(column = "invoice_due_date")
    private String invoiceDueDate;

    @CsvBindByName(column = "invoice_number")
    private String invoiceNumber;

    @CsvBindByName(column = "invoice_amount")
    private String invoiceAmount;

    @CsvBindByName(column = "invoice_currency")
    private String invoiceCurrency;

    @CsvBindByName(column = "invoice_status")
    private String invoiceStatus;

    @CsvBindByName(column = "supplier")
    private String supplier;
}
