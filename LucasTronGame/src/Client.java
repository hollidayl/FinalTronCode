import CatlinGraphics2D.GraphicsWindow;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

class Client {

    static TronGame gameCanvas;
    private static boolean readyBoolean;

    Socket socket;
    InputStream inputStream;
    OutputStream outputStream;

    static String username;

    DataOutputStream dataOutputStream;
    DataInputStream dataInputStream;

    BlockingQueue<Messages> playerBlockingQueue = new LinkedBlockingDeque<>();
    BlockingQueue<Point> opponentBlockingQueue = new LinkedBlockingDeque<>();

    TronSendPoints sendPoints = new TronSendPoints();
    TronRecievePoints receivePoints = new TronRecievePoints();

    Client (Socket socket, String username) throws IOException {
        System.out.println("HERE IN CLIENT");
        this.socket = socket;
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();

        dataOutputStream = new DataOutputStream(outputStream);
        dataInputStream = new DataInputStream(inputStream);

        this.username = username;
    }

    public void sendMessage() throws IOException {
        dataOutputStream.writeByte(0);
        dataOutputStream.writeUTF(username);
        sendPoints.start();
    }


    class TronSendPoints extends Thread{
        public void run() {
            try {
                while(true) {
                    Messages holdMessage = playerBlockingQueue.take();
                    holdMessage.sendMessage(dataOutputStream);
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }

            System.out.println("CLIENT SEND POINTS THREAD ENDING");
        }
    }

    class TronRecievePoints extends Thread{
        public void run() {
            try {
                while(true) {
                    byte type = dataInputStream.readByte();
                    //System.out.println("RECEIVED TYPE: " + type);
                    if (type == 0) {
                        gameCanvas.opponentName = dataInputStream.readUTF();
                        System.out.println("OPP NAME: " + gameCanvas.opponentName);
                    } else if (type == 1) {
                        gameCanvas.myScore += 1;
                        gameCanvas.playerTimeSpeed = Integer.MAX_VALUE;
                        gameCanvas.gameOver = true;
                        gameCanvas.ready = false;
                        gameCanvas.oppReady = false;
                    } else if (type == 2) {
                        gameCanvas.opponentScore += 1;
                        gameCanvas.playerTimeSpeed = Integer.MAX_VALUE;
                        gameCanvas.gameOver = true;
                        gameCanvas.ready = false;
                        gameCanvas.oppReady = false;
                    } else if (type == 3) {
                        gameCanvas.oppReady = true;
                    } else if (type == 4){
                        gameCanvas.playerTimeSpeed = Integer.MAX_VALUE;
                        gameCanvas.gameOver = true;
                        gameCanvas.ready = false;
                        gameCanvas.oppReady = false;
                    } else {
                        int holdRow = dataInputStream.readInt();
                        int holdCol = dataInputStream.readInt();
                        Point givenPoint = new Point(holdRow, holdCol);
                        //Point givenPoint = decodeMessage(dataInputStream.readUTF());
                        opponentBlockingQueue.put(givenPoint);
                    }
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("CLIENT RECEIVE POINTS THREAD ENDING");
        }
    }

//        // STEP 8B: CLIENT CLOSES CONNECTION
//
//        bufferedReader.close();
//        printStream.close();
////        socket.close();
//
//    }

    // NO LONGER USED
    private Point decodeMessage (String given){
        String row = "";
        String col = "";
        boolean half = false;
        for (int i = 0; i < given.length(); i += 1){
            String hold = Character.toString((given.charAt(i)));
            //System.out.println("HOLD STRING: " + hold);
            if (hold.equals("|")){
                //System.out.println("FOUND MIDDLE");
                half = true;
            } else if (half){
                col = col + hold;
            } else {
                row = row + hold;
            }
        }
        //System.out.println("ROW: " + row + " COL: " + col);
        return new Point(Integer.parseInt(row),Integer.parseInt(col));
    }

    public static void main(String[] args)
    {
        try {

            // STEP 2: CLIENT CONNECTS TO SERVER

            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter player name: ");
            username = scanner.nextLine();

            //Socket socket = new Socket("cs.catlin.edu", 8006);
            Socket socket = new Socket("cs.catlin.edu", 8006);

            Client client = new Client(socket, username);

            gameCanvas = new TronGame(950, 700, client.playerBlockingQueue, client.opponentBlockingQueue);
            GraphicsWindow.makeWindow(gameCanvas, "Simple Game");
            gameCanvas.myName = username;

            client.sendMessage();
            client.receivePoints.start();

        }
        catch (IOException ioe) {
            System.out.println("ACK! ACK!! It's an Exception!!");
            System.out.println(ioe);
        }
    }
}