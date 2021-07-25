package com.example.csvparser.invoice.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "invoice")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceXmlBean {
    @XmlElement(name = "buyer")
    private String buyer;

    @XmlElement(name = "image_name")
    private String imageName;

    @XmlElement(name = "invoice_due_date")
    private String invoiceDueDate;

    @XmlElement(name = "invoice_number")
    private String invoiceNumber;

    @XmlElement(name = "invoice_amount")
    private String invoiceAmount;

    @XmlElement(name = "invoice_currency")
    private String invoiceCurrency;

    @XmlElement(name = "invoice_status")
    private String invoiceStatus;

    @XmlElement(name = "supplier")
    private String supplier;

}
