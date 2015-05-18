package com.ttcgroup.tm1.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 4/15/2015.
 */
public class Helper {
    /**
     * Execute command line
     * @param folderPath Folder Path
     * @param cmd file bat, exe ...
     */
    public static void ExecuteCMD(String folderPath, String cmd)
    {
        String command[] = new String[]{"cmd","/c",cmd};
        ProcessBuilder pb1 = new ProcessBuilder(command);
        pb1.directory(new File(folderPath));
        File commands = new File("Commands.txt");;
        File output = new File("ProcessLog.txt");
        File errors = new File("ErrorLog.txt");
        Logger logger = LoggerFactory.getLogger(Helper.class);

        if (!commands.exists())
        {
            try {
                commands.createNewFile();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }

        if (!output.exists())
        {
            try {
                output.createNewFile();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }

        if (!output.exists())
        {
            try {
                output.createNewFile();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
        pb1.redirectInput(commands);
        pb1.redirectError(errors);
        pb1.redirectOutput(output);


        try{

            Process process = pb1.start();

            int exitStatus = process.waitFor();
            if (exitStatus != 0)
            {
                throw new Exception("Import SSP to temp is failed");
            }
        }
        catch (Exception ex ) {
            logger.error(ex.getMessage());
        }
    }
}
