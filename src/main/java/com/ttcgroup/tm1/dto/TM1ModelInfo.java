package com.ttcgroup.tm1.dto;

import java.util.List;

/**
 * Created by Administrator on 7/9/2015.
 */
public class TM1ModelInfo {
    private List<String> user;
    private List<String> process;
    private List<TM1DimensionInfo> dimension;
    private List<TM1CubeInfo> cube;
    private List<String> group;


    public List<String> getUser() {
        return user;
    }

    public void setUser(List<String> user) {
        this.user = user;
    }

    public List<String> getProcess() {
        return process;
    }

    public void setProcess(List<String> process) {
        this.process = process;
    }

    public List<String> getGroup() {
        return group;
    }

    public void setGroup(List<String> group) {
        this.group = group;
    }

    public List<TM1DimensionInfo> getDimension() {
        return dimension;
    }

    public void setDimension(List<TM1DimensionInfo> dimension) {
        this.dimension = dimension;
    }

    public List<TM1CubeInfo> getCube() {
        return cube;
    }

    public void setCube(List<TM1CubeInfo> cube) {
        this.cube = cube;
    }
}
