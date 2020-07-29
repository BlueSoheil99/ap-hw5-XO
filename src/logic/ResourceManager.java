package logic;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ResourceManager {
    private static ResourceManager instance;
    private Properties properties;

    private ResourceManager() {
        FileReader reader;
        try {
            properties = new Properties();
            reader = new FileReader("res/configFile.properties");
            properties.load(reader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("main config file doesn't exist");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static ResourceManager getInstance() {
        if (instance == null) instance = new ResourceManager();
        return instance;
    }

    public Integer getServerPort() {
        Integer ans;
        String port = properties.getProperty("serverPort");
        if (port != null) ans = Integer.parseInt(port);
        else ans = null;
        return ans;
    }

    public String getProfilesPath() {
        return properties.getProperty("profilesPath");
    }
}
