import com.sun.net.httpserver.HttpServer;

import java.io.IOException;

public class Main {


    public static void main(String[] args){
        int port = 8081;
        try {
            HttpServer server = Server.startServer(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
