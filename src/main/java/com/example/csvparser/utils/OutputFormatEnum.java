package com.example.csvparser.utils;

public enum OutputFormatEnum {
    CSV("csv"),
    XML("xml");

    public final String outputFormat;

    private OutputFormatEnum(String outputFormat) {
        this.outputFormat = outputFormat;
    }
}
