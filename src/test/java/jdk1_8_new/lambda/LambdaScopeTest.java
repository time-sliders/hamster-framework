package jdk1_8_new.lambda;

import org.junit.Test;

import java.util.function.Consumer;

public class LambdaScopeTest {

    public static final String sfStr = "staticString";

    private String propertyString = "propertyString";

    @Test
    public void testLambdaScope() {

        final String methodStr = "methodStr";

        Consumer<String> cs1 = s -> System.out.println(this.getClass() + ":" + sfStr + "_" + propertyString + "_" + methodStr + "_" + s);
        cs1.accept("Java");

        Consumer<String> cs2 = new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println(this.getClass() + ":" + sfStr + "_" + propertyString + "_" + methodStr + "_" + s);
            }
        };
        cs2.accept("java");


        //class jdk1_8_new.lambda.LambdaScopeTest:staticString_propertyString_methodStr_Java
        //class jdk1_8_new.lambda.LambdaScopeTest$1:staticString_propertyString_methodStr_java
        /**
         * lambda 表达式内部this
         */
    }

}
