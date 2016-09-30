package jdk1_8_new.functionInterface;

import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author luyun
 */
public class TestOptional {

    @Test
    public void test1() {
        // of 方法创建一个Optional  (参数不允许为null)
        Optional<String> optional = Optional.of("Hello World!");
        Optional<String> nullOptional = Optional.empty();
        // isPresent 判断Optional对象是否有值
        if (optional.isPresent()) {
            // get 方法获取Optional对象存储的值
            System.out.println(optional.get());// Hello World!
        }
        //ifElse方法 相当于puIfAbsent
        System.out.println(optional.orElse("Default Value"));
        System.out.println(nullOptional.orElse("Default Value"));

        // optional 配合lambda表达式使用
        optional.ifPresent(System.out::println);

    }

    @Test
    public void test2() {
        ConcurrentMap<String, String> map = new ConcurrentHashMap<>();
        map.putIfAbsent("name", "Java");
        map.putIfAbsent("name", "C++");
        System.out.println(map.get("name"));
    }
}
