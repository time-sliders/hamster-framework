package com.noob.storage.proxy.javassist;

public class MyJavassist {

    private String name;

    public void say() {
        System.out.println("MyJavassist say hello");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
