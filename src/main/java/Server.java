import dto.PersonDto;
import dao.PersonDao;
import sql.CRUDRepository;
import sql.DbService;
import utils.SearchUtils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static methods.Methods.GET;
import static methods.Methods.POST;

public class Server{
    private HttpServer server;
    private final CRUDRepository db = new DbService();
    private final int port;

    public Server(int port) {
        this.port = port;
    }

    public HttpServer initContexts() throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/api/save", (exchange -> {
            if(exchange.getRequestMethod().equals(POST.getValue())) {
                try {
                    save(exchange);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                exchange.sendResponseHeaders(403, -1);
            }
        }));

        server.createContext("/api/all", (exchange -> {
            if(exchange.getRequestMethod().equals(GET.getValue())) {
                findAll(exchange);
            } else {
                exchange.sendResponseHeaders(403, -1);
            }
        }));

        server.createContext("/api/get", (exchange -> {
            if(exchange.getRequestMethod().equals(GET.getValue())) {
                getByIndex(exchange);
            } else {
                exchange.sendResponseHeaders(403, -1);
            }
        }));

        server.createContext("/api/delete", (exchange -> {
            if(exchange.getRequestMethod().equals(POST.getValue())) {
                try {
                    delete(exchange);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                exchange.sendResponseHeaders(403, -1);
            }
        }));

        server.createContext("/api/update", (exchange -> {
            if(exchange.getRequestMethod().equals(POST.getValue())) {
                try {
                    update(exchange);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                exchange.sendResponseHeaders(403, -1);
            }
        }));

        return server;
    }

    private  void getByIndex(HttpExchange exchange) throws IOException {
        List<PersonDao> daos = getPeopleByIndices(exchange);
        if(!daos.isEmpty()) {
            List<PersonDto> dtos = new ArrayList<>();
            for(var dao : daos) {
                dtos.add(dao.convertFioAndCreatePerson());
            }
            System.out.println(dtos);
            writeResponse(exchange, dtos.toString());
        } else {
            exchange.sendResponseHeaders(400, -1);
        }
        exchange.close();
    }

    private void delete(HttpExchange exchange) throws SQLException, IOException {
        List<PersonDao> daos = getPeopleByIndices(exchange);
        if(!daos.isEmpty()) {
            List<PersonDto> dtos = new ArrayList<>();
            for (var dao : daos) {
                if (!db.delete(dao.getId())) {
                    exchange.sendResponseHeaders(400, -1);
                } else {
                    dtos.add(dao.convertFioAndCreatePerson());
                    System.out.println("Person with id = " + dao.getId() + " has successfully been deleted");
                }
            }
            writeResponse(exchange, dtos.toString());
        } else {
            exchange.sendResponseHeaders(400, -1);
        }
        exchange.close();
    }


    private void save(HttpExchange exchange) throws IOException, SQLException {
        Map<String, String> queryMap = SearchUtils.queryToMap(exchange.getRequestURI().getQuery());
        PersonDto dto = SearchUtils.getPersonFromQuery(queryMap);
        if(dto == null) return;

           if(db.save(dto)) {
               String message = dto + " has successfully been saved";
               exchange.sendResponseHeaders(200, message.length());
               System.out.println(message);
               writeResponse(exchange, message);
           }
           exchange.close();
    }

    private void update(HttpExchange exchange) throws SQLException, IOException {
        String query = exchange.getRequestURI().getQuery();
        Map<String, String> map = SearchUtils.queryToMap(query);
        if(db.update(map)) {
            String response = "person with id = " + map.get("id") + " has been updated";
            System.out.println(response);
            writeResponse(exchange, response);
        } else {
            exchange.sendResponseHeaders(400, -1);
        }
    }

    private void findAll(HttpExchange exchange) throws IOException {
        List<PersonDao> people = db.findAll();
        String response = people.toString();

        writeResponse(exchange,  response);
        people.forEach(System.out::println);
        exchange.close();
    }


    private List<PersonDao> getPeopleByIndices(HttpExchange exchange) {
        List<Integer> ids = SearchUtils.getIndicesFromURI(exchange);
        List<PersonDao> daos = new ArrayList<>();
        for(int id : ids) {
            daos.add(db.findPersonById(id));
        }
        return daos;
    }


    private void writeResponse(HttpExchange exchange, String response) throws IOException {
        exchange.sendResponseHeaders(200, response.length());
        OutputStream output = exchange.getResponseBody();
        output.write(response.getBytes(StandardCharsets.UTF_8));
        output.flush();
    }


    public void startServer() {
        try {
            server = initContexts();
            server.setExecutor(null);
            server.start();
        } catch (Exception e) {
            closeServer();
        }
    }


    public void closeServer() {
        try {
            if (server != null) {
                closeServer();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
