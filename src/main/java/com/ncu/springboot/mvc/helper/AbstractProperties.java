package com.ncu.springboot.mvc.helper;

import org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration;
import org.springframework.boot.env.PropertiesPropertySourceLoader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

public abstract class AbstractProperties {
    public String getPropertiesValue(String key){
        Properties properties = new Properties();
        try {
            EncodedResource encodedResource = new EncodedResource(new ClassPathResource(getPropertiesFileName()),"UTF-8");
            properties = PropertiesLoaderUtils.loadProperties(encodedResource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties.getProperty(key);
    }

    public abstract String getPropertiesFileName();
}
