package com.innoveworkshop.gametest.engine;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Rectangle extends GameObject {
    private float x, y;
    private float width, height;
    private Paint paint;

    public Rectangle(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        paint = new Paint();
        paint.setColor(Color.BLUE);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(x, y, x + width, y + height, paint);
    }
}
