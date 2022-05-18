import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class ClientHandler {

    ClientHandler opponent;
    Hub.Game server;

    Socket socket;
    InputStream inputStream;
    OutputStream outputStream;
    NetworkReading networkReading;
    NetworkWriting networkWriting;

    boolean playerOne = true;


    DataOutputStream dataOutputStream;
    DataInputStream dataInputStream;

    //BlockingQueue<String> blockingQueue = new LinkedBlockingDeque<>();
    BlockingQueue<Messages> blockingQueue = new LinkedBlockingDeque<>();


    public ClientHandler (Socket socket) throws IOException {

        this.socket = socket;
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();

        dataOutputStream = new DataOutputStream(outputStream);
        dataInputStream = new DataInputStream(inputStream);

        System.out.println("NEW CLIENT HANDLER");

    }

    public void startThreads() {
        System.out.println("PLAYER TYPE " + playerOne);
        networkReading = new NetworkReading();
        networkWriting = new NetworkWriting();

        networkReading.start();
        networkWriting.start();
    }

    class NetworkReading extends Thread{
        public void run() {
            try {
                while(true) {
                    Messages holdMessage = null;

                    int type = dataInputStream.readByte();
                    if (type == 10){
                        int holdRow = dataInputStream.readInt();
                        int holdCol = dataInputStream.readInt();
                        holdMessage = new Messages(new Point(holdRow,holdCol));
                        holdMessage.playerOneMessage = playerOne;
                        //System.out.println("CREATED MESSAGE " + holdMessage.playerOneMessage);
                        server.serverQueue.put(holdMessage);
                    } else if (type == 0){
                        String holdString = dataInputStream.readUTF();
                        holdMessage = new Messages(holdString);
                    } else {
                        holdMessage = new Messages(type);
                    }

                    opponent.blockingQueue.put(holdMessage);
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();

            }
            System.out.println("SERVER NETWORK READING THREAD ENDING");

        }
    }

    class NetworkWriting extends Thread{
        public void run() {
            try {
                while(true) {
                    Messages message = blockingQueue.take();
                    //System.out.println("SENT " + message.length);
                    message.sendMessage(dataOutputStream);
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
            System.out.println("SERVER NETWORK WRITING THREAD ENDING");
        }

    }
}
