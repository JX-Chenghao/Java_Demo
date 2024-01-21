package com.ncu.springboot.algorithm.cache;

import java.util.HashMap;
import java.util.Map;

public class OwnerCache {

    private static final ThreadLocal<OwnerCache> INSATANCE = new ThreadLocal<OwnerCache>(){
        @Override
        protected OwnerCache initialValue() {
            return new OwnerCache();
        }

    };

    private OwnerCache(){

    }

    public static OwnerCache instance(){
        return INSATANCE.get();
    }

    private final Map<Class<?>, Map<Object,Object>> cache = new HashMap<>();

    public static void forceClear(){
        INSATANCE.remove();
    }

    public <T> void put(Class<? super T> className, Object key, T value){
        Map<Object, Object> map = cache.get(className);
        if (map == null) {
            map = new HashMap<Object, Object>();
            cache.put(className, map);
        }
        map.put(key, value);
    }


    public <T> T get(Class<? super T> className, Object key){
        Map<Object, Object> map = cache.get(className);
        if (map == null) {
            return null;
        }
        return (T) map.get(key);
    }
}
