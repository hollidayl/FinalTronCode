import java.awt.*;
import java.util.HashSet;

public class PointSet {
    private HashSet<Point> mySet = new HashSet<>();

    public synchronized void addPoint (Point point){
        mySet.add(point);
    }

    public synchronized void drawPoints (Graphics pen, int xStart, int yStart){
        for (Point holdPoint : mySet) {
            pen.fillRect(xStart + holdPoint.row * 7, yStart + holdPoint.col * 7, 7, 7);
        }
    }

    public synchronized boolean contains (Point point){
        return mySet.contains(point);
    }

    public synchronized void clear (){
        mySet.clear();
    }


}
