package com.noob.storage.rpc.serializer;

import com.noob.storage.model.test.ComplexModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * @author LuYun
 * @since 2018.04.12
 */
public class TestSerial {

    public static void main(String[] args) throws IOException {
        File file = new File("/Users/zhangwei/Downloads/oos");
        if(file.exists() && file.delete()){
            System.out.println("delete old");
        }
        if(file.createNewFile()){
            System.out.println("create new");
        }
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        oos.writeObject(new ComplexModel());
        System.out.println("write finish");
    }

}
