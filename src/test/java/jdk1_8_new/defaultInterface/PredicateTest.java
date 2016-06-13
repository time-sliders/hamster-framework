package jdk1_8_new.defaultInterface;

import java.util.function.Predicate;

public class PredicateTest {

    private static final String NAME = "zhw";

    public static void main(String[] args) {

        /*********************Predicate*****************************/

        Predicate<String> predicate = (s) -> s.length() > 0;

        System.out.println(predicate.test(NAME));               // true
        System.out.println(predicate.negate().test(NAME));      // false

        Predicate<String> isEmpty = String::isEmpty;
        System.out.println(isEmpty.test(NAME));                 // false

        Predicate<String> isNotEmpty = isEmpty.negate();
        System.out.println(isNotEmpty.test(NAME));              // true
    }

}
