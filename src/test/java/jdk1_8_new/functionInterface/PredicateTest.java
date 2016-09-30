package jdk1_8_new.functionInterface;

import java.util.function.*;

public class PredicateTest {

    private static final String NAME = "zhw";

    public static void main(String[] args) {
        /*********************Predicate*****************************/
        Predicate<String> predicate = (s) -> s != null && s.length() > 0;

        System.out.println(predicate.test(NAME));               // true
        System.out.println(predicate.negate().test(NAME));      // false

        Predicate<String> allPredicate = predicate.or(predicate.negate());
        System.out.println(allPredicate.test(null));
        System.out.println(allPredicate.test(NAME));

        IntPredicate intPredicate = (a) -> (a > 3);
        //DoublePredicate;
        //LongPredicate;
        System.out.println("intPredicate>>>" + intPredicate.test(10));

        Predicate<String> isEmpty = String::isEmpty;
        System.out.println(isEmpty.test(NAME));                 // false

        Predicate<String> isNotEmpty = isEmpty.negate();
        System.out.println(isNotEmpty.test(NAME));              // true
    }
}
