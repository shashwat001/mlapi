package com.assignment.training;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by shashwat on 4/25/18.
 */
public class PythonExecutor
{
    public static String execute(String filename, Map<String, String> args) throws IOException
    {
        Process p = Runtime.getRuntime().exec(String.format("python %s %s", filename, getStringFromArgs(args)));

        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(p.getInputStream()));

        String s, output = "";

        while ((s = stdInput.readLine()) != null)
        {
            output += s;
        }

        return output;

    }

    private static String getStringFromArgs(Map<String, String> args)
    {
        return args.entrySet()
                .stream()
                .map(entry -> "--" + entry.getKey() + " " + entry.getValue())
                .collect(Collectors.joining(" "));
    }
}
