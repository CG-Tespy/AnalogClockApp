package cop4656.gcabrera.analogclockapp_solo;

import android.graphics.Point;

/**
 * Encapsulates properties of lines you might have drawn on the screen.
 */
public class Line
{
    // Fields
    protected double length, angle;
    protected int width = 1;
    protected int color;
    protected Point startPoint, endPoint;

    // Methods
    
    // Constructors
    public Line()
    {
        startPoint =                    new Point();
        endPoint =                      new Point();
    }
    
    public Line(double length, int width, double angle)
    {
        this();
        this.length =                   length;
        this.width =                    width;
        this.angle =                    angle;
        updateEndPoint();
    }

    public Line(double length, int width, double angle, int color)
    {
        this(length, width, angle);
        this.color =                    color;
        this.startPoint =               startPoint;
        updateEndPoint();
    }

    public Line(double length, int width, double angle, Point startPoint)
    {
        this(length, width, angle);
        this.startPoint =               startPoint;
        updateEndPoint();
    }

    public Line(double length, int width, double angle, int color, Point startPoint)
    {
        this(length, width, angle, color);
        this.startPoint =               startPoint;
        updateEndPoint();
    }
    
    // Getters
    public double getLength() { return length; }
    public double getWidth() { return width; }
    public double getAngle() { return angle; }
    public int getColor() { return color; }
    public Point getStartPoint() { return startPoint; }
    public Point getEndPoint() { return endPoint; }

    // Setters
    public void setLength(double value)
    {
        this.length =               value;
        updateEndPoint();
    }

    public void setWidth(int value) { width = value; }

    public void setAngle(double value)
    {
        angle =                     value;
        updateEndPoint();
    }

    public void setColor(int value) { color = value; }

    public void setStartPoint(Point value)
    {
        startPoint =                value;
        updateEndPoint();
    }

    // Etc

    /*
    The endpoint needs to change based on the length, angle, and start point
     */
    private void updateEndPoint()
    {
        int xOffset =           (int)(length * Math.sin(angle));
        int yOffset =           (int) (-length * Math.cos(angle));
        // ^ Negative length due to coord sys

        endPoint.set(startPoint.x + xOffset, startPoint.y + yOffset);
    }
}
