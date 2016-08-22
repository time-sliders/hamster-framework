package jdk1_8_new.functionInterface;

/**
 * 函数式接口
 * 通过 ［ @FunctionalInterface ］ 标明一个接口是函数式接口
 * 加了这个注解,编译器就会验证接口是否符合函数式接口规范
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

    //String execute(Object param);

}
