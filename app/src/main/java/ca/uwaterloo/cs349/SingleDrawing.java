package ca.uwaterloo.cs349;

import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.view.View;
import android.widget.Button;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collections;

public class SingleDrawing {
    String drawingTitle;
    int id;
    private float startX;
    private float startY;
    private ArrayList<PointF> points;

    public SingleDrawing (String title, float x, float y, ArrayList<PointF> points) {
        drawingTitle = title;
        startX = x;
        startY = y;
        this.points = new ArrayList<>(points);
    }

    public ArrayList<PointF> getPoints() {
        return points;
    }

    public String getDrawingTitle() {
        return drawingTitle;
    }

    public void setDrawingTitle(String drawingTitle) {
        this.drawingTitle = drawingTitle;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public float getStartX() {
        return startX;
    }

    public float getStartY() {
        return startY;
    }

    public void setStartX(float startX) {
        this.startX = startX;
    }

    public void setStartY(float startY) {
        this.startY = startY;
    }
}
