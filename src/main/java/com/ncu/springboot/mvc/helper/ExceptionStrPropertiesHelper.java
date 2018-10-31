package com.ncu.springboot.mvc.helper;

public class ExceptionStrPropertiesHelper extends  AbstractProperties{
    private static ExceptionStrPropertiesHelper instance ;

    public static ExceptionStrPropertiesHelper getInstance() {
        if(instance == null){
            instance = new ExceptionStrPropertiesHelper();
        }
        return instance;
    }

    public String getPropertiesFileName(){
        return "exceptionStrDefine.properties";
    }

}
