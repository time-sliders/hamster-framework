package jdk1_8_new.functionInterface;

import java.util.HashMap;
import java.util.Map;

/**
 * 函数式接口
 * 通过 ［ @FunctionalInterface ］ 标明一个接口是函数式接口
 */
@FunctionalInterface
public interface NewInterface {

    Map<String,String> map = new HashMap<String,String>();

    /**
     * 函数式接口必须有一个默认的抽象方法。
     */
    void execute(String a);

    /**
     * overwrite java.lang.Object的方法不会作为必须的抽象方法。
     */
    String toString();

    /**
     * default 关键字标明一个提供默认实现的方法
     */
    default String getName(){
        return "luyun";
    }

}
