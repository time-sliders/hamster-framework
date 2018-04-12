package com.noob.storage.model.test;

import java.io.Serializable;

/**
 * @author LuYun
 * @since 2018.04.12
 */
public class ComplexModel implements Serializable {
    private static final long serialVersionUID = 1826649879857470413L;

    private String s1 = "s1";

    private transient String s2 = "s2";

    private static String s3 = "s3";

    private Integer i0 = 10;

    private int i1 = 11;

    private String[] sa = new String[3];

    private TestEnum e = TestEnum.E1;

    public String getS1() {
        return s1;
    }

    public void setS1(String s1) {
        this.s1 = s1;
    }

    public String getS2() {
        return s2;
    }

    public void setS2(String s2) {
        this.s2 = s2;
    }

    public static String getS3() {
        return s3;
    }

    public static void setS3(String s3) {
        ComplexModel.s3 = s3;
    }

    public Integer getI0() {
        return i0;
    }

    public void setI0(Integer i0) {
        this.i0 = i0;
    }

    public int getI1() {
        return i1;
    }

    public void setI1(int i1) {
        this.i1 = i1;
    }

    public String[] getSa() {
        return sa;
    }

    public void setSa(String[] sa) {
        this.sa = sa;
    }

    public TestEnum getE() {
        return e;
    }

    public void setE(TestEnum e) {
        this.e = e;
    }
}
