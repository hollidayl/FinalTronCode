package CatlinGraphics2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/*****************************************************************************************************************
 *
 * A Window (possibly full screen) for your graphics or animation.
 *
 <p>
 IN YOUR MAIN FUNCTION, PICK ONE OF THE FOLLOWING THREE OPTIONS:
 <ul>

 <li>make a normal window<br>
 <code> GraphicsWindow.makeWindow(graphicsCanvas, "My Graphics!"); </code>

 <li>make a full-screen window without a frame (true full screen)<br>
 <code> GraphicsWindow.makeFullScreen(graphicsCanvas, true); </code>

 <li>make a maximized window without a frame (fake full screen, may show menu bar)<br>
 <code> GraphicsWindow.makeFullScreen(graphicsCanvas, false); </code>

 </ul>

 The graphicsCanvas you provide will be placed in the Center of a BorderLayout.
 You are welcome to add other components or containers to the other regions as you wish.

 @author Andrew Merrill
 @version 1.0.1
 */

public class GraphicsWindow extends JFrame {

    /*****************************************************************************************************************
     * Creates a regular window with a title bar
     *
     * @param graphicsCanvas The Component to display in the window
     * @param title The title of the window (displayed in the title bar)
     */
    public static GraphicsWindow makeWindow(Component graphicsCanvas, String title) {
        return new GraphicsWindow(graphicsCanvas, title, false, false);
    }


    /*****************************************************************************************************************
     * Creates a full-screen window, using Java's full screen exclusive mode
     *
     * @param graphicsCanvas The Component to display in the window
     *
     */

    public static GraphicsWindow makeFullScreen(Component graphicsCanvas) {
        return makeFullScreen(graphicsCanvas, true);
    }


    /*****************************************************************************************************************
     * Creates a full-screen window
     *
     * @param graphicsCanvas The Component to display in the window
     * @param trueFullScreen If this parameter is true,
     *                       then we use Java's full screen exclusive mode.
     *                       If this parameter is false, then the window will simply
     *                       be maximized.
     */

    public static GraphicsWindow makeFullScreen(Component graphicsCanvas, boolean trueFullScreen) {
        return new GraphicsWindow(graphicsCanvas, "", true, trueFullScreen);
    }

    //////////////////////////////////////////////////////////////////////////////////

    private GraphicsWindow(Component graphicsCanvas, String title, boolean maximized, boolean fullscreen) {
        super(title);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        add(graphicsCanvas, BorderLayout.CENTER);
        setIgnoreRepaint(true);
        GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        if (fullscreen && graphicsDevice.isFullScreenSupported()) {
            setUndecorated(true);
            graphicsDevice.setFullScreenWindow(this);
        } else if (maximized) {
            setUndecorated(true);
            setExtendedState(JFrame.MAXIMIZED_BOTH);
        } else {
            pack();
            setLocationRelativeTo(null); // center the window on the screen
        }

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        setVisible(true);
        bringToFront();
    }

    private void bringToFront() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                toFront();
            }
        });
    }

}