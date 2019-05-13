package cop4656.gcabrera.analogclockapp_solo;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AnalogClock;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder;
import android.widget.ImageView;

import java.util.Calendar;

import java.util.Calendar;

/*

 */
public class AnalogClockView extends SurfaceView implements SurfaceHolder.Callback
{
    private final String CTag = "AnalogClockView"; // for logging errors
    private Fragment parentFragment;
    private View parentView;
    private ImageView clockFaceImageView;

    // Clock hands
    private DrawableLine secondHand, minuteHand, hourHand;
    private final int secondLength =        250;
    private final int minuteLength =        180;
    private final int hourLength =          110;

    private final int secondWidth =         1;
    private final int minuteWidth =         3;
    private final int hourWidth =           9;

    private ImageView image;

    // Paint settings for the clock hands
    private Paint bgPaint =                 new Paint();
    private Paint circlePaint =             new Paint();
    private Paint secondPaint =             new Paint();
    private Paint minutePaint =             new Paint();
    private Paint hourPaint =               new Paint();

    private final int secondColor =         Color.RED;
    private final int minuteColor =         Color.BLUE;
    private final int hourColor =           Color.GREEN;

    private AnalogClockThread clockThread;
    private SurfaceHolder holder =          getHolder();

    // Time
    protected Calendar calendar;
    protected int second, minute, hour;

    // Etc
    Resources resources;
    Bitmap clockPic;


    // Methods
    public AnalogClockView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        holder = getHolder();
        holder.addCallback(this);
        calendar =              Calendar.getInstance();


    }

    // Update methods
    // Treated as an update method. Mainly to be called by the thread.
    // Note that this is also called when a view is first added to a layout, or when
    // the project is rebuilt. So it'll show up in the design tab.
    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        /* For testing without running
        setupPaintSettings();
        setupClockBitmap();
        setupClockHands();
        */

        /*
        // Draw the background and a circle. Let the clock hands be drawn at the circle's center.
        // For christ's sake.
        drawBackground(canvas, Color.WHITE);
        //canvas.drawCircle(getWidth() / 2, getHeight() / 2, 250, circlePaint);
        canvas.drawBitmap(clockPic, 0, 0, circlePaint);

        updateHandPositions();
        drawClock(canvas);
*/
        //invalidate();

    }



    private void updateTime()
    {
        calendar =              Calendar.getInstance();
        second =                calendar.get(Calendar.SECOND);
        minute =                calendar.get(Calendar.MINUTE);
        hour =                  calendar.get(Calendar.HOUR);
    }

    private void updateHandPositions()
    {
        // Set the angles of the hands so they point in the right places.
        // Note that unlike in some APIs, the 0-degree mark starts at 12 o clock... if you
        // reverse the distance variable when getting the y offset, as the book tells you to.
        // Makes things a bit easier.



        final int hoursPerQuadrant =        3;
        // ^ How many hours are moved from the beginning of one quadrant to reach the beginning
        // of the next.
        final int degreesPerQuadrant =      90; // 90 * 4 = 360, which is how many degrees in a circle
        final double degreesPerHour =       degreesPerQuadrant / hoursPerQuadrant;

        double hourAngle =                  Math.toRadians(hour * degreesPerHour);
        // ^ Sine and cosine functions work with radians, not degrees.

        hourHand.setAngle(hourAngle);

        // The minute hand's calculations build off of the hour hand's.
        final int minutesPerHour =          60;
        final int minutesPerQuadrant =      15;
        final int minutesPerHourMark =      minutesPerQuadrant / hoursPerQuadrant;

        final double degreesPerMinute =     degreesPerHour / minutesPerHourMark;

        double minuteAngle =                Math.toRadians(minute * degreesPerMinute); // 18 * 0.5 = 15

        minuteHand.setAngle(minuteAngle);


        // Decide angle based on things like how far into a minute the minute counter is...
        final int degreesInCircle =         360;
        final int secondsInMinute =         60;
        final float progIntoMinute =        (float) second / secondsInMinute;

        double secondAngle =                0;
        if (progIntoMinute == 0) // Avoid division by 0 error
            secondAngle =                   0;
        else
            secondAngle =                   Math.toRadians( (degreesInCircle * progIntoMinute));
        secondHand.setAngle(secondAngle);
        // Second counter was on 6

        // TODO: Alter the hour and minute calculations, so they can go between hour and tick marks.

        // Raise the hour hand's angle based on how far into the next hour the clock is.
        final double progIntoHour =         (double) minute / minutesPerHour;
        double addHourAngle =               Math.toRadians(degreesPerHour * progIntoHour);

        hourHand.setAngle(hourAngle + addHourAngle);

        // similar logic for the minute hand, though this time relying on the seconds.
        double addMinuteAngle =             Math.toRadians(degreesPerMinute * progIntoMinute);
        minuteHand.setAngle(minuteAngle + addMinuteAngle);

    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder)
    {

        setupPaintSettings();
        setupClockBitmap();
        //setupUIElements();
        setupClockHands();
        clockThread =                       new AnalogClockThread(this);
        clockThread.setIsRunning(true);
        clockThread.start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2)
    {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder)
    {
        clockThread.setIsRunning(false);

        boolean waiting =               true;
        while (waiting)
        {
            try
            {
                if (clockThread != null)
                    clockThread.join(); // wait for clockThread to finish
                waiting =               false;
            }
            catch (InterruptedException e)
            {
                Log.e(CTag, "Thread interrupted", e);
            }
        }
    }

    private void drawClock(Canvas canvas)
    {
        // The circle, the numbers, and the hands
        canvas.drawBitmap(clockPic, 0, 0, circlePaint);

        secondHand.draw(canvas);
        minuteHand.draw(canvas);
        hourHand.draw(canvas);

    }

    // Getters
    public DrawableLine getSecondHand()         { return secondHand; }
    public DrawableLine getMinuteHand()         { return minuteHand; }
    public DrawableLine getHourHand()           { return hourHand; }
    public ImageView getClockFaceImageView()    { return clockFaceImageView; }
    public Paint getBgPaint()                   { return bgPaint; }

    // Setters
    public void setParentFragment(Fragment parentFragment)
    {
        this.parentFragment =   parentFragment;
        parentView =            parentFragment.getView();
    }

    public void setParentView(View parentView)
    {
        this.parentView = parentView;
    }

    // Helpers

    private void setupClockBitmap()
    {
        // Create our own clock bitmap programmatically.
        // Paint settings
        Paint clockPaint =              new Paint();
        clockPaint.setStrokeWidth(2);
        clockPaint.setAntiAlias(true);
        clockPaint.setColor(Color.BLACK);
        clockPaint.setStyle(Paint.Style.STROKE);

        // The bitmap
        Bitmap.Config conf =            Bitmap.Config.ALPHA_8.ARGB_8888;
        clockPic =                      Bitmap.createBitmap(getWidth(), getHeight(), conf);
        float clockRadius =             250;

        // Have a canvas draw the right things on the bitmap

        Point clockCenter =             new Point(getWidth() / 2, getHeight() / 2);
        Canvas canvas =                 new Canvas(clockPic);
        canvas.drawCircle(clockCenter.x, clockCenter.y, clockRadius, clockPaint);

        // TODO: Draw the numbers within the circle
        final int hoursPerQuadrant =        3;
        final int degreesPerQuadrant =      90;
        final double degreesPerHour =       degreesPerQuadrant / hoursPerQuadrant;
        final double hourDegreeOffset =     90;
        final double radiansPerHour =       Math.toRadians(degreesPerHour);

        // Start drawing text at 1
        double numAngle =                   radiansPerHour;
        final int numsOnClock =             12;
        Paint textPaint =                   new Paint(circlePaint);
        textPaint.setTextSize(40);
        textPaint.setTextAlign(Paint.Align.LEFT);
        int numDist =                       (int)(clockRadius * 0.80);

        for (int i = 0; i < numsOnClock; i++)
        {
            // Find position of current number to draw (which is i + 1, btw)
            int numToDraw =                 i + 1;

            int xOffset =                   (int)(numDist * Math.sin(numAngle));
            int yOffset =                   (int) (-numDist * Math.cos(numAngle));

            Point numPos =                  new Point(clockCenter.x + xOffset - 15, clockCenter.y + yOffset);
            canvas.drawText(Integer.toString(numToDraw), numPos.x, numPos.y, textPaint);
            numAngle +=                     radiansPerHour;
        }


    }

    private void drawBackground(Canvas canvas, int color)
    {
        bgPaint.setColor(color);
        canvas.drawRect(0, 0, getWidth(), getHeight(), bgPaint);
    }

    private void setupUIElements()
    {
        //clockFaceImageView =            (ImageView) parentView.findViewById(R.id.clockFaceImageView);
    }

    private void setupPaintSettings()
    {
        bgPaint.setColor(Color.WHITE);

        circlePaint.setColor(Color.BLACK);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(2);
        circlePaint.setAntiAlias(true);

        secondPaint.setStrokeWidth(secondWidth);
        secondPaint.setStyle(Paint.Style.FILL);
        secondPaint.setColor(secondColor);
        secondPaint.setAntiAlias(true);

        minutePaint.setStrokeWidth(minuteWidth);
        minutePaint.setStyle(Paint.Style.FILL);
        minutePaint.setColor(minuteColor);
        minutePaint.setAntiAlias(true);

        hourPaint.setStrokeWidth(hourWidth);
        hourPaint.setStyle(Paint.Style.FILL);
        hourPaint.setColor(hourColor);
        hourPaint.setAntiAlias(true);
    }

    private void setupClockHands()
    {
        // Initialize
        secondHand =            new DrawableLine(secondLength, secondWidth, 0);
        minuteHand =            new DrawableLine(minuteLength, minuteWidth, 0);
        hourHand =              new DrawableLine(hourLength, hourWidth, 0);

        // Set paint settings
        secondHand.setPaintSettingsRef(secondPaint);
        minuteHand.setPaintSettingsRef(minutePaint);
        hourHand.setPaintSettingsRef(hourPaint);

        // Set positioning.
        // Get the position of this view. Credit to TaIL from Stack Overflow
        /*
        Rect posRect =          new Rect();
        this.getGlobalVisibleRect(posRect);
        Point handPos =         new Point((int) posRect.exactCenterX(), (int) posRect.exactCenterY());
        */
        Point handPos = new Point(getWidth() / 2, getHeight() / 2);
        secondHand.setStartPoint(handPos);
        minuteHand.setStartPoint(handPos);
        hourHand.setStartPoint(handPos);
    }


    /**
     * Handles drawing the UI elements onto the analog clock image.
     */
    private class AnalogClockThread extends Thread
    {
        protected final SurfaceHolder surfaceHolder;
        protected AnalogClockView clock;
        protected boolean isRunning =               false;


        public AnalogClockThread(@NonNull AnalogClockView clock)
        {
            this.clock =                            clock;
            this.surfaceHolder =                    clock.getHolder();
            updateTime();
        }

        // Treat this as an update method
        @Override
        public void run()
        {
            super.run();
            Canvas canvas =                         null; // for drawing

            while (this.isRunning)
            {
                // Make sure this is the only thing drawing on the canvas at this time
                canvas =                            surfaceHolder.lockCanvas(null);

                synchronized (surfaceHolder)
                {
                    try
                    {
                        updateTime();


                        if (canvas != null)
                        {
                            drawBackground(canvas, Color.WHITE);
                            updateHandPositions();
                            drawClock(canvas);

                        }
                    }
                    finally
                    {
                        // Display the canvas' contents, and let other threads use the canvas
                        if (canvas != null)
                            surfaceHolder.unlockCanvasAndPost(canvas);
                    }

                }
            }
        }

        // Getters
        public boolean getIsRunning() { return isRunning; }

        // Setters
        public void setIsRunning(boolean value) { isRunning = value; }
    }

}
