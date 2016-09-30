package jdk1_8_new.functionInterface;

import java.util.function.Function;

public class FunctionTest {

    public static void main(String[] args) {

        /**
         * Function 接收一个参数返回另外一个参数
         */
        Function<String, String> f1 = (s) -> (s + "_suffix");
        System.out.println(f1.apply("name"));

        /**
         * andThen 在当前function apply()方法执行之 [后] 执行另外一个function的apply()方法
         */
        Function<String, String> f2 = f1.andThen((s) -> (s + "_suffix0000"));
        System.out.println(f2.apply("name"));

        /**
         * compose 在当前function apply()方法执行之 [前] 执行另外一个function的apply()方法
         */
        Function<String, String> f3 = f1.compose((s) -> (s + "_suffix0000"));
        System.out.println(f3.apply("name"));

    }

}
