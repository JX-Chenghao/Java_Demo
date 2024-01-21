package com.ncu.springboot.algorithm;

import com.ncu.springboot.algorithm.cache.OwnerCache;

import java.util.*;

public class FunctionUtils {



    public static Set<String> print = new HashSet<>(Arrays.asList("cheng"));

    public static void main(String[] args)  {

        Object o = null;

        String o1 = (String) o;

        System.out.println("o1-" + o1);


        HashMap<String, Object> map = new HashMap<>();

        map.put("room", 1);

        try {

            String intA1 = (String) map.get("room");

        } catch (Exception e) {

            System.out.println("(String) error");

        }

// Integer a = 1;

// String a1 = (String) a; // ??????

// ?????

        HashMap<String, Object> map1 = new HashMap<>();

        map1.put("room", 1);

        try {

            String intA1 = String.valueOf(map.get("room"));

        } catch (Exception e) {

            System.out.println("String.valueOf error");

        }

        boolean equals = "1".equals(null);

        System.out.println("equals-" + equals);

        if (null instanceof Integer) {

            System.out.println("null-Integer");

        }

        Integer i = null;

        if (i instanceof Integer) {

            System.out.println("i-Integer");

        }

        if (print.contains(null)) {

            System.out.println("1");

        }

        if (print.contains("")) {

            System.out.println("2");

        }

        if (print.contains("chen")) {

            System.out.println("3");

        }

        if (print.contains("cheng")) {

            System.out.println("4");

        }

        HashSet<Integer> set = new HashSet<>();

        boolean contains = set.contains(null);

        System.out.println("set contains null-" + contains);

        HashSet<Integer> set1 = new HashSet<>();

        set1.add(null);

        boolean contains1 = set1.contains(null);

        System.out.println("set1 contains null-" + contains1);

        try {

            OwnerCache.instance().put(String.class, "key", "BCD");


            OwnerCache.instance().put(OwnerCache.class, "key", OwnerCache.instance());


            String key = OwnerCache.instance().get(String.class, "key");

            OwnerCache cache = OwnerCache.instance().get(OwnerCache.class, "key");


            System.out.println("OwnerCache key:" + key);

            System.out.println("OwnerCache key1:" + cache);


        } catch (Exception e) {

            System.out.println("???? null error");

        }

        try {

            long l = Long.parseLong("022260644564656235552");

            System.out.println("Long.parseLong: " + l);

        } catch (Exception e) {

            System.out.println("Long.parseLong error");

        }

        try {

            Long l = Long.parseLong(null);

            System.out.println("Long.parseLong: " + l);

        } catch (Exception e) {

            System.out.println("Long.parseLong error");

        }

        List<String> a = new ArrayList<>();

        a.add("a");

        editList(a);

        System.out.println("list ?????????:" + a.toString()); // ???????????????????

        Set<Long> longSet = new HashSet<>();

        longSet.add(1L);

        longSet.add(2L);

//longSet.add(null);

        if (longSet.contains(null)) {

            System.out.println("set contain null true");

        } else {

            System.out.println("set contain null false");

        }

        List<String> test = new ArrayList<>();

        test.add("a");

        test.add("b");

        if (test.contains(null)) {

            System.out.println("List contain null true");

        } else {

            System.out.println("List contain null false");

        }

        System.out.println(test);

    }

    private static void editList(List<String> a) {

        List<String> b = new ArrayList<>();

        b.add("a");

        b.add("b");

        a = b;

    }

    private Long getA() {

        return null;

    }

}
