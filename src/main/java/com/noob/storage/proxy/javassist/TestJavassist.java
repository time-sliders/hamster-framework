package com.noob.storage.proxy.javassist;

import javassist.*;

public class TestJavassist {

    public static void main(String[] args) {
        try {
            ClassPool cp = ClassPool.getDefault();
            cp.insertClassPath(new ClassClassPath(TestJavassist.class));
            CtClass cc = cp.get("com.noob.storage.proxy.javassist.MyJavassist");
            cc.setSuperclass(cp.get("com.noob.storage.proxy.javassist.SuperClass"));
            CtMethod cm = cc.getDeclaredMethod("say");
            cm.insertBefore("super.say();");

            Class clazz = cc.toClass();
            System.out.println(clazz.getSuperclass().getSimpleName());
            MyJavassist myJavassist = (MyJavassist) clazz.newInstance();
            myJavassist.say();

            Loader loader = new Loader(cp);
            Class clazz1 = loader.loadClass("com.noob.storage.proxy.javassist.MyJavassist");
            Object obj = clazz1.newInstance();
            System.out.println(obj.getClass().getSimpleName());
            System.out.println(myJavassist.getClass().getSimpleName());
            System.out.println(myJavassist.getClass().equals(obj.getClass()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
