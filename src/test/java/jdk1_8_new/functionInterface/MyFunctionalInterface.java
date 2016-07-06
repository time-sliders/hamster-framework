package jdk1_8_new.functionInterface;

/**
 * 函数式接口
 * 通过 ［ @FunctionalInterface ］ 标明一个接口是函数式接口
 */
@FunctionalInterface
public interface MyFunctionalInterface {

    /**
     * 函数式接口必须有一个默认的抽象方法。
     */
    String apply(int i);

    /**
     *  java.lang.Object的方法不会作为必须的抽象方法。
     */
    String toString();

    /**
     * default 关键字标明一个提供默认实现的方法
     */
    default String getName(){
        return "luyun";
    }

}
