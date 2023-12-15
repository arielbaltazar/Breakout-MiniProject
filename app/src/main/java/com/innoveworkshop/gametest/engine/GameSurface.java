package com.innoveworkshop.gametest.engine;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class GameSurface extends SurfaceView {
    private SurfaceHolder holder;
    private Timer timer;

    private GameObject root;
    private ArrayList<GameObject> gameObjects;

    public GameSurface(Context context) {
        this(context, null);
    }

    public GameSurface(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GameSurface(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        gameObjects = new ArrayList<>();

        setZOrderOnTop(true);
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                setWillNotDraw(false);
                root.onStart(GameSurface.this);
                timer = new Timer();
                timer.scheduleAtFixedRate(new FixedUpdateTimer(), 0, 1000 / 30);
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

            }
        });
    }

    public void setRootGameObject(GameObject root) {
        this.root = root;
    }

    public void addGameObject(GameObject gameObject) {
        gameObjects.add(gameObject);
        gameObject.onStart(this);
    }

    public boolean removeGameObject(GameObject gameObject) {
        return gameObjects.remove(gameObject);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        root.draw(canvas);
        for (GameObject gameObject : gameObjects) {
            gameObject.draw(canvas);
        }
    }

    class FixedUpdateTimer extends TimerTask {
        @Override
        public void run() {
            for (GameObject gameObject : gameObjects) {
                gameObject.onFixedUpdate();
            }
            root.onFixedUpdate();
            invalidate();
        }
    }
}
