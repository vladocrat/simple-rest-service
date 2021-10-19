package Utils;

import Dto.PersonDto;
import com.sun.net.httpserver.HttpExchange;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;



public class SearchUtils {


    public static Map<String, String> queryToMap(String query){
        Map<String, String> result = new HashMap<String, String>();
        for (String param : query.split("&")) {
            String pair[] = param.split("=");
            if (pair.length>1) {
                result.put(pair[0], pair[1]);
            }else{
                result.put(pair[0], "");
            }
        }
        return result;
    }


    public static PersonDto getPersonFromQuery(Map<String, String> map) {
        if(map.isEmpty()) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return new PersonDto(map.get("surname"),
                map.get("name"),
                map.get("secondname"),
                LocalDate.parse(map.get("date")));
    }

    public static List<Integer> getIndicesFromURI(HttpExchange exchange) {
        String query = exchange.getRequestURI().getQuery();
        Map<String, String> map = SearchUtils.queryToMap(query);
        List<Integer> ids = new ArrayList<>();
        for(var item : map.entrySet()) {
            String value = item.getValue();
            ids.add(Integer.parseInt(value));
        }
        return ids;
    }
}
