package sql;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;



@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DbConfig {
    private String url;
    private String password;
    private String username;
}
