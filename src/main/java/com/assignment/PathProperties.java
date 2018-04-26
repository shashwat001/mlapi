package com.assignment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by shashwat on 4/26/18.
 */
public class PathProperties
{
    public static Properties config;
    static
    {
        String fileName = "settings.properties";
        ClassLoader classLoader = new PathProperties().getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());

        config =new Properties();
        try
        {
            config.load(new FileInputStream(file));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
