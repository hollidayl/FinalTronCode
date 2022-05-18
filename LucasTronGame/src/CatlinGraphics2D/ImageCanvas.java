package CatlinGraphics2D;

import java.awt.*;
import java.awt.image.*;

/****************************************
 *
 * A Canvas for double-buffered graphics.

 Useful methods in this class:

 <pre>
 public Graphics2D getPen();  // gets a pen for drawing on off-screen buffer
 public void display();       // displays the off-screen buffer on the screen
 public void clear();         // clears the off-screen buffer

 public int getWidth();
 public int getHeight();

 public Point getMousePoint();
 public void hideMouse();
 public void showMouse();

 public void setBackgroundColor(Color newBackgroundColor);
 public Color getBackgroundColor();
 </pre>
 */

public class ImageCanvas extends EventfulCanvas2D
{
    private BufferStrategy strategy = null;
    private Color backgroundColor = Color.WHITE;

    //**********************************************************************
    // Constructor: pass the width and height you want for this canvas.

    public ImageCanvas(int width, int height)
    {
        setSize(new Dimension(width, height));
        setPreferredSize(new Dimension(width, height));
        setIgnoreRepaint(true);
    }

    //*****************************************************************
    // This is called automatically to notify the Canvas that it has been added to the GUI
    public void addNotify()
    {
        super.addNotify();
        createBufferStrategy(2);
        strategy = getBufferStrategy();
    }

    /************************
     * Returns the Graphics2D pen used to draw on the off-screen buffer.
     */

    public Graphics2D getPen()
    {
        return (Graphics2D) strategy.getDrawGraphics();
    }


    /************************
     * Call display() when you are done drawing a frame on the off-screen buffer.
     * This displays the contents of the buffer on the screen.
     */

    public void display()
    {
        strategy.show();
    }


    //**********************************************************************

    public void setBackgroundColor(Color newBackgroundColor)
    {
        backgroundColor = newBackgroundColor;
    }

    //**********************************************************************

    public Color getBackgroundColor()
    {
        return backgroundColor;
    }

    //**********************************************************************
    // clears the image panel

    public void clear(Graphics2D pen)
    {
        pen.setColor(backgroundColor);
        pen.fillRect(0, 0, getWidth(), getHeight());
    }

}
