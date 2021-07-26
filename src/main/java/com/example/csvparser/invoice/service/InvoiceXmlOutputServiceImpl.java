package com.example.csvparser.invoice.service;

import com.example.csvparser.invoice.service.model.InvoiceCsvBean;
import com.example.csvparser.invoice.service.model.InvoiceXmlBean;
import com.example.csvparser.invoice.service.model.InvoiceXmlBeanList;
import com.example.csvparser.utils.InvoiceCsvReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import java.io.*;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Slf4j
public class InvoiceXmlOutputServiceImpl implements InvoiceXmlOutputService {
    public static final String XML_FILE_EXTENSION = ".xml";

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void downloadInvoicesXmlOutput(HttpServletResponse httpServletResponse, MultipartFile file) {
        try (ZipOutputStream zos = new ZipOutputStream(httpServletResponse.getOutputStream())) {
            Set<String> buyers = InvoiceCsvReader.getDistinctBuyers(file);
            for (String buyer : buyers) {
                try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                    CsvToBean<InvoiceCsvBean> csvToBean = new CsvToBeanBuilder(reader)
                            .withType(InvoiceCsvBean.class)
                            .withIgnoreLeadingWhiteSpace(true)
                            .withFilter(b -> b[0].equals(buyer))
                            .build();

                    List<InvoiceCsvBean> csvBeanList = csvToBean.parse();
                    List<InvoiceXmlBean> xmlBeanList = csvBeanList.stream().map(b -> modelMapper.map(b, InvoiceXmlBean.class)).collect(Collectors.toList());

                    csvBeanList.stream().forEach(b -> convertBase64EncodedStringToImage(b.getInvoiceImage(), b.getImageName()));

                    String filename = buyer + XML_FILE_EXTENSION;
                    ZipEntry entry = new ZipEntry(filename);
                    zos.putNextEntry(entry);

                    try {
                        JAXBContext jaxbContext = JAXBContext.newInstance(InvoiceXmlBeanList.class);
                        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
                        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

                        XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(
                                new OutputStreamWriter(zos));
                        jaxbMarshaller.marshal(new InvoiceXmlBeanList(xmlBeanList), writer);
                    } catch (JAXBException e) {
                        log.error("Unable to write XML beans to response output stream. Error message: " + e.getMessage());
                    }
                    zos.closeEntry();
                } catch (Exception ex) {
                    log.error("Unable to read the input CSV file. Error message: " + ex.getMessage());
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void convertBase64EncodedStringToImage(String base64EncodedString, String imageName) {
        if (base64EncodedString == null || base64EncodedString.isEmpty() ||
                imageName == null || imageName.isEmpty()) {
            return;
        }

        try {
            String fileName = "src/main/resources/images/" + imageName;
            byte[] decodedBytes = Base64.getDecoder().decode(base64EncodedString);
            FileUtils.writeByteArrayToFile(new File(fileName), decodedBytes);
        } catch (IOException e) {
            log.error("Unable to convert Base64 string to image: " + imageName + ". Error message: " + e.getMessage());
        }
    }
}
