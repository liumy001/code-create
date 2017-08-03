package org.unique.generator;

import java.io.*;
import java.util.Properties;

public class ConfigUtil {
    private static Properties pps = new Properties();
    static{
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(ConfigUtil.class.getResource("/app.properties").getPath()));
            pps.load(in);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    //根据Key读取Value
    public static String getValue(String key) {
        return pps.getProperty(key);
    }

    public static void main(String[] args) {
        System.out.println(getValue("mysql.username"));

    }
}
