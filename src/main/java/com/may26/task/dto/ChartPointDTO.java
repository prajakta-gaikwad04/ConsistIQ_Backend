package com.may26.task.dto;

public class ChartPointDTO {

    private String label;   
    private long value;     

    public ChartPointDTO(String label, long value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public long getValue() {
        return value;
    }
}