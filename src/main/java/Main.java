import java.util.Scanner;

public class Main {


    public static void main(String[] args){
        int port = 8081;
        Server server = new Server(port);
        Scanner in = new Scanner(System.in);

        while(true) {
            server.startServer();
            in.nextLine();
        }
    }
}
