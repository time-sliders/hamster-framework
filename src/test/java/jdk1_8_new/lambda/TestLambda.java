package jdk1_8_new.lambda;

import java.io.File;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class TestLambda {

    public static void main(String[] args) {

        /**
         * lambda 表达式是一个拥有上下文相关类型的表达式
         */
        AddFunction lambda = (a, b) -> a + b;

        AddFunction anonymous = new AddFunction() {
            @Override
            public int add(int a, int b) {
                return a + b;
            }
        };

        System.out.println(lambda.add(1, 2));
        System.out.println(anonymous.add(1, 2));
    }

    public void testLambda() {

        /**
         * Function
         * 接受T 返回 R
         */
        Function<File, Boolean> fileFilterFunction = (f) -> f.getName().endsWith(".java");

        /**
         * Predicate
         * 接收一个 T 返回 Boolean
         */
        Predicate<Collection> emptyCollectionChecker = (s) -> s == null || s.size() == 0;
        Predicate<File> javaFilePredicate = (f) -> f.getName().endsWith(".java");

        /**
         * Consumer
         * 接收T对象，不返回值(消费者)
         * O::M 表示使用 O对象的M方法消费传入的参数
         */
        Consumer<String> printer = System.out::println;//(s) -> System.out.println(s);
        printer.accept("hello world");

        /**
         * Supplier
         * 提供T对象(例如工厂),不接收值
         */
        Supplier<String> stringSupplier = () -> "hello world";
    }
}

interface AddFunction {

    int add(int a, int b);

}