import CatlinGraphics2D.AnimationCanvas2D;
import CatlinGraphics2D.GraphicsWindow;
import CatlinGraphics2D.ImageUtilities;


import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

class TronGame extends AnimationCanvas2D {

    String opponentName = "N/A";
    String myName = "N/A";

    int myScore = 0;
    int opponentScore = 0;

    final int xStart = 110; // 1000
    final int yStart = 100; // 700
    final int gridWidth = 2;
    final int xLength = 728; // 28 * ROW #
    final int yLength = 560; // 28 * COL #

    int playerTimeSpeed = 35;
    boolean gameOver;
    boolean ready;
    boolean oppReady;

    double playerX, playerY;
    int headRow, headCol;

    Direction playerDirection;

    HashSet<Point> playerSet = new HashSet<>();

    //HashSet<Point> opponentSet = new HashSet<>();

    PointSet opponentSet = new PointSet();

    Font gameOverFont = new Font(Font.SANS_SERIF, Font.PLAIN, 100);
    //Font playerFont = new Font(Font.SANS_SERIF, Font.ITALIC, 20);
    Font playerFont = new Font(Font.SANS_SERIF, Font.ROMAN_BASELINE, 30);
    Font scoreFont = new Font(Font.SANS_SERIF, Font.BOLD, 50);
    Random random = new Random();

    //BufferedImage tronBackground;

    long resetTime;

    BlockingQueue<Messages> playerBlockingQueue;
    BlockingQueue<Point> opponentBlockingQueue;

    OpponentSetThread opponentThread;

    public TronGame(int width, int height,BlockingQueue<Messages> playerBlockingQueue, BlockingQueue<Point> opponentBlockingQueue) {
        super(width, height, 100);
        setBackgroundColor(Color.BLACK);
        this.playerBlockingQueue = playerBlockingQueue;
        this.opponentBlockingQueue = opponentBlockingQueue;
    }

    @Override
    public void start() {
        resetTime = System.currentTimeMillis();


        gameOver = true;
        ready = false;
        oppReady = false;

        playerX = 803;
        playerY = 380;

        headRow = (int)(playerX-xStart)/(7);
        headCol = (int)(playerY-yStart)/(7);




        playerDirection = Direction.LEFT;

//        tronBackground = ImageUtilities.loadImage("tronImages", "tronBackgroundDesign.png");
//        tronBackground = ImageUtilities.scaleImage(tronBackground,0.65);

        System.out.println("before run");
        opponentThread = new OpponentSetThread();
        opponentThread.start();
        System.out.println("after run");

        playerTimeSpeed = Integer.MAX_VALUE;



    }

    @Override
    public void update(double elapsedMilliseconds) {

        if ((isKeyPressed(KeyEvent.VK_SPACE)) && ready == false){
            //gameOver = false;
            ready = true;
            try {
                playerBlockingQueue.put(new Messages(true));
            } catch (InterruptedException e){
                System.out.println(e);
            }



        }

        if (ready == true && oppReady == true && gameOver == true){
            gameOver = false;
            playerSet.clear();
            opponentSet.clear();
            playerX = 803;
            playerY = 380;

            headRow = (int)(playerX-xStart)/(7);
            headCol = (int)(playerY-yStart)/(7);

            playerDirection = Direction.LEFT;

            playerSet.add(new Point(headRow,headCol));
            playerTimeSpeed = 35;
        }



        if ((System.currentTimeMillis()-resetTime) > playerTimeSpeed){
            if (isKeyPressed(KeyEvent.VK_LEFT) && playerDirection != Direction.RIGHT){
                playerDirection = Direction.LEFT;
            } else if (isKeyPressed(KeyEvent.VK_RIGHT) && playerDirection != Direction.LEFT){
                playerDirection = Direction.RIGHT;
            } else if (isKeyPressed(KeyEvent.VK_UP) && playerDirection != Direction.DOWN){
                playerDirection = Direction.UP;
            } else if (isKeyPressed(KeyEvent.VK_DOWN) && playerDirection != Direction.UP){
                playerDirection = Direction.DOWN;
            }
        }

        int row = headRow;
        int col = headCol;

        if ((System.currentTimeMillis()-resetTime) > playerTimeSpeed){
            if (playerDirection == Direction.LEFT){
                Point holderPoint = new Point(headRow-1,headCol);
                if (testPoint(holderPoint)) {
                    playerSet.add(holderPoint);
                } else {
                    System.out.println("MAX TIME SPEED");
//                    playerTimeSpeed = Integer.MAX_VALUE;
//                    gameOver = true;
//                    ready = false;
//                    oppReady = false;
                }
                row = headRow - 1;
                col = headCol;
            } else if (playerDirection == Direction.RIGHT){
                Point holderPoint = new Point(headRow+1,headCol);
                if (testPoint(holderPoint)) {
                    playerSet.add(holderPoint);
                } else {
//                    System.out.println("MAX TIME SPEED");
//                    playerTimeSpeed = Integer.MAX_VALUE;
//                    gameOver = true;
//                    ready = false;
//                    oppReady = false;
                }
                row = headRow + 1;
                col = headCol;
            } else if (playerDirection == Direction.UP){
                Point holderPoint = new Point(headRow,headCol-1);
                if (testPoint(holderPoint)) {
                    playerSet.add(holderPoint);
                } else {
//                    System.out.println("MAX TIME SPEED");
//                    playerTimeSpeed = Integer.MAX_VALUE;
//                    gameOver = true;
//                    ready = false;
//                    oppReady = false;
                }
                row = headRow;
                col = headCol-1;
            } else if (playerDirection == Direction.DOWN){
                Point holderPoint = new Point(headRow,headCol+1);
                if (testPoint(holderPoint)) {
                    playerSet.add(holderPoint);
                } else {
//                    System.out.println("MAX TIME SPEED");
//                    playerTimeSpeed = Integer.MAX_VALUE;
//                    gameOver = true;
//                    ready = false;
//                    oppReady = false;
                }
                row = headRow;
                col = headCol+1;
            }

            resetTime = System.currentTimeMillis();
        }

        headRow = row;
        headCol = col;

        // ================================================

    }

    @Override
    public void draw(Graphics2D pen) {

        // +++++ BACKGROUND +++++
        //ImageUtilities.drawImage(pen, tronBackground, 0, 0);

        //+++++ GRID BACKGROUND +++++
        pen.setColor(new Color(41, 51, 51));
        pen.fillRect(xStart, yStart, xLength, yLength);

        //  +++++ GRID LINES +++++
        pen.setColor(new Color(91, 101, 101));

        // 26 WIDE
        for (int x = xStart; x <= xLength + xStart; x += 28) {
            pen.fillRect(x, yStart, gridWidth, yLength);
        }

        // 20 TALL
        for (int y = yStart; y <= yLength + yStart; y += 28) {
            pen.fillRect(xStart, y, xLength + gridWidth, gridWidth);
        }
        // +++++ GRID OUTLINE SILVER +++++
        //pen.setColor(new Color(111, 119, 127));
        //pen.setColor(new Color(175, 175, 175));
        pen.setColor(new Color(240, 240, 240));

        pen.fillRect(xStart, yStart, gridWidth, yLength);
        pen.fillRect(xLength + xStart, yStart, gridWidth, yLength);

        pen.fillRect(xStart, yStart, xLength + gridWidth, gridWidth);
        pen.fillRect(xStart, yLength + yStart, xLength + gridWidth, gridWidth);

        int topBoxHeight = 50;
        pen.fillRect(xStart,yStart-topBoxHeight,xLength+gridWidth,topBoxHeight);




        // +++++ PLAYER ++++++

        pen.setColor(new Color(51,204,153));

        for (Point holdPoint : playerSet) {
            pen.fillRect(xStart + holdPoint.row * 7, yStart + holdPoint.col * 7, 7, 7);
        }

        pen.setColor(new Color(255,102,255));

//        for (Point holdPoint : opponentSet) {
//            pen.fillRect(xStart + holdPoint.row * 7, yStart + holdPoint.col * 7, 7, 7);
//        }
        opponentSet.drawPoints(pen,xStart,yStart);


        // ++++++ GAME OVER +++++
//        if (gameOver) {
//            pen.setColor(Color.RED);
//            pen.setFont(gameOverFont);
//            pen.drawString("GAME OVER", 200, 300);
//        }

//         ++++++ OPPONENT NAME TEST +++++++

        int myScoreX = 560;
        int oppScoreX = 380;
        int scoreY = 95;

        pen.setFont(scoreFont);
        pen.setColor(new Color(200, 200, 200));
        //pen.setColor(new Color(41, 51, 51));

        //pen.fillRect(xStart+200,yStart-topBoxHeight+5,xLength+gridWidth-400,topBoxHeight-5); //MID
        pen.fillRect(xStart+3,yStart-topBoxHeight+3,xLength+gridWidth-6,topBoxHeight-4); //FULL


        pen.setColor(new Color(41, 51, 51));
        pen.fillRect(475,50,3,53);


        pen.setColor(new Color(51,204,153));
        pen.drawString(Integer.toString(myScore),myScoreX,scoreY);

        pen.setColor(new Color(255,102,255));
        pen.drawString(Integer.toString(opponentScore),oppScoreX,scoreY);




        pen.setFont(playerFont);
        pen.setColor(new Color(0,102,51));
        pen.drawString(myName,670,90);

        pen.setColor(new Color(153,51,153));
        pen.drawString(opponentName,200,90);


        if (!ready || !oppReady){
            pen.setColor(new Color(200,200,200,99));
            pen.fillRect(350,305,300,65);

            if (ready){
                pen.setColor(new Color(26,158,41));
            } else {
                pen.setColor(new Color(200,200,200));
            }

            pen.drawString("SPACE WHEN READY",355,350);
        }




        //pen.drawString("Test",0,700);
    }

//    private boolean testPoint (Point givenPoint){
//        if (!opponentSet.contains(givenPoint) && !playerSet.contains(givenPoint) && givenPoint.row >= 0 && givenPoint.row < 104 && givenPoint.col >= 0 && givenPoint.col < 80){
//            try{
//                playerBlockingQueue.put(new Messages(givenPoint));
//            }catch (InterruptedException e){
//                System.out.println(e);
//            }
//
//            return true;
//        }
//        return false;
//    }
//    private boolean testPoint (Point givenPoint){
//        if (!playerSet.contains(givenPoint)){
//            try{
//                playerBlockingQueue.put(new Messages(givenPoint));
//            }catch (InterruptedException e){
//                System.out.println(e);
//            }
//            return true;
//        }
//        return false;
//    }

    private boolean testPoint (Point givenPoint){
        try{
            playerBlockingQueue.put(new Messages(givenPoint));
        }catch (InterruptedException e){
            System.out.println(e);
        }
        return true;
    }

//    private boolean testPoint (Point givenPoint){
//        if (givenPoint.row >= 0 && givenPoint.row < 104 && givenPoint.col >= 0 && givenPoint.col < 80) {
//            try{
//                playerBlockingQueue.put(new Messages(givenPoint));
//            }catch (InterruptedException e){
//                System.out.println(e);
//            }
//            return true;
//        }
//        return false;
//    }



    private int mirrorRow (int row){
        return (-1)*row + 103;
    }

    private int mirrorCol (int col){
        return (-1)*col + 79;
    }

    class OpponentSetThread extends Thread{
        public void run() {
            //System.out.println("thread reached");
            try {
                while(true) {
                    Point hold = opponentBlockingQueue.take();

                    hold.row = mirrorRow(hold.row);
                    //hold.col = mirrorRow(hold.col);
                    if (hold.row >= 0 && hold.row < 104 && hold.col >= 0 && hold.col < 80) {
                        opponentSet.addPoint(hold);
                    }

                    //System.out.println("TOOK OPPONENT VALUE");
                    //System.out.println(hold.row);
                    //System.out.println(hold.col);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("TRON GAME THREAD ENDING");

        }
    }
}