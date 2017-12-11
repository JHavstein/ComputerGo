package com.example.jonasalexanderhavsteineriksen.computergo;


public class XYCoordinate implements Coordinate {

    /** variables specifying horizontal position on the board */
    private int x;

    /** variable specifying vertical positoin on the board */
    private int y;

    /** constructor creating a Coordinate from x and y values */
    public XYCoordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public boolean checkBoundaries(int xSize, int ySize) {
        if (this.x <= xSize && this.y <= ySize && this.x >= 0 && this.y >= 0) { // x-coordinate is > xSize-1
            return true;
        }
        return false;
    }

    @Override
    public Coordinate shift(int dx, int dy) {
        return new XYCoordinate(this.x+dx,this.y+dy); // makes new x from existing
        // returns the new coordinates as class isntances of
        // XYCoordinate (the return type was originally 'Coordinate' but coordinate is not a class
        // - it's an interface? Error from Peter? Misunderstanding from me?
    }

}
