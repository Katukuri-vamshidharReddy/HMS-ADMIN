package com.example.hmsadmin;

public class AllBedroom {
    private String data0;

    private String data1;

    private String data2;

    private String data3;

    public AllBedroom(String data0, String data1, String data2, String data3) {
        this.data0 = data0;
        this.data1 = data1;
        this.data2 = data2;
        this.data3 = data3;
    }

    public String getData0() {
        return data0;
    }

    public void setData0(String data0) {
        this.data0 = data0;
    }

    public String getData1() {
        return data1;
    }

    public void setData1(String data1) {
        this.data1 = data1;
    }

    public String getData2() {
        return data2;
    }

    public void setData2(String data2) {
        this.data2 = data2;
    }

    public String getData3() {
        return data3;
    }

    public void setData3(String data3) {
        this.data3 = data3;
    }

    @Override
    public String toString() {

        return data2;

    }


}

