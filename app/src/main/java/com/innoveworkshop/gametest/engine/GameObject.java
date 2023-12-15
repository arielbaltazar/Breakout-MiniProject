package com.innoveworkshop.gametest.engine;

import android.graphics.Canvas;

public abstract class GameObject {
    protected GameSurface gameSurface;

    public void onStart(GameSurface gameSurface) {
        this.gameSurface = gameSurface;
    }

    public abstract void draw(Canvas canvas);

    public void onFixedUpdate() {

    }
}
