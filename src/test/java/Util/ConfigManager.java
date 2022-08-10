package Util;

import org.slf4j.Logger;

import java.io.FileInputStream;
import java.util.Properties;

import static org.slf4j.LoggerFactory.getLogger;

public class ConfigManager {

    private static ConfigManager configManager = null;
    public static Properties prop = new Properties();

    private static final Logger logger = getLogger(ConfigManager.class);


    private ConfigManager() {
        try {
            logger.info("Loading properties files");
            String filename = Constants.Path_Config;
            logger.info("File name :" + filename);
            prop.load(new FileInputStream(filename));
            logger.info("properties file loaded successfully");
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }


    public static synchronized ConfigManager getConfigManagerInstance(){
        logger.info("Executing Util.ConfigManager Instance");
        if(configManager == null) {
            synchronized (ConfigManager.class) {
                configManager = new ConfigManager();
            }
        }
        return configManager;
    }


    public String getKeyValue(String key)
    {
        System.out.println("Returning value from key [" + key+ "] :" + System.getProperty(key, prop.getProperty(key)));
        return System.getProperty(key, prop.getProperty(key));
    }


}
