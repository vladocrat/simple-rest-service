package utils;

import sql.DbConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;


public class YamlUtils {

    public static DbConfig getSqlConfig()  {
        YAMLFactory factory = new YAMLFactory();
        ObjectMapper mapper = new ObjectMapper(factory);
        mapper.findAndRegisterModules();
        DbConfig config = null;
        try {
           config =  mapper.readValue(new File("src/main/resources/config.yaml"), DbConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return config;
    }
}
