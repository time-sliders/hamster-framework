package jdk1_8_new.defaultinterface;

/**
 * 如果子类继承的 多个接口 对一个完全相同的方法 提供了不同的default实现。
 * 那么子类必须重新定义一遍
 */
public class TestDefaultInterface implements AInterface, CInterface {
    @Override
    public String getName() {
        return "JAVA" + "C++";
    }
}

interface AInterface {
    default String getName() {
        return "Java";
    }
}

interface CInterface {
    default String getName() {
        return "C++";
    }
}
