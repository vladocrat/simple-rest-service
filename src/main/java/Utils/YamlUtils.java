package Utils;

import SQL.DbConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;

public class YamlUtils {

    public static DbConfig getSqlConfig() throws IOException {
        YAMLFactory factory = new YAMLFactory();
        ObjectMapper mapper = new ObjectMapper(factory);
        mapper.findAndRegisterModules();
        return mapper.readValue(new File("src/main/resources/config.yaml"), DbConfig.class);
    }
}
