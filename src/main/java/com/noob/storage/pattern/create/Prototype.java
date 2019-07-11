package com.noob.storage.pattern.create;

public class Prototype implements Cloneable {

    private String name = "zhw";

    public static void main(String[] args) throws CloneNotSupportedException {
        Prototype p = new Prototype();
        Prototype c = (Prototype) p.clone();
        System.out.println(c.name + " " + (c == p));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
