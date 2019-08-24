package com.ncu.springboot.test.unsafe;

import sun.misc.Unsafe;
import sun.reflect.ReflectionFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class UnsafeTest implements Serializable{
    private String name = "ch";
    private int age = 22;
    private long studentNo = 8000114L;
    private String password;

    //自定义序列化
    private  void  writeObject(ObjectOutputStream out) throws IOException {
        //out.defaultWriteObject();

        out.writeObject(name);
        out.writeObject(new StringBuilder(password).reverse());
    }

    //ObjectStreamClass 记录描述类信息Class对象，再通过反射机制，生成对象
    private  void  readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        //in.defaultReadObject();


        //《Effective Java》
        // readObject()的作用相当于参数为ObjectInputStream类型的构造器，因此要像构造器一样，对于参数的有效性进行检查
        //age>0
        this.name=(String)in.readObject();
        if (name.startsWith("a")) {

        }

        StringBuilder pwd=(StringBuilder)in.readObject();
        this.password=pwd.reverse().toString();
    }

    //private  void  readObjectNoData()
    // 当序列化的类版本和反序列化的类版本不同时，或者ObjectInputStream流被修改时，会调用此方法

    //private Object writeReplace()  writeReplace()方法被实现后，序列化机制会先调用writeReplace()方法将当前对象替换成另一个对象（该方法会返回替换后的对象）并将其写入流中
    //writeReplace()的返回值会被自动调用默认序列化机制写入输出流中，同时因为对象类型已经被替换
    //替换的对象 也应实现序列化

    //private Object readResolve()   readResolve()方法会在readObject()后调用
    //readResolve()最重要的应用场景就是保护性恢复单例模式的对象


    //Externalizable接口继承于Serializable
    // 当读取对象时，会调用被序列化类的无参构造器去创建一个新的对象（所以基类需要无参构造器）
    @Override
    public String toString() {
        return name + ":[" +age + "]";
    }

    public static void main(String[] args) throws Exception{

        System.out.println("**********************allocateInstance*******************");
        //allocateInstance方式
        Field f = Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        Unsafe us = (Unsafe) f.get(null);
        UnsafeTest helper1 = (UnsafeTest) us.allocateInstance(UnsafeTest.class);
        System.out.println(helper1);

        //java反序列化方式
        ReflectionFactory rf = ReflectionFactory.getReflectionFactory();
        Constructor constructor = rf.newConstructorForSerialization(UnsafeTest.class, Object.class.getConstructor());
        UnsafeTest helper2 = (UnsafeTest) constructor.newInstance();
        System.out.println(helper2);

        constructor = rf.newConstructorForSerialization(UnsafeTest.class, UnsafeTest.class.getConstructor());
        UnsafeTest helper3 = (UnsafeTest) constructor.newInstance();
        System.out.println(helper3);

        //new
        System.out.println(new UnsafeTest() );

        System.out.println("****************arrayBaseOffset+arrayIndexScale**************");


        Field field = Unsafe.class.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        Unsafe unsafe = (Unsafe) field.get(null);

        String[] array1 = new String[]{"abc", "123", "456", "cde", "ch", "xyz"};
        String[] array2 = new String[]{"abc1", "1231", "4561", "cde1", "ch1", "xyz1"};
        Class<?> ak = String[].class;
        ABASE = unsafe.arrayBaseOffset(ak);
        int scale = unsafe.arrayIndexScale(ak);
        ASHIFT = 31 - Integer.numberOfLeadingZeros(scale);
        String array11 = (String) unsafe.getObject(array1, ((long) 2 << ASHIFT) + ABASE);
        String array21 = (String) unsafe.getObject(array2, ((long) 2 << ASHIFT) + ABASE);
        System.out.println(ABASE);
        System.out.println(scale);
        System.out.println(ASHIFT);
        System.out.println(array11);
        System.out.println(array21);


        System.out.println("******** ***objectFieldOffset+getInt+putInt**************");
        UnsafeTest unsafeTest = new UnsafeTest();
        System.out.println(unsafeTest);
        Field age = UnsafeTest.class.getDeclaredField("age");
        //获取普通变量的偏移量
        long fieldOffset = unsafe.objectFieldOffset(age);
        System.out.println( "offset(age)int->4个字节："+ fieldOffset);
        System.out.println( "offset(studentNo)long->8个字节："+ unsafe.objectFieldOffset(UnsafeTest.class.getDeclaredField("studentNo")));
        System.out.println( "offset(name)引用->4个字节："+ unsafe.objectFieldOffset(UnsafeTest.class.getDeclaredField("name")));
        System.out.println( "offset(password)引用->8个字节："+ unsafe.objectFieldOffset(UnsafeTest.class.getDeclaredField("password")));

        //直接根据偏移地址操作对象变量age 的值
        int oldAge = unsafe.getInt(unsafeTest, fieldOffset);
        System.out.println( "oldAge:"+oldAge);
        //直接根据偏移地址操作对象变量age 的值
        unsafe.putInt(unsafeTest, fieldOffset, 23);
        System.out.println(unsafeTest);
    }

    private static int ASHIFT;
    private static long ABASE;







}
