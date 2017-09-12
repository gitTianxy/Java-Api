package com.java.api.util;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigUtil {
    static Logger logger = Logger.getLogger(ConfigUtil.class);
    static Properties properties;
    static {
        InputStream inputStream = ConfigUtil.class.getClassLoader().getResourceAsStream("config.properties");
        try {
            properties = new Properties();
            properties.load(inputStream);
        } catch (Exception e) {
            logger.error("error when loading 'config.properties'", e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                logger.error("error when loading 'config.properties'", e);
            }
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
