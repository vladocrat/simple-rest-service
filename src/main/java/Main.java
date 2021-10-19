import com.sun.net.httpserver.HttpServer;

import java.io.IOException;

public class Main {


    public static void main(String[] args){
        int port = 8081;
        try {
            int sleep = 1000000000;
            while(true) {
                HttpServer server = Server.startServer(port);
                Thread.sleep(sleep * 1000L);
                Server.closeServer();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }



}
