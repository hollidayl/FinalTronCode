package CatlinGraphics2D;

import java.awt.*;
import java.awt.event.*;

/**
 * A Canvas for animated double-buffered graphics.
 * <p>
 * You should extend this class and override these methods:<p>
 * <pre>
 * public abstract void start();
 * public abstract void update(double elapsedMilliseconds);
 * public abstract void draw(Graphics2D pen);
 * </pre>
 * Useful methods in this class:
 * <pre>
 * public void stopAnimation();   // stops the animation
 * public double getActualFPS();  // returns the current frame rate in frames per second
 * </pre>
 */

public abstract class AnimationCanvas2D extends ImageCanvas {
    private javax.swing.Timer timer;
    private int frameCount = 0;
    private long startTime = 0;
    private long lastFrameTime = 0;

    public AnimationCanvas2D(int width, int height, double desiredFrameRate) {
        super(width, height);
        int frameDelayMS = (int) (1000.0 / desiredFrameRate);
        timer = new javax.swing.Timer(frameDelayMS, new AnimationCanvasTimerListener());
    }

    // override these!
    public abstract void start();

    public abstract void update(double elapsedMilliseconds);

    public abstract void draw(Graphics2D pen);

    //************************************************************************************

    /**
     * This is called automatically to notify the Canvas that it has been added to the GUI.
     * When that happens, it starts the animation timer.
     */

    public void addNotify() {
        super.addNotify();
        startAnimation();
    }


    /********************
     * Start the animation
     */

    private void startAnimation() {
        start();
        frameCount = 0;
        startTime = System.nanoTime();
        lastFrameTime = startTime;
        timer.start();
    }

    /********************
     * Stop the animation
     */

    public void stopAnimation() {
        timer.stop();
    }

    /********************
     * Returns the actual framerate as the number of frames per second.
     */

    public double getActualFPS() {
        long stopTime = System.nanoTime();
        double duration = (stopTime - startTime) / 1e9;
        return frameCount / duration;
    }

    private class AnimationCanvasTimerListener implements ActionListener {
        // this function will be called every frame

        public void actionPerformed(ActionEvent event) {
            frameCount++;
            long time = System.nanoTime();
            double elapsedMilliseconds = (time - lastFrameTime) / 1e6;
            update(elapsedMilliseconds);  // override in extension class
            Graphics2D pen = getPen();
            clear(pen);
            draw(pen); // override in extension class
            display();
            lastFrameTime = time;
        }
    }

}