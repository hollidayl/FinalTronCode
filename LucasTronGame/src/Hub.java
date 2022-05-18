import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

class Hub {

    public static void main(String[] args) throws IOException {

        int port = 8006;
        ArrayList<ClientHandler> clients = new ArrayList<>();
        //ArrayList<ServerThread> serverThreads = new ArrayList<>();

        ServerSocket serverSocket = new ServerSocket(port);
        int numPlayersWaiting = 0;
        int arrayIndex = 0;

        while (true) {
            try {

                Socket holdSocket;
                holdSocket = serverSocket.accept();
                System.out.println("HERE IN HUB");

                ClientHandler newClient = new ClientHandler(holdSocket);
                clients.add(newClient);
                numPlayersWaiting += 1;
                if (numPlayersWaiting == 2) {
                    System.out.println("STARTING GAME");
                    numPlayersWaiting = 0;
                    Game holdServer = new Game();

                    ClientHandler playerOneClient = clients.get(arrayIndex - 1);
                    ClientHandler playerTwoClient = clients.get(arrayIndex);
//                    System.out.println("NEW CLIENT: " + newClient);
//                    System.out.println("SHOULD BE EQUAL: " + playerTwoClient);

                    holdServer.playerOne = playerOneClient;

                    playerTwoClient.opponent = playerOneClient;
                    playerTwoClient.playerOne = false;
                    //System.out.println(newClient.player1);
                    playerTwoClient.server = holdServer;

                    holdServer.playerTwo = playerTwoClient;

                    playerOneClient.opponent = playerTwoClient;
                    playerOneClient.server = holdServer;

                    playerTwoClient.startThreads();
                    playerOneClient.startThreads();
                    holdServer.startThreads();
                }
                arrayIndex += 1;

            } catch (IOException ioe) {
                System.out.println("ACK! ACK!! It's an Exception!!");
                System.out.println(ioe);
                serverSocket.close();
            }
        }
    }

    public static class Game {

        ClientHandler playerOne;
        ClientHandler playerTwo;

        HashSet<Point> playerOneSet = new HashSet<>();
        HashSet<Point> playerTwoSet = new HashSet<>();

        Point playerOneLastPoint = new Point(0,0);
        Point playerTwoLastPoint = new Point(0,0);

        Collisions collisions;
        BlockingQueue<Messages> serverQueue = new LinkedBlockingDeque<>();

        public void startThreads() {
            collisions = new Collisions();
            collisions.start();
        }

        private int mirrorRow (int row){
            return (-1)*row + 103;
        }

        class Collisions extends Thread {
            public void run() {
                try {
                    while (true) {
                        Messages message = serverQueue.take();
                        //System.out.println("SERVER BOOLEAN " + message.playerOneMessage);
                        Point givenPoint;
                        if (message.playerOneMessage) {
                            //System.out.println("PLAYER ONE");
                            givenPoint = message.point;
                            playerOneLastPoint = givenPoint;

                            if (playerTwoLastPoint.equals(givenPoint)){
                                System.out.println("HEAD TO HEAD COLLISION");
                                playerOne.dataOutputStream.writeByte(4);
                                playerTwo.dataOutputStream.writeByte(4);
                                playerOneSet.clear();
                                playerTwoSet.clear();
                                playerOneLastPoint = new Point(0,0);
                                playerTwoLastPoint = new Point(0,0);

                            } else if (!playerTwoSet.contains(givenPoint) && !playerOneSet.contains(givenPoint)  && givenPoint.row >= 0 && givenPoint.row < 104 && givenPoint.col >= 0 && givenPoint.col < 80) {
                                playerOneSet.add(givenPoint);
                                //System.out.println("ADD");
                            } else {
                                playerOne.dataOutputStream.writeByte(2);
                                playerTwo.dataOutputStream.writeByte(1);
                                playerOneSet.clear();
                                playerTwoSet.clear();
                                playerOneLastPoint = new Point(0,0);
                                playerTwoLastPoint = new Point(0,0);
                            }

                        } else {
                            //System.out.println("PLAYER TWO");
                            givenPoint = new Point(mirrorRow(message.point.row),message.point.col);
                            playerTwoLastPoint = givenPoint;

                            if (playerOneLastPoint.equals(givenPoint)){
                                System.out.println("HEAD TO HEAD COLLISION");
                                playerOne.dataOutputStream.writeByte(4);
                                playerTwo.dataOutputStream.writeByte(4);
                                playerOneSet.clear();
                                playerTwoSet.clear();
                                playerOneLastPoint = new Point(0,0);
                                playerTwoLastPoint = new Point(0,0);
                            } else if (!playerOneSet.contains(givenPoint) && !playerTwoSet.contains(givenPoint) && givenPoint.row >= 0 && givenPoint.row < 104 && givenPoint.col >= 0 && givenPoint.col < 80) {
                                playerTwoSet.add(givenPoint);
                                //System.out.println("ADD");
                            } else {
                                playerOne.dataOutputStream.writeByte(1);
                                playerTwo.dataOutputStream.writeByte(2);
                                playerOneSet.clear();
                                playerTwoSet.clear();
                                playerOneLastPoint = new Point(0,0);
                                playerTwoLastPoint = new Point(0,0);
                            }
                        }
                    }

                    } catch (InterruptedException | IOException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        }
    }
}
//    class ServerThread extends Thread{
//
//        socket = socket;
//        inputStream = socket.getInputStream();
//        outputStream = socket.getOutputStream();
//
//        dataOutputStream = new DataOutputStream(outputStream);
//        dataInputStream = new DataInputStream(inputStream);
//
//        BlockingQueue<byte[]> blockingQueue = new LinkedBlockingDeque<>();
//
//        public void run() {
//            while(true){
//                try {
//                    //Point point = blockingQueue.take();
//                    String message = serverQueue.take();
//                    System.out.println("WRITING TOOK: " + message);
//                    dataOutputStream.writeUTF(message);
//                } catch (InterruptedException | IOException e) {
//                    e.printStackTrace();
//                }
//
//
//                //pull thing from your blocking queue
//            }
//        }
//
//        // loop forever to see if there is anything in blocking queue
//
//        // if thing in blocking queue, send first message in queue
//    }

//}