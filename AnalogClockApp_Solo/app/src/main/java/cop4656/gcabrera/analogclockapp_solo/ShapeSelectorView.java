package cop4656.gcabrera.analogclockapp_solo;

import android.view.View;
import android.content.Context;
import android.util.AttributeSet;
import android.content.res.TypedArray;
import android.content.res.Resources.Theme;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Canvas;

/**
 * Created by gabri on 4/21/2019.
 * Following a tutorial from https://guides.codepath.com/android/defining-custom-views
 */
public class ShapeSelectorView extends View
{
    private int shapeColor;
    private boolean displayShapeName;


    private int shapeWidth =            500;
    private int shapeHeight =           100;
    private int textXOffset =           0;
    private int textYOffset =           30;
    private Paint paintShape;


    private Context context;
    private Theme theme;

    // We must provide a constructor that takes a Context and an AttributeSet.
    // This constructor allows the UI to create and edit an instance of your view.
    public ShapeSelectorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupAttributes(attrs);
        setupPaint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw the shape starting at the top right of this view
        canvas.drawRect(0, 0, shapeWidth, shapeHeight, paintShape);

        // Handle displaying the name of the shape
        if (displayShapeName) {
            canvas.drawText("Square", shapeWidth + textXOffset, shapeHeight + textYOffset, paintShape);
        }

    }

    private void setupPaint() {
        paintShape = new Paint();
        paintShape.setStyle(Style.FILL);
        paintShape.setColor(shapeColor);
        paintShape.setTextSize(30);
    }

    private void setupAttributes(AttributeSet attrs)
    {
        context =               getContext();
        theme =                 context.getTheme();

        // Obtain a typed array of attributes
        TypedArray a =          theme.obtainStyledAttributes(attrs, R.styleable.ShapeSelectorView, 0, 0);
        // Extract custom attributes into member variables
        try
        {
            shapeColor =        a.getColor(R.styleable.ShapeSelectorView_shapeColor, Color.BLACK);
            displayShapeName =  a.getBoolean(R.styleable.ShapeSelectorView_displayShapeName, false);
        } finally
        {
            // TypedArray objects are shared and must be recycled.
            a.recycle();
        }
    }

    // Property methods
    public boolean isDisplayingShapeName()
    {
        return displayShapeName;
    }

    public void setDisplayingShapeName(boolean state)
    {
        this.displayShapeName = state;
        invalidate();
        requestLayout();
    }

    public int getShapeColor()
    {
        return shapeColor;
    }

    public void setShapeColor(int color)
    {
        this.shapeColor = color;
        invalidate();
        requestLayout();
    }



}
