package com.ttcgroup.tm1.dto;

import java.util.List;

/**
 * Created by Administrator on 7/9/2015.
 */
public class TM1CubeInfo {
    private String name;
    private List<String> dimension;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getDimension() {
        return dimension;
    }

    public void setDimension(List<String> dimension) {
        this.dimension = dimension;
    }
}
