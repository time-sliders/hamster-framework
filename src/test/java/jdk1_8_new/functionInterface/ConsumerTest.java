package jdk1_8_new.functionInterface;

import com.noob.storage.http.base.Property;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class ConsumerTest {

    @Test
    public void testListIterator() {

        Consumer<String> consumer = s -> System.out.println(s);
        Consumer<String> consumer2 = System.out::println;
        consumer = consumer.andThen(consumer2);
        consumer.accept("zhwwashere");

        // 一般bean里面的setter方法属于消费者
        consumer = new Property("name", "Java")::setValue;
    }
}
