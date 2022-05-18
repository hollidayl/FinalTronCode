import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter player name (3 letter max): ");
        String playerName = scanner.nextLine();
        Socket socket = new Socket("localhost", 1074);
//        Client client = new Client(socket,playerName);
//        client.recieve();
//        client.send();

    }
}
