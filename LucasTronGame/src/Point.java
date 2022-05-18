public class Point {
    int row;
    int col;

    Point (int row, int col){
        this.row = row;
        this.col = col;
    }

    @Override
    //
    public int hashCode (){
        return Integer.hashCode(row) ^ Integer.hashCode(col);
    }

    @Override
    //row and col equal
    public boolean equals (Object givenObject){
        Point givenPoint = (Point)givenObject;
        if (givenPoint.row == row && givenPoint.col == col){
            return true;
        }
        return false;
    }
}
