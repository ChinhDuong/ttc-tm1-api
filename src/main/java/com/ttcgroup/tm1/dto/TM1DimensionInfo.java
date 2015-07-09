package com.ttcgroup.tm1.dto;

import java.util.List;

/**
 * Created by Administrator on 7/9/2015.
 */
public class TM1DimensionInfo {
    private String name;
    private List<String> element;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getElement() {
        return element;
    }

    public void setElement(List<String> element) {
        this.element = element;
    }
}
