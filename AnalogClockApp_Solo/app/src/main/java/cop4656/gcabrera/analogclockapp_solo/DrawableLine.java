package cop4656.gcabrera.analogclockapp_solo;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.NonNull;

public class DrawableLine extends Line
{
    // Fields
    private Paint paintSettings =           new Paint();

    // Methods

    // Constructors
    public DrawableLine()
    {
        super();
        paintSettings.setColor(color);
        paintSettings.setStrokeWidth((float)width);
    }

    public DrawableLine(double length, int width, double angle)
    {
        super(length, width, angle);
        paintSettings.setStrokeWidth((float)width);
        paintSettings.setColor(color);
    }

    public DrawableLine(double length, int width, double angle, int color)
    {
        super(length, width, angle, color);
        paintSettings.setColor(color);
    }

    public DrawableLine(double length, int width, double angle, Point startPoint)
    {
        super(length, width, angle, startPoint);
        paintSettings.setColor(color);
    }

    public DrawableLine(double length, int width, double angle, int color, Point startPoint)
    {
        super(length, width, angle, color, startPoint);
        paintSettings.setColor(color);
    }

    // Main functionality

    public void draw(Canvas canvas)
    {
        canvas.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y, paintSettings);
    }

    // Setters

    /*
    Has this line's paint settings mirror that of the passed settings.
     */
    public void setPaintSettings(Paint src)
    {
        paintSettings.set(src);
        setColor(paintSettings.getColor());
    }

    /*
    Has the passed paint settings be what this object uses.
     */
    public void setPaintSettingsRef(@NonNull Paint src)
    {
        paintSettings =             src;
        setColor(paintSettings.getColor());
    }

    // Getters
    public Paint getPaintSettings() { return paintSettings; }
}
