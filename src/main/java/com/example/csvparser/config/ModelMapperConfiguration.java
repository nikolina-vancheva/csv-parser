package com.example.csvparser.config;

import com.example.csvparser.invoice.service.model.InvoiceCsvBean;
import com.example.csvparser.invoice.service.model.InvoiceXmlBean;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        modelMapper.createTypeMap(InvoiceCsvBean.class, InvoiceXmlBean.class);

        return modelMapper;
    }
}