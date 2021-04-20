package ca.uwaterloo.cs349;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class DisplayDrawing extends View {
    private int mDrawColor;
    private Paint mPaint, mPaintCircle;
    private float x, y;
    private ArrayList<PointF> points;

    public DisplayDrawing(Context c) {
        super(c);
        mDrawColor = ResourcesCompat.getColor(getResources(),
                R.color.black, null);
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

        mPaintCircle = new Paint();
        mPaintCircle.setColor(mDrawColor);
        mPaintCircle.setAntiAlias(true);
        mPaint.setDither(true);
        mPaintCircle.setStyle(Paint.Style.FILL);
        mPaintCircle.setStrokeJoin(Paint.Join.ROUND);
        mPaintCircle.setStrokeCap(Paint.Cap.ROUND);
    }

    public DisplayDrawing(Context c, AttributeSet attrs) {
        super(c, attrs);
        mDrawColor = ResourcesCompat.getColor(getResources(),
                R.color.black, null);
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

        mPaintCircle = new Paint();
        mPaintCircle.setColor(Color.BLACK);
        mPaintCircle.setAntiAlias(true);
        mPaint.setDither(true);
        mPaintCircle.setStyle(Paint.Style.FILL);
        mPaintCircle.setStrokeJoin(Paint.Join.ROUND);
        mPaintCircle.setStrokeCap(Paint.Cap.ROUND);
    }

    public void setAttr(float x, float y, ArrayList<PointF> points) {
        this.x = x;
        this.y = y;
        this.points = new ArrayList<>(points);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(points != null && points.size() > 0) {
            Path np = new Path();
            np.moveTo(x, y);
            for(int i = 1; i < points.size(); i++) {
                float prevX = points.get(i-1).x;
                float prevY = points.get(i-1).y;
                float currX = points.get(i).x;
                float currY = points.get(i).y;
                np.quadTo(prevX, prevY, (prevX+currX)/2, (prevY+currY)/2);
            }
            Matrix matrix = new Matrix();
            matrix.setScale(0.23148f, 0.151515f);
            np.transform(matrix);
            float xn = x*0.23148f;
            float yn = y*0.151515f;
            canvas.drawCircle(xn, yn, 10, mPaintCircle);
            canvas.drawPath(np, mPaint);
        }
    }
}

