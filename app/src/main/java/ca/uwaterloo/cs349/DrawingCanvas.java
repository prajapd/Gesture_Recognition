package ca.uwaterloo.cs349;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;

public class DrawingCanvas extends View {
    private Paint mPaint, mPaintCircle;
    private Path mPath;
    private int mDrawColor;
    private Canvas mExtraCanvas;
    private Bitmap mExtraBitmap;

    private float lastPathStartX;
    private float lastPathStartY;
    private ArrayList<PointF> points;

    private float mX, mY;  //starting point for next path
    private static final float TOUCH_TOLERANCE = 4; //if finger barely moves,
    //no need to draw, using the path not necessary to draw every pixel and request
    // a refresh of the display

    private boolean redraw = false;

    public DrawingCanvas(Context context) {
        super(context);
    }

    public DrawingCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);

        mDrawColor = ResourcesCompat.getColor(getResources(),
                R.color.black, null);
        // Holds the path we are currently drawing.
        mPath = new Path();
        //initialize array of points
        points = new ArrayList<PointF>();
        // Set up the paint with which to draw.
        mPaint = new Paint();
        mPaint.setColor(mDrawColor);
        // Smooths out edges of what is drawn without affecting shape.
        mPaint.setAntiAlias(true);
        // Dithering affects how colors with higher-precision device
        // than the are down-sampled.
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE); // default: FILL
        mPaint.setStrokeJoin(Paint.Join.ROUND); // default: MITER
        mPaint.setStrokeCap(Paint.Cap.ROUND); // default: BUTT
        mPaint.setStrokeWidth(12); // default: Hairline-width (really thin)

        //set up paint for circle

        mPaintCircle = new Paint();
        mPaintCircle.setColor(mDrawColor);
        mPaintCircle.setAntiAlias(true);
        mPaint.setDither(true);
        mPaintCircle.setStyle(Paint.Style.FILL);
        mPaintCircle.setStrokeJoin(Paint.Join.ROUND);
        mPaintCircle.setStrokeCap(Paint.Cap.ROUND);
    }

    public float getLastPathStartX() {
        return lastPathStartX;
    }

    public float getLastPathStartY() {
        return lastPathStartY;
    }

    public ArrayList<PointF> getPoints() {
        return points;
    }

    public void saveCompleted() {
        if(mExtraBitmap != null) {
            mExtraBitmap.eraseColor(Color.WHITE);
            mExtraCanvas.drawCircle(lastPathStartX, lastPathStartY, 15, mPaintCircle);
            mPath.reset();
            if(points != null) {
                mPath.moveTo(points.get(0).x, points.get(0).y);
                for (int i = 1; i < points.size(); i++) {
                    float prevX = points.get(i - 1).x;
                    float prevY = points.get(i - 1).y;
                    float currX = points.get(i).x;
                    float currY = points.get(i).y;
                    mPath.quadTo(prevX, prevY, (prevX + currX) / 2, (prevY + currY) / 2);
                }
                mExtraCanvas.drawPath(mPath, mPaint);
                mPath.reset();
            }
            invalidate();
        }
    }

    public void clearBitmap() {
        if(mExtraBitmap != null) {
            mExtraBitmap.eraseColor(Color.WHITE);
            points.clear();
            invalidate();
        }
    }

    public void redrawSetup(ArrayList<PointF> points, float x, float y) {
        redraw = true;
        this.points.clear();
        //sets up the variables for redraw in "onDraw()"
        this.points.addAll(points);
        lastPathStartX = x;
        lastPathStartY = y;
    }
    private void touchStart(float x, float y) {
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
        mExtraCanvas.drawCircle(x, y, 15, mPaintCircle);
        lastPathStartX = x;
        lastPathStartY = y;
        points.clear(); //clear points
        PointF p = new PointF(x, y);
        points.add(p);
    }

    private void touchMove(float x, float y) {
        float dx = Math.abs(x-mX);
        float dy = Math.abs(y-mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            //QuadTo creates a smoother line
            //here we set the control point to be the last point recorded on path
            mPath.quadTo(mX, mY, (x+mX)/2, (y+mY)/2);
            mX = x;
            mY = y;
            PointF p = new PointF(x, y);
            points.add(p);
            //save the path to the extra bitmap, which we access via the canvas
            mExtraCanvas.drawPath(mPath, mPaint);
        }
    }

    private void touchUp () {
        mPath.reset(); //so the path isn't redrawn
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);


        //create bitmap, create canvas with bitmap
            mExtraBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mExtraCanvas = new Canvas(mExtraBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //draw the bitmap that stores the path the user has drawn
        // initially the user has not drawn anything so we don't see any colour
        //this sets the bitmap being used to the main canvas,
        //the extra canvas we have is so that we can add to the bitmap
        // from other functions without having to be in the onDraw function
        // and then it'll show
        canvas.drawBitmap(mExtraBitmap, 0, 0, null);
        if(redraw) {
            mExtraCanvas.drawCircle(lastPathStartX, lastPathStartY, 15, mPaintCircle);
            if(points != null) {
                mPath.moveTo(points.get(0).x, points.get(0).y);
                for (int i = 1; i < points.size(); i++) {
                    float prevX = points.get(i - 1).x;
                    float prevY = points.get(i - 1).y;
                    float currX = points.get(i).x;
                    float currY = points.get(i).y;
                    mPath.quadTo(prevX, prevY, (prevX + currX) / 2, (prevY + currY) / 2);
                }
                mExtraCanvas.drawPath(mPath, mPaint);
                mPath.reset();
            }
            redraw = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                final View root = getLayout.inflate(R.layout.fragment_addition, container, false);
                touchStart(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(x, y);
                break;
            case MotionEvent.ACTION_UP:
                touchMove(x, y);
                touchUp();
                break;
            default:
        }
        invalidate();
        return true;
    }
}
