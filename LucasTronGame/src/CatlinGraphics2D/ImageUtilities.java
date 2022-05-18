package CatlinGraphics2D;

import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import javax.imageio.*;
import java.io.*;

/****************************************
 *
 * General utility functions for images.
 */


public class ImageUtilities
{
    private static GraphicsConfiguration graphicsConfiguration;

    static
    {
        GraphicsEnvironment graphEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice graphDevice = graphEnv.getDefaultScreenDevice();
        graphicsConfiguration = graphDevice.getDefaultConfiguration();
    }

    private ImageUtilities() {}

    //////////////////////////////////////////////////////////////////////////////

    /**********************************************************************************
     * A utility function to load an image from a file.
     *
     * @param folder    The folder containing the image file.
     * @param filename  The file that contains the image to load.
     * @return
     */

    public static BufferedImage loadImage(String folder, String filename)
    {
        try
        {
            if (folder.length() > 0)
                folder = folder + "/";
            BufferedImage fileImage = ImageIO.read(ImageUtilities.class.getResource("/" + folder + filename));
            return copyToFastBufferedImage(fileImage);
        }
        catch (Exception e)
        {
            throw new RuntimeException("loading image '" + filename + "' failed: " + e.getMessage());
        }
    }

    /**********************************************************************************
     * A utility function to load an image from a file.
     *
     * @param filename  The file that contains the image to load.
     * @return
     */

    public static BufferedImage loadImage(String filename)
    {
        return loadImage("", filename);
    }

    /********************************************************************
     * Given a filename, this saves an image into that file.
     *
     * @param image
     * @param filename
     */

    public static void saveFile(BufferedImage image, String filename)
    {
        if (!filename.endsWith(".png"))
        {
            filename = filename + ".png";
        }
        try
        {
            FileOutputStream file;
            file = new FileOutputStream(filename);
            ImageIO.write(image, "png", file);
            System.out.println("saved image to file " + filename);
        }
        catch (Exception e)
        {
            System.out.println("saveFile failed: " + e.getMessage());
        }
    }

    /**********************************************************************
     * resizes a given image
     * @param image the image to resize
     * @param width the new width for the image
     * @param height the new height for the image
     */

    public static BufferedImage resizeImage(Image image, int width, int height)
    {
        Image newImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return copyToFastBufferedImage(newImage);
    }

    /**********************************************************************
     * scales a given image
     * @param image the image to resize
     * @param scaleFactor the amount by which to scale (1.0 means no scaling, 2.0 means double in both axes, 0.5 means half as big in both axes)
     */

    public static BufferedImage scaleImage(Image image, double scaleFactor)
    {
        int width = (int) (image.getWidth(null) * scaleFactor);
        int height = (int) (image.getHeight(null) * scaleFactor);
        Image newImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return copyToFastBufferedImage(newImage);
    }

    /**********************************************************************
     * flips a given image
     * @param image the image to resize
     * @param horizontal a boolean, True to flip horizontally
     * @param vertical a boolean, True to flip vertically
     */

    public static BufferedImage flipImage(BufferedImage image, boolean horizontal, boolean vertical)
    {
        int hFlip = 1;
        int hShift = 0;
        if (horizontal) {
            hFlip = -1;
            hShift = -image.getWidth(null);
        }
        int vFlip = 1;
        int vShift = 0;
        if (vertical) {
            vFlip = -1;
            vShift = -image.getHeight(null);
        }
        AffineTransform tx = AffineTransform.getScaleInstance(hFlip, vFlip);
        tx.translate(hShift, vShift);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);

        BufferedImage newBufferedImage = graphicsConfiguration.createCompatibleImage(image.getWidth(null), image.getHeight(null), Transparency.BITMASK);
        Graphics2D pen = newBufferedImage.createGraphics();
        pen.drawImage(image, op, 0, 0);
        return newBufferedImage;
    }


    /**********************************************************************
     * draws a given image on the off-screen buffer
     * @param pen the Graphics pen to use
     * @param image the image to draw
     * @param x the x coordinate of the top-left corner of the image
     * @param y the y coordinate of the top-left corner of the image
     */

    public static void drawImage(Graphics pen, BufferedImage image, int x, int y)
    {
        pen.drawImage(image, x, y, null);
    }

    /**********************************************************************
     * draws a given image on the off-screen buffer, centered
     * @param pen the Graphics pen to use
     * @param image the image to draw
     * @param x the x coordinate of the center of the image
     * @param y the y coordinate of the center of the image
     */

    public static void drawCenteredImage(Graphics pen, BufferedImage image, int x, int y)
    {
        pen.drawImage(image, x-image.getWidth()/2, y-image.getHeight()/2, null);
    }

    /**********************************************************************
     * draws a given image on the offscreen buffer, centered and rotated
     * @param pen the Graphics pen to use
     * @param image the image to draw
     * @param x the x coordinate of the center of the image
     * @param y the y coordinate of the center of the image
     * @param angleRotation the angle to rotate, in degrees, where a positive angle specifies clockwise rotation
     */

    public static void drawRotatedImage(Graphics2D pen, BufferedImage image, int x, int y, double angleRotation)
    {
        AffineTransform rotateTransform = AffineTransform.getRotateInstance(angleRotation/180.0*Math.PI, image.getWidth()/2, image.getHeight()/2);
        AffineTransformOp rotateOp = new AffineTransformOp(rotateTransform, AffineTransformOp.TYPE_BILINEAR);
        pen.drawImage(image, rotateOp, x-image.getWidth()/2, y-image.getHeight()/2);
    }

    /**************************************************************************/

    public static BufferedImage copyToFastBufferedImage(Image image)
    {
        BufferedImage newBufferedImage = graphicsConfiguration.createCompatibleImage(image.getWidth(null), image.getHeight(null), Transparency.BITMASK);
        Graphics pen = newBufferedImage.getGraphics();
        pen.drawImage(image, 0, 0, null);
        return newBufferedImage;
    }

}