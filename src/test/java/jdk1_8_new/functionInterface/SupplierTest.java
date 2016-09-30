package jdk1_8_new.functionInterface;

import com.noob.storage.http.base.Property;

import java.util.function.Supplier;

public class SupplierTest {

    public static void main(String[] args) {

        Supplier<String> stringSupplier = () -> ("hello world");
        System.out.println(stringSupplier.get());

        // constructor属于supplier
        stringSupplier = () -> new String();
        stringSupplier = String::new;

        // bean 里面的getter方法属于supplier
        //stringSupplier = Property::getName;
        stringSupplier = Property::staticGetter;
    }

}
