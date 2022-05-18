package CatlinGraphics2D;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.HashSet;

/**
 * A Canvas that listens to Mouse and Keyboard events.
 * Override the listeners that you care about in an extension class.
 */

public class EventfulCanvas2D extends Canvas implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener
{
    private HashSet<Integer> pressedKeys = new HashSet<>();
    private boolean centeredMouse = false;
    private boolean useCenteredMouse = false;
    private Robot robot = null;

    public EventfulCanvas2D()
    {
        addKeyListener(this);
        addKeyListener(new KeyHandler());
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
    }

    public boolean isKeyPressed(int keyCode) {
        return pressedKeys.contains(keyCode);
    }


    /** This is called automatically to notify the Canvas that it has been added to the GUI.
     * You should not call this function yourself.
     */
    public void addNotify()
    {
        super.addNotify();
        requestFocusInWindow();
    }

    //**********************************************************************
    // returns the Point where the mouse is

    public java.awt.Point getMousePoint()
    {
        PointerInfo pointerInfo = MouseInfo.getPointerInfo();
        java.awt.Point mousePoint = pointerInfo.getLocation();
        java.awt.Point panelCorner = getLocationOnScreen();
        mousePoint.translate(-panelCorner.x, -panelCorner.y);
        return mousePoint;
    }

    /******************
     * Hides the mouse cursor
     */

    public void hideMouse()
    {
        BufferedImage blank = new BufferedImage(16,16,BufferedImage.TYPE_4BYTE_ABGR);
        Cursor blankCursor = getToolkit().createCustomCursor(blank,new Point(0,0),"blankCursor");
        setCursor(blankCursor);
    }

    /******************
     * Restores the mouse cursor
     */

    public void showMouse()
    {
        setCursor(Cursor.getDefaultCursor());
    }


    public void forceCenteredMouse() {
        System.out.println("forceCenteredMouse");
        centeredMouse = true;
        try {
            if (robot == null)
                robot = new Robot();
            Point center = getCanvasCenter();
            System.out.println(" center = " + center);
            robot.mouseMove(center.x, center.y);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public boolean isCenteredMouse() {
        return centeredMouse;
    }

    public Dimension getCenteredMouseMovement() {
        if (! centeredMouse) throw new IllegalStateException("You must call forceCenteredMouse before using this function");
        Point center = getCanvasCenter();
        PointerInfo pointerInfo = MouseInfo.getPointerInfo();
        Point mousePoint = pointerInfo.getLocation();
        robot.mouseMove(center.x, center.y);
        if (useCenteredMouse)
            return new Dimension(mousePoint.x - center.x, mousePoint.y - center.y);
        else {  // ignore first centered mouse movement, since it contains the movement for the original centering of the mouse
            useCenteredMouse = true;
            return new Dimension(0, 0);
        }
    }

    private Point getCanvasCenter() {
        Dimension size = getSize();
        Point canvasCorner = getLocationOnScreen();
        int x = canvasCorner.x + size.width / 2;
        int y = canvasCorner.y + size.height / 2;
        return new Point(x, y);
    }

    ///////////////////////////////////////////////////////////////////////

    // you should override any of these methods that you need to

    public void mousePressed(MouseEvent event)
    {
        // int mouseX = event.getX();
        // int mouseY = event.getY();
    }

    public void mouseReleased(MouseEvent event)
    {
    }

    public void mouseDragged(MouseEvent event)
    {
    }

    public void mouseMoved(MouseEvent event)
    {
    }

    public void mouseClicked(MouseEvent event)
    {
    }

    public void mouseEntered(MouseEvent event)
    {
    }

    public void mouseExited(MouseEvent event)
    {
    }

    public void mouseWheelMoved(MouseWheelEvent event)
    {
        // int clicks = event.getWheelRotation();
    }

    public void keyPressed(KeyEvent event)
    {
    /*  examples:
     int code = event.getKeyCode();
     if (code == KeyEvent.VK_LEFT) {
     }
     if (code == KeyEvent.VK_RIGHT) {
     }
     if (code == KeyEvent.VK_UP) {
     }
     if (code == KeyEvent.VK_DOWN) {
     }
     if (code == KeyEvent.VK_SPACE) {
     }
     */
    }

    public void keyReleased(KeyEvent event)
    {
    }

    public void keyTyped(KeyEvent event)
    {
    }

    ////////////////////////////////////////////////////////////////////////

    private class KeyHandler implements KeyListener {

        public void keyPressed(KeyEvent event) {
            int code = event.getKeyCode();
            if (code == KeyEvent.VK_ESCAPE) {
                System.exit(0);
            }
            pressedKeys.add(code);
        }

        public void keyReleased(KeyEvent event) {
            pressedKeys.remove(event.getKeyCode());
        }

        public void keyTyped(KeyEvent event) {
        }
    }

}