package Basic;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigHandler {
    private static final String CONFIG_FILE = "config.properties";
    private static Properties properties;

    static {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
            properties.load(fis);
        } catch (IOException e) {
            System.err.println("Error loading configuration file: " + e.getMessage());
        }
    }

    public static String getScreenshotDirectory() {
        return properties.getProperty("screenshot.directory", "Resources");
    }

    public static String getAdbPath() {
        return properties.getProperty("adb.path", "adb");
    }
}

