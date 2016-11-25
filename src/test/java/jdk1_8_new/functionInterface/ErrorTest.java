package jdk1_8_new.functionInterface;

import java.util.function.Predicate;

/**
 *
 */
public class ErrorTest {

    public static void main(String[] args) {



    }

    @FunctionalInterface
    interface NewPredicate<T>{
        boolean assertNotNull(T t);
    }

}
