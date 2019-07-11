package com.noob.storage.pattern.create;

class Car {
    int engine;
    int wheel;
    int body;

    @Override
    public String toString() {
        return "Car{" +
                "engine=" + engine +
                ", wheel=" + wheel +
                ", body=" + body +
                '}';
    }
}


public class Builder {

    private Car car;

    public Builder() {
        car = new Car();
    }

    public Builder setEngine(int i) {
        car.engine = i;
        return this;
    }

    public Builder setWheel(int i) {
        car.wheel = i;
        return this;
    }

    public Builder setBody(int i) {
        car.body = i;
        return this;
    }

    public Car build() {
        return car;
    }

    public static void main(String[] args) {
        Car c = new Builder().setBody(1).setEngine(2).setWheel(4).build();
        System.out.println(c.toString());
    }
}


