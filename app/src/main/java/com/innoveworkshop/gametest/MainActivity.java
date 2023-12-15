package com.innoveworkshop.gametest;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private GameView gameView;
    private float paddleX;
    private int dWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // keep the screen on during the game
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // create an instance of the GameView
        gameView = new GameView(this);

        // get the display width
        dWidth = getResources().getDisplayMetrics().widthPixels;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                // update the paddle position based on touch input
                paddleX = touchX - (gameView.getPaddleWidth() / 2);

                // ensure the paddle stays within the screen bounds
                if (paddleX < 0) {
                    paddleX = 0;
                } else if (paddleX > dWidth - gameView.getPaddleWidth()) {
                    paddleX = dWidth - gameView.getPaddleWidth();
                }

                // set the updated paddle position in the GameView
                gameView.setPaddleX(paddleX);
                gameView.invalidate(); // redraw the GameView
                break;
        }

        return true;
    }

    // method called when the "Start Game" button is clicked
    public void startGame(View view) {
        setContentView(gameView);
    }
}
