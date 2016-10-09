package jdk1_8_new.functionInterface;

import java.util.function.Predicate;

/**
 *
 */
public class ErrorTest {

    public static void main(String[] args) {

        Predicate<String>             p  =    s -> s != null;
        NewPredicate<String>          p2 =    s -> s != null;

        p.test("Java");
        p2.assertNotNull("Java");

//        (s -> s != null).test("Javas");
//        ((String s) -> s != null).test("Java");

    }

    @FunctionalInterface
    interface NewPredicate<T>{
        boolean assertNotNull(T t);
    }

}
