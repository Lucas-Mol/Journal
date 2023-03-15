package com.journal.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Constants {
	
	private static final String PROP_CONFIG_PATH= "config.properties";
    public static final String MAIL_FROM;
    public static final String MAIL_FROM_PASSWORD;
    public static final String MAIL_TO_TEST;
    public static final String PROFESSINAL_MAIL;
    
    static {
        Properties properties = new Properties();
        try (InputStream inputStream = Constants.class.getClassLoader().getResourceAsStream(PROP_CONFIG_PATH)) {
            properties.load(inputStream);
            
            MAIL_FROM = properties.getProperty("MAIL_FROM");
            MAIL_FROM_PASSWORD = properties.getProperty("MAIL_FROM_PASSWORD");
            MAIL_TO_TEST = properties.getProperty("MAIL_TO_TEST");
            PROFESSINAL_MAIL = properties.getProperty("PROFESSINAL_MAIL");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Não foi possível ler o arquivo de propriedades " + PROP_CONFIG_PATH, e);
        } 
    }

}
